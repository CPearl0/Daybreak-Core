package io.github.cpearl0.daybreakcore.world;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DaybreakSavedData extends SavedData {
    public int timeState = NIGHT_POSITIVE;
    public static final int NIGHT_POSITIVE = 0;
    public static final int NIGHT_NEGATIVE = 1;
    public static final int NORMAL = 2;

    public DaybreakSavedData(int timeState) {
        this.timeState = timeState;
    }

    public DaybreakSavedData() {
        this(NIGHT_POSITIVE);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.putInt("state", timeState);
        return pTag;
    }

    public static DaybreakSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        var data = new DaybreakSavedData();
        data.timeState = tag.getInt("state");
        return data;
    }
}
