package net.dented.personalplayer;

import net.dented.personalplayer.component.ModDataComponentTypes;
import net.dented.personalplayer.item.ModItemGroups;
import net.dented.personalplayer.item.ModItems;
import net.dented.personalplayer.sound.ModSoundEvents;
import net.dented.personalplayer.util.MusicDiscRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalPlayerMod implements ModInitializer {
	public static final String MOD_ID = "personalplayer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModSoundEvents.registerSounds();
		ModItems.registerModItems();
		ModItemGroups.addToItemGroups();
		ModDataComponentTypes.registerModDataComponentTypes();


	}

}