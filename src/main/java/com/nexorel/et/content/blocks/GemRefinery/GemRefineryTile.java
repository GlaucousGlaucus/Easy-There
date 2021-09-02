package com.nexorel.et.content.blocks.GemRefinery;

import com.nexorel.et.Registries.BlockEntityInit;
import com.nexorel.et.Registries.RecipeInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class GemRefineryTile extends BlockEntity {

    private ItemStackHandler ih = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> ih);
    private RecipeWrapper wrapper = new RecipeWrapper(ih);
    SimpleContainer inv = new SimpleContainer(wrapper.getItem(0), wrapper.getItem(1), wrapper.getItem(2));
    SimpleContainer inv_in = new SimpleContainer(wrapper.getItem(0), wrapper.getItem(1));
    SimpleContainer inv_r = new SimpleContainer(wrapper.getItem(2));

    public int current_progress;
    public int Max_Time = 100;

    private final RecipeType<? extends GemRefiningRecipe> recipeType;


    public GemRefineryTile(BlockPos pos, BlockState state) {
        super(BlockEntityInit.GEM_REFINERY_TILE.get(), pos, state);
        this.recipeType = RecipeInit.GEM_REFINING_TYPE;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    public void tickServer() {
        if (level != null && !level.isClientSide) {
            ItemStack output = this.ih.getStackInSlot(2);

            if (this.getRecipe(this.ih.getStackInSlot(0), this.ih.getStackInSlot(1)) != null) {
                ItemStack Result = this.getRecipe(this.ih.getStackInSlot(0), this.ih.getStackInSlot(1)).getResultItem();
                if (output.getContainerItem().getCount() < 64) {
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
        Set<Recipe<?>> recipes = findRecipesByType(RecipeInit.GEM_REFINING_TYPE, this.level);
        for (Recipe<?> iRecipe : recipes) {
            GemRefiningRecipe recipe = (GemRefiningRecipe) iRecipe;
            if (recipe.matches(new RecipeWrapper(ih), this.level)) {
                return recipe;
            }
        }

        return null;
    }

    public static Set<Recipe<?>> findRecipesByType(RecipeType<?> typeIn, Level world) {
        return world != null ? world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
    }

    @Override
    public void load(CompoundTag tag) {
        ih.deserializeNBT(tag.getCompound("inv"));
        current_progress = tag.getInt("current_progress");
        super.load(tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
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
