package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.gamerules.ModGamerules;
import com.keeganator.keegsweapons.gamerules.ServerRef;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.HashMap;
import java.util.Map;

public class WeaponGlobalState extends PersistentState {
    private final Map<String, Boolean> craftedWeapons = new HashMap<>();

    private static final Codec<WeaponGlobalState> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    NbtCompound.CODEC.fieldOf("CraftedWeapons").forGetter(state -> {
                        NbtCompound nbt = new NbtCompound();
                        state.craftedWeapons.forEach(nbt::putBoolean);
                        return nbt;
                    })
            ).apply(instance, (nbt) -> {
                WeaponGlobalState state = new WeaponGlobalState();
                for (String key : nbt.getKeys()) {
                    nbt.getBoolean(key).ifPresent(val -> state.craftedWeapons.put(key, val));
                }
                return state;
            })
    );

    public static final PersistentStateType<WeaponGlobalState> TYPE = new PersistentStateType<>(
            "keegsweapons_crafted_weapons",
            WeaponGlobalState::new,
            CODEC,
            DataFixTypes.LEVEL
    );

    public WeaponGlobalState() {}

    public static WeaponGlobalState getServerState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(TYPE);
    }

    public boolean hasBeenCrafted(String weaponId) {
        return craftedWeapons.getOrDefault(weaponId, false);
    }

    public void setCrafted(String weaponId) {
        if (!ServerRef.weaponForgeRecipeBlockingEnabled()) return;
        craftedWeapons.put(weaponId, true);
        this.markDirty();
    }
    public void resetCrafted(String weaponId) {
        craftedWeapons.remove(weaponId);
        this.markDirty();
    }
    public void resetAllCrafted() {
        craftedWeapons.clear();
        this.markDirty();
    }

    public Map<String, Boolean> getCraftedWeapons() {
        return craftedWeapons;
    }


    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound weaponsTag = new NbtCompound();
        craftedWeapons.forEach(weaponsTag::putBoolean);
        nbt.put("CraftedWeapons", weaponsTag);
        return nbt;
    }
}