package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.gamerules.ModGamerules;
import com.keeganator.keegsweapons.gamerules.ServerRef;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public class WeaponGlobalState extends SavedData {
    private final Map<String, Boolean> craftedWeapons = new HashMap<>();

    private static final Codec<WeaponGlobalState> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CompoundTag.CODEC.fieldOf("CraftedWeapons").forGetter(state -> {
                        CompoundTag nbt = new CompoundTag();
                        state.craftedWeapons.forEach(nbt::putBoolean);
                        return nbt;
                    })
            ).apply(instance, (nbt) -> {
                WeaponGlobalState state = new WeaponGlobalState();
                for (String key : nbt.keySet()) {
                    nbt.getBoolean(key).ifPresent(val -> state.craftedWeapons.put(key, val));
                }
                return state;
            })
    );

    public static final SavedDataType<WeaponGlobalState> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "keegsweapons_crafted_weapons"),
            WeaponGlobalState::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    public WeaponGlobalState() {}

    public static WeaponGlobalState getServerState(ServerLevel world) {
        ServerLevel overworld = world.getServer().getLevel(Level.OVERWORLD);

        if (overworld == null) {
            throw new IllegalStateException("Overworld is not available");
        }

        return overworld.getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean hasBeenCrafted(String weaponId) {
        if (!ServerRef.weaponForgeRecipeBlockingEnabled()) {
            return false;
        }

        return craftedWeapons.getOrDefault(weaponId, false);
    }

    public void setCrafted(String weaponId) {
        craftedWeapons.put(weaponId, true);
        this.setDirty();
    }
    public void resetCrafted(String weaponId) {
        craftedWeapons.remove(weaponId);
        this.setDirty();
    }
    public void resetAllCrafted() {
        craftedWeapons.clear();
        this.setDirty();
    }

    public Map<String, Boolean> getCraftedWeapons() {
        return craftedWeapons;
    }


    public CompoundTag writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        CompoundTag weaponsTag = new CompoundTag();
        craftedWeapons.forEach(weaponsTag::putBoolean);
        nbt.put("CraftedWeapons", weaponsTag);
        return nbt;
    }
}