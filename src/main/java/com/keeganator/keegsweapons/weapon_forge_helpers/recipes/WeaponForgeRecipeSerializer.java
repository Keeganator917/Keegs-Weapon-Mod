package com.keeganator.keegsweapons.weapon_forge_helpers.recipes;

import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponForgeRegistries;
import com.keeganator.keegsweapons.weapon_forge_helpers.WeaponType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class WeaponForgeRecipeSerializer {

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

                    ItemStackTemplate.CODEC.fieldOf("result")
                            .forGetter(WeaponForgeRecipe::getOutput)

            ).apply(instance, WeaponForgeRecipeSerializer::readFromMap));

    private static WeaponForgeRecipe readFromMap(WeaponType weaponType, List<String> pattern, Map<String, Ingredient> keys, ItemStackTemplate output) {
        int height = pattern.size();
        int maxWidth = 0;
        for (String row : pattern) {
            maxWidth = Math.max(maxWidth, row.length());
        }

        final int width = maxWidth;
        NonNullList<Optional<Ingredient>> ingredients = NonNullList.withSize(width * height, Optional.empty());

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

    private static void write(RegistryFriendlyByteBuf buf, WeaponForgeRecipe recipe) {
        buf.writeUtf(recipe.getWeaponType().id());
        buf.writeVarInt(recipe.getWidth());
        buf.writeVarInt(recipe.getHeight());

        for (Optional<Ingredient> ing : recipe.getIngredients()) {
            buf.writeBoolean(ing.isPresent());
            ing.ifPresent(i -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, i));
        }

        ItemStackTemplate.STREAM_CODEC.encode(buf, recipe.getOutput());
    }

    private static WeaponForgeRecipe read(RegistryFriendlyByteBuf buf) {
        WeaponType weaponType = WeaponType.fromId(buf.readUtf());

        int width = buf.readVarInt();
        int height = buf.readVarInt();

        NonNullList<Optional<Ingredient>> ingredients =
                NonNullList.withSize(width * height, Optional.empty());

        for (int i = 0; i < ingredients.size(); i++) {
            if (buf.readBoolean()) {
                ingredients.set(i, Optional.of(Ingredient.CONTENTS_STREAM_CODEC.decode(buf)));
            }
        }

        ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buf);

        return new WeaponForgeRecipe(weaponType, width, height, ingredients, output, List.of(), Map.of());
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, WeaponForgeRecipe> PACKET_CODEC =
            StreamCodec.of(WeaponForgeRecipeSerializer::write, WeaponForgeRecipeSerializer::read);

    public static final RecipeSerializer<WeaponForgeRecipe> INSTANCE = new RecipeSerializer<>(CODEC, PACKET_CODEC);
}