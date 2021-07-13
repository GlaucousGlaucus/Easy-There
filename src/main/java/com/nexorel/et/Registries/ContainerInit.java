package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GRC;
import com.nexorel.et.content.blocks.GemRefinery.GemRefineryTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class ContainerInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    public static final RegistryObject<ContainerType<GRC>> GRC = CONTAINERS.register("gem_refinery", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getCommandSenderWorld();
        return new GRC(windowId, inv, inv.player, (GemRefineryTile) world.getBlockEntity(pos));
    }));
}
