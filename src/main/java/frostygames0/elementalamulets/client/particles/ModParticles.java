/*
 *    This file is part of Elemental Amulets.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

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
        Minecraft.getInstance().particleEngine.register(COMBINATION_PARTICLE.get(), CombinationParticle.Factory::new);
    }
}
