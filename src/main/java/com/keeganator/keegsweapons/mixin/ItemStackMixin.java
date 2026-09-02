package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.util.ModRarity;
import com.keeganator.keegsweapons.util.ModTags;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract boolean hasEnchantments();

    @Inject(method = "getRarity", at = @At("RETURN"), cancellable = true)
    public void getRarity(CallbackInfoReturnable<Rarity> cir) {
        if (this.hasEnchantments()) {
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

    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void injectNameColor(CallbackInfoReturnable<Text> cir) {
        boolean isLegendaryTag = this.isIn(ModTags.Items.LEGENDARY_RARITY);

        if (isLegendaryTag) {
            Text original = cir.getReturnValue();
            Text newText = original.copy().formatted(ModRarity.LEGENDARY);
            cir.setReturnValue(newText);
        }
    }

    @Shadow
    public abstract boolean isIn(TagKey<Item> tag);

    @Unique
    private static final Identifier ATTACK_SPEED_DEBUFF_ID = Identifier.of("keegsweapons", "two_handed_attack_speed_debuff");

    @Inject(method = "postHit", at = @At("HEAD"))
    private void keegsweapons$twoHanded(LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        ItemStack mainhand = attacker.getMainHandStack();
        ItemStack offhand = attacker.getOffHandStack();
        ItemStack stack = (ItemStack) (Object) this;

        EntityAttributeInstance attackSpeed = attacker.getAttributeInstance(EntityAttributes.ATTACK_SPEED);
        if (attackSpeed == null) return;
        attackSpeed.removeModifier(ATTACK_SPEED_DEBUFF_ID);

        if (stack.isIn(ModTags.Items.GREATSWORD) || stack.isIn(ModTags.Items.KATANA)) {
            if (ItemStack.areEqual(stack, mainhand) && !offhand.isEmpty() || ItemStack.areEqual(stack, offhand) && !mainhand.isEmpty()) {
                EntityAttributeModifier modifier = new EntityAttributeModifier(ATTACK_SPEED_DEBUFF_ID, -0.4, EntityAttributeModifier.Operation.ADD_VALUE);
                attackSpeed.addTemporaryModifier(modifier);
            }
        }
    }

}