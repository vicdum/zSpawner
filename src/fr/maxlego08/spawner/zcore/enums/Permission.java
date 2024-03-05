package fr.maxlego08.spawner.zcore.enums;

public enum Permission {

    ZSPAWNER_USE,
    ZSPAWNER_RELOAD,

	ZSPAWNER_GIVE;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
