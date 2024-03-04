package fr.maxlego08.spawner.zcore.enums;

public enum Permission {

    ZSPAWNER_USE,
	EXAMPLE_PERMISSION_RELOAD,

	;

	private String permission;

	private Permission() {
		this.permission = this.name().toLowerCase().replace("_", ".");
	}

	public String getPermission() {
		return permission;
	}

}
