# ToDo

- [x] G�rer les explosions du block, pouvoir activer / d�sactiver
- [x] Ajouter un syst�me de silk spawner
- [ ] Proposer un syst�me d'upgrade de spawner par spawner
- [x] V�rifier le placage des blocks dans le cuboid
- [ ] Ajouter le syst�me de whitelist / blacklist de materials pour les materials
- [ ] Ajouter une option pour avoir des spawners stackable � l'infini
- [ ] Ajouter une option pour que les mobs qui spawn d'un spawner vont �tre sans IA
- [ ] Ajouter une option pour d�sactiver l'ajout des loots de mobs dans le spawners infini 
- [ ] Ajouter une option pour permettre la mort des mobs sans avoir l'animation de morts, uniquement utiliser le EntityDamageEvent

- [ ] Ajouter une option pour que les spawners virtuel puissent �tre ouvert par tout le monde et r�cup�rer par tout le monde
- [ ] V�rifier si l'autokill fonctionne correctement par d�faut, sans activ� l'option mais juste avec la configuraiton


# Unreleased

# 4.0.9

- Update to zMenu 1.1.0.0

# 4.0.8

- Update to last zMenu version
- Fix slime virtual spawner
- Cancel virtual chicken laying 

# 4.0.7

- Improvement and correction of the spawner option system
- Added placeholders to display the options in a spawner item
- Added an information button to display the options of a spawner
- Use Sarah for database management (you can now use MYSQL)
- Fixed slime spawn

# 4.0.6

- Add zEssentials mailbox on give commands (allows you to receive items in your mailbox if you are full)

# 4.0.5

- Added Shop button, allows to sell the content of the virtual spawner (only work with zshop for the moment)
- Fix error with default spawner option
- Added blacklist materials for virtual spawner
- Player with the permission "zspawner.bypass" can now open virtual spawner

# 4.0.4

- Some fixs

# 4.0.3

- Update auto kill, if the player is online then the mob will be killed by the owner of the spawner

# 4.0.2

- Add option ``breakUpVirtualSpawner``
- Fixed the possibility of placing a block at the entity position for a virtual spawner
- Fixed the autocompletion for ``/spawners option``

# 4.0.1

- Add Silk Spawner for natural Spawner
- Fix virtual spawner with random entity spawn
- Fix squid KnockBack
- Fix entity spawn on entity kill with Virtual Spawner
