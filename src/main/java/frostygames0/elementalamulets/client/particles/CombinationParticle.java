package frostygames0.elementalamulets.client.particles;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

/**
 * @author Frostygames0
 * @date 03.06.2021 18:40
 */
public class CombinationParticle extends PortalParticle {

    public CombinationParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.particleRed = 1;
        this.particleGreen = 1;
        this.particleBlue = 1;
    }

    public static class Factory implements IParticleFactory<BasicParticleType> {

        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            PortalParticle portalparticle = new CombinationParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            portalparticle.selectSpriteRandomly(this.spriteSet);
            return portalparticle;
        }
    }


}
