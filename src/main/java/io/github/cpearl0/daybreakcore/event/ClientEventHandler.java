package io.github.cpearl0.daybreakcore.event;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.network.ClientPayloadHandler;
import io.github.cpearl0.daybreakcore.network.TimeStateMessage;
import io.github.cpearl0.daybreakcore.registry.DBCEntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = DaybreakCore.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToClient(
                TimeStateMessage.TYPE,
                TimeStateMessage.STREAM_CODEC,
                ClientPayloadHandler::handleTimeStateMessage
        );
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DBCEntityTypes.EYE_OF_TRIAL.get(), pContext -> new ThrownItemRenderer<>(pContext, 1.0F, true));
        event.registerEntityRenderer(DBCEntityTypes.SUN_ROCKET.get(), pContext -> new ThrownItemRenderer<>(pContext, 5.0F, true));
    }
}
