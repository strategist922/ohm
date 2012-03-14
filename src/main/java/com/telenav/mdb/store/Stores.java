package com.telenav.mdb.store;

import org.apache.hadoop.conf.Configuration;

import com.telenav.mdb.store.hbase.HBaseSimpleStore;
import com.telenav.mdb.store.hbase.HBaseStore;

public class Stores {

	static Configuration getConfig() {
		String quorum = "172.16.101.227,172.16.101.228,172.16.101.229,172.16.101.230,172.16.101.231";
		String clientPort = "2181";

		Configuration config = new Configuration();
		config.set("hbase.zookeeper.quorum", quorum);
		config.set("hbase.zookeeper.property.clientPort", clientPort);

		return config;
	}

	public static TStore getDefault() throws Exception {
		Configuration config = getConfig();

		HBaseSimpleStore simpleStore = new HBaseSimpleStore(config);
		return simpleStore;
	}

	public static TStore getHBase() throws Exception {
		Configuration config = getConfig();

		HBaseStore simpleStore = new HBaseStore(config);
		return simpleStore;
	}

}
