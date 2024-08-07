package io.github.cpearl0.daybreakcore.data.language;

import io.github.cpearl0.daybreakcore.DaybreakCore;
import io.github.cpearl0.daybreakcore.registry.DBCItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ChineseLanguageProvider extends LanguageProvider {
    public ChineseLanguageProvider(PackOutput output) {
        super(output, DaybreakCore.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.DaybreakCore", "破晓：核心");
        addItem(DBCItems.TRIAL_EYE, "试炼之眼");
        addItem(DBCItems.SUN_ROCKET, "破晓火箭");
    }
}
