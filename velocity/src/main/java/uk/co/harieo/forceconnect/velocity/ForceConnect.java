package uk.co.harieo.forceconnect.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;
import uk.co.harieo.forceconnect.common.DataConverter;
import uk.co.harieo.forceconnect.common.TokenFileHandler;
import uk.co.harieo.forceconnect.common.TokenGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "forceconnect", name="ForceConnect", version = "1.1.0",
        authors = {"Harieo", "deanveloper"}, url = "https://www.spigotmc.org/resources/forceconnect.85990/")
public class ForceConnect {

    private final ProxyServer proxyServer;
    private final Logger logger;
    private final File dataDirectory;

    private Config config;
    private TokenGenerator tokenGenerator;
    private TokenFileHandler tokenFileHandler;
    private String token;

    @Inject
    public ForceConnect(ProxyServer server, Logger logger, @DataDirectory Path dataDirectoryPath) {
        this.proxyServer = server;
        this.logger = logger;

        this.dataDirectory = dataDirectoryPath.toFile();
        if (!dataDirectory.exists() && !dataDirectory.mkdir()) {
            throw new RuntimeException("Failed to create data directory");
        }
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        File configFile = new File(dataDirectory, "config.yml");
        // Verify that the config file exists and if it doesn't, copy it from resource
        if (!configFile.exists()) {
            try (InputStream resourceStream = ForceConnect.class.getClassLoader().getResourceAsStream("config.yml")) {
                if (resourceStream != null) {
                    Files.copy(resourceStream, configFile.toPath());
                } else {
                    throw new IOException("Unable to copy resource config.yml");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Load the config file using yaml
        Yaml yaml = new Yaml();
        try {
            this.config = new Config(yaml.load(new FileReader(configFile)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Create token handlers using config values
        this.tokenGenerator = new TokenGenerator(config.getHashingAlgorithm());
        this.tokenFileHandler = new TokenFileHandler(dataDirectory);

        // Check if token exists and create if not
        if (tokenFileHandler.base64Exists()) {
            verboseLog(Level.INFO, "A token file already exists. If you need a new one, use /forceconnect generate");
            try {
                token = tokenFileHandler.readToken();
            } catch (IOException e) {
                e.printStackTrace();
                logger.severe("Failed to read private key, this is a fatal error.");
            }
        } else {
            generateToken();
        }

        // Register base command
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("forceconnect").aliases("fc", "fconnect").build();
        commandManager.register(commandMeta, new ForceConnectCommand(this));
    }

    @Subscribe
    public void onHandshake(ServerPreConnectEvent event) {
        RegisteredServer registeredServer = event.getOriginalServer();
        ServerInfo dummyServer = new ServerInfo(registeredServer.getServerInfo().getName() + "-encrypted", new InetSocketAddress(token, registeredServer.getServerInfo().getAddress().getPort()));
        RegisteredServer registeredServer1 = proxyServer.registerServer(dummyServer);
        event.setResult(ServerPreConnectEvent.ServerResult.allowed(registeredServer1));
    }

    /**
     * Generates a new token, hashes it then saves the hash to a file
     */
    void generateToken() {
        try {
            verboseLog("Generating a new secure key...");
            byte[] tokenArray = tokenGenerator.nextToken();
            token = DataConverter.convertBytesToHex(tokenArray);
            verboseLog("Key generated, attempting to hash...");
            byte[] hash = tokenGenerator.hash(DataConverter.convertHexToBytes(token));
            verboseLog("Hashed successfully. Saving to file...");
            tokenFileHandler.writeHash(hash);
            tokenFileHandler.writeToken(token);
            logger.info("Your token file has been generated at path: " + tokenFileHandler.getTokenHashFile().getPath());
        } catch (SecurityException e) {
            e.printStackTrace();
            logger.severe("There has been a security failure generating the token");
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Unable to write hashed token to file");
        }
    }

    /**
     * Logs using the plugin logger if {@link Config#isVerbose()}
     *
     * @param level the logging level
     * @param log the log
     */
    public void verboseLog(Level level, String log) {
        if (config.isVerbose()) {
            logger.log(level, log);
        }
    }

    /**
     * An overload of {@link #verboseLog(Level, String)} with logging level {@link Level#INFO}
     *
     * @param log the log
     */
    public void verboseLog(String log) {
        verboseLog(Level.INFO, log);
    }

}
