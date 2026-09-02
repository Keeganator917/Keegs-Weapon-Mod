package com.keeganator.keegsweapons.commands;

import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponGlobalState;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionPredicate;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ModCommands {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("keegsweapons") //.requires(source -> source.getPermissions() >= 2)
                        .then(CommandManager.literal("weapon_forge")
                                .then(CommandManager.literal("reset_crafting_recipe")
                                        .then(CommandManager.literal("all")
                                                .executes(context -> {
                                                    ServerWorld world = context.getSource().getWorld();
                                                    WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                                    state.resetAllCrafted();

                                                    context.getSource().sendFeedback(() -> Text.literal("All weapon crafting recipes reset."), true);
                                                    return 1;
                                                })
                                        )
                                        .then(CommandManager.argument("weapon", IdentifierArgumentType.identifier())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("keegsweapons:reapers_scythe");
                                                    builder.suggest("keegsweapons:grand_assassins_dagger");
                                                    builder.suggest("keegsweapons:netherite_dagger");
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    Identifier id = IdentifierArgumentType.getIdentifier(context, "weapon");
                                                    String weaponId = id.toString();

                                                    ServerWorld world = context.getSource().getWorld();
                                                    WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                                    state.resetCrafted(id.toString());

                                                    context.getSource().sendFeedback(
                                                            () -> Text.literal("Reset crafting recipe for weapon: " + weaponId),
                                                            true
                                                    );

                                                    return 1;
                                                })
                                        )
                                )

                                .then(CommandManager.literal("listCrafted")
                                        .executes(context -> {

                                            ServerWorld world = context.getSource().getWorld();
                                            WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                            Map<String, Boolean> crafted = state.getCraftedWeapons();

                                            if (crafted.isEmpty()) {
                                                context.getSource().sendFeedback(() -> Text.literal("No weapons have been crafted."), false);
                                                return 1;
                                            }
                                            context.getSource().sendFeedback(() -> Text.literal("Crafted Weapons:"), false);
                                            crafted.forEach((weapon, craftedFlag) -> {
                                                if (craftedFlag) {
                                                    context.getSource().sendFeedback(() -> Text.literal(" - " + weapon), false);
                                                }
                                            });

                                            return 1;
                                        })
                                )
                        )
        );
    }
}
