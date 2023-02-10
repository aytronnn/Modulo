package fr.aytronn.moduloapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.aytronn.moduloapi.modules.IModuleManager;
import fr.aytronn.moduloapi.mongodb.IMongoService;
import fr.aytronn.moduloapi.mongodb.MongoConnector;
import fr.aytronn.moduloapi.mongodb.MongoService;
import org.javacord.api.DiscordApi;
import org.slf4j.Logger;

import java.lang.reflect.Modifier;

public abstract class ModuloApi {

    private static ModuloApi instance;

    private final Gson gson;

    private IMongoService mongoService;

    protected ModuloApi() {
        instance = this;
        this.gson = new GsonBuilder().setPrettyPrinting().setLenient().disableHtmlEscaping().enableComplexMapKeySerialization().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).create();
    }

    /**
     * Useful to get the logger of the plugin
     *
     * @return the logger of the plugin
     */
    public abstract Logger getLogger();

    /**
     * Useful to get the DiscordApi
     *
     * @return the DiscordApi
     */
    public abstract DiscordApi getDiscordApi();

    /**
     * Useful to get the instance of the api
     *
     * @return the instance of the api
     */
    public static ModuloApi getInstance() {
        return instance;
    }

    /**
     * Useful to get the Gson instance
     *
     * @return the Gson instance
     */
    public abstract Gson getGson();

    public void setupMongo(String mongoUri) {
        if (mongoUri == null || mongoUri.isEmpty()) {
            getLogger().warn("MongoDB URI is empty, disabling MongoDB");
            return;
        }
        final var mongoConnector = MongoConnector.getInstance();
        final var mongoSettings = mongoConnector.createSettings(mongoUri);
        final var mongoLocalService = mongoConnector.registerService(new MongoService("MongoDB", mongoSettings));
        mongoConnector.registerService(mongoLocalService);
        this.mongoService = mongoLocalService;
    }

    /**
     * Useful to get the mongo database manager
     *
     * @return the mongo database manager
     */
    public IMongoService getMongoService() {
        return this.mongoService;
    }

    /**
     * Useful to get the module manager
     *
     * @return the module manager
     */
    public abstract IModuleManager getModuleManager();
}
