package com.keeganator.keegsweapons.merchant.villager;

import com.keeganator.keegsweapons.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WeaponsmithTrades {

    public static List<RandomisedTradeItem> LVL5_ITEMS = new ArrayList<>();
    public static TradeOffers.Factory LVL5_TRADE = (world, entity, random) -> {
        if (LVL5_ITEMS.isEmpty()) return null;

        RandomisedTradeItem item = LVL5_ITEMS.get(random.nextInt(LVL5_ITEMS.size()));

        ItemStack enchantedStack = EnchantmentHelper.enchant(
                random,
                new ItemStack(item.item()),
                5 + random.nextInt(15),
                world.getRegistryManager(),
                Optional.empty()
        );

        return new TradeOffer(
                new TradedItem(Items.EMERALD, item.emeraldCost()),
                enchantedStack,
                5,
                30,
                0.2f
        );
    };

    public static void initTradeLists() {
        LVL5_ITEMS.clear();
        add(ModItems.DIAMOND_DAGGER, 24);
        add(ModItems.DIAMOND_SCYTHE, 36);
        add(ModItems.DIAMOND_GREATSWORD, 48);
        add(ModItems.DIAMOND_KATANA, 32);
    }

    private static void add(Item item, int emeraldCost) {
        LVL5_ITEMS.add(new RandomisedTradeItem(item, emeraldCost));
    }

    public record RandomisedTradeItem(Item item, int emeraldCost) {}
}