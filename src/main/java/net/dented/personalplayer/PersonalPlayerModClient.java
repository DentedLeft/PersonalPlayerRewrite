package net.dented.personalplayer;

import net.dented.personalplayer.util.ModModelPredicateProvider;
import net.fabricmc.api.ClientModInitializer;

public class PersonalPlayerModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		ModModelPredicateProvider.registerModModelPredicates();

	}

}