package io.github.cpearl0.daybreakcore.entity;

import io.github.cpearl0.daybreakcore.registry.DBCEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EyeOfTrial extends Entity implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(EyeOfTrial.class, EntityDataSerializers.ITEM_STACK);
    private double tx;
    private double ty;
    private double tz;
    private int life;
    private boolean surviveAfterDeath;

    public EyeOfTrial(EntityType<? extends EyeOfTrial> pEntityType, Level pLevel)    {
        super(pEntityType, pLevel);
    }

    public EyeOfTrial(Level pLevel, double pX, double pY, double pZ) {
        this(DBCEntityTypes.EYE_OF_TRIAL.get(), pLevel);
        setPos(pX, pY, pZ);
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

    public void signalTo(BlockPos pPos) {
        double d0 = (double)pPos.getX();
        int i = pPos.getY();
        double d1 = (double)pPos.getZ();
        double d2 = d0 - getX();
        double d3 = d1 - getZ();
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        if (d4 > 12.0) {
            tx = getX() + d2 / d4 * 12.0;
            tz = getZ() + d3 / d4 * 12.0;
            ty = getY() + 8.0;
        } else {
            tx = d0;
            ty = (double)i;
            tz = d1;
        }

        life = 0;
        surviveAfterDeath = random.nextInt(5) > 0;
    }

    /**
     * Updates the entity motion clientside, called by packets from the server
     */
    @Override
    public void lerpMotion(double pX, double pY, double pZ) {
        setDeltaMovement(pX, pY, pZ);
        if (xRotO == 0.0F && yRotO == 0.0F) {
            double d0 = Math.sqrt(pX * pX + pZ * pZ);
            setYRot((float)(Mth.atan2(pX, pZ) * 180.0F / (float)Math.PI));
            setXRot((float)(Mth.atan2(pY, d0) * 180.0F / (float)Math.PI));
            yRotO = getYRot();
            xRotO = getXRot();
        }
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = getDeltaMovement();
        double d0 = getX() + vec3.x;
        double d1 = getY() + vec3.y;
        double d2 = getZ() + vec3.z;
        double d3 = vec3.horizontalDistance();
        setXRot(Projectile.lerpRotation(xRotO, (float)(Mth.atan2(vec3.y, d3) * 180.0F / (float)Math.PI)));
        setYRot(Projectile.lerpRotation(yRotO, (float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI)));
        if (!level().isClientSide) {
            double d4 = tx - d0;
            double d5 = tz - d2;
            float f = (float)Math.sqrt(d4 * d4 + d5 * d5);
            float f1 = (float)Mth.atan2(d5, d4);
            double d6 = Mth.lerp(0.0025, d3, (double)f);
            double d7 = vec3.y;
            if (f < 1.0F) {
                d6 *= 0.8;
                d7 *= 0.8;
            }

            int j = getY() < ty ? 1 : -1;
            vec3 = new Vec3(Math.cos((double)f1) * d6, d7 + ((double)j - d7) * 0.015F, Math.sin((double)f1) * d6);
            setDeltaMovement(vec3);
        }

        float f2 = 0.25F;
        if (isInWater()) {
            for (int i = 0; i < 4; i++) {
                level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25, d1 - vec3.y * 0.25, d2 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }
        } else {
            level().addParticle(
                            ParticleTypes.PORTAL,
                            d0 - vec3.x * 0.25 + random.nextDouble() * 0.6 - 0.3,
                            d1 - vec3.y * 0.25 - 0.5,
                            d2 - vec3.z * 0.25 + random.nextDouble() * 0.6 - 0.3,
                            vec3.x,
                            vec3.y,
                            vec3.z
                    );
        }

        if (!level().isClientSide) {
            setPos(d0, d1, d2);
            life++;
            if (life > 80 && !level().isClientSide) {
                playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F);
                discard();
                if (surviveAfterDeath) {
                    level().addFreshEntity(new ItemEntity(level(), getX(), getY(), getZ(), getItem()));
                } else {
                    level().levelEvent(LevelEvent.PARTICLES_EYE_OF_ENDER_DEATH, blockPosition(), 0);
                }
            }
        } else {
            setPosRaw(d0, d1, d2);
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
