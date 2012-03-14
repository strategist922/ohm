package com.telenav.mdb.store.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.log4j.Logger;

import com.telenav.mdb.store.TStore;
import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.bean.ConverterUtil;
import com.telenav.mdb.store.query.Criteria;
import com.telenav.mdb.store.query.CriteriaFilter;

/**
 * save whole object into single column
 * 
 * @author leef
 * 
 */
public class HBaseSimpleStore implements TStore {
	static Logger logger = Logger.getLogger(HBaseSimpleStore.class);

	static byte[] family = "obj_family".getBytes();

	static byte[] qualifier = "obj_qualifier".getBytes();

	static byte[] meta_family = "obj_meta_family".getBytes();

	static byte[] meta_key = "meta".getBytes();

	Configuration conf = null;

	HBaseAdmin admin = null;

	HTableFactory factory = null;

	Map<String, HTableInterface> tables = null;

	Map<String, Class> tableTypes = null;

	Converter converter = null;

	public HBaseSimpleStore(Configuration conf, Converter converter)
			throws MasterNotRunningException, ZooKeeperConnectionException {
		this.conf = conf;
		this.converter = converter;
		this.admin = new HBaseAdmin(conf);
		this.factory = new HTableFactory();

		this.tables = new HashMap<String, HTableInterface>();
		this.tableTypes = new HashMap<String, Class>();
	}

	public HBaseSimpleStore(Configuration conf)
			throws MasterNotRunningException, ZooKeeperConnectionException {
		this(conf, null);
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	public void createTable(String table, Class clz) throws IOException {
		if (admin.tableExists(table)) {
			logger.info(table + " already exist ");
			return;
		}

		logger.info("create table " + table);
		HTableDescriptor desc = new HTableDescriptor(table);
		desc.addFamily(new HColumnDescriptor(family));
		desc.addFamily(new HColumnDescriptor(meta_family));
		admin.createTable(desc);

		HTableInterface htable = factory.createHTableInterface(conf,
				table.getBytes());
		Put put = new Put(meta_key);
		put.add(meta_family, qualifier, clz.getName().getBytes());
		htable.put(put);
		logger.info("create table " + table + " success");
	}

	public void insertObject(String table, String key, Object obj)
			throws Exception {
		HTableInterface htable = this.getTable(table);

		byte[] value = ConverterUtil.convert(converter, obj);

		Put put = new Put(key.getBytes());
		put.add(family, qualifier, value);
		htable.put(put);
	}

	public Object getObject(String table, String key) throws Exception {
		return getObject(table, key, 0);
	}

	/**
	 * in default, hbase only store 3 version
	 * 
	 */
	public Object getObject(String table, String key, int version)
			throws Exception {
		HTableInterface htable = this.getTable(table);

		Get get = new Get(key.getBytes());
		if (version > 0)
			get.setMaxVersions(version);
		Result result = htable.get(get);

		Object obj = fromResult(table, result);

		return obj;
	}

	protected Object fromResult(String table, Result result) throws Exception {
		HTableInterface htable = this.getTable(table);

		if (result.isEmpty())
			return null;
		byte[] value = result.getValue(family, qualifier);

		Class clz = getTableObjectType(htable, table);
		Object obj = ConverterUtil.convert(converter, clz, value);

		return obj;
	}

	public void deleteObject(String table, String key) throws Exception {
		HTableInterface htable = this.getTable(table);

		Delete delete = new Delete(key.getBytes());
		delete.deleteFamily(family);
		htable.delete(delete);
	}

	public List<Object> findObject(String table, Criteria criteria)
			throws Exception {
		HTableInterface htable = this.getTable(table);

		Scan scan = new Scan();
		scan.addColumn(family, qualifier);

		ResultScanner scanner = htable.getScanner(scan);
		List<Object> retList = fromResult(table, scanner, criteria);

		return retList;
	}

	List<Object> fromResult(String table, ResultScanner scanner,
			Criteria criteria) throws Exception {
		CriteriaFilter filter = new CriteriaFilter(criteria);

		List<Object> retList = new ArrayList<Object>();
		for (Result result : scanner) {
			Object obj = fromResult(table, result);
			boolean accept = filter.isAccept(obj);
			if (accept)
				retList.add(obj);
		}

		return retList;
	}

	HTableInterface getTable(String name) throws IOException {
		HTableInterface table = tables.get(name);
		if (table == null) {
			table = factory.createHTableInterface(conf, name.getBytes());
			tables.put(name, table);
		}

		return table;
	}

	Class getTableObjectType(HTableInterface table, String name)
			throws IOException, ClassNotFoundException {
		Class clz = tableTypes.get(name);
		if (clz == null) {
			Get get = new Get(meta_key);
			Result result = table.get(get);
			byte[] value = result.getValue(meta_family, qualifier);

			String clzName = new String(value);
			clz = Class.forName(clzName);
		}

		return clz;
	}
}
