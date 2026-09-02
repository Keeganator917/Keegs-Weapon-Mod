package com.keeganator.keegsweapons;

import com.keeganator.keegsweapons.block.ModBlocks;
import com.keeganator.keegsweapons.commands.ModCommands;
import com.keeganator.keegsweapons.effects.ModEffects;
import com.keeganator.keegsweapons.enchantment.ModEnchantmentEffects;
import com.keeganator.keegsweapons.events.ExpertEvent;
import com.keeganator.keegsweapons.gamerules.ModGamerules;
import com.keeganator.keegsweapons.gamerules.ServerRef;
import com.keeganator.keegsweapons.item.ModItems;
import com.keeganator.keegsweapons.loot.ModLootTableInjector;
import com.keeganator.keegsweapons.network.MyModNetwork;
import com.keeganator.keegsweapons.particles.ModParticles;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeegsWeapons implements ModInitializer {
	public static final String MOD_ID = "keegsweapons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModEnchantmentEffects.registerEnchantmentEffects();
		ModEffects.registerEffects();
		WeaponForgeRegistries.registerWeaponForge();

		RecipeSynchronization.synchronizeRecipeSerializer(WeaponForgeRegistries.WEAPON_FORGE_SERIALIZER);

		ModLootTableInjector.init();
		ModParticles.init();
		ModGamerules.init();
		ServerRef.init();

		MyModNetwork.register();
		ExpertEvent.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ModCommands.register(dispatcher);
		});

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
