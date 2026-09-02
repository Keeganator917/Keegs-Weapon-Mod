package com.keeganator.keegsweapons.mixin.weaponforge;

import com.keeganator.keegsweapons.gamerules.ServerRef;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

    // Prevents /locate taking up resources trying to search for a non-existent structure when weapon forges are disabled
    @Inject(method = "findNearestMapStructure", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$failFastWhenDisabled(ServerLevel level, HolderSet<Structure> wantedStructures, BlockPos pos, int maxSearchRadius, boolean createReference, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir) {
        if (ServerRef.weaponForgeStructuresEnabled()) return;

        TagKey<Structure> weaponForgeTag = TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath("keegsweapons", "weapon_forge"));

        boolean allRequestedAreDisabled = wantedStructures.stream().allMatch(entry -> entry.is(weaponForgeTag));

        if (allRequestedAreDisabled) {
            cir.setReturnValue(null);
        }
    }
}