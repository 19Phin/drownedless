package net.dialingspoon.drownedless.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drowned.class)
public class DrownedMixin {

    @Inject(method = "checkDrownedSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void noSkyAccess(EntityType<Drowned> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
        if (!((serverLevelAccessor.getLevel().dimensionType().natural() && (serverLevelAccessor.getLevel().isNight())) || drownedless$canSeeSkyFromBelowWater(serverLevelAccessor, blockPos))) {
            cir.setReturnValue(false);
        }
    }

    @ModifyConstant(method = "checkDrownedSpawnRules", constant = @Constant(intValue = 40))
    private static int lessCommon(int constant) {
        return 120;
    }

    @Unique
    private static boolean drownedless$canSeeSkyFromBelowWater(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos) {
        if (blockPos.getY() >= serverLevelAccessor.getSeaLevel()) {
            return drownedless$canSeeSky(serverLevelAccessor, blockPos);
        } else {
            BlockPos blockPos2 = new BlockPos(blockPos.getX(), serverLevelAccessor.getSeaLevel(), blockPos.getZ());
            if (!drownedless$canSeeSky(serverLevelAccessor, blockPos2)) {
                return false;
            } else {
                for(blockPos2 = blockPos2.below(); blockPos2.getY() > blockPos.getY(); blockPos2 = blockPos2.below()) {
                    BlockState blockState = serverLevelAccessor.getBlockState(blockPos2);
                    if (blockState.getLightBlock(serverLevelAccessor, blockPos2) > 0 && !blockState.liquid()) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    @Unique
    private static boolean drownedless$canSeeSky(ServerLevelAccessor serverLevelAccessor, BlockPos arg) {
        return serverLevelAccessor.getBrightness(LightLayer.SKY, arg) >= 0;
    }

}
