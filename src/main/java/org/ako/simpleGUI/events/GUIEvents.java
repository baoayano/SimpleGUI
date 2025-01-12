package org.ako.simpleGUI.events;

import org.ako.simpleGUI.SimpleGUI;
import org.ako.simpleGUI.utils.ChatFormat;
import org.ako.simpleGUI.utils.GUICreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class GUIEvents implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() instanceof GUICreator gui) {
            e.setCancelled(true);

            if (e.getCurrentItem() != null) {
                String guiId = gui.getGuiId();

                File pluginFolder = SimpleGUI.getInstance().getDataFolder();
                File guiFolder = new File(pluginFolder, "gui");

                if (guiFolder.exists() && guiFolder.isDirectory()) {
                    File[] guiFiles = guiFolder.listFiles();

                    if (guiFiles != null) {
                        for (File guiFile : guiFiles) {
                            String guiCommand = guiFile.getName().replace(".yml", "");

                            if (guiId.equals(guiCommand)) {
                                FileConfiguration guiConfig = SimpleGUI.getInstance().getGUIConfig(guiCommand);

                                for (String key : Objects.requireNonNull(guiConfig.getConfigurationSection("items")).getKeys(false)) {
                                    int slot = (guiConfig.getInt("items." + key + ".position_x") - 1) + (9 * ((guiConfig.getInt("items." + key + ".position_y") - 1)));

                                    if (slot == e.getSlot()) {
                                        guiConfig.getStringList("items." + key + ".commands").forEach(command -> {
                                            if (command.equals("close")) {
                                                e.getWhoClicked().closeInventory();
                                                return;
                                            } else if (!command.contains(":")) {
                                                SimpleGUI.getInstance().getServer().dispatchCommand(
                                                        e.getWhoClicked(),
                                                        ChatFormat.convert(gui.getPlayer(), command)
                                                );
                                                return;
                                            }

                                            String[] cmd = command.split(":");

                                            if (Objects.equals(cmd[0], "console")) {
                                                SimpleGUI.getInstance().getServer().dispatchCommand(
                                                        SimpleGUI.getInstance().getServer().getConsoleSender(),
                                                        ChatFormat.convert(gui.getPlayer(), cmd[1])
                                                );
                                            } else if (Objects.equals(cmd[0], "player")) {
                                                SimpleGUI.getInstance().getServer().dispatchCommand(
                                                        e.getWhoClicked(),
                                                        ChatFormat.convert(gui.getPlayer(), cmd[1])
                                                );
                                            } else if (Objects.equals(cmd[0], "open")) {
                                                FileConfiguration newGuiConfig = SimpleGUI.getInstance().getGUIConfig(cmd[1]);

                                                GUICreator newGui = new GUICreator(
                                                        cmd[1],
                                                        ChatFormat.convert(gui.getPlayer(), Objects.requireNonNullElse(newGuiConfig.getString("title"), "SimpleGUI")),
                                                        newGuiConfig.getInt("rows") * 9,
                                                        gui.getPlayer()
                                                );

                                                for (String newKey : Objects.requireNonNull(newGuiConfig.getConfigurationSection("items")).getKeys(false)) {
                                                    ArrayList<String> enchantments = new ArrayList<>();

                                                    newGuiConfig.getStringList("items." + newKey + ".enchantments").forEach(enchantment -> {
                                                        String[] enchantmentSplit = enchantment.split(":");
                                                        enchantments.add(enchantmentSplit[0] + ":" + enchantmentSplit[1]);
                                                    });

                                                    ArrayList<String> lore = new ArrayList<>(newGuiConfig.getStringList("items." + newKey + ".lore").stream()
                                                            .map(s -> ChatFormat.convert(newGui.getPlayer(), s))
                                                            .toList());

                                                    int newSlot = (newGuiConfig.getInt("items." + newKey + ".position_x") - 1) + (9 * ((newGuiConfig.getInt("items." + newKey + ".position_y") - 1)));

                                                    newGui.addItem(
                                                            newSlot,
                                                            newGuiConfig.getString("items." + newKey + ".name") != null ?
                                                                    ChatFormat.convert(newGui.getPlayer(), Objects.requireNonNull(newGuiConfig.getString("items." + newKey + ".name"))) : null,
                                                            newGuiConfig.getString("items." + newKey + ".material"),
                                                            newGuiConfig.getInt("items." + newKey + ".amount") == 0 ? 1 : newGuiConfig.getInt("items." + newKey + ".amount"),
                                                            enchantments,
                                                            (ArrayList<String>) newGuiConfig.getStringList("items." + newKey + ".flags"),
                                                            lore
                                                    );
                                                }

                                                if (newGuiConfig.getBoolean("filter.enabled")) {
                                                    String filter_name = newGuiConfig.getString("filter.name");

                                                    String filter_material = Objects.requireNonNullElse(newGuiConfig.getString("filter.material"), "GRAY_STAINED_GLASS_PANE");
                                                    int filter_amount = newGuiConfig.getInt("filter.amount") == 0 ? 1 : newGuiConfig.getInt("filter.amount");

                                                    ArrayList<String> filter_enchantments = new ArrayList<>();

                                                    newGuiConfig.getStringList("filter.enchantments").forEach(enchantment -> {
                                                        String[] enchantmentSplit = enchantment.split(":");
                                                        filter_enchantments.add(enchantmentSplit[0] + ":" + enchantmentSplit[1]);
                                                    });

                                                    ArrayList<String> filter_flags = new ArrayList<>(newGuiConfig.getStringList("filter.flags"));

                                                    ArrayList<String> filter_lore = new ArrayList<>(newGuiConfig.getStringList("filter.lore").stream()
                                                            .map(s -> ChatFormat.convert(newGui.getPlayer(), s))
                                                            .toList());

                                                    newGui.addFilterItem(
                                                            filter_name != null ? ChatFormat.convert(newGui.getPlayer(), filter_name) : null,
                                                            filter_material,
                                                            filter_amount,
                                                            filter_enchantments,
                                                            filter_flags,
                                                            filter_lore
                                                    );
                                                }

                                                newGui.openGUI();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } else {
                        SimpleGUI.getInstance().getLogger().warning("No GUI files found in the gui folder.");
                    }
                } else {
                    SimpleGUI.getInstance().getLogger().warning("No gui folder found in the plugin folder.");
                }
            }
        }
    }
}
