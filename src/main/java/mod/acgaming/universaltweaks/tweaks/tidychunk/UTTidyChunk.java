package mod.acgaming.universaltweaks.tweaks.tidychunk;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import mod.acgaming.universaltweaks.UniversalTweaks;
import mod.acgaming.universaltweaks.config.UTConfig;

// Courtesy of OreCruncher
@Mod.EventBusSubscriber(modid = UniversalTweaks.MODID)
public class UTTidyChunk
{
    public static Int2ObjectArrayMap<UTWorldContext> worldData = new Int2ObjectArrayMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void utOnWorldUnload(WorldEvent.Unload event)
    {
        if (!UTConfig.TWEAKS_WORLD.utTidyChunkToggle) return;
        if (UTConfig.DEBUG.utDebugToggle) UniversalTweaks.LOGGER.debug("UTTidyChunk ::: World unload event");
        World world = event.getWorld();
        if (world.isRemote) return;
        getWorldContext(world).searchAndDestroy(world);
        worldData.remove(world.provider.getDimension());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void utOnWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!UTConfig.TWEAKS_WORLD.utTidyChunkToggle) return;
        if (UTConfig.DEBUG.utDebugToggle) UniversalTweaks.LOGGER.debug("UTTidyChunk ::: World tick event");
        if (event.side != Side.SERVER || event.phase != Phase.END) return;
        UTWorldContext ctx = getWorldContext(event.world);
        ctx.searchAndDestroy(event.world);
        ctx.removeOldContext(event.world);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void utOnChunkPopulate(PopulateChunkEvent.Pre event)
    {
        if (!UTConfig.TWEAKS_WORLD.utTidyChunkToggle) return;
        if (UTConfig.DEBUG.utDebugToggle) UniversalTweaks.LOGGER.debug("UTTidyChunk ::: Populate chunk event");
        if (event.getWorld().isRemote) return;
        UTWorldContext ctx = getWorldContext(event.getWorld());
        ctx.add(new ChunkPos(event.getChunkX(), event.getChunkZ()), event.getWorld());
    }

    @SubscribeEvent
    public static void utOnEntityJoin(EntityJoinWorldEvent event)
    {
        if (!UTConfig.TWEAKS_WORLD.utTidyChunkToggle) return;
        if (UTConfig.DEBUG.utDebugToggle) UniversalTweaks.LOGGER.debug("UTTidyChunk ::: Entity join event");
        Entity entity = event.getEntity();
        World world = entity.getEntityWorld();
        if (world.isRemote || !UTWorldContext.isTargetEntity(entity)) return;
        UTWorldContext ctx = getWorldContext(world);
        if (ctx.isContained(entity))
        {
            ctx.removeEntity(entity);
            event.setCanceled(true);
        }
    }

    public static UTWorldContext createWorldContext(World world)
    {
        UTWorldContext ctx;
        worldData.put(world.provider.getDimension(), ctx = new UTWorldContext());
        return ctx;
    }

    public static UTWorldContext getWorldContext(World world)
    {
        UTWorldContext ctx = worldData.get(world.provider.getDimension());
        return ctx == null ? createWorldContext(world) : ctx;
    }
}