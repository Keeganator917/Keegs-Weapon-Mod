package com.keeganator.keegsweapons.merchant.villager;

import com.keeganator.keegsweapons.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;


public class ModVillagerTrades {

    public static void init() {

        // LEVEL 1
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.WEAPONSMITH, 1,
                factories -> {
                    factories.add((world, entity, random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD, 6),
                                    new ItemStack(ModItems.IRON_DAGGER),
                                    5, 8, 0.02f));

                    factories.add((world, entity, random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD, 8),
                                    new ItemStack(ModItems.IRON_KATANA),
                                    5, 8, 0.02f));
                });

        // LEVEL 2
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.WEAPONSMITH, 2,
                factories -> {
                    factories.add((world, entity, random) ->
                            new TradeOffer(
                                    new TradedItem(Items.EMERALD, 12),
                                    new ItemStack(ModItems.IRON_GREATSWORD),
                                    5, 9, 0.035f));
                });

        // LEVEL 5
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.WEAPONSMITH, 5,
                factories -> {
                    if (!WeaponsmithTrades.LVL5_ITEMS.isEmpty()) {
                        factories.add(WeaponsmithTrades.LVL5_TRADE);
                    }
                });
    }
}