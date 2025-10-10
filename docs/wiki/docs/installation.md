# Installation and Configuration Guide

This guide will help you install ShyBossBar and create your first custom boss bar.

## Understanding BossBar Types

ShyBossBar supports three different types of boss bars, each with specific use cases:

### GLOBAL BossBar

**When to use:** Best for servers with proper permission management and role-based access control.

**How it works:** 

* Automatically displays when a player has the required permission
* Dynamically updates when player permissions change
* Always visible as long as the permission is active

**Example use case:** Display different boss bars for VIP members, staff, or different game modes.

### COMMAND BossBar

**When to use:** Ideal for servers with OP players or when you need manual control over boss bar visibility.

**How it works:**

* Requires manual activation using `/shybossbar add <bossbar> [player]`
* Player must have both the permission AND the command must be executed
* Perfect for integration with world management or region plugins

**Example use case:** Show boss bars when players enter specific worlds, minigames, or areas.

### WORLDGUARD BossBar

**When to use:** Recommended if you're already using WorldGuard for region management.

**How it works:**

* Automatically displays when players enter regions with the `shybossbar` flag
* Supports overlapping regions with priority system
* Compatible with WorldGuard versions 6 and 7

**Example use case:** Display boss bars for shops, PvP zones, or special areas.

## Creating Your First BossBar

### Step 1: Prepare the Configuration Files

1. Navigate to your server's plugin folder:
   ```
   /plugins/ShyBossBar/bossbar/
   ```

2. Open the `sample_bossbar.yml` file

3. **Important:** Change the type from `GLOBAL` to `COMMAND` to disable the sample boss bar:
   ```yaml
   type: "COMMAND"
   ```

4. Create your new boss bar file:
   - Copy `sample_bossbar.yml` to `my_bossbar.yml`
   - You can name the file anything you want (e.g., `welcome_bossbar.yml`, `pvp_zone.yml`)

### Step 2: Configure Your BossBar

Open your new boss bar file and customize these essential settings:

1. **Set the unique name:**
   ```yaml
   name: "my_bossbar"
   ```

2. **Choose the type:**
   ```yaml
   type: "GLOBAL"  # or "COMMAND" or "WORLDGUARD"
   ```

3. **Set your message:**
   ```yaml
   message: "&aWelcome to our server!"
   ```

4. **Configure additional properties as needed:**

   **Priority (lower number = higher priority):**
   ```yaml
   priority: 5
   ```

   **Update frequency (in ticks, 20 ticks = 1 second):**
   ```yaml
   refreshTicks: 60
   ```

   **Visual appearance:**
   ```yaml
   color: "GREEN"  # PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE
   progress: "1.0"  # 0.0 to 1.0
   style: "PROGRESS"  # PROGRESS, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20
   ```

### Step 3: Activate Your BossBar

1. **Reload the plugin:**
   ```
   /shybossbar reload
   ```

2. **Set up based on your chosen type:**

#### For GLOBAL BossBar

* Grant the permission: `shybossbar.bossbar.my_bossbar`
* The boss bar will appear automatically

#### For COMMAND BossBar

* Grant the permission: `shybossbar.bossbar.my_bossbar`
* Manually activate with: `/shybossbar add my_bossbar [player]`
* Integrate with world/region plugins for automatic activation

#### For WORLDGUARD BossBar

* Grant the permission: `shybossbar.bossbar.my_bossbar`
* Set the WorldGuard flag: `/region flag <region> shybossbar my_bossbar`

## Configuration Examples

### Example 1: Welcome Message for New Players
```yaml
name: "welcome_message"
type: "GLOBAL"
priority: 1
refreshTicks: 20
message: "&a&lWelcome to our server, %player_name%!"
color: "GREEN"
progress: "1.0"
style: "PROGRESS"
flags: []
```

### Example 2: PvP Zone Warning
```yaml
name: "pvp_warning"
type: "WORLDGUARD"
priority: 10
refreshTicks: 40
message: "&c&lPvP Zone - Be Careful!"
color: "RED"
progress: "0.8"
style: "NOTCHED_6"
flags: ["DARKEN_SKY"]
```

### Example 3: Event Countdown
```yaml
name: "event_countdown"
type: "COMMAND"
priority: 5
refreshTicks: 20
message: "&e&lEvent starts in: %countdown_time%"
color: "YELLOW"
progress: "%countdown_progress%"
style: "PROGRESS"
flags: []
```

## Troubleshooting

### BossBar Not Appearing

1. Check that the player has the correct permission
2. Verify the boss bar name matches exactly
3. Ensure the plugin has been reloaded after configuration changes
4. Use `/shybossbar update [player]` to refresh the display

### Multiple BossBars Conflicting

* Check priority values (lower number = higher priority)
* Only one boss bar can be displayed at a time per player
* The highest priority boss bar will be shown

### PlaceholderAPI Not Working

* Ensure PlaceholderAPI is installed and running
* Check that placeholder syntax is correct
* Verify the specific placeholder expansion is installed
