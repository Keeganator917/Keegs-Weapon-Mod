package com.keeganator.keegsweapons.mixin.util;

import com.keeganator.keegsweapons.util.ModRarity;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract boolean isEnchanted();

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    public void getRarity(CallbackInfoReturnable<Rarity> cir) {
        if (this.isEnchanted()) {
            Rarity currentRarity = cir.getReturnValue();

            switch (currentRarity) {
                case COMMON:
                case UNCOMMON:
                    cir.setReturnValue(Rarity.RARE);
                    break;
                case RARE:
                    cir.setReturnValue(Rarity.EPIC);
                    break;
                case EPIC:
                    cir.setReturnValue(Rarity.EPIC);
                    break;
                default:
                    break;
            }
        }
    }

    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void injectNameColor(CallbackInfoReturnable<Component> cir) {
        boolean isLegendaryTag = this.is(holder -> holder.is(ModTags.Items.LEGENDARY_RARITY));

        if (isLegendaryTag) {
            Component original = cir.getReturnValue();
            Component newText = original.copy().withStyle(ModRarity.LEGENDARY);
            cir.setReturnValue(newText);
        }
    }

    @Shadow
    public abstract boolean is(final Predicate<Holder<Item>> item);

    @Unique
    private static final Identifier ATTACK_SPEED_DEBUFF_ID = Identifier.fromNamespaceAndPath("keegsweapons", "two_handed_attack_speed_debuff");

    @Inject(method = "hurtEnemy", at = @At("HEAD"))
    private void keegsweapons$twoHanded(LivingEntity mob, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        ItemStack mainhand = attacker.getMainHandItem();
        ItemStack offhand = attacker.getOffhandItem();
        ItemStack stack = (ItemStack) (Object) this;

        AttributeInstance attackSpeed = attacker.getAttribute(Attributes.ATTACK_SPEED);
        if (attackSpeed == null) return;
        attackSpeed.removeModifier(ATTACK_SPEED_DEBUFF_ID);


        if (ItemStack.isSameItem(stack, mainhand) && !offhand.isEmpty() || ItemStack.isSameItem(stack, offhand) && !mainhand.isEmpty()) {
            if (stack.is(ModTags.Items.GREATSWORD) || stack.is(ModTags.Items.KATANA)) {
                AttributeModifier modifier = new AttributeModifier(ATTACK_SPEED_DEBUFF_ID, -0.4, AttributeModifier.Operation.ADD_VALUE);
                attackSpeed.addTransientModifier(modifier);
            }
        }
    }

}