package com.telenav.mdb.fs.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;

import com.telenav.mdb.fs.TFile;
import com.telenav.mdb.fs.TPath;

public class HFile implements TFile {
	TFile delegate = null;

	HPath path = null;

	public HFile(HPath path, FileSystem fs) {
		this.path = path;

		if (path.isLocal()) {
			delegate = new HLocalFile(path);
		} else {
			delegate = new HRemoteFile(path, fs);
		}

	}

	public boolean isLocal() {
		return path.isLocal();
	}

	public boolean isDir() throws IOException {
		return delegate.isDir();
	}

	public long length() throws IOException {
		return delegate.length();
	}

	public TPath path() {
		return delegate.path();
	}

	public TPath parent() {
		return delegate.parent();
	}

	public TPath[] childs() throws IOException {
		return delegate.childs();
	}

	public int version() throws IOException {
		return delegate.version();
	}

	public Map<String, String> getMeta() throws IOException {
		return delegate.getMeta();
	}

	public void setMeta(Map<String, String> meta) throws IOException {
		delegate.setMeta(meta);
	}

	public InputStream inputStream() throws IOException {
		return delegate.inputStream();
	}

	public InputStream inputStream(int version) throws IOException {
		return delegate.inputStream(version);
	}

	public OutputStream outputStream(boolean append) throws IOException {
		return delegate.outputStream(append);
	}

	public boolean delete(boolean forceOnDir) throws IOException {
		return delegate.delete(forceOnDir);
	}

	public TFile getDelegate() {
		return delegate;
	}
}
