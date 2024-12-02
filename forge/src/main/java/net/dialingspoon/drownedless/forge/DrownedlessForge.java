package net.dialingspoon.drownedless.forge;

import dev.architectury.platform.forge.EventBuses;
import net.dialingspoon.drownedless.Drownedless;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Drownedless.MOD_ID)
public final class DrownedlessForge {
    public DrownedlessForge() {
        // Run our common setup.
        Drownedless.init();
    }
}
