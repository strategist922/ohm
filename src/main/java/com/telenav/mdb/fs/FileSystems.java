package com.telenav.mdb.fs;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.telenav.mdb.fs.hdfs.HFileSystem;

public class FileSystems {

	public static TFileSystem getDefault() throws IOException {
		// read from config file
		String fs_name = "hdfs://172.16.101.227:9000/";

		Configuration config = new Configuration();
		config.set("fs.default.name", fs_name);

		FileSystem hdfs = FileSystem.get(config);

		ExecutorService es = Executors.newFixedThreadPool(4);
		HFileSystem fs = new HFileSystem(hdfs, es);

		return fs;
	}
}
