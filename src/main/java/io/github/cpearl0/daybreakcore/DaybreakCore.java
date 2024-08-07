package io.github.cpearl0.daybreakcore;

import com.mojang.logging.LogUtils;
import io.github.cpearl0.daybreakcore.registry.DBCCreativeTabs;
import io.github.cpearl0.daybreakcore.registry.DBCEntityTypes;
import io.github.cpearl0.daybreakcore.registry.DBCItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(DaybreakCore.MODID)
public class DaybreakCore
{
    public static final String MODID = "daybreakcore";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DaybreakCore(IEventBus modBus) {
        DBCItems.register(modBus);
        DBCCreativeTabs.register(modBus);
        DBCEntityTypes.register(modBus);
    }
}
