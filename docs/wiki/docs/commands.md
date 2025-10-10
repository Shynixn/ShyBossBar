# Commands Reference

This page contains all available ShyBossBar commands with detailed explanations and examples.

## Getting Help

To view all available commands in-game, use:
```
/shybossbar help 1
```

## Command Overview

All ShyBossBar commands start with `/shybossbar` and require appropriate permissions.

### Player Management Commands

#### /shybossbar add

**Purpose:** Adds a COMMAND-type boss bar to a player

**Syntax:**
```
/shybossbar add <bossbar> [player]
```

**Parameters:**

* `<bossbar>` - The name of the boss bar configuration file (without .yml extension)
* `[player]` - Optional: Target player name. If omitted, applies to command sender

**Behavior:**

* Only works with boss bars that have `type: "COMMAND"`
* Player must have the permission `shybossbar.bossbar.<bossbar-name>`
* If multiple boss bars are added, the one with highest priority (lowest number) is displayed
* Has no effect on GLOBAL or WORLDGUARD type boss bars

**Examples:**
```
/shybossbar add welcome_message
/shybossbar add pvp_warning PlayerName
```

#### /shybossbar remove

**Purpose:** Removes a COMMAND-type boss bar from a player

**Syntax:**
```
/shybossbar remove <bossbar> [player]
```

**Parameters:**

* `<bossbar>` - The name of the boss bar to remove
* `[player]` - Optional: Target player name. If omitted, applies to command sender

**Behavior:**

* Only affects COMMAND-type boss bars
* Removes the boss bar from the player's active list
* If other boss bars are active, the next highest priority one will be displayed
* Has no effect on GLOBAL or WORLDGUARD type boss bars

**Examples:**
```
/shybossbar remove welcome_message
/shybossbar remove pvp_warning PlayerName
```

#### /shybossbar set

**Purpose:** Replaces all active COMMAND-type boss bars with a single specified boss bar

**Syntax:**
```
/shybossbar set <bossbar> [player]
```

**Parameters:**

* `<bossbar>` - The name of the boss bar to set as active
* `[player]` - Optional: Target player name. If omitted, applies to command sender

**Behavior:**

* Removes all currently active COMMAND-type boss bars
* Adds the specified boss bar as the only active one
* Useful for switching between different boss bar states
* Player must have permission for the target boss bar

**Examples:**
```
/shybossbar set event_countdown
/shybossbar set maintenance_mode PlayerName
```

### System Commands

#### /shybossbar update

**Purpose:** Refreshes and fixes boss bar display issues

**Syntax:**
```
/shybossbar update [respawn] [player]
```

**Parameters:**

* `[respawn]` - Optional: Include this to trigger a respawn-like refresh
* `[player]` - Optional: Target player name. If omitted, applies to command sender

**Use Cases:**

* Boss bar not displaying correctly
* Another plugin has interfered with the boss bar
* Player's boss bar appears corrupted or stuck
* After permission changes that should affect boss bar visibility

**Examples:**
```
/shybossbar update
/shybossbar update respawn
/shybossbar update PlayerName
/shybossbar update respawn PlayerName
```

#### /shybossbar reload

**Purpose:** Reloads all configuration files and boss bar definitions

**Syntax:**
```
/shybossbar reload
```

**Behavior:**

* Reads all configuration files from the plugin directory
* Updates existing boss bars with new settings
* Loads newly created boss bar files
* Applies changes to all online players
* **Required after any configuration file changes**

**Example:**
```
/shybossbar reload
```

## Usage Examples

### Setting Up a Welcome System

1. Create a welcome boss bar:
   ```
   /shybossbar add welcome_message NewPlayer
   ```

2. Later remove it:
   ```
   /shybossbar remove welcome_message NewPlayer
   ```

### Event Management

1. Switch to event mode:
   ```
   /shybossbar set event_countdown
   ```

2. Update if there are display issues:
   ```
   /shybossbar update respawn
   ```

3. Reload after changing event settings:
   ```
   /shybossbar reload
   ```

### Integration with Other Plugins

These commands work well with world management and region plugins:

**For MultiVerse worlds:**
```
/mv modify set onPlayerEnter /shybossbar add world_info %p%
/mv modify set onPlayerLeave /shybossbar remove world_info %p%
```

**For region-based activation:**
```
# Add to region enter commands
/shybossbar add shop_info %player%

# Add to region exit commands  
/shybossbar remove shop_info %player%
```

## Command Permissions

Make sure players have the appropriate permissions:

* **shybossbar.command** - Required to use any /shybossbar command
* **shybossbar.add** - Required for `/shybossbar add`
* **shybossbar.remove** - Required for `/shybossbar remove`  
* **shybossbar.set** - Required for `/shybossbar set`
* **shybossbar.update** - Required for `/shybossbar update`
* **shybossbar.reload** - Required for `/shybossbar reload`

See the [Permissions Guide](permission.md) for complete permission details.