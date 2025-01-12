package org.ako.simpleGUI.utils.command;

import org.ako.simpleGUI.SimpleGUI;
import org.ako.simpleGUI.utils.ChatFormat;
import org.ako.simpleGUI.utils.GUICreator;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class CustomCommand {
    public static void registerAllGUICommands() {
        FileConfiguration config = SimpleGUI.getInstance().getPluginConfig();
        File pluginFolder = SimpleGUI.getInstance().getDataFolder();
        File guiFolder = new File(pluginFolder, "gui");

        if (guiFolder.exists() && guiFolder.isDirectory()) {
            File[] guiFiles = guiFolder.listFiles();

            if (guiFiles != null) {
                for (File guiFile : guiFiles) {
                    String guiCommand = guiFile.getName().replace(".yml", "");
                    FileConfiguration guiConfig = SimpleGUI.getInstance().getGUIConfig(guiCommand);

                    ArrayList<String> commandList = (ArrayList<String>) guiConfig.getStringList("commands");
                    if (guiConfig.getBoolean("file_name_command")) {
                        commandList.add(guiCommand);
                    }

                    commandList.forEach(command -> {
                        registerCommands(command, (commandSender, strings) -> {
                            if (!(commandSender instanceof Player p)) {
                                commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("player_only"))));
                                return;
                            } else if (!p.hasPermission("simplegui." + guiCommand) && !p.hasPermission("simplegui.*") && config.getBoolean("require_permission")) {
                                commandSender.sendMessage(ChatFormat.convertOnlyColor((config.getString("prefix") + config.getString("no_permission"))));
                                return;
                            }

                            GUICreator gui = new GUICreator(
                                    guiCommand,
                                    ChatFormat.convert(p, Objects.requireNonNullElse(guiConfig.getString("title"), "SimpleGUI")),
                                    guiConfig.getInt("rows") * 9,
                                    p
                            );

                            for (String key : Objects.requireNonNull(guiConfig.getConfigurationSection("items")).getKeys(false)) {
                                ArrayList<String> enchantments = new ArrayList<>();

                                guiConfig.getStringList("items." + key + ".enchantments").forEach(enchantment -> {
                                    String[] enchantmentSplit = enchantment.split(":");
                                    enchantments.add(enchantmentSplit[0] + ":" + enchantmentSplit[1]);
                                });

                                ArrayList<String> flags = new ArrayList<>(guiConfig.getStringList("items." + key + ".flags"));
                                ArrayList<String> lore = new ArrayList<>(guiConfig.getStringList("items." + key + ".lore").stream()
                                        .map(s -> ChatFormat.convert(p, s)).toList());

                                int slot = (guiConfig.getInt("items." + key + ".position_x") - 1) + (9 * ((guiConfig.getInt("items." + key + ".position_y") - 1)));

                                gui.addItem(
                                        slot,
                                        guiConfig.getString("items." + key + ".name") != null ?
                                                ChatFormat.convert(p, Objects.requireNonNull(guiConfig.getString("items." + key + ".name"))) : null,
                                        guiConfig.getString("items." + key + ".material"),
                                        guiConfig.getInt("items." + key + ".amount") == 0 ? 1 : guiConfig.getInt("items." + key + ".amount"),
                                        enchantments,
                                        flags,
                                        lore
                                );
                            }

                            if (guiConfig.getBoolean("filter.enabled")) {
                                String filter_name = guiConfig.getString("filter.name");

                                String filter_material = Objects.requireNonNullElse(guiConfig.getString("filter.material"), "GRAY_STAINED_GLASS_PANE");
                                int filter_amount = guiConfig.getInt("filter.amount") == 0 ? 1 : guiConfig.getInt("filter.amount");

                                ArrayList<String> filter_enchantments = new ArrayList<>();

                                guiConfig.getStringList("filter.enchantments").forEach(enchantment -> {
                                    String[] enchantmentSplit = enchantment.split(":");
                                    filter_enchantments.add(enchantmentSplit[0] + ":" + enchantmentSplit[1]);
                                });

                                ArrayList<String> filter_flags = new ArrayList<>(guiConfig.getStringList("filter.flags"));

                                ArrayList<String> filter_lore = new ArrayList<>(guiConfig.getStringList("filter.lore").stream()
                                        .map(s -> ChatFormat.convert(p, s)).toList());

                                gui.addFilterItem(
                                        filter_name != null ? ChatFormat.convert(p, filter_name) : null,
                                        filter_material,
                                        filter_amount,
                                        filter_enchantments,
                                        filter_flags,
                                        filter_lore
                                );
                            }

                            gui.openGUI();
                        });
                    });
                }
            }
        }
    }

    private static void registerCommands(String name, GUICommandExecutor clazz) {
        try {
            final Field commandMapField = SimpleGUI.getInstance().getServer().getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(SimpleGUI.getInstance().getServer().getPluginManager());

            Command cmd = new Command(name) {
                @Override
                public boolean execute(CommandSender commandSender, String s, String[] strings) {
                    clazz.execute(commandSender, strings);
                    return true;
                }
            };

            commandMap.register(SimpleGUI.getInstance().getDescription().getName(), cmd);
            SimpleGUI.getInstance().getLogger().info("Registered command: " + name);
        } catch (Exception e) {
            SimpleGUI.getInstance().getLogger().severe("Failed to register command: " + name);
            e.printStackTrace();
        }
    }
}
