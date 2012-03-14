package com.telenav.mdb.fs.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import com.telenav.mdb.fs.TFile;
import com.telenav.mdb.fs.TPath;
import com.telenav.mdb.util.String2Map;

/**
 * [0, file not exist | 1, file first modify | 2, file second mofiy ...]. If no
 * version is given, it's mean the latest. E.g filename filename.1 filename.2
 * 
 * The latest version is 3, see {@link #version()}.
 * 
 * History version is stable and can not be changed.
 * 
 * 
 * @author leef
 * 
 */
public class HRemoteFile implements TFile {
	HPath hpath = null;

	Path path = null;

	FileSystem fs = null;

	static String encoding = "utf-8";

	private static String meta_version = "meta_version";

	public HRemoteFile(HPath path, FileSystem fs) {
		this.hpath = path;
		this.path = path.path();
		this.fs = fs;
	}

	public boolean isDir() throws IOException {
		return !fs.isFile(path);
	}

	public long length() throws IOException {
		return fs.getFileStatus(path).getLen();
	}

	public TPath path() {
		return hpath;
	}

	public TPath parent() {
		String parentPath = path.getParent().getName();
		HPath parent = new HPath(parentPath);
		return parent;
	}

	public TPath[] childs() throws IOException {
		FileStatus[] filestatus = fs.listStatus(path);
		TPath[] childs = new TPath[filestatus.length];
		for (int i = 0; i < filestatus.length; i++) {
			String path = filestatus[i].getPath().getName();
			childs[i] = new HPath(path);
		}

		return childs;
	}

	/**
	 * latest version, [1, version) is history.
	 * 
	 * @throws IOException
	 */
	public int version() throws IOException {
		if (!fs.exists(path))
			return 0;

		Map<String, String> meta = getMeta(true);

		String version = meta.get(meta_version);
		if (version == null) {
			// version is not exist, find and set it
			final String[] versionParam = new String[0];
			final Pattern pattern = Pattern.compile(path.getName()
					+ "\\.(\\d+)");
			Path parent = path.getParent();
			fs.listStatus(parent, new PathFilter() {

				public boolean accept(Path current) {
					Matcher matcher = pattern.matcher(current.getName());
					boolean isMatch = matcher.find();

					if (isMatch) {
						String v = matcher.group(1);
						if (versionParam[0] == null
								|| Integer.parseInt(versionParam[0]) < Integer
										.parseInt(v))
							versionParam[0] = v;
					}

					return isMatch;
				}
			});

			version = versionParam[0];

			if (version == null) {
				version = "0"; // no history version yet
			}

			// plus current version
			this.setVersion(Integer.parseInt(version) + 1);
		}

		return Integer.parseInt(version);
	}

	void setVersion(int version) throws IOException {
		Map<String, String> map = this.getMeta();
		map.put(meta_version, "" + version);
		this.setMeta(map);
	}

	/**
	 * get meta data of this file.
	 * 
	 */
	public Map<String, String> getMeta() throws IOException {
		Map<String, String> retVal = getMeta(false);

		return retVal;
	}

	Map<String, String> getMeta(boolean include_sys) throws IOException {
		Map<String, String> retVal = Collections.EMPTY_MAP;

		Path p = getMetaPath();
		if (fs.exists(p)) {
			InputStream input = fs.open(p);
			List<String> lines = IOUtils.readLines(input, encoding);
			input.close();

			retVal = String2Map.str2map(lines.get(0));

			if (!include_sys) {
				retVal.remove(meta_version);
			}
		}

		return retVal;
	}

	/**
	 * set meta data of this file.
	 * 
	 */
	public void setMeta(Map<String, String> map) throws IOException {
		Map<String, String> old = this.getMeta(true);

		// compare version
		if (old.containsKey(meta_version)) {
			int oldv = Integer.parseInt(old.get(meta_version));
			int newv = 0;
			if (map.containsKey(meta_version))
				newv = Integer.parseInt(map.get(meta_version));
			if (oldv > newv) // reuse old version
				map.put(meta_version, "" + oldv);
		}

		Path p = getMetaPath();
		OutputStream output = fs.create(p);
		IOUtils.write(String2Map.map2Str(map), output, encoding);
		output.flush();
		output.close();
	}

	protected Path getMetaPath() {
		String p = hpath.pathString() + ".meta";

		return new Path(p);
	}

	public Path getVersionedPath(int version) {
		String p = hpath.pathString() + "." + version;

		return new Path(p);
	}

	public InputStream inputStream(int version) throws IOException {
		int v = this.version();
		if (version >= 1 && version < v) {
			Path p = getVersionedPath(version);

			return fs.open(p);
		} else
			throw new IOException(version + " is not valid, valid is [1, " + v
					+ ")");
	}

	public InputStream inputStream() throws IOException {
		return fs.open(path);
	}

	/**
	 * can not change old version.
	 * 
	 */
	public OutputStream outputStream(boolean append) throws IOException {
		OutputStream out = null;
		if (append)
			out = fs.append(path);
		else
			out = fs.create(path);

		OutputStreamCallback callback = new OutputStreamCallback(out);
		return callback;
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void archiveVersion() throws IOException {
		int version = this.version();
		if (version != 0) {
			Path newPath = getVersionedPath(version);

			// copy to
			FileUtil.copy(fs, path, fs, newPath, false, fs.getConf());
		}

		// update version
		this.setVersion(version + 1);
	}

	public boolean delete(boolean forceOnDir) throws IOException {
		// delete all the version

		int version = this.version();
		for (int i = 1; i <= version; i++) {
			Path vPath = new Path(hpath.pathString() + i);
			fs.delete(vPath, forceOnDir);
		}

		return fs.delete(path, forceOnDir);
	}

	class OutputStreamCallback extends OutputStream {
		OutputStream out;
		boolean isArch = false;

		OutputStreamCallback(OutputStream out) {
			this.out = out;
		}

		@Override
		public void write(int b) throws IOException {
			check();

			out.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			check();

			out.write(b);
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			check();

			out.write(b, off, len);
		}

		void check() throws IOException {
			if (!isArch) {
				archiveVersion();

				isArch = true;
			}
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void close() throws IOException {
			out.close();
		}
	}
}
