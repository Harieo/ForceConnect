package uk.co.harieo.forceconnect.velocity;

import java.util.Map;

/**
 * A blank class representing the configurable values for the plugin
 */
public class Config {

    // Default values
    boolean verbose;
    String hashingAlgorithm;

    public Config() {
        this.verbose = true;
        this.hashingAlgorithm = "SHA-256";
    }

    public Config(Map<String, Object> valueMap) {
        verbose = (Boolean) valueMap.getOrDefault("verbose", true);
        hashingAlgorithm = (String) valueMap.getOrDefault("hashing-algorithm", "SHA-256");
    }

    /**
     * @return whether the plugin should do verbose logging
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * @return the hashing algorithm that should be used
     */
    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

}
