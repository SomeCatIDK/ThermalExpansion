package cofh.thermalexpansion.util.managers.machine;

import cofh.core.inventory.ComparableItemStackValidatedNBT;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CrucibleManager {

	private static Map<ComparableItemStackValidatedNBT, CrucibleRecipe> recipeMap = new Object2ObjectOpenHashMap<>();
	private static Set<ComparableItemStackValidatedNBT> lavaSet = new ObjectOpenHashSet<>();

	public static final int DEFAULT_ENERGY = 8000;

	public static CrucibleRecipe getRecipe(ItemStack input) {

		return input.isEmpty() ? null : recipeMap.get(new ComparableItemStackValidatedNBT(input));
	}

	public static boolean recipeExists(ItemStack input) {

		return getRecipe(input) != null;
	}

	public static CrucibleRecipe[] getRecipeList() {

		return recipeMap.values().toArray(new CrucibleRecipe[0]);
	}

	public static boolean isLava(ItemStack input) {

		return !input.isEmpty() && lavaSet.contains(new ComparableItemStackValidatedNBT(input));
	}

	public static void initialize() {

	}

	public static void refresh() {

		Map<ComparableItemStackValidatedNBT, CrucibleRecipe> tempMap = new Object2ObjectOpenHashMap<>(recipeMap.size());
		Set<ComparableItemStackValidatedNBT> tempSet = new ObjectOpenHashSet<>();
		CrucibleRecipe tempRecipe;

		for (Entry<ComparableItemStackValidatedNBT, CrucibleRecipe> entry : recipeMap.entrySet()) {
			tempRecipe = entry.getValue();
			ComparableItemStackValidatedNBT input = new ComparableItemStackValidatedNBT(tempRecipe.input);
			tempMap.put(input, tempRecipe);

			if (FluidRegistry.LAVA.equals(tempRecipe.getOutput().getFluid())) {
				tempSet.add(input);
			}
		}
		recipeMap.clear();
		recipeMap = tempMap;

		lavaSet.clear();
		lavaSet = tempSet;
	}

	/* ADD RECIPES */
	public static CrucibleRecipe addRecipe(int energy, ItemStack input, FluidStack output, int requiredTier) {

		if (input.isEmpty() || output == null || output.amount <= 0 || energy <= 0 || recipeExists(input)) {
			return null;
		}
		ComparableItemStackValidatedNBT inputCrucible = new ComparableItemStackValidatedNBT(input);

		CrucibleRecipe recipe = new CrucibleRecipe(input, output, energy, requiredTier);
		recipeMap.put(inputCrucible, recipe);

		if (FluidRegistry.LAVA.equals(output.getFluid())) {
			lavaSet.add(inputCrucible);
		}
		return recipe;
	}
	
	public static CrucibleRecipe addRecipe(int energy, ItemStack input, FluidStack output) {
		return addRecipe(energy, input, output, 0);
	}

	/* REMOVE RECIPES */
	public static CrucibleRecipe removeRecipe(ItemStack input) {

		ComparableItemStackValidatedNBT inputCrucible = new ComparableItemStackValidatedNBT(input);
		lavaSet.remove(inputCrucible);
		return recipeMap.remove(inputCrucible);
	}

	/* HELPERS */
	public static ComparableItemStackValidatedNBT convertInput(ItemStack stack) {

		return new ComparableItemStackValidatedNBT(stack);
	}

	/* RECIPE CLASS */
	public static class CrucibleRecipe {

		final ItemStack input;
		final FluidStack output;
		final int energy;
		final int requiredTier;

		CrucibleRecipe(ItemStack input, FluidStack output, int energy, int requiredTier) {

			this.input = input;
			this.output = output;
			this.energy = energy;
			this.requiredTier = requiredTier;
		}

		public ItemStack getInput() {

			return input;
		}

		public FluidStack getOutput() {

			return output;
		}

		public int getEnergy() {

			return energy;
		}
		
		public int getRequiredTier() {
			return requiredTier;
		}
	}

}
