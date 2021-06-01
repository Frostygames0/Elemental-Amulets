package frostygames0.elementalamulets.core.init;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Frostygames0
 * @date 31.05.2021 20:53
 */
public class OriginBiomeProvider extends BiomeProvider {
    public static final Codec<OriginBiomeProvider> CODEC = RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY)
            .xmap(OriginBiomeProvider::new, OriginBiomeProvider::getRegistry).codec();

    private final Layer genBiomes;
    private static final List<RegistryKey<Biome>> SPAWN = ImmutableList.of(Biomes.TAIGA, Biomes.SNOWY_TAIGA);
    private final Registry<Biome> biomeRegistry;

    public OriginBiomeProvider(Registry<Biome> registry) {
        super(getBiomesToSpawn(registry));
        this.biomeRegistry = registry;
        this.genBiomes = LayerUtil.func_237215_a_(9857, false, 8, 4);
    }

    private static List<Biome> getBiomesToSpawn(Registry<Biome> registry) {
        return SPAWN.stream().map(biome -> registry.getOrDefault(biome.getLocation())).collect(Collectors.toList());
    }
    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    public Registry<Biome> getRegistry() {
        return this.biomeRegistry;
    }

    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return new OriginBiomeProvider(biomeRegistry);
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return genBiomes.func_242936_a(biomeRegistry, x, z);
    }
}
