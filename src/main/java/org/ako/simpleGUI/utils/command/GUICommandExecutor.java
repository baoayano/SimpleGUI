package org.ako.simpleGUI.utils.command;

import org.bukkit.command.CommandSender;

public interface GUICommandExecutor {
    void execute(CommandSender commandSender, String[] args);
}
