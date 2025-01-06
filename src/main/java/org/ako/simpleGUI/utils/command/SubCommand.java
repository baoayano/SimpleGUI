package org.ako.simpleGUI.utils.command;

import org.ako.simpleGUI.SimpleGUI;
import org.ako.simpleGUI.utils.ChatFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class SubCommand implements CommandExecutor {
    private final HashMap<String, GUICommandExecutor> subCommands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        FileConfiguration config = SimpleGUI.getInstance().getPluginConfig();

        if (strings.length == 0) {
            commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("usage"))));
            return true;
        }

        GUICommandExecutor subCommand = subCommands.get(strings[0]);

        if (subCommand == null) {
            commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("unknown_command"))));
            return true;
        }

        subCommand.execute(commandSender, strings);

        return true;
    }

    public void registerSubCommand(String name, GUICommandExecutor clazz) {
        subCommands.put(name, clazz);
    }
}
