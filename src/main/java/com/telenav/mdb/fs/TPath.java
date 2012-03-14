package com.telenav.mdb.fs;

/**
 */
public interface TPath {
	/**
	 * is a local file or remote.
	 * 
	 * @return
	 */
	public boolean isLocal();

	/**
	 * 
	 * @return
	 */
	public TScheme getScheme();

	/**
	 * the url of the path, different protocol are supported.
	 * 
	 * @return
	 */
	public String pathString();

}
