package moriyashiine.houraielixir.common;

import moriyashiine.houraielixir.api.HouraiElixirAPI;
import moriyashiine.houraielixir.api.component.HouraiComponent;
import moriyashiine.houraielixir.common.registry.ModItems;
import moriyashiine.houraielixir.common.registry.ModSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

@SuppressWarnings("ConstantConditions")
public class HouraiElixir implements ModInitializer {
	public static final String MOD_ID = "houraielixir";
	
	@Override
	public void onInitialize() {
		ModItems.init();
		ModSoundEvents.init();
	}
	
	public static float handleDamage(LivingEntity entity, DamageSource source, float amount) {
		if (!entity.world.isClient && HouraiElixirAPI.isImmortal(entity) && entity.getHealth() - amount <= 0) {
			entity.world.playSound(null, entity.getBlockPos(), ModSoundEvents.ENTITY_GENERIC_RESURRECT, entity.getSoundCategory(), 1, 1);
			if (entity.getY() <= -64 && source == DamageSource.OUT_OF_WORLD) {
				ServerWorld world;
				BlockPos worldSpawnPos;
				if (entity instanceof ServerPlayerEntity player) {
					world = player.world.getServer().getWorld(player.getSpawnPointDimension());
					worldSpawnPos = player.getSpawnPointPosition() == null ? world.getSpawnPos() : player.getSpawnPointPosition();
				}
				else {
					world = entity.world.getServer().getOverworld();
					worldSpawnPos = world.getSpawnPos();
				}
				FabricDimensions.teleport(entity, world, new TeleportTarget(Vec3d.of(worldSpawnPos), Vec3d.ZERO, entity.headYaw, entity.getPitch()));
			}
			entity.setHealth(entity.getMaxHealth());
			HouraiComponent.maybeGet(entity).ifPresent(houraiComponent -> houraiComponent.setWeaknessTimer(Math.min(houraiComponent.getWeaknessTimer() + 400, 1600)));
			return 0;
		}
		return amount;
	}
}
