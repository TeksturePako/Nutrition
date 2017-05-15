package ca.wescook.nutrition.effects;

import ca.wescook.nutrition.nutrients.NutrientList;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

public class EffectsList {
	private static List<JsonEffect> jsonEffects = new ArrayList<>(); // Raw deserialized data from JSON
	private static List<Effect> effects = new ArrayList<>(); // Parsed effects list

	// Register single JSON object
	public static void register(JsonEffect jsonEffectIn) {
		jsonEffects.add(jsonEffectIn);
	}

	// Register list of JSON objects
	public static void register(List<JsonEffect> jsonEffectsIn) {
		jsonEffects.addAll(jsonEffectsIn);
	}

	// Parse JSON data into more useful objects
	public static void parseJson() {
		for (JsonEffect effectRaw : jsonEffects) {
			// Skip if potion is not enabled
			if (!effectRaw.enabled)
				break;

			// Get potion from config
			Potion potion = Potion.getPotionFromResourceLocation(effectRaw.potion);
			if (potion == null) {
				System.out.println("Potion '" + effectRaw.potion + "' is not valid (" + effectRaw.name + ").");
				return;
			}

			// Copying and cleaning data
			Effect effect = new Effect();
			effect.name = effectRaw.name;
			effect.potion = potion;
			effect.amplifier = effectRaw.amplifier;
			effect.minimum = effectRaw.minimum;
			effect.maximum = effectRaw.maximum;
			effect.detect = effectRaw.detect;

			// Assigning appropriate nutrient
			if (effect.detect.equals("nutrient")) {
				effect.nutrient = NutrientList.getByName(effectRaw.nutrient);
				if (effect.nutrient == null) {
					System.out.println("Nutrient '" + effectRaw.nutrient + "' cannot be found.");
					return;
				}
			}

			// Register effect
			effects.add(effect);
		}
	}

	// Return all parsed effects
	public static List<Effect> get() {
		return effects;
	}

	// Return effect by name (null if not found)
	public static Effect getByName(String name) {
		for (Effect effect : effects) {
			if (effect.name.equals(name))
				return effect;
		}
		return null;
	}
}