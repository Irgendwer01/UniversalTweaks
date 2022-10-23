package mod.acgaming.universaltweaks.core;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.Side;

import mod.acgaming.universaltweaks.UniversalTweaks;
import mod.acgaming.universaltweaks.config.UTConfig;
import zone.rong.mixinbooter.IEarlyMixinLoader;

@IFMLLoadingPlugin.Name("UniversalTweaksCore")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class UTLoadingPlugin implements IFMLLoadingPlugin, IEarlyMixinLoader
{
    public static final boolean isClient = FMLLaunchHandler.side() == Side.CLIENT;

    static
    {
        if (UTConfig.bugfixes.utLocaleToggle && Locale.getDefault().getLanguage().equals("tr"))
        {
            UniversalTweaks.LOGGER.info("The locale is Turkish, which is unfortunately not supported by some mods. Changing to English...");
            Locale.setDefault(Locale.ENGLISH);
        }
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }

    @Override
    public List<String> getMixinConfigs()
    {
        return isClient ? Arrays.asList(
            "mixins.bugfixes.blockoverlay.json",
            "mixins.bugfixes.dimensionchange.json",
            "mixins.bugfixes.entityaabb.json",
            "mixins.bugfixes.ladderflying.json",
            "mixins.bugfixes.pistontile.json",
            "mixins.bugfixes.skeletonaim.json",
            "mixins.bugfixes.teloadorder.json",
            "mixins.tweaks.attributes.json",
            "mixins.tweaks.autojump.json",
            "mixins.tweaks.bedobstruction.json",
            "mixins.tweaks.falldamage.json",
            "mixins.tweaks.mobdespawn.json") :
            Arrays.asList(
                "mixins.bugfixes.dimensionchange.json",
                "mixins.bugfixes.entityaabb.json",
                "mixins.bugfixes.ladderflying.json",
                "mixins.bugfixes.pistontile.json",
                "mixins.bugfixes.skeletonaim.json",
                "mixins.bugfixes.teloadorder.json",
                "mixins.tweaks.attributes.json",
                "mixins.tweaks.bedobstruction.json",
                "mixins.tweaks.falldamage.json",
                "mixins.tweaks.mobdespawn.json"
            );
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig)
    {
        if (isClient)
        {
            switch (mixinConfig)
            {
                case "mixins.bugfixes.blockoverlay.json":
                    return UTConfig.bugfixes.utBlockOverlayToggle;
                case "mixins.tweaks.autojump.json":
                    return UTConfig.tweaks.utAutoJumpToggle;
            }
        }
        switch (mixinConfig)
        {
            case "mixins.bugfixes.dimensionchange.json":
                return UTConfig.bugfixes.utDimensionChangeToggle;
            case "mixins.bugfixes.entityaabb.json":
                return UTConfig.bugfixes.utEntityAABBToggle;
            case "mixins.bugfixes.ladderflying.json":
                return UTConfig.bugfixes.utLadderFlyingToggle;
            case "mixins.bugfixes.pistontile.json":
                return UTConfig.bugfixes.utPistonTileToggle;
            case "mixins.bugfixes.skeletonaim.json":
                return UTConfig.bugfixes.utSkeletonAimToggle;
            case "mixins.bugfixes.teloadorder.json":
                return UTConfig.bugfixes.utTELoadOrderToggle;
            case "mixins.tweaks.attributes.json":
                return UTConfig.tweaks.utAttributesToggle;
            case "mixins.tweaks.bedobstruction.json":
                return UTConfig.tweaks.utBedObstructionToggle;
            case "mixins.tweaks.falldamage.json":
                return UTConfig.tweaks.utFallDamageToggle;
            case "mixins.tweaks.mobdespawn.json":
                return UTConfig.tweaks.utMobDespawnToggle;
        }
        return true;
    }
}