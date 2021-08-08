package com.nexorel.et.content.blocks.AuraInfestedBlock;

/**
 *  import com.nexorel.et.content.items.Weapons.AuraWand;
 import net.minecraft.block.Block;
 import net.minecraft.block.BlockRenderType;
 import net.minecraft.block.BlockState;
 import net.minecraft.block.material.Material;
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.LivingEntity;
 import net.minecraft.entity.player.PlayerEntity;
 import net.minecraft.particles.ParticleTypes;
 import net.minecraft.potion.EffectInstance;
 import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class AuraInfestedBlock extends Block {

    private static final VoxelShape RENDER_SHAPE = VoxelShapes.empty();

    public AuraInfestedBlock() {
        super(Properties.of(Material.WOOD));
    }

    @Override
    public void stepOn(World world, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (!(player.getItemInHand(Hand.MAIN_HAND).getItem() instanceof AuraWand)) {
                    LivingEntity target = (LivingEntity) entity;
                    target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20, 2));
                    target.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 200, 2));
                }
            } else {
                LivingEntity target = (LivingEntity) entity;
                target.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20, 2));
                target.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 200, 2));
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AuraInfestedTile();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return RENDER_SHAPE;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
    return BlockRenderType.MODEL;
    }

 @Override public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
 world.addParticle(
 ParticleTypes.PORTAL,
 pos.getX(), pos.getY() + 0.5, pos.getZ(),
 0.1, 0.1, 0.1);
 }
 }*/
