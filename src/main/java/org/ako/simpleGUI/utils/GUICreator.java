package org.ako.simpleGUI.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GUICreator implements InventoryHolder {
    private final String guiId;
    private final String title;
    private final int size;
    private final Player player;
    private final HashMap<Integer, ItemStack> items = new HashMap<>();

    public GUICreator(String guiId, String title, int size, Player player) {
        this.guiId = guiId;
        this.title = title;
        this.size = size;
        this.player = player;
    }

    public void addItem(
            int slot,
            String displayName,
            String material,
            int amount,
            ArrayList<String> enchantment,
            ArrayList<String> itemFlags,
            ArrayList<String> lore
    ) {
        Material itemMaterial = Material.getMaterial(material);

        if (itemMaterial != null || slot <= size || slot > 0) {
            ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)), amount);
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                assert displayName != null;
                meta.setDisplayName(displayName);

                assert lore != null;
                meta.setLore(lore);

                assert enchantment != null;
                for (String enchant : enchantment) {
                    String[] enchantSplit = enchant.split(":");
                    Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(enchantSplit[0].toLowerCase()));

                    if (ench != null) {
                        meta.addEnchant(ench, Integer.parseInt(enchantSplit[1]), true);
                    }
                }

                assert itemFlags != null;
                for (String flag : itemFlags) {
                    meta.addItemFlags(ItemFlag.valueOf(flag));
                }
            }

            item.setItemMeta(meta);
            items.put(slot, item);
        }

    }

    public void addFilterItem(
            String displayName,
            String material,
            int amount,
            ArrayList<String> enchantment,
            ArrayList<String> itemFlags,
            ArrayList<String> lore
    ) {
        for (int i = 0; i < size; i++) {
            if (!items.containsKey(i)) {
                addItem(i, displayName, material, amount, enchantment, itemFlags, lore);
            }
        }
    }

    public void openGUI() {
        Inventory inv = Bukkit.createInventory(this, size, title);

        for (int slot : items.keySet()) {
            inv.setItem(slot, items.get(slot));
        }

        player.openInventory(inv);
    }

    public String getGuiId() {
        return guiId;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
