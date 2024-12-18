/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.houraielixir.common.event;

import moriyashiine.houraielixir.common.HouraiElixir;
import moriyashiine.houraielixir.common.component.entity.HouraiComponent;
import moriyashiine.houraielixir.common.init.ModEntityComponents;
import moriyashiine.houraielixir.common.init.ModSoundEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class HouraiEvent implements ServerLivingEntityEvents.AllowDeath {
	@Override
	public boolean allowDeath(LivingEntity entity, DamageSource damageSource, float damageAmount) {
		if (HouraiElixir.isImmortal(entity)) {
			entity.getWorld().playSound(null, entity.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_RESURRECT, entity.getSoundCategory(), 1, 1);
			if (entity.getY() <= entity.getWorld().getBottomY() && damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
				ServerWorld world = entity.getWorld().getServer().getOverworld();
				BlockPos spawnPos = world.getSpawnPos();
				if (entity instanceof ServerPlayerEntity player) {
					world = entity.getWorld().getServer().getWorld(player.getSpawnPointDimension());
					BlockPos playerSpawnPos = player.getSpawnPointPosition();
					if (playerSpawnPos != null) {
						spawnPos = playerSpawnPos;
					}
				}
				entity.teleportTo(new TeleportTarget(world, Vec3d.ofCenter(spawnPos), Vec3d.ZERO, entity.getHeadYaw(), entity.getPitch(), TeleportTarget.NO_OP));
			}
			entity.setHealth(entity.getMaxHealth());
			HouraiComponent houraiComponent = ModEntityComponents.HOURAI.get(entity);
			houraiComponent.setWeaknessTimer(Math.min(houraiComponent.getWeaknessTimer() + 400, 1600));
			return false;
		}
		return true;
	}
}
