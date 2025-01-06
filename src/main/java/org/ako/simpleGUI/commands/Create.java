package org.ako.simpleGUI.commands;

import org.ako.simpleGUI.SimpleGUI;
import org.ako.simpleGUI.utils.ChatFormat;
import org.ako.simpleGUI.utils.command.GUICommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Create implements GUICommandExecutor {
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        FileConfiguration config = SimpleGUI.getInstance().getPluginConfig();

        if (commandSender.hasPermission("simplegui.create")) {
            commandSender.sendMessage("Coming soon!");
        } else {
            commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("no_permission"))));
        }
    }
}
