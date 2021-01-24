package uk.co.harieo.forceop.common.upgrade.v2_jsonfiles;

import com.google.gson.annotations.Expose;

// a v2 frozen version of KeyFile
class KeyFileV2 {
	private int version;
	private String key;

	// write this comment to files, but don't bother reading it
	@Expose(deserialize = false)
	private String comment;

	public static String PROXY_KEY_COMMENT = "leave this key in the proxy's plugin configuration folder. DO NOT DISTRIBUTE!";
	public static String SERVER_KEY_COMMENT = "put this key in each server's plugin configuration folder.";

	public KeyFileV2() {}

	public KeyFileV2(String key, String comment) {
		this.version = 2;
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
