package com.telenav.mdb.fs;

import java.io.IOException;


public interface TFileSystem {

	/**
	 * copy given version to local.
	 * 
	 * @param src
	 * @param version
	 * @param dest
	 * @return
	 */
	public boolean copyToLocal(TPath src, int version, TPath dest) throws IOException;

	/**
	 * if src and dest are dirs, copy recursive. For local -> remote, it means
	 * upload. For remote -> upload, it means download.
	 * 
	 * @param src
	 * @param desc
	 * @return
	 * @throws IOException
	 */
	public boolean copy(TPath src, TPath dest) throws IOException;

	/**
	 * same with {@link #copy(TPath, TPath)} while in async style.
	 * 
	 * @param src
	 * @param dest
	 * @return
	 * @throws IOException
	 */
	public Progress copyAsync(TPath src, TPath dest) throws IOException;

	/**
	 * Same path can have different version. If the version is 0, it means the
	 * latest version.
	 * 
	 * @param fileIdentifier
	 * @return
	 * @throws IOException
	 */
	public TFile open(TPath fileIdentifier) throws IOException;

	/**
	 * 
	 * @param fileIdentifier
	 * @param forceOnDir
	 * @return
	 * @throws IOException
	 */
	public boolean delete(TPath fileIdentifier, boolean forceOnDir)
			throws IOException;

}
