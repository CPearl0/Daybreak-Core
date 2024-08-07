package io.github.cpearl0.daybreakcore.registry;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DBCCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DaybreakCore.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main_tab", () ->CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.DaybreakCore"))
            .icon(() -> new ItemStack(DBCItems.TRIAL_EYE.asItem()))
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(DBCItems.TRIAL_EYE.asItem());
                pOutput.accept(DBCItems.SUN_ROCKET.asItem());
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
