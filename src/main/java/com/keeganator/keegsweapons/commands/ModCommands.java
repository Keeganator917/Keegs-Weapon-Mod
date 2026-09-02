package com.keeganator.keegsweapons.commands;

import com.keeganator.keegsweapons.weapon_forge_helpers.recipes.WeaponGlobalState;
import com.mojang.brigadier.CommandDispatcher;
import java.util.Map;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

public class ModCommands {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("keegsweapons")
                        .then(Commands.literal("weapon_forge")
                                .then(Commands.literal("reset_crafting_recipe")
                                        .then(Commands.literal("all")
                                                .executes(context -> {
                                                    ServerLevel world = context.getSource().getLevel();
                                                    WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                                    state.resetAllCrafted();

                                                    context.getSource().sendSuccess(() -> Component.literal("All weapon crafting recipes reset."), true);
                                                    return 1;
                                                })
                                        )
                                        .then(Commands.argument("weapon", IdentifierArgument.id())
                                                .suggests((context, builder) -> {
                                                    builder.suggest("keegsweapons:reapers_scythe");
                                                    builder.suggest("keegsweapons:grand_assassins_dagger");
                                                    builder.suggest("keegsweapons:shoguns_katana");
                                                    builder.suggest("keegsweapons:kingly_greatsword");
                                                    return builder.buildFuture();
                                                })
                                                .executes(context -> {
                                                    Identifier id = IdentifierArgument.getId(context, "weapon");
                                                    String weaponId = id.toString();

                                                    ServerLevel world = context.getSource().getLevel();
                                                    WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                                    state.resetCrafted(id.toString());

                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal("Reset crafting recipe for weapon: " + weaponId),
                                                            true
                                                    );

                                                    return 1;
                                                })
                                        )
                                )

                                .then(Commands.literal("listCrafted")
                                        .executes(context -> {

                                            ServerLevel world = context.getSource().getLevel();
                                            WeaponGlobalState state = WeaponGlobalState.getServerState(world);

                                            Map<String, Boolean> crafted = state.getCraftedWeapons();

                                            if (crafted.isEmpty()) {
                                                context.getSource().sendSuccess(() -> Component.literal("No weapons have been crafted."), false);
                                                return 1;
                                            }
                                            context.getSource().sendSuccess(() -> Component.literal("Crafted Weapons:"), false);
                                            crafted.forEach((weapon, craftedFlag) -> {
                                                if (craftedFlag) {
                                                    context.getSource().sendSuccess(() -> Component.literal(" - " + weapon), false);
                                                }
                                            });

                                            return 1;
                                        })
                                )
                        )
        );
    }
}
