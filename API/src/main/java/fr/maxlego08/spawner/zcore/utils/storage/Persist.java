package fr.maxlego08.spawner.zcore.utils.storage;

import com.google.gson.Gson;
import fr.maxlego08.spawner.zcore.enums.Folder;
import fr.maxlego08.spawner.zcore.logger.Logger;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Type;

public class Persist  {

    private final Gson gson;
	private final Plugin p;

	public Persist(Plugin p, Gson gson) {
		this.p = p;
        this.gson = gson;
	}

	// ------------------------------------------------------------ //
	// GET NAME - What should we call this type of object?
	// ------------------------------------------------------------ //

	public static String getName(Class<?> clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	public static String getName(Object o) {
		return getName(o.getClass());
	}

	public static String getName(Type type) {
		return getName(type.getClass());
	}

	// ------------------------------------------------------------ //
	// GET FILE - In which file would we like to store this object?
	// ------------------------------------------------------------ //

	public File getFile(String name) {
		return new File(p.getDataFolder(), name + ".json");
	}

	public File getFile(Class<?> clazz) {
		return getFile(getName(clazz));
	}

	public File getFile(Object obj) {
		return getFile(getName(obj));
	}

	public File getFile(Type type) {
		return getFile(getName(type));
	}

	// NICE WRAPPERS

	public <T> T loadOrSaveDefault(T def, Class<T> clazz) {
		return loadOrSaveDefault(def, clazz, getFile(clazz));
	}

	public <T> T loadOrSaveDefault(T def, Class<T> clazz, String name) {
		return loadOrSaveDefault(def, clazz, getFile(name));
	}

	public <T> T loadOrSaveDefault(T def, Class<T> clazz, Folder folder, String name) {
		return loadOrSaveDefault(def, clazz, getFile(folder.toFolder() + File.separator + name));
	}


	public <T> T loadOrSaveDefault(T def, Class<T> clazz, File file) {
		if (!file.exists()) {
            Logger.info("Creating default: " + file, Logger.LogType.SUCCESS);
			this.save(def, file);
			return def;
		}

		T loaded = this.load(clazz, file);

		if (loaded == null) {
			Logger.info("Using default as I failed to load: " + file, Logger.LogType.WARNING);

			/*
			 * Create new config backup
			 */

			File backup = new File(file.getPath() + "_bad");
			if (backup.exists())
				backup.delete();
			Logger.info("Backing up copy of bad file to: " + backup, Logger.LogType.WARNING);

			file.renameTo(backup);

			return def;
		} else {

			Logger.info(file.getAbsolutePath() + " loaded successfully !", Logger.LogType.SUCCESS);

		}

		return loaded;
	}

	// SAVE

	public boolean save(Object instance) {
		return save(instance, getFile(instance));
	}

	public boolean save(Object instance, String name) {
		return save(instance, getFile(name));
	}

	public boolean save(Object instance, Folder folder, String name) {
		return save(instance, getFile(folder.toFolder() + File.separator + name));
	}

	public boolean save(Object instance, File file) {
		try {
			boolean b = DiscUtils.writeCatch(file, this.gson.toJson(instance));
			Logger.info(file.getAbsolutePath() + " successfully saved !", Logger.LogType.SUCCESS);
			return b;
		} catch (Exception e) {
			Logger.info("cannot save file " + file.getAbsolutePath(), Logger.LogType.ERROR);
			return false;
		}
	}

	// LOAD BY CLASS

	public <T> T load(Class<T> clazz) {
		return load(clazz, getFile(clazz));
	}

	public <T> T load(Class<T> clazz, String name) {
		return load(clazz, getFile(name));
	}

	public <T> T load(Class<T> clazz, File file) {
		String content = DiscUtils.readCatch(file);
		if (content == null) {
			return null;
		}
		try {
            return this.gson.fromJson(content, clazz);
		} catch (Exception ex) { // output the error message rather than full
									// stack trace; error parsing the file, most
									// likely
			Logger.info(ex.getMessage(), Logger.LogType.ERROR);
		}
		return null;
	}

	// LOAD BY TYPE
	public <T> T load(Type typeOfT, String name) {
		return load(typeOfT, getFile(name));
	}

	public <T> T load(Type typeOfT, File file) {
		String content = DiscUtils.readCatch(file);
		if (content == null) {
			return null;
		}

		try {
			return this.gson.fromJson(content, typeOfT);
		} catch (Exception ex) { // output the error message rather than full
									// stack trace; error parsing the file, most
									// likely
			Logger.info(ex.getMessage(), Logger.LogType.ERROR);
		}
		return null;
	}

}
