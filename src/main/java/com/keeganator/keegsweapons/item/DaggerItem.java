package com.keeganator.keegsweapons.item;

import com.keeganator.keegsweapons.KeegsWeapons;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class DaggerItem extends Item {
    public DaggerItem(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Settings settings) {
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
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
        boolean allowed = enchantment.matchesKey(Enchantments.SHARPNESS) || enchantment.matchesKey(Enchantments.BANE_OF_ARTHROPODS) || enchantment.matchesKey(Enchantments.SMITE)
                || enchantment.matchesKey(Enchantments.UNBREAKING) || enchantment.matchesKey(Enchantments.FIRE_ASPECT) || enchantment.matchesKey(Enchantments.LOOTING)
                || enchantment.matchesKey(Enchantments.KNOCKBACK) || enchantment.matchesKey(Enchantments.MENDING);
        if (allowed) {
            return true;
        }
        boolean denied = enchantment.matchesKey(Enchantments.FORTUNE) || enchantment.matchesKey(Enchantments.IMPALING) || enchantment.matchesKey(Enchantments.SWEEPING_EDGE)
                || enchantment.matchesKey(Enchantments.BREACH) || enchantment.matchesKey(Enchantments.DENSITY) || enchantment.matchesKey(Enchantments.WIND_BURST);
        if (denied) {
            return false;
        }
        return enchantment.value().isAcceptableItem(stack);
    }
}
