package us.tastybento.bskyblock.api.configuration;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import us.tastybento.bskyblock.database.BSBDatabase;
import us.tastybento.bskyblock.database.flatfile.FlatFileDatabase;
import us.tastybento.bskyblock.database.managers.AbstractDatabaseHandler;

/**
 * Simple interface for tagging all classes containing ConfigEntries.
 *
 * @author Poslovitch
 * @author tastybento
 * @param <T>
 */
public interface ISettings<T> {

    // ----------------Saver-------------------
    @SuppressWarnings("unchecked")
    default void saveSettings() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IntrospectionException, SQLException {
        // Get the handler
        AbstractDatabaseHandler<T> settingsHandler = (AbstractDatabaseHandler<T>) new FlatFileDatabase().getHandler(getInstance().getClass());
        // Load every field in the config class
        settingsHandler.saveSettings(getInstance());
    }

    default void saveBackup() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, IntrospectionException, SQLException {
        // Save backup
        @SuppressWarnings("unchecked")
        AbstractDatabaseHandler<T> backupHandler =  (AbstractDatabaseHandler<T>) new FlatFileDatabase().getHandler(getInstance().getClass());
        backupHandler.saveObject(getInstance());
    }

    // --------------- Loader ------------------
    @SuppressWarnings("unchecked")
    default T loadSettings() throws InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, IntrospectionException, SQLException  {
        // See if this settings object already exists in the database
        AbstractDatabaseHandler<T> dbhandler =  (AbstractDatabaseHandler<T>) BSBDatabase.getDatabase().getHandler(getClass());
        T dbConfig = null;
        if (dbhandler.objectExists(this.getUniqueId())) {
            // Load it
            dbConfig = dbhandler.loadObject(getUniqueId());
        }
        // Get the handler
        AbstractDatabaseHandler<T> configHandler = (AbstractDatabaseHandler<T>) new FlatFileDatabase().getHandler(getInstance().getClass());
        // Load every field in the config class
        return configHandler.loadSettings(getUniqueId(), dbConfig);
    }

    /**
     * @return instance of the implementing class, i.e., return this.
     */
    T getInstance();

    /**
     * @return the uniqueId
     */
    String getUniqueId();

    /**
     * @param uniqueId - unique ID the uniqueId to set
     */
    void setUniqueId(String uniqueId);

}