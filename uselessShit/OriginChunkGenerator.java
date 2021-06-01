package frostygames0.elementalamulets.core.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;

/**
 * @author Frostygames0
 * @date 31.05.2021 21:16
 */
public class OriginChunkGenerator extends ChunkGenerator {
    public static final Codec<Settings> CODEC = RecordCodecBuilder.create(instance -> {
        instance.group(
                Codec.INT.fieldOf("height").forGetter(Settings::getStartHeight),
                Codec.FLOAT.fieldOf("xzVariance").forGetter(Settings::getxzVariance),
                Codec.FLOAT.fieldOf("yVariance").forGetter(Settings::getyVariance)
        ).apply(instance, Settings::new);
    });
    private Settings settings;

    public OriginChunkGenerator(Registry<Biome> registry, Settings settings) {
        super(new OriginBiomeProvider(registry), new DimensionStructuresSettings(false));
        this.settings = settings;
    }

    @Override
    protected Codec<? extends ChunkGenerator> func_230347_a_() {
        return null;
    }

    @Override
    public ChunkGenerator func_230349_a_(long p_230349_1_) {
        return null;
    }

    @Override
    public void generateSurface(WorldGenRegion p_225551_1_, IChunk p_225551_2_) {

    }

    @Override
    public void func_230352_b_(IWorld p_230352_1_, StructureManager p_230352_2_, IChunk p_230352_3_) {

    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public IBlockReader func_230348_a_(int p_230348_1_, int p_230348_2_) {
        return null;
    }

    public static class Settings {
        private final int startHeight;
        private final float xzVariance;
        private final float yVariance;

        public Settings(int startHeight, float xzVariance, float yVariance) {

            this.startHeight = startHeight;
            this.xzVariance = xzVariance;
            this.yVariance = yVariance;
        }
        public int getStartHeight() {
            return startHeight;
        }
        public float getxzVariance() {
            return xzVariance;
        }
        public float getyVariance() {
            return yVariance;
        }
    }
}
