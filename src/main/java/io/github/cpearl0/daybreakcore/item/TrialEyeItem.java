package io.github.cpearl0.daybreakcore.item;

import io.github.cpearl0.daybreakcore.entity.EyeOfTrial;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TrialEyeItem extends Item {
    public TrialEyeItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pPlayer.startUsingItem(pHand);
        if (pLevel instanceof ServerLevel serverlevel) {
            BlockPos blockpos = serverlevel.findNearestMapStructure(StructureTags.ON_TRIAL_CHAMBERS_MAPS, pPlayer.blockPosition(), 100, false);
            if (blockpos != null) {
                EyeOfTrial eyeoftrial = new EyeOfTrial(pLevel, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ());
                eyeoftrial.setItem(itemstack);
                eyeoftrial.signalTo(blockpos);
                pLevel.gameEvent(GameEvent.PROJECTILE_SHOOT, eyeoftrial.position(), GameEvent.Context.of(pPlayer));
                pLevel.addFreshEntity(eyeoftrial);

                float f = Mth.lerp(pLevel.random.nextFloat(), 0.33F, 0.5F);
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, f);
                itemstack.consume(1, pPlayer);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                pPlayer.swing(pHand, true);
                return InteractionResultHolder.success(itemstack);
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }
}
