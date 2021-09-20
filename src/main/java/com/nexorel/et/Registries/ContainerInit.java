package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GRC;
import com.nexorel.et.content.blocks.GemRefinery.GemRefineryTile;
import com.nexorel.et.content.items.talisBag.TalismanBagContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MODID;

public class ContainerInit {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public static final RegistryObject<MenuType<GRC>> GRC = CONTAINERS.register("gem_refinery", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new GRC(windowId, inv, inv.player, (GemRefineryTile) world.getBlockEntity(pos));
    }));

    public static final RegistryObject<MenuType<TalismanBagContainer>> TBC = CONTAINERS.register("talisman_bag", () -> IForgeContainerType.create((windowId, inv, data) -> new TalismanBagContainer(windowId, inv, inv.player)));

    /*public static final RegistryObject<MenuType<QuestionContainer>> QUESTION_CONTAINER = CONTAINERS.register("question_block", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new QuestionContainer(windowId, inv, inv.player, (QuestionBE) world.getBlockEntity(pos));
    }));*/
}
