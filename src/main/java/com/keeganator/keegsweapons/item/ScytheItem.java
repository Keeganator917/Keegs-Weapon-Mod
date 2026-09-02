package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

public class ScytheItem extends Item {
    public ScytheItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Properties settings) {
        super(settings.sword(toolMaterial, attackDamage, attackSpeed)
        );
    }

    private static Tool createToolComponent() {
        Holder<Block> bambooEntry = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BAMBOO);
        Holder<Block> cobwebEntry = net.minecraft.core.registries.BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.COBWEB);

        return new Tool(
                List.of(Tool.Rule.minesAndDrops(HolderSet.direct(cobwebEntry), 15.0F),
                        Tool.Rule.overrideSpeed(HolderSet.direct(bambooEntry), 1.5F)), 1.0F, 1, true);
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(BlockTags.MINEABLE_WITH_HOE)) return 15.0F;
        if (state.is(Blocks.VINE) || state.is(Blocks.GLOW_LICHEN)) return 8.0F;
        return super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
        return true;
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {}

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.is(Enchantments.SHARPNESS) || enchantment.is(Enchantments.BANE_OF_ARTHROPODS) || enchantment.is(Enchantments.SMITE)
                || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.FIRE_ASPECT) || enchantment.is(Enchantments.LOOTING)
                || enchantment.is(Enchantments.MENDING);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.is(Enchantments.FORTUNE) || enchantment.is(Enchantments.IMPALING) || enchantment.is(Enchantments.SWEEPING_EDGE)
                || enchantment.is(Enchantments.KNOCKBACK) || enchantment.is(Enchantments.BREACH) || enchantment.is(Enchantments.DENSITY)
                || enchantment.is(Enchantments.WIND_BURST);
        if (denied) {
            return false;
        }
        return enchantment.value().canEnchant(stack);
    }
}