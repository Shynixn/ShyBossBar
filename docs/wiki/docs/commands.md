# Commands

The following commands are available in ShyBossBar. You can access them by typing:

```
/shybossbar help 1
```

### /shybossbar add

```
/shybossbar add <bossbar> [player]
```

Adds a new bossbar with type COMMAND to the given player. If a bossbar has got type GLOBAl, it does nothing. If multiple bossbars have been added to a player, 
the one with the highest priority is displayed.

### /shybossbar remove

```
/shybossbar remove <bossbar> [player]
```

Removes a bossbar with type COMMAND from the given player. If a bossbar has got type GLOBAl, it does nothing.

### /shybossbar set

```
/shybossbar set <bossbar> [player]
```

Removes all command based bossbars in the priority list and applies the given bossbar.


### /shybossbar update

```
/shybossbar update [respawn] [player] 
```

Refreshes the bossbar of the current player. If another bossbar of another plugin has overwritten this bossbar, then this fixes the state of the bossbar of a player.

### /shybossbar reload

```
/shybossbar reload
```

Reloads all configurations and bossbars.
