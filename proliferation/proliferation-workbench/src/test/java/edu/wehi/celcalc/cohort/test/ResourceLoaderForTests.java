package edu.wehi.celcalc.cohort.test;

import java.net.URL;

public class ResourceLoaderForTests {

	public static String getResource(String res) {
		URL path = ResourceLoaderForTests.class.getResource("");
		String p = path + "/" + res;
		p = p.replace("file:/", "");
		return p;
	}

	public static String getResource(FILES file) {
		return getResource(file.file);
	}

}
