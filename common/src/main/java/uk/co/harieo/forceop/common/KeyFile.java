package uk.co.harieo.forceop.common;

import com.google.gson.annotations.Expose;

import java.security.Key;

class KeyFile {
	private int version;
	private String key;

	// write this comment to files, but don't bother reading it
	@Expose(deserialize = false)
	private String comment;

	public static String PROXY_KEY_COMMENT = "leave this key in the proxy's plugin configuration folder. DO NOT DISTRIBUTE!";
	public static String SERVER_KEY_COMMENT = "put this key in each server's plugin configuration folder.";

	public KeyFile() {}

	public KeyFile(int version, String key, String comment) {
		this.version = version;
		this.key = key;
		this.comment = comment;
	}

	public int getVersion() {
		return version;
	}

	public String getKey() {
		return key;
	}
}
