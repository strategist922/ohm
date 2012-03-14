package com.telenav.mdb.store.hbase;

import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SkipFilter;

import com.telenav.mdb.store.converters.Converter;
import com.telenav.mdb.store.converters.FieldConverter;
import com.telenav.mdb.store.converters.FieldMeta;
import com.telenav.mdb.store.converters.bean.BeanConverter;
import com.telenav.mdb.store.converters.bean.BeanFieldMetas;
import com.telenav.mdb.store.converters.bean.ConverterUtil;
import com.telenav.mdb.store.query.Condition;
import com.telenav.mdb.store.query.Criteria;
import com.telenav.mdb.store.query.Operator;

/**
 * 
 * save fields into different columns.
 * 
 * @author leef
 * 
 */
public class HBaseStore extends HBaseSimpleStore {

	public HBaseStore(Configuration conf) throws MasterNotRunningException,
			ZooKeeperConnectionException {
		super(conf);
	}

	public HBaseStore(Configuration conf, Converter converter)
			throws MasterNotRunningException, ZooKeeperConnectionException {
		super(conf, converter);
	}

	@Override
	public void insertObject(String table, String key, Object obj)
			throws Exception {
		super.insertObject(table, key, obj);

		HTableInterface htable = this.getTable(table);
		Put put = new Put(key.getBytes());

		// insert separate column
		FieldMeta meta = BeanFieldMetas.instance.getMeta(obj.getClass());
		int[] numbers = meta.fieldNumbers();
		for (int num : numbers) {
			// value
			Object v = meta.getFieldValue(num, obj);
			if (v == null)
				continue;

			FieldConverter fc = ((BeanConverter) converter).getFieldConverter(
					obj.getClass(), num);
			byte[] value = ConverterUtil.convert(fc, v);

			// qualify
			byte[] qualify = qualify(num);

			put.add(family, qualify, value);
		}

		htable.put(put);
	}

	@Override
	public List<Object> findObject(String table, Criteria criteria)
			throws Exception {
		HTableInterface htable = this.getTable(table);

		Scan scan = new Scan();
		scan.addColumn(family, qualifier);

		Class clz = getTableObjectType(htable, table);
		Filter hfilter = toFilter(clz, criteria);
		scan.setFilter(hfilter);

		ResultScanner scanner = htable.getScanner(scan);
		List<Object> retList = fromResult(table, scanner, criteria);

		return retList;
	}

	Filter toFilter(Class clz, Criteria criteria) throws Exception {
		FilterList flist = new FilterList();
		Iterator<Condition> it = criteria.iterator();
		while (it.hasNext()) {
			Filter filter = toFilter(clz, it.next());
			flist.addFilter(filter);
		}

		return flist;
	}

	Filter toFilter(Class clz, Condition condition) throws Exception {
		// operator
		CompareFilter.CompareOp cop = null;
		Operator op = condition.op;
		switch (op) {
		case eq:
			cop = CompareOp.EQUAL;
			break;
		case ne:
			cop = CompareOp.NOT_EQUAL;
			break;
		case lt:
			cop = CompareOp.LESS;
			break;
		case gt:
			cop = CompareOp.GREATER;
			break;
		case le:
			cop = CompareOp.LESS_OR_EQUAL;
			break;
		case ge:
			cop = CompareOp.GREATER_OR_EQUAL;
			break;
		}

		// qualify
		String prop = condition.property;
		FieldMeta meta = BeanFieldMetas.instance.getMeta(clz);
		int number = meta.getFieldNumber(prop);
		byte[] qualify = qualify(number);

		// value
		byte[] value = ConverterUtil.convert(converter, condition.value);

		Filter filter = new SingleColumnValueFilter(family, qualify, cop, value);

		// skip an entire row
		return new SkipFilter(filter);
	}

	byte[] qualify(int num) {
		return ("" + num).getBytes();
	}
}
