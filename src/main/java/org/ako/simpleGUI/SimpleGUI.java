package org.ako.simpleGUI;

import org.ako.simpleGUI.commands.Create;
import org.ako.simpleGUI.commands.List;
import org.ako.simpleGUI.commands.Reload;
import org.ako.simpleGUI.events.GUIEvents;
import org.ako.simpleGUI.utils.command.CustomCommand;
import org.ako.simpleGUI.utils.command.SubCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Objects;

public final class SimpleGUI extends JavaPlugin {
    private static SimpleGUI instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        createGUIFolder();

        getServer().getPluginManager().registerEvents(new GUIEvents(), this);

        SubCommand subCommand = new SubCommand();
        Objects.requireNonNull(getCommand("simplegui")).setExecutor(subCommand);
        subCommand.registerSubCommand("reload", new Reload());
        subCommand.registerSubCommand("list", new List());
        subCommand.registerSubCommand("create", new Create());

        CustomCommand.registerAllGUICommands();

        getLogger().info("SimpleGUI has been enabled!");
    }

    public static SimpleGUI getInstance() {
        return instance;
    }

    public FileConfiguration getPluginConfig() {
        return getConfig();
    }

    public FileConfiguration getGUIConfig(String name) {
        File guiFolder = new File(getDataFolder(), "gui");
        File guiFile = new File(guiFolder, name + ".yml");

        return YamlConfiguration.loadConfiguration(guiFile);
    }

    public void createGUIFolder() {
        File pluginFolder = getDataFolder();
        File guiFolder = new File(pluginFolder, "gui");

        if (!guiFolder.exists()) {
            guiFolder.mkdirs();
            getLogger().info("GUI folder has been created!");
            saveExampleConfig();
        } else {
            getLogger().info("Successfully loaded GUI folder!");
        }
    }

    public void saveExampleConfig() {
        File exampleConfig = new File(getDataFolder() + "/gui", "example.yml");

        if (!exampleConfig.exists()) {
            try (InputStream in = getResource("gui/example.yml")) {
                Files.copy(in, exampleConfig.toPath());
                getLogger().info("Example config has been saved!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getLogger().info("Example config already exists!");
        }
    }

    public void reloadGUIConfigs() {
        File guiFolder = new File(getDataFolder(), "gui");
        File[] guiFiles = guiFolder.listFiles();

        if (guiFiles != null) {
            for (File guiFile : guiFiles) {
                String guiName = guiFile.getName().replace(".yml", "");
                FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(guiFile);

                if (guiConfig.getKeys(false).isEmpty()) {
                    guiFile.delete();
                    getLogger().info("Deleted " + guiName + ".yml");
                }
            }
        }

        CustomCommand.registerAllGUICommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
