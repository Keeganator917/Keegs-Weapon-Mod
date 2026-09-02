package com.keeganator.keegsweapons.mixin;

import com.keeganator.keegsweapons.gamerules.ServerRef;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(JigsawStructure.class)
public abstract class JigsawStructureMixin {

    @Inject(method = "getStructurePosition", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$checkGameRule(Structure.Context context, CallbackInfoReturnable<Optional<Structure.StructurePosition>> cir) {
       if (ServerRef.weaponForgeStructuresEnabled()) {
            return;
        }

        RegistryEntry<Structure> entry = context.dynamicRegistryManager()
                .getOrThrow(RegistryKeys.STRUCTURE)
                .getEntry((Structure) (Object) this);

        if (entry.isIn(TagKey.of(RegistryKeys.STRUCTURE, Identifier.of("keegsweapons", "weapon_forge")))) {
            cir.setReturnValue(Optional.empty());
        }
    }
}