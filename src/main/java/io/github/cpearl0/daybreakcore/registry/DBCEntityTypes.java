package io.github.cpearl0.daybreakcore.registry;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.entity.EyeOfTrial;
import io.github.cpearl0.daybreakcore.entity.SunRocket;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DBCEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, DaybreakCore.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<EyeOfTrial>> EYE_OF_TRIAL = ENTITY_TYPES.register("eye_of_trial",
            resourceLocation -> EntityType.Builder.<EyeOfTrial>of(EyeOfTrial::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(4)
                    .build(resourceLocation.toString()));

    public static final DeferredHolder<EntityType<?>, EntityType<SunRocket>> SUN_ROCKET = ENTITY_TYPES.register("sun_rocket",
            resourceLocation -> EntityType.Builder.<SunRocket>of(SunRocket::new, MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .clientTrackingRange(4)
                    .updateInterval(4)
                    .build(resourceLocation.toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
