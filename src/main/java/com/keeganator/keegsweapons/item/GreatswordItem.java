package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;

public class GreatswordItem extends Item {
    public GreatswordItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.sword(toolMaterial, attackDamage, attackSpeed)
        );
    }

    private static ToolComponent createToolComponent() {
        RegistryEntry<Block> bambooEntry = net.minecraft.registry.Registries.BLOCK.getEntry(Blocks.BAMBOO);
        RegistryEntry<Block> cobwebEntry = net.minecraft.registry.Registries.BLOCK.getEntry(Blocks.COBWEB);

        return new ToolComponent(
                List.of(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(cobwebEntry), 15.0F),
                        ToolComponent.Rule.of(RegistryEntryList.of(bambooEntry), 1.5F)), 1.0F, 1, false);
    }

    private static AttributeModifiersComponent createAttributes(ToolMaterial material, float attackDamage, float attackSpeed) {
        float materialDamage = material.attackDamageBonus();

        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(Identifier.of(KeegsWeapons.MOD_ID, "base_attack_damage"),
                                (double)(attackDamage + materialDamage),
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(Identifier.of(KeegsWeapons.MOD_ID, "base_attack_speed"),
                                (double)attackSpeed,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.SWEEPING_DAMAGE_RATIO,
                        new EntityAttributeModifier(Identifier.of(KeegsWeapons.MOD_ID, "base_sweeping_damage"),
                                1.0,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }

    @Override
    public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
        return !user.isInCreativeMode();
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        Window windowHandle = MinecraftClient.getInstance().getWindow();
        boolean shiftDown = InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT)
                || InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (shiftDown) {
            textConsumer.accept(Text.translatable("tooltip.keegsweapons.two_handed.tooltip"));
        } else {
            textConsumer.accept(Text.translatable("tooltip.keegsweapons.hold_shift.tooltip"));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.matchesKey(Enchantments.SHARPNESS) || enchantment.matchesKey(Enchantments.BANE_OF_ARTHROPODS) || enchantment.matchesKey(Enchantments.SMITE)
                || enchantment.matchesKey(Enchantments.UNBREAKING) || enchantment.matchesKey(Enchantments.FIRE_ASPECT) || enchantment.matchesKey(Enchantments.LOOTING)
                || enchantment.matchesKey(Enchantments.KNOCKBACK) || enchantment.matchesKey(Enchantments.MENDING) || enchantment.matchesKey(Enchantments.SWEEPING_EDGE);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.matchesKey(Enchantments.FORTUNE) || enchantment.matchesKey(Enchantments.IMPALING) || enchantment.matchesKey(Enchantments.BREACH)
                || enchantment.matchesKey(Enchantments.DENSITY) || enchantment.matchesKey(Enchantments.WIND_BURST);
        if (denied) {
            return false;
        }
        return enchantment.value().isAcceptableItem(stack);
    }
}