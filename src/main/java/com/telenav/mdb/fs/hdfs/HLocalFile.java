package com.telenav.mdb.fs.hdfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.telenav.mdb.fs.TFile;
import com.telenav.mdb.fs.TPath;

public class HLocalFile implements TFile {
	String path = null;
	File file = null;
	HPath hpath = null;

	public HLocalFile(HPath hpath) {
		this.hpath = hpath;
		this.path = hpath.pathString();
		this.file = new File(path);
	}

	public boolean isDir() throws IOException {
		return file.isDirectory();
	}

	public long length() throws IOException {
		return file.length();
	}

	public TPath path() {
		return hpath;
	}

	public TPath parent() {
		String p = file.getParent();
		HPath path = new HPath(p);
		return path;
	}

	public TPath[] childs() {
		// assume it's directory
		File[] child = file.listFiles();

		TPath[] ret = new TPath[child.length];
		for (int i = 0; i < child.length; i++)
			ret[i] = new HPath(child[i].getAbsolutePath());

		return ret;
	}

	public int version() {
		throw new UnsupportedOperationException();
	}

	public Map<String, String> getMeta() throws IOException {
		throw new UnsupportedOperationException();
	}

	public void setMeta(Map<String, String> meta) throws IOException {
		throw new UnsupportedOperationException();
	}

	public InputStream inputStream() throws IOException {
		// assume it's file
		return FileUtils.openInputStream(file);
	}

	public InputStream inputStream(int version) throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream outputStream(boolean append) throws IOException {
		// assume it's file
		return FileUtils.openOutputStream(file, append);
	}

	public boolean delete(boolean forceOnDir) throws IOException {
		return file.delete();
	}
}
