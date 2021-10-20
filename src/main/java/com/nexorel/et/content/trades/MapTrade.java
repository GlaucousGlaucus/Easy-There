package com.nexorel.et.content.trades;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public class MapTrade implements VillagerTrades.ItemListing {
    private final int emeraldCost;
    private final StructureFeature<?> destination;
    private final MapDecoration.Type destinationType;
    private final int maxUses;
    private final int villagerXp;

    public MapTrade(int p_35811_, StructureFeature<?> p_35812_, MapDecoration.Type p_35813_, int p_35814_, int p_35815_) {
        this.emeraldCost = p_35811_;
        this.destination = p_35812_;
        this.destinationType = p_35813_;
        this.maxUses = p_35814_;
        this.villagerXp = p_35815_;
    }

    @Nullable
    public MerchantOffer getOffer(Entity p_35817_, Random p_35818_) {
        if (!(p_35817_.level instanceof ServerLevel)) {
            return null;
        } else {
            ServerLevel serverlevel = (ServerLevel) p_35817_.level;
            BlockPos blockpos = serverlevel.findNearestMapFeature(this.destination, p_35817_.blockPosition(), 100, true);
            if (blockpos != null) {
                ItemStack itemstack = MapItem.create(serverlevel, blockpos.getX(), blockpos.getZ(), (byte) 2, true, true);
                MapItem.renderBiomePreviewMap(serverlevel, itemstack);
                MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", this.destinationType);
                itemstack.setHoverName(new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), itemstack, this.maxUses, this.villagerXp, 0.2F);
            } else {
                return null;
            }
        }
    }
}
