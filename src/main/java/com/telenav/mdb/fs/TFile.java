package com.telenav.mdb.fs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * remote or local file
 * 
 */
public interface TFile {
	/**
	 * is a directory or a file.
	 * 
	 * @return
	 */
	public boolean isDir() throws IOException;

	/**
	 * file length.
	 * 
	 * @return
	 */
	public long length() throws IOException;

	/**
	 * file path.
	 * 
	 * @return
	 */
	public TPath path();

	/**
	 * parent path.
	 * 
	 * @return
	 */
	public TPath parent();

	/**
	 * childs path.
	 * 
	 * @return
	 */
	public TPath[] childs() throws IOException;

	/**
	 * the version of the file.
	 * 
	 * @return
	 */
	public int version() throws IOException;

	/**
	 * meta data to the file.
	 * 
	 * @return
	 */
	public Map<String, String> getMeta() throws IOException;

	/**
	 * set meta data.
	 * 
	 * @param meta
	 * @throws IOException
	 */
	public void setMeta(Map<String, String> meta) throws IOException;

	/**
	 * get InputStream for read.
	 * 
	 * @return
	 */
	public InputStream inputStream() throws IOException;

	/**
	 * 
	 * @param version
	 * @return
	 * @throws IOException
	 */
	public InputStream inputStream(int version) throws IOException;

	/**
	 * get OutputStream for write.
	 * 
	 * @param append
	 * @return
	 */
	public OutputStream outputStream(boolean append) throws IOException;

	/**
	 * delete the file.
	 * 
	 * @param forceOnDir
	 * @return
	 */
	public boolean delete(boolean forceOnDir) throws IOException;
}
