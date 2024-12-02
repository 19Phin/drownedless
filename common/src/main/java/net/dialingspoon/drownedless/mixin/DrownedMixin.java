package net.dialingspoon.drownedless.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drowned.class)
public class DrownedMixin {

    @Inject(method = "checkDrownedSpawnRules", at = @At("HEAD"), cancellable = true)
    private static void noSkyAccess(EntityType<Drowned> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource, CallbackInfoReturnable<Boolean> cir) {
        if (!((serverLevelAccessor.getLevel().dimensionType().natural() && (serverLevelAccessor.getLevel().isNight())) || serverLevelAccessor.canSeeSkyFromBelowWater(blockPos))) {
            cir.setReturnValue(false);
        }
    }

}
