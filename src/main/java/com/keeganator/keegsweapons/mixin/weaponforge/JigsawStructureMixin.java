package com.keeganator.keegsweapons.mixin.weaponforge;

import com.keeganator.keegsweapons.gamerules.ServerRef;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

@Mixin(JigsawStructure.class)
public abstract class JigsawStructureMixin {

    @Inject(method = "findGenerationPoint", at = @At("HEAD"), cancellable = true)
    private void keegsweapons$checkGameRule(Structure.GenerationContext context, CallbackInfoReturnable<Optional<Structure.GenerationStub>> cir) {
       if (ServerRef.weaponForgeStructuresEnabled()) {
            return;
        }

        Holder<Structure> entry = context.registryAccess()
                .lookupOrThrow(Registries.STRUCTURE)
                .wrapAsHolder((Structure) (Object) this);

        if (entry.is(TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath("keegsweapons", "weapon_forge")))) {
            cir.setReturnValue(Optional.empty());
        }
    }
}