package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {
    @Unique
    private static final float WEAPON_SPAWN_CHANCE = 0.05F;

    @Inject(method = "initEquipment", at = @At("TAIL"))
    private void yourmod$maybeEquipCustomWeapon(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        ZombieEntity self = (ZombieEntity) (Object) this;

        if (!self.getMainHandStack().isEmpty()) {
            return;
        }


        if (random.nextFloat() < WEAPON_SPAWN_CHANCE) {
            self.getEntityWorld().getRegistryManager().getOrThrow(RegistryKeys.ITEM)
                    .getOptional(ModTags.Items.ZOMBIE_CAN_SPAWN_WITH).ifPresent(tag -> {

                        RegistryEntry<Item> weapon = tag.get(random.nextInt(tag.size()));

                        self.equipStack(EquipmentSlot.MAINHAND, new ItemStack(weapon.value()));

                        self.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.085F);
                    });
        }
    }
}
