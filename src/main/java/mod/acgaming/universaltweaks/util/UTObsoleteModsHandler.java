package mod.acgaming.universaltweaks.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraftforge.fml.client.CustomModLoadingErrorDisplayException;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.acgaming.universaltweaks.config.UTConfig;

public class UTObsoleteModsHandler
{
    public static void throwIncompatibility()
    {
        List<String> messages = new ArrayList<>();
        messages.add("Universal Tweaks has replaced and improved upon functionalities from the following mods.");
        messages.add("Therefore, these mods are now incompatible with Universal Tweaks:");
        messages.add("");

        if (Loader.isModLoaded("aiimprovements") && (UTConfig.tweaks.utAIReplacementToggle || UTConfig.tweaks.utAIRemovalToggle)) messages.add("AI Improvements");
        if (Loader.isModLoaded("attributefix") && UTConfig.tweaks.utAttributesToggle) messages.add("AttributeFix");
        if (Loader.isModLoaded("bedbreakbegone") && UTConfig.tweaks.utBedObstructionToggle) messages.add("BedBreakBegone");
        if (Loader.isModLoaded("blockfire") && UTConfig.bugfixes.utBlockFireToggle) messages.add("BlockFire");
        if (Loader.isModLoaded("blockoverlayfix") && UTConfig.bugfixes.utBlockOverlayToggle) messages.add("Block Overlay Fix");
        if (Loader.isModLoaded("bottomsugarcanharvest") && UTConfig.tweaks.utSugarCaneSize != 3) messages.add("Bottom Sugar Cane Harvest");
        if (Loader.isModLoaded("bowinfinityfix") && UTConfig.tweaks.utBowInfinityToggle) messages.add("Bow Infinity Fix");
        if (Loader.isModLoaded("chunkgenlimit") && UTConfig.tweaks.utChunkGenLimitToggle) messages.add("Chunk Generation Limiter");
        if (Loader.isModLoaded("classiccombat") && UTConfig.tweaks.utAttackCooldownToggle) messages.add("Classic Combat");
        if (Loader.isModLoaded("collisiondamage") && UTConfig.tweaks.utCollisionDamageToggle) messages.add("Collision Damage");
        if (Loader.isModLoaded("configurablecane") && UTConfig.tweaks.utSugarCaneSize != 3) messages.add("Configurable Cane");
        if (Loader.isModLoaded("continousmusic") && UTConfig.tweaks.utInfiniteMusicToggle) messages.add("Infinite Music");
        if (Loader.isModLoaded("creeperconfetti") && UTConfig.tweaks.utCreeperConfettiToggle) messages.add("Creeper Confetti");
        if (Loader.isModLoaded("damagetilt") && UTConfig.tweaks.utDamageTiltToggle) messages.add("Damage Tilt");
        if (Loader.isModLoaded("entity_desync_fix") && UTConfig.bugfixes.utEntityDesyncToggle) messages.add("EntityDesyncFix");
        if (Loader.isModLoaded("experiencebugfix") && UTConfig.bugfixes.utDimensionChangeToggle) messages.add("Fix Experience Bug");
        if (Loader.isModLoaded("fastleafdecay") && UTConfig.tweaks.utLeafDecayToggle) messages.add("Fast Leaf Decay");
        if (Loader.isModLoaded("fencejumper") && UTConfig.tweaks.utFenceWallJumpToggle) messages.add("Fence Jumper");
        if (Loader.isModLoaded("givemebackmyhp") && UTConfig.bugfixes.utMaxHealthToggle) messages.add("Give Me Back My HP");
        if (Loader.isModLoaded("helpfixer") && UTConfig.bugfixes.utHelpToggle) messages.add("HelpFixer");
        if (Loader.isModLoaded("leafdecay") && UTConfig.tweaks.utLeafDecayToggle) messages.add("Leaf Decay Accelerator");
        if (Loader.isModLoaded("letmedespawn") && UTConfig.tweaks.utMobDespawnToggle) messages.add("Let Me Despawn");
        if (Loader.isModLoaded("loginhpfix") && UTConfig.bugfixes.utMaxHealthToggle) messages.add("Login HP Fix");
        if (Loader.isModLoaded("mendingfix") && UTConfig.tweaks.utMendingToggle) messages.add("Mending Fix");
        if (Loader.isModLoaded("norecipebook") && UTConfig.tweaks.utRecipeBookToggle) messages.add("No Recipe Book");
        if (Loader.isModLoaded("overpowered_mending") && UTConfig.tweaks.utMendingOPToggle) messages.add("Overpowered Mending");
        if (Loader.isModLoaded("quickleafdecay") && UTConfig.tweaks.utLeafDecayToggle) messages.add("Quick Leaf Decay");
        if (Loader.isModLoaded("savemystronghold") && UTConfig.tweaks.utStrongholdToggle) messages.add("Save My Stronghold!");
        if (Loader.isModLoaded("stepupfix") && UTConfig.tweaks.utAutoJumpToggle) messages.add("StepupFixer");
        if (Loader.isModLoaded("surge")) messages.add("Surge");
        if (Loader.isModLoaded("tconfixes")) messages.add("TConFixes");
        if (Loader.isModLoaded("tidychunk") && UTConfig.tweaks.utTidyChunkToggle) messages.add("TidyChunk");
        if (Loader.isModLoaded("unloader") && UTConfig.tweaks.utUnloaderToggle) messages.add("Unloader");

        try
        {
            if (UTConfig.bugfixes.utLocaleToggle)
            {
                Class.forName("io.github.jikuja.LocaleTweaker");
                messages.add("LocaleFixer");
            }
        }
        catch (ClassNotFoundException ignored) {}

        if (messages.size() > 3)
        {
            if (FMLLaunchHandler.side() == Side.CLIENT
                && UTConfig.debug.utObsoleteModsToggle
                && !Loader.isModLoaded("enderio")
                && !Loader.isModLoaded("gregtech"))
            {
                UTObsoleteModsHandler.throwException(messages);
            }
            else
            {
                messages.add("");
                throw new RuntimeException(String.join(System.lineSeparator(), messages));
            }
        }
    }

    public static void throwException(List<String> messages)
    {
        throw new Exception(messages);
    }

    @SideOnly(Side.CLIENT)
    private static class Exception extends CustomModLoadingErrorDisplayException
    {
        private final List<String> messages;

        public Exception(List<String> messages)
        {
            this.messages = messages;
        }

        @Override
        public void initGui(GuiErrorScreen errorScreen, FontRenderer fontRenderer) {}

        @Override
        public void drawScreen(GuiErrorScreen errorScreen, FontRenderer fontRenderer, int mouseRelX, int mouseRelY, float tickTime)
        {
            int x = errorScreen.width / 2;
            int y = 75;
            for (String message : messages)
            {
                errorScreen.drawCenteredString(fontRenderer, message, x, y, 0xFFFFFF);
                y += 15;
            }
        }
    }
}