package fr.maxlego08.spawner.zcore.enums;

public enum Permission {

    ZSPAWNER_USE,
    ZSPAWNER_HELP,
    ZSPAWNER_RELOAD,
	ZSPAWNER_GIVE,
	ZSPAWNER_ADD,
	ZSPAWNER_REMOVE,
    ZSPAWNER_OPTION,
	ZSPAWNER_SHOW;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
