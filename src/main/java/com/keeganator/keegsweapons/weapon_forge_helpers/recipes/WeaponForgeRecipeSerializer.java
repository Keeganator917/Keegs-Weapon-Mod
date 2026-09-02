package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WeaponForgeRecipeSerializer implements RecipeSerializer<WeaponForgeRecipe> {

    public static final MapCodec<WeaponForgeRecipe> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(

                    Codec.STRING.fieldOf("weapon_type")
                            .xmap(WeaponType::fromId, WeaponType::id)
                            .forGetter(WeaponForgeRecipe::getWeaponType),

                    Codec.STRING.listOf()
                            .fieldOf("pattern")
                            .forGetter(WeaponForgeRecipe::getPattern),

                    Codec.unboundedMap(Codec.STRING, Ingredient.CODEC)
                            .fieldOf("key")
                            .forGetter(WeaponForgeRecipe::getKeyMap),

                    ItemStack.VALIDATED_CODEC
                            .fieldOf("result")
                            .forGetter(WeaponForgeRecipe::getOutput)

            ).apply(instance, WeaponForgeRecipeSerializer::readFromMap));

    private static WeaponForgeRecipe readFromMap(WeaponType weaponType, List<String> pattern, Map<String, Ingredient> keys, ItemStack output) {
        int height = pattern.size();
        int maxWidth = 0;
        for (String row : pattern) {
            maxWidth = Math.max(maxWidth, row.length());
        }

        final int width = maxWidth;
        DefaultedList<Optional<Ingredient>> ingredients = DefaultedList.ofSize(width * height, Optional.empty());

        try {
            for (int y = 0; y < height; y++) {
                String row = pattern.get(y);
                for (int x = 0; x < row.length(); x++) {
                    char c = row.charAt(x);
                    if (c != ' ') {
                        Ingredient ing = keys.get(String.valueOf(c));
                        if (ing == null) {
                            throw new IllegalArgumentException("Recipe " + weaponType.id() + " is missing key: " + c);
                        }
                        ingredients.set(x + y * width, Optional.of(ing));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse Weapon Forge Recipe: " + e.getMessage());
            throw e;
        }

        return new WeaponForgeRecipe(weaponType, width, height, ingredients, output, pattern, keys);
    }

    private static void write(RegistryByteBuf buf, WeaponForgeRecipe recipe) {
        buf.writeString(recipe.getWeaponType().id());
        buf.writeVarInt(recipe.getWidth());
        buf.writeVarInt(recipe.getHeight());

        for (Optional<Ingredient> ing : recipe.getIngredients()) {
            buf.writeBoolean(ing.isPresent());
            ing.ifPresent(i -> Ingredient.PACKET_CODEC.encode(buf, i));
        }

        ItemStack.PACKET_CODEC.encode(buf, recipe.getOutput());
    }

    private static WeaponForgeRecipe read(RegistryByteBuf buf) {
        WeaponType weaponType = WeaponType.fromId(buf.readString());

        int width = buf.readVarInt();
        int height = buf.readVarInt();

        DefaultedList<Optional<Ingredient>> ingredients =
                DefaultedList.ofSize(width * height, Optional.empty());

        for (int i = 0; i < ingredients.size(); i++) {
            if (buf.readBoolean()) {
                ingredients.set(i, Optional.of(Ingredient.PACKET_CODEC.decode(buf)));
            }
        }

        ItemStack output = ItemStack.PACKET_CODEC.decode(buf);

        return new WeaponForgeRecipe(weaponType, width, height, ingredients, output, List.of(), Map.of());
    }

    @Override
    public MapCodec<WeaponForgeRecipe> codec() {
        return CODEC;
    }

    public static final PacketCodec<RegistryByteBuf, WeaponForgeRecipe> PACKET_CODEC =
            PacketCodec.ofStatic(WeaponForgeRecipeSerializer::write, WeaponForgeRecipeSerializer::read);

    @Override
    public PacketCodec<RegistryByteBuf, WeaponForgeRecipe> packetCodec() {
        return PACKET_CODEC;
    }
}