package com.telenav.mdb.fs.hdfs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telenav.mdb.fs.FileSystems;
import com.telenav.mdb.fs.TFile;
import com.telenav.mdb.fs.TFileSystem;

public class FSTest {
	static TFileSystem fs = null;

	String local = null;

	String remote = null;

	@BeforeClass
	public static void init() throws IOException {
		fs = FileSystems.getDefault();
	}

	@Before
	public void setup() {
		local = "/home/readme.txt";
		remote = HPath.hdfs_scheme + "/tmp/readme";
	}

	@Test
	public void testUpload() throws IOException {
		fs.copy(new HPath(local), new HPath(remote));
	}

	@Test
	public void testVersion() throws IOException {
		int version = fs.open(new HPath(remote)).version();
		fs.copy(new HPath(local), new HPath(remote));
		int newV = fs.open(new HPath(remote)).version();

		Assert.assertEquals(version + 1, newV);

		local = local + System.currentTimeMillis();
		fs.copyToLocal(new HPath(remote), version, new HPath(local));
	}

	@Test
	public void testDownload() throws IOException {
		local = local + System.currentTimeMillis();
		fs.copy(new HPath(remote), new HPath(local));
	}

	@Test
	public void testMeta() throws IOException {
		TFile file = fs.open(new HPath(remote));

		Map<String, String> meta = makeMeta();
		file.setMeta(meta);
		Map<String, String> meta2 = file.getMeta();

		Assert.assertEquals(meta.toString(), meta2.toString());
	}

	Map<String, String> makeMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("color", "red");
		meta.put("symbols", "e");
		meta.put("empty", "");

		return meta;
	}

}
