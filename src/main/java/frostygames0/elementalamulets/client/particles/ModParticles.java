package frostygames0.elementalamulets.client.particles;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Frostygames0
 * @date 03.06.2021 18:58
 */
public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<BasicParticleType> COMBINATION_PARTICLE = PARTICLES.register("combination",
            () -> new BasicParticleType(true));

    public static void register() {
        Minecraft.getInstance().particles.registerFactory(COMBINATION_PARTICLE.get(), CombinationParticle.Factory::new);
    }
}
