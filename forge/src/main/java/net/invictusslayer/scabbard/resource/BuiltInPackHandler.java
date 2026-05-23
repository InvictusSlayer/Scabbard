package net.invictusslayer.scabbard.resource;

import java.util.ArrayList;
import java.util.List;

public class BuiltInPackHandler {
	public static final List<PackData> PACKS = new ArrayList<>();

	public record PackData(String modId, String packId, String name, boolean enabled) {}
}
