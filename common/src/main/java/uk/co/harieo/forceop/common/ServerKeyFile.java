package uk.co.harieo.forceop.common;

import com.google.gson.annotations.Expose;

class ServerKeyFile {
	private final int version;
	private final String hash;

	// write this comment to files, but don't bother reading it
	@Expose(deserialize = false)
	private final String comment = "put this file in each of your minecraft servers";

	public ServerKeyFile(int version, String hash) {
		this.version = version;
		this.hash = hash;
	}

	public int getVersion() {
		return version;
	}

	public String getHash() {
		return hash;
	}
}
