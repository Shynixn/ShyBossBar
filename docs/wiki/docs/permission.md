# Permissions Guide

This guide explains all permissions available in ShyBossBar and how to set them up properly.

## Permission Overview

ShyBossBar uses a comprehensive permission system to control who can see boss bars and who can manage them.

## Permission Levels

**User Level:** Permissions that regular players should have

**Admin Level:** Permissions that only administrators and staff should have

## Complete Permissions List

| Permission | Level | Description |
|------------|-------|-------------|
| `shybossbar.bossbar.*` | User | Allows seeing all boss bars on the server |
| `shybossbar.bossbar.[bossbar-name]` | User | Allows seeing a specific boss bar |
| `shybossbar.command` | Admin | Required to use any `/shybossbar` command |
| `shybossbar.reload` | Admin | Allows reloading plugin configurations |
| `shybossbar.add` | Admin | Allows adding boss bars to players |
| `shybossbar.set` | Admin | Allows setting boss bars for players |
| `shybossbar.remove` | Admin | Allows removing boss bars from players |
| `shybossbar.update` | Admin | Allows refreshing player boss bars |

## BossBar Viewing Permissions

### Individual BossBar Permissions

Each boss bar requires a specific permission to be visible:

**Format:** `shybossbar.bossbar.[bossbar-name]`

**Examples:**

* `shybossbar.bossbar.welcome_message` - For a boss bar named "welcome_message"
* `shybossbar.bossbar.pvp_zone` - For a boss bar named "pvp_zone"  
* `shybossbar.bossbar.shop_info` - For a boss bar named "shop_info"

### Wildcard Permission

Use `shybossbar.bossbar.*` to grant access to all boss bars at once.

**Recommended for:**

* VIP players who should see all boss bars
* Staff members who need to test boss bars
* Special user groups with universal access

## Command Permissions

### Base Command Permission

`shybossbar.command` is required for ANY use of `/shybossbar` commands.

**Important:** Players need BOTH `shybossbar.command` AND the specific command permission.

### Specific Command Permissions

Each command has its own permission:

**Administrative Commands:**

* `shybossbar.reload` - Reload configurations
* `shybossbar.add` - Add boss bars to players
* `shybossbar.remove` - Remove boss bars from players
* `shybossbar.set` - Set boss bars for players
* `shybossbar.update` - Fix boss bar display issues

## Permission Setup Examples

### For LuckPerms

#### Basic User Group
```
/lp group default permission set shybossbar.bossbar.welcome_message true
/lp group default permission set shybossbar.bossbar.server_info true
```

#### VIP Group
```
/lp group vip permission set shybossbar.bossbar.* true
```

#### Staff Group
```
/lp group staff permission set shybossbar.command true
/lp group staff permission set shybossbar.add true
/lp group staff permission set shybossbar.remove true
/lp group staff permission set shybossbar.set true
/lp group staff permission set shybossbar.update true
```

#### Admin Group
```
/lp group admin permission set shybossbar.* true
```

### For PermissionsEx

#### Default Group
```
/pex group default add shybossbar.bossbar.welcome_message
/pex group default add shybossbar.bossbar.server_info
```

#### Staff Group
```
/pex group staff add shybossbar.command
/pex group staff add shybossbar.add
/pex group staff add shybossbar.remove
/pex group staff add shybossbar.set
/pex group staff add shybossbar.update
```

### For Individual Players

#### Grant specific boss bar access
```
/lp user PlayerName permission set shybossbar.bossbar.special_event true
```

#### Grant command access
```
/lp user PlayerName permission set shybossbar.command true
/lp user PlayerName permission set shybossbar.add true
```

## Permission-Based Boss Bar Types

### GLOBAL Boss Bars

**Required Permissions:**

* `shybossbar.bossbar.[bossbar-name]` - The boss bar appears automatically

**Behavior:**

* Boss bar shows immediately when permission is granted
* Boss bar hides immediately when permission is removed
* Perfect for role-based systems

### COMMAND Boss Bars

**Required Permissions:**

* `shybossbar.bossbar.[bossbar-name]` - Required to see the boss bar
* `shybossbar.command` + `shybossbar.add` - Required to activate the boss bar

**Behavior:**

* Permission alone is NOT enough
* Must use `/shybossbar add` command to activate
* Allows manual control over boss bar visibility

### WORLDGUARD Boss Bars

**Required Permissions:**

* `shybossbar.bossbar.[bossbar-name]` - Required to see the boss bar
* WorldGuard region entry permission (handled by WorldGuard)

**Behavior:**

* Shows when entering a region with the appropriate flag
* Permission is checked when entering the region

## Common Permission Setups

### Server Network Setup

**Hub Server:**
```
# All players see hub information
shybossbar.bossbar.hub_welcome
shybossbar.bossbar.server_info
```

**Game Servers:**
```
# Players see game-specific boss bars
shybossbar.bossbar.game_info
shybossbar.bossbar.match_status
```

### Rank-Based Setup

**Default Players:**
```
shybossbar.bossbar.welcome_message
shybossbar.bossbar.basic_info
```

**VIP Players:**
```
shybossbar.bossbar.*  # All boss bars
```

**Staff Members:**
```
shybossbar.*  # All permissions
```

### Event-Based Setup

**During Events:**
```
# Temporarily grant to all players
shybossbar.bossbar.event_info
shybossbar.bossbar.event_countdown
```

**Special Participants:**
```
shybossbar.bossbar.participant_status
shybossbar.bossbar.team_info
```

## Troubleshooting Permissions

### Boss Bar Not Showing

1. **Check base permission:** Verify player has `shybossbar.bossbar.[bossbar-name]`
2. **Check boss bar type:** 
   - COMMAND type requires manual activation
   - WORLDGUARD type requires region flag
3. **Test with wildcard:** Try `shybossbar.bossbar.*` to rule out permission issues
4. **Reload permissions:** Use your permission plugin's reload command

### Commands Not Working

1. **Check base permission:** Player needs `shybossbar.command`
2. **Check specific permission:** Each command has its own permission
3. **Check inheritance:** Ensure permission groups are set up correctly

### Permission Plugin Integration

**LuckPerms:**
```
/lp editor  # Web-based permission editor
/lp verbose on  # Debug permission checks
```

**PermissionsEx:**
```
/pex  # View permission commands
/pex user [player] check [permission]  # Test specific permissions
```
