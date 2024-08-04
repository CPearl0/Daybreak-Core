package io.github.cpearl0.daybreakcore.event;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.network.ClientPayloadHandler;
import io.github.cpearl0.daybreakcore.network.TimeStateMessage;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
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
}
