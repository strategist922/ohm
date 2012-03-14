package com.telenav.mdb.fs.hdfs;

import org.apache.hadoop.fs.Path;

import com.telenav.mdb.fs.TPath;
import com.telenav.mdb.fs.TScheme;

public class HPath implements TPath {
	Path path = null;
	String pathString = null;
	TScheme scheme;

	public final static String hdfs_scheme = "hdfs:";

	public HPath(String pathString) {
		this.path = new Path(pathString);
		this.pathString = pathString;

		scheme = pathString.startsWith(hdfs_scheme) ? TScheme.HDFS
				: TScheme.LOCAL;
	}

	public String pathString() {
		return pathString;
	}

	public boolean isLocal() {
		return scheme == TScheme.LOCAL;
	}

	public TScheme getScheme() {
		return scheme;
	}

	public Path path() {
		return path;
	}

}
