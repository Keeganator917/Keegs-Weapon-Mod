package com.keeganator.keegsweapons.weapon_forge_helpers;

public enum WeaponType {
    SCYTHE("scythe"),
    DAGGER("dagger"),
    KATANA("katana"),
    GREATSWORD("greatsword");

    private final String id;

    WeaponType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static WeaponType fromId(String id) {
        for (WeaponType type : values()) {
            if (type.id.equals(id)) return type;
        }
        throw new IllegalArgumentException("Unknown weapon type: " + id);
    }
}