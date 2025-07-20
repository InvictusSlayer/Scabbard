package net.invictusslayer.scabbard.data;

import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class LangProvider implements DataProvider {
	private final Map<String, String> data = new TreeMap<>();
	private final PackOutput output;
	private final String modId;
	private final String locale;

	public LangProvider(PackOutput output, String modId, String locale) {
		this.output = output;
		this.modId = modId;
		this.locale = locale;
	}

	protected abstract void addTranslations();

	public CompletableFuture<?> run(CachedOutput cache) {
		addTranslations();

		if (!data.isEmpty()) return save(cache, output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(modId).resolve("lang").resolve(locale + ".json"));

		return CompletableFuture.allOf();
	}

	public String getName() {
		return "Languages: " + locale;
	}

	private CompletableFuture<?> save(CachedOutput cache, Path path) {
		JsonObject json = new JsonObject();
		data.forEach(json::addProperty);

		return DataProvider.saveStable(cache, json, path);
	}

	protected void addBlock(Supplier<? extends Block> key, String name) {
		add(key.get(), name);
	}

	protected void add(Block key, String name) {
		add(key.asItem().getDescriptionId(), name);
	}

	protected void addItem(Supplier<? extends Item> key, String name) {
		add(key.get(), name);
	}

	protected void add(Item key, String name) {
		add(key.getDescriptionId(), name);
	}

	protected void addItemStack(Supplier<ItemStack> key, String name) {
		add(key.get(), name);
	}

	protected void add(ItemStack key, String name) {
		add(key.getItem().getDescriptionId(), name);
	}

	protected void addEnchantment(Supplier<? extends Enchantment> key, String name) {
		add(key.get(), name);
	}

	protected void add(Enchantment key, String name) {
		add(key.description().getString(), name);
	}

	protected void addEffect(Supplier<Holder.Reference<MobEffect>> key, String name) {
		add(key.get().value(), name);
	}

	protected void add(MobEffect key, String name) {
		add(key.getDescriptionId(), name);
	}

	protected void addEntityType(Supplier<? extends EntityType<?>> key, String name) {
		add(key.get(), name);
	}

	protected void add(EntityType<?> key, String name) {
		add(key.getDescriptionId(), name);
	}

	protected void add(String key, String value) {
		if (data.put(key, value) != null) throw new IllegalStateException("Duplicate translation key " + key);
	}

	protected void addSound(Supplier<? extends SoundEvent> key, String name) {
		add(key.get().location().toLanguageKey("subtitles"), name);
	}

	protected void addItemDesc(Supplier<? extends Item> key, String name) {
		add(key.get().getDescriptionId() + ".desc", name);
	}

	protected void addCreativeTab(String key, String name) {
		add("itemGroup." + modId + "." + key, name);
	}

	protected void addConfig(String key, String name) {
		add("text.autoconfig." + modId + "." + key, name);
	}

	protected void addConfigTitle(String name) {
		addConfig("title", name);
	}

	protected void addConfigCategory(String key, String name) {
		addConfig("category." + key, name);
	}

	protected void addConfigOption(String key, String name) {
		addConfig("option." + key, name);
	}

	protected void addConfigOptionPrefix(String key, String name, String prefix) {
		addConfigOption(key, name);
		addConfigOption(key + ".@PrefixText", prefix);
	}

	protected void addConfigEnumTooltip(String key, int ordinal, String name) {
		addConfig(key + ".@Tooltip[" + ordinal + "]", name);
	}
}
