package org.ako.simpleGUI.commands;

import org.ako.simpleGUI.SimpleGUI;
import org.ako.simpleGUI.utils.ChatFormat;
import org.ako.simpleGUI.utils.command.GUICommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Objects;

public class List implements GUICommandExecutor {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player p = (Player) commandSender;

        FileConfiguration config = SimpleGUI.getInstance().getPluginConfig();
        File pluginFolder = SimpleGUI.getInstance().getDataFolder();
        File guiFolder = new File(pluginFolder, "gui");

        if (p.hasPermission("simplegui.list")) {
            if (guiFolder.exists() && guiFolder.isDirectory()) {
                File[] guiFiles = guiFolder.listFiles();

                if (guiFiles != null && guiFiles.length > 0) {
                    StringBuilder guiList = new StringBuilder();

                    for (File guiFile : guiFiles) {
                        String guiName = guiFile.getName().replace(".yml", "");
                        guiList.append(guiName).append(guiFile.equals(guiFiles[guiFiles.length - 1]) ? "" : ", ");
                    }

                    commandSender.sendMessage(ChatFormat.convert(p, (config.getString("prefix") + Objects.requireNonNull(config.getString("list_gui")).replace("%_list_%", guiList.toString()))));
                } else {
                    commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("empty"))));
                }
            } else {
                commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("error"))));
            }
        } else {
            commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("no_permission"))));
        }
    }
}
