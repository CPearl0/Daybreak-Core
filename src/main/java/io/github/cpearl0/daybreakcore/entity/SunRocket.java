package io.github.cpearl0.daybreakcore.entity;

import io.github.cpearl0.daybreakcore.registry.DBCEntityTypes;
import io.github.cpearl0.daybreakcore.world.DaybreakSavedData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SunRocket extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(SunRocket.class, EntityDataSerializers.ITEM_STACK);

    private int life = 0;

    public SunRocket(EntityType<? extends SunRocket> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SunRocket(Level pLevel, double pX, double pY, double pZ) {
        this(DBCEntityTypes.SUN_ROCKET.get(), pLevel);
        setPos(pX, pY, pZ);
        setDeltaMovement(0, 1, 0);
    }

    public void setItem(ItemStack pStack) {
        if (pStack.isEmpty()) {
            getEntityData().set(DATA_ITEM_STACK, getDefaultItem());
        } else {
            getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
        }
    }

    @Override
    public @NotNull ItemStack getItem() {
        return getEntityData().get(DATA_ITEM_STACK);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_ITEM_STACK, getDefaultItem());
    }

    /**
     * Checks if the entity is in range to render.
     */
    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = getBoundingBox().getSize() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }

        d0 *= 64.0;
        return pDistance < d0 * d0;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = getDeltaMovement();
        double nx = getX() + vec3.x;
        double ny = getY() + vec3.y;
        double nz = getZ() + vec3.z;

       level().addParticle(
               ParticleTypes.PORTAL,
               nx - vec3.x * 0.25 + random.nextDouble() * 0.6 - 0.3,
               ny - vec3.y * 0.25 - 0.5,
               nz - vec3.z * 0.25 + random.nextDouble() * 0.6 - 0.3,
               vec3.x,
               vec3.y,
               vec3.z
       );

        if (!level().isClientSide) {
            setPos(nx, ny, nz);
            life++;
            if (life > 80) {
                playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, 1.0F);
                var serverLevel = (ServerLevel) level();
                serverLevel.getDataStorage().set("time_state", new DaybreakSavedData(DaybreakSavedData.NORMAL));
                discard();
            }
        } else {
            setPosRaw(nx, ny, nz);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("Item", getItem().save(registryAccess()));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Item", Tag.TAG_COMPOUND)) {
            setItem(ItemStack.parse(registryAccess(), pCompound.getCompound("Item")).orElse(getDefaultItem()));
        } else {
            setItem(getDefaultItem());
        }
    }

    private ItemStack getDefaultItem() {
        return new ItemStack(Items.ENDER_EYE);
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean isAttackable() {
        return false;
    }
}
