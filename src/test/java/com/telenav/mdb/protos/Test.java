package com.telenav.mdb.protos;

import com.telenav.mdb.protos.PoiProtos.Poi;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Poi poi = Poi.newBuilder().setCity("shanghai").setId("1")
				.buildPartial();
		System.out.println(poi.getCity());

		byte[] poiByte = poi.toByteArray();
	}

}
