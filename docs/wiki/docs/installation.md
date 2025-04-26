# How to create a new bossbar

## Understanding bossbar types

ShyBossBar comes with 3 types of bossbars ``GLOBAL``, ``COMMAND`` and ``WORLDGUARD``. All types can be set in the file of the individual bossbar.

#### GLOBAL

A global bossbar is always visible as long as a player has got the permission to that bossbar. For example, if you have got a bossbar 
named ``sample_bossbar`` it will always be displayed if a player has got the permission ``shybossbar.bossbar.sample_bossbar``.

Global bossbars should be used if you **do not have** ``OP PLAYERS`` and have got correctly setup roles with a permission plugin like ``LuckPerms``. 
e.g. you have got admin roles, user roles, etc. Now, if a player enters minigames or enters regions on your server, then he should be added to different permissions roles like ``blockball`` or ``skyblock`` etc. 
Global bossbars automatically detect permission changes and display the correct bossbar.

#### COMMAND

A command bossbar is only visible after being added via the command ``/shybossbar add <bossbar> [player]`` to a player. This ensures a bossbar is not always visible, which works
best for servers having ``OP Players``. Add this command to worlds or regions on your server managed by your world or region plugins. This allows to 
display different bossbars when a player enters different worlds, regions or minigames.

#### WORLDGUARD 

A worldguard bossbar is only visible after a flag with the name of the bossbar has been added to an existing WorldGuard region. This is the recommended type
of bossbar if you are already using WorldGuard on your server. This type also works with overlapping regions and is compatible to WorldGuard 7 and 6.

## Creating a bossbar

### Managing files

1. Open the ``/plugins/ShyBossBar/bossbar`` folder.
2. Open the ``sample_bossbar.yml`` file.
3. Set type from ``GLOBAL`` to ``COMMAND``. This disables the sample bossbar.
4. Copy the ``sample_bossbar.yml`` to a new file in the same folder and name it ``my_bossbar.yml``.
5. Open ``my_bossbar.yml`` and set the name to ``my_bossbar.``
6. Decide if you want to go with a ``GLOBAL`` or ``COMMAND`` bossbar and set the type.
7. Set the message to your wanted message. 
8. Change any additional properties.

### InGame

* Execute the ``/shybossbar reload`` command.

#### GLOBAL

* If you have selected ``GLOBAL``, the bossbar will be visible after you have obtained the ``shybossbar.bossbar.my_bossbar`` permission.

#### COMMAND

* If you have selected ``COMMAND``, the bossbar will be visible after you have obtained the ``shybossbar.bossbar.my_bossbar`` permission and executed the command ``/shybossbar add <bossbar>``. 
* You need to add the ``/shybossbar add <bossbar>`` command and the ``/shybossbar remove <bossbar>`` command to all worlds and regions managed by your world and region plugins to display the correct bossbar in the correct region.

#### WORLDGUARD

* If you have selected ``WORLDGUARD``, the bossbar will be visible after you have obtained the ``shybossbar.bossbar.my_bossbar`` permission and entered a WorldGuard region with a flag ``shybossbar`` set to ``my_bossbar``.
* You can add this flag via the WorldGuard command ``/region flag <region> shybossbar <bossbar>``
