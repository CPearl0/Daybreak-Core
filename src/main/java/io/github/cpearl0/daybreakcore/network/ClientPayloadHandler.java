package io.github.cpearl0.daybreakcore.network;

import io.github.cpearl0.daybreakcore.data.DaybreakSavedData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {
    public static int timeState = DaybreakSavedData.NIGHT_POSITIVE;
    public static void handleTimeStateMessage(final TimeStateMessage message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            timeState = message.timeState();
        });
    }
}
