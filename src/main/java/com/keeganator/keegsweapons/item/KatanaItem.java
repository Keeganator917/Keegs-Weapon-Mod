package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import com.keeganator.keegsweapons.enchantment.ModEnchantments;
import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.util.DashKeyUtil;
import com.keeganator.keegsweapons.util.ModKeyBinding;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class KatanaItem extends Item {
    public KatanaItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Properties settings) {
        super(settings.sword(toolMaterial, attackDamage, attackSpeed)
        );
    }

    private static Tool createToolComponent() {
        Holder<Block> bambooEntry = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BAMBOO);
        Holder<Block> cobwebEntry = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.COBWEB);

        return new Tool(
                List.of(Tool.Rule.minesAndDrops(HolderSet.direct(cobwebEntry), 15.0F),
                        Tool.Rule.overrideSpeed(HolderSet.direct(bambooEntry), 1.5F)), 1.0F, 1, false);
    }

    private static ItemAttributeModifiers createAttributes(ToolMaterial material, float attackDamage, float attackSpeed) {
        float materialDamage = material.attackDamageBonus();

        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "base_attack_damage"),
                                (double)(attackDamage + materialDamage),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "base_attack_speed"),
                                (double)attackSpeed,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.SWEEPING_DAMAGE_RATIO,
                        new AttributeModifier(Identifier.fromNamespaceAndPath(KeegsWeapons.MOD_ID, "base_sweeping_damage"),
                                1.0,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
        return !user.hasInfiniteMaterials();
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        Window windowHandle = Minecraft.getInstance().getWindow();
        boolean shiftDown = InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputConstants.isKeyDown(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (shiftDown) {
            textConsumer.accept(Component.translatable("tooltip.keegsweapons.two_handed.tooltip"));
        } else {
            textConsumer.accept(Component.translatable("tooltip.keegsweapons.hold_shift.tooltip"));
        }
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.FIRE_ASPECT) || enchantment.is(Enchantments.LOOTING)
                || enchantment.is(Enchantments.KNOCKBACK) || enchantment.is(Enchantments.MENDING);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.BREACH) || enchantment.is(Enchantments.DENSITY) || enchantment.is(Enchantments.WIND_BURST);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        Holder<Enchantment> poisonTipped =
                user.level()
                        .registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ModEnchantments.DASH)
                        .orElse(null);

        if (poisonTipped != null) {
            int level = EnchantmentHelper.getItemEnchantmentLevel(poisonTipped, stack);

            if (level > 0) {
                if (world.isClientSide()) {
                    if (DashKeyUtil.isDashBoundToRightClick(ModKeyBinding.DASH_KEY)) {
                        MyModNetwork.sendDash();
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}