package com.keeganator.keegsweapons.mixin.util;

import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieEntityMixin {
    @Unique
    private static final float WEAPON_SPAWN_CHANCE = 0.05F;

    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void yourmod$maybeEquipCustomWeapon(RandomSource random, DifficultyInstance localDifficulty, CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;

        if (!self.getMainHandItem().isEmpty()) {
            return;
        }


        if (random.nextFloat() < WEAPON_SPAWN_CHANCE) {
            self.level().registryAccess().lookupOrThrow(Registries.ITEM)
                    .get(ModTags.Items.ZOMBIE_CAN_SPAWN_WITH).ifPresent(tag -> {

                        Holder<Item> weapon = tag.get(random.nextInt(tag.size()));

                        self.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(weapon.value()));

                        self.setDropChance(EquipmentSlot.MAINHAND, 0.085F);
                    });
        }
    }
}
