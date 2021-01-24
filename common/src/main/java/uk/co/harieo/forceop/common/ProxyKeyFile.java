package uk.co.harieo.forceop.common;

import com.google.gson.annotations.Expose;

class ProxyKeyFile {
	private final int version;
	private final String token;

	// write this comment to files, but don't bother reading it
	@Expose(deserialize = false)
	private final String comment = "only keep this key on the proxy, do not distribute!";

	public ProxyKeyFile(int version, String token) {
		this.version = version;
		this.token = token;
	}

	public int getVersion() {
		return version;
	}

	public String getToken() {
		return token;
	}
}
