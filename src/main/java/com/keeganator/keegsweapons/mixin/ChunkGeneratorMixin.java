package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.gamerules.ServerRef;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

    // Prevents /locate taking up resources trying to search for a non-existent structure when weapon forges are disabled
    @Inject(method = "locateStructure*", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$failFastWhenDisabled(ServerWorld world, RegistryEntryList<Structure> structures, BlockPos center, int radius, boolean skipReferencedStructures, CallbackInfoReturnable<Pair<BlockPos, RegistryEntry<Structure>>> cir) {
        if (ServerRef.weaponForgeStructuresEnabled()) return;

        TagKey<Structure> weaponForgeTag = TagKey.of(RegistryKeys.STRUCTURE, Identifier.of("keegsweapons", "weapon_forge"));

        boolean allRequestedAreDisabled = structures.stream().allMatch(entry -> entry.isIn(weaponForgeTag));

        if (allRequestedAreDisabled) {
            cir.setReturnValue(null);
        }
    }
}