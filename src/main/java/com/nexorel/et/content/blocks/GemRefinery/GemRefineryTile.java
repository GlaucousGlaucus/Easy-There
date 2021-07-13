package com.nexorel.et.content.blocks.GemRefinery;

import com.nexorel.et.Registries.RecipeInit;
import com.nexorel.et.Registries.TileInit;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DModel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class GemRefineryTile extends TileEntity implements ITickableTileEntity {

    private ItemStackHandler ih = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> ih);
    private RecipeWrapper wrapper = new RecipeWrapper(ih);
    Inventory inv = new Inventory(wrapper.getItem(0), wrapper.getItem(1), wrapper.getItem(2));
    Inventory inv_in = new Inventory(wrapper.getItem(0), wrapper.getItem(1));
    Inventory inv_r = new Inventory(wrapper.getItem(2));

    public int current_progress;
    public int Max_Time = 100;

    private final IRecipeType<? extends GemRefiningRecipe> recipeType;


    public GemRefineryTile() {
        super(TileInit.GEM_REFINERY_TILE.get());
        this.recipeType = RecipeInit.GEM_REFINING_TYPE;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public void tick() {
        System.out.print("World");
        if (level != null && !level.isClientSide) {
            ItemStack output = this.ih.getStackInSlot(2);

            if (this.getRecipe(this.ih.getStackInSlot(0), this.ih.getStackInSlot(1)) != null) {
                ItemStack Result = this.getRecipe(this.ih.getStackInSlot(0), this.ih.getStackInSlot(1)).getResultItem();
                if (output.getStack().getCount() < 64) {
                    if (output.sameItem(Result) || output.isEmpty()) {
//                        if (this.current_progress != this.Max_Time) {
//                            this.current_progress++;
//                            this.setChanged();
//                        } else {
                            this.current_progress = 0;
                            ih.insertItem(2, Result.copy(), false);
                            ih.extractItem(0, 1, false);
                            ih.extractItem(1, 1, false);
                            setChanged();
//                        }
                    }
                }
            }
        }
        this.setChanged();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nullable
    private GemRefiningRecipe getRecipe(ItemStack stack, ItemStack stack1) {
        if (stack == null || stack1 == null) {
            return null;
        }
        Set<IRecipe<?>> recipes = findRecipesByType(RecipeInit.GEM_REFINING_TYPE, this.level);
        for (IRecipe<?> iRecipe : recipes) {
            GemRefiningRecipe recipe = (GemRefiningRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(ih), this.level)) {
                return recipe;
            }
        }

        return null;
    }

    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        ih.deserializeNBT(tag.getCompound("inv"));
        current_progress = tag.getInt("current_progress");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("inv", ih.serializeNBT());
        tag.putInt("currentProgress", current_progress);
        return super.save(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

}
