package io.github.cpearl0.daybreakcore.data.language;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.registry.DBCItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {
    public EnglishLanguageProvider(PackOutput output) {
        super(output, DaybreakCore.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.DaybreakCore", "Daybreak Core");
        addItem(DBCItems.TRIAL_EYE, "Eye of Trial");
        addItem(DBCItems.SUN_ROCKET, "Daybreak Rocket");
    }
}
