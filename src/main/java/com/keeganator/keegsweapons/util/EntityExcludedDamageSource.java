package com.keeganator.keegsweapons.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

public class EntityExcludedDamageSource extends DamageSource {

    protected final List<EntityType<?>> entities;
    private final ItemStack weaponStack;

    public EntityExcludedDamageSource(RegistryEntry<DamageType> type, EntityType<?>... entities) {
        super(type);
        this.entities = Arrays.stream(entities).toList();
        this.weaponStack = this.getWeaponStack();
    }

    @Override
    public Text getDeathMessage(LivingEntity living) {
        LivingEntity attacker = living.getAttacker();

        String baseKey = "death.attack." + this.getType().msgId();
        String playerKey = baseKey + ".player";
        String itemKey = baseKey + ".item";

        if (attacker != null) {
            for (EntityType<?> entity : entities) {
                if (attacker.getType() == entity) {
                    return Text.translatable(baseKey, living.getDisplayName());
                }
            }

            ItemStack weapon = this.weaponStack;

            if (!weapon.isEmpty()) {
                return Text.translatable(itemKey, living.getDisplayName(), attacker.getDisplayName(), weapon.getCustomName());
            }

            return Text.translatable(playerKey, living.getDisplayName(), attacker.getDisplayName());
        }

        return Text.translatable(baseKey, living.getDisplayName());
    }
}