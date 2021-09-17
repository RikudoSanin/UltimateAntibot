package me.kr1s_d.ultimateantibot.bungee.database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Config{
    private final Plugin plugin;

    private final Logger logger;

    private final TaskScheduler scheduler;

    public Config(Plugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.scheduler = plugin.getProxy().getScheduler();
    }

    public Configuration getConfiguration(String file) {
        file = replaceDataFolder(file);
        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(file));
        } catch (IOException e) {
            return new Configuration();
        }
    }

    public void createConfiguration(String file) {
        try {
            file = replaceDataFolder(file);
            File configFile = new File(file);
            if (!configFile.exists()) {
                String[] files = file.split("/");
                InputStream inputStream = this.plugin.getClass().getClassLoader().getResourceAsStream(files[files.length - 1]);
                File parentFile = configFile.getParentFile();
                if (parentFile != null)
                    parentFile.mkdirs();
                if (inputStream != null) {
                    Files.copy(inputStream, configFile.toPath());
                } else {
                    configFile.createNewFile();
                }
                this.logger.info("File " + configFile + " has been created!");
            }
        } catch (IOException e) {
            this.logger.info("Unable to create configuration file '" + file + "'!");
        }
    }

    public void saveConfiguration(Configuration configuration, String file) {
        String replacedFile = replaceDataFolder(file);
        this.scheduler.runAsync(this.plugin, () -> {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(replacedFile));
            } catch (IOException e) {
                this.logger.info("Unable to save configuration file '" + replacedFile + "'!");
            }
        });
    }

    public void deleteConfiguration(String file) {
        String replacedFile = replaceDataFolder(file);
        File file1 = new File(replacedFile);
        if (file1.exists()) {
            file1.delete();
            this.logger.info("File " + replacedFile + " has been deleted!");
        }
    }

    private String replaceDataFolder(String string) {
        File dataFolder = this.plugin.getDataFolder();
        return string.replace("%datafolder%", dataFolder.toPath().toString());
    }
}
