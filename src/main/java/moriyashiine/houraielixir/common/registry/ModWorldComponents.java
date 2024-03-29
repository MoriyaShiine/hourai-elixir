/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.houraielixir.common.registry;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import moriyashiine.houraielixir.common.HouraiElixir;
import moriyashiine.houraielixir.common.component.world.ImmortalEntitiesComponent;

public class ModWorldComponents implements WorldComponentInitializer {
	public static final ComponentKey<ImmortalEntitiesComponent> IMMORTAL_ENTITIES = ComponentRegistry.getOrCreate(HouraiElixir.id("immortal_entities"), ImmortalEntitiesComponent.class);

	@Override
	public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
		registry.register(IMMORTAL_ENTITIES, world -> new ImmortalEntitiesComponent());
	}
}
