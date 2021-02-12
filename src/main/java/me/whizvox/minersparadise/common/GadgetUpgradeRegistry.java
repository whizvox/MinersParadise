package me.whizvox.minersparadise.common;

import me.whizvox.minersparadise.api.IGadgetUpgrade;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.function.Consumer;

public class GadgetUpgradeRegistry implements Iterable<IGadgetUpgrade> {

  private final Map<ResourceLocation, IGadgetUpgrade> registry;

  public GadgetUpgradeRegistry() {
    registry = new HashMap<>();
  }

  // public accessors

  public boolean contains(ResourceLocation key) {
    return registry.containsKey(key);
  }

  public Optional<IGadgetUpgrade> get(ResourceLocation key) {
    return Optional.ofNullable(getRaw(key));
  }

  // public mutators

  public <T extends IGadgetUpgrade> T register(T upgrade) {
    if (contains(upgrade.getId())) {
      throw new IllegalArgumentException("Key conflict: " + upgrade.getId());
    }
    registry.put(upgrade.getId(), upgrade);
    return upgrade;
  }

  public IGadgetUpgrade deregister(ResourceLocation key) {
    return registry.remove(key);
  }

  // private helpers

  private IGadgetUpgrade getRaw(ResourceLocation key) {
    return registry.getOrDefault(key, null);
  }

  @Override
  public Iterator<IGadgetUpgrade> iterator() {
    return registry.values().iterator();
  }

  @Override
  public Spliterator<IGadgetUpgrade> spliterator() {
    return registry.values().spliterator();
  }

  @Override
  public void forEach(Consumer<? super IGadgetUpgrade> action) {
    registry.values().forEach(action);
  }

}
