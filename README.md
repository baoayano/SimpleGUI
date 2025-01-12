# SimpleGUI
A simple Minecraft plugin that allows you to create GUIs easily.

**Current version:** 1.0.1

![image](https://github.com/user-attachments/assets/6e91fcaf-6797-42e1-b79a-46aa261f1ff1)

## Features
- Simple configuration.
- Custom commands for GUIs.
- Easy to manage.
- PlaceholderAPI dependency.

## Configuration
_config.yml_
```yaml
prefix: "&7[&6SimpleGUI&7] "
no_permission: "&cBạn không có quyền để thực hiện hành động này."
no_permission_command: "&cBạn không có quyền để sử dụng lệnh này."
usage: "&7Sử dụng: /simplegui [subcommand]"
unknown_command: "&cLệnh không tồn tại!"
reload_success: "&aĐã tải lại cấu hình thành công!"
error: "&cĐã xảy ra lỗi, vui lòng thử lại sau."
empty: "&cKhông có dữ liệu để hiển thị."
list_gui: "&7Danh sách GUI: &e%_list_%"
player_only: "&cLệnh này chỉ dành cho người chơi."
```
_gui/example.yml_
```yaml
title: "&6&lSimpleGUI" # Title of GUI
rows: 3 # 1 - 6 (MAX_ROWS)
file_name_command: true # If true, the file name will be used as a command
require_permission: true # If true, the player must have permission to open GUI
commands: # Commands that can be used in GUI
  - gui
filter: # Filter of GUI
  enabled: true # If true, the filter will be displayed
  material: GRAY_STAINED_GLASS_PANE # Material of filter
  # The filter has the same properties as items (except position_x, position_y and commands)
  # Such as name, lore, material, amount, enchantments, flags
items: # Items in GUI
  example_item:
    # You can use PlaceholderAPI here
    name: "&6&lFree Diamond for %player_name%" # Name of item
    lore: # Lore of item
      - "&7Click to get a free diamond."
    material: DIAMOND # Material of item
    amount: 1 # Amount of item
    position_x: 5 # 1 - 9
    position_y: 2 # 1 - MAX_ROWS
    enchantments: # Enchantments of item
      - "SHARPNESS:1" # ENCHANTMENT:LEVEL
    flags: # Flags of item
      - HIDE_ENCHANTS # ItemFlag
    commands: # Commands that will be executed when clicking on item
      - "console:give %player_name% diamond 1"
      - "player:give %player_name% diamond 1"
      - "open:example2" # Open another GUI (use file name)
      - "close" # Close GUI
  close:
    name: "&c&lClose"
    lore:
      - "&7Click to close the GUI."
    material: BARRIER
    amount: 1
    position_x: 9
    position_y: 3
    commands:
      - "close"
```

## Commands
- /simplegui reload: Reload the configuration
- **Permission:** simplegui.reload
- /simplegui list: List all GUIs
- **Permission:** simplegui.list
- /simplegui create (Coming soon): Create a new GUI
- **Permission:** simplegui.create
- /simplegui delete (Coming soon): Delete a GUI
- **Permission:** simplegui.delete
- /[command]: Open a GUI
- **Permission:** simplegui.[command]

**_For all features:_** simplegui.*

## License
I'm Java Beginner, so I'm happy if you can help me improve this plugin. Thank you!

I don't have any license for this plugin, so you can use it for free. But please don't remove the credit in the plugin.

## Download
You can download the plugin at the release page.