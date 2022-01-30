package frostygames0.elementalamulets.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;


/**
 * @author Frostygames0
 * @date 30.01.2022 1:13
 */
// TODO: I'm still thinking about it....
public final class WorldUtil {
    public static RegistryKey<Biome> getBiomeAtPos(World level, BlockPos pos) {
        return level.getBiomeName(pos).orElseThrow(() -> new NullPointerException("Cannot identify biome at: " + level.dimension().getRegistryName() + ", pos: [" + pos.toShortString() + "]!"));
    }

    public static boolean isType(RegistryKey<Biome> biome, BiomeDictionary.Type... types) {
        for (BiomeDictionary.Type type : types) {
            if (!BiomeDictionary.hasType(biome, type))
                return false;
        }
        return true;
    }

    public static boolean isNatural(World level, BlockPos pos) {
        RegistryKey<Biome> biome = getBiomeAtPos(level, pos);
        return (level.dimensionType().natural() || BiomeDictionary.hasType(biome, BiomeDictionary.Type.OVERWORLD))
                && !isType(biome, BiomeDictionary.Type.DRY, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.DEAD);
    }
}
