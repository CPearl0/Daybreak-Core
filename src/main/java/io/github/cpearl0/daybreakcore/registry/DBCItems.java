package io.github.cpearl0.daybreakcore.registry;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.item.SunRocketItem;
import io.github.cpearl0.daybreakcore.item.TrialEyeItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DBCItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(DaybreakCore.MODID);

    public static final DeferredItem<TrialEyeItem> TRIAL_EYE = ITEMS.register("trial_eye", () -> new TrialEyeItem(new Item.Properties()));
    public static final DeferredItem<SunRocketItem> SUN_ROCKET = ITEMS.register("sun_rocket", () -> new SunRocketItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
