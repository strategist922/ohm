package com.telenav.mdb.fs.hdfs;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.telenav.mdb.fs.Progress;
import com.telenav.mdb.fs.TFile;
import com.telenav.mdb.fs.TFileSystem;
import com.telenav.mdb.fs.TPath;

/**
 * using HDFS as remote file system.
 * 
 */
public class HFileSystem implements TFileSystem {
	static Logger logger = Logger.getLogger(HFileSystem.class);

	static boolean overwrite = true;

	FileSystem fs = null;

	ExecutorService ioExecutor = null;

	public HFileSystem(FileSystem fs, ExecutorService ioExecutor) {
		this.fs = fs;
		this.ioExecutor = ioExecutor;
	}

	public boolean copyToLocal(TPath src, int version, TPath dest)
			throws IOException {
		Path destPath = ((HPath) dest).path();

		HRemoteFile hfile = (HRemoteFile) ((HFile) this.open(src))
				.getDelegate();
		Path srcPath = hfile.getVersionedPath(version);
		fs.copyToLocalFile(false, srcPath, destPath);

		return true;
	}

	public boolean copy(TPath src, TPath dest) throws IOException {
		Path srcPath = ((HPath) src).path();
		Path destPath = ((HPath) dest).path();

		if (src.isLocal() && !dest.isLocal()) { // copy from local
			// archive first
			HRemoteFile hfile = (HRemoteFile) ((HFile) this.open(dest))
					.getDelegate();
			hfile.archiveVersion();

			fs.copyFromLocalFile(false, overwrite, srcPath, destPath);
		} else if (!src.isLocal() && dest.isLocal()) { // copy to local
			fs.copyToLocalFile(false, srcPath, destPath);
		} else if (src.isLocal() && dest.isLocal()) { // local to local
			String s = src.pathString();
			String d = src.pathString();

			File sfile = new File(s);
			File dfile = new File(d);
			if (sfile.isDirectory())
				FileUtils.copyDirectory(sfile, dfile);
			else
				FileUtils.copyFile(sfile, dfile);
		} else { // remote to remote
			// archive first
			HRemoteFile hfile = (HRemoteFile) ((HFile) this.open(dest))
					.getDelegate();
			hfile.archiveVersion();

			FileUtil.copy(fs, srcPath, fs, destPath, false, fs.getConf());
		}

		return true;
	}

	public Progress copyAsync(final TPath src, final TPath dest)
			throws IOException {
		final Future<?> future = ioExecutor.submit(new Runnable() {

			public void run() {
				try {
					copy(src, dest);
				} catch (IOException e) {
					logger.error("fail to copy from " + src.pathString()
							+ " to " + dest.pathString(), e);
				}
			}

		});

		Progress progress = new Progress() {
			Future<?> f = future;

			public int progress() {
				return 0; // TODO
			}

			public boolean isDone() {
				return f.isDone();
			}

		};

		return progress;
	}

	public TFile open(TPath path) throws IOException {
		HFile file = new HFile((HPath) path, fs);

		return file;
	}

	public boolean delete(TPath path, boolean forceOnDir) throws IOException {
		if (path.isLocal()) {
			String s = path.pathString();

			File f = new File(s);
			if (f.isDirectory())
				FileUtils.deleteDirectory(f);
			else
				FileUtils.deleteQuietly(f);

		} else {
			Path p = ((HPath) path).path();
			fs.delete(p, forceOnDir);
		}

		return true;
	}

}
