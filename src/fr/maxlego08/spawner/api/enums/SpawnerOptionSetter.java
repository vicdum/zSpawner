package fr.maxlego08.spawner.api.enums;

import fr.maxlego08.spawner.api.SpawnerOption;

import java.util.function.BiConsumer;

public enum SpawnerOptionSetter {
    DISTANCE(Double.class, (option, value) -> option.setDistance(Double.parseDouble(value))),
    EXPERIENCE_MULTIPLIER(Double.class, (option, value) -> option.setExperienceMultiplier(Double.parseDouble(value))),
    LOOT_MULTIPLIER(Double.class, (option, value) -> option.setLootMultiplier(Double.parseDouble(value))),
    AUTO_KILL(Boolean.class, (option, value) -> option.setAutoKill(Boolean.parseBoolean(value))),
    AUTO_SELL(Boolean.class, (option, value) -> option.setAutoSell(Boolean.parseBoolean(value))),
    MAX_ENTITY(Integer.class, (option, value) -> option.setMaxEntity(Integer.parseInt(value))),
    MIN_DELAY(Integer.class, (option, value) -> option.setMinDelay(Integer.parseInt(value))),
    MAX_DELAY(Integer.class, (option, value) -> option.setMaxDelay(Integer.parseInt(value))),
    MIN_SPAWN(Integer.class, (option, value) -> option.setMinSpawn(Integer.parseInt(value))),
    MAX_SPAWN(Integer.class, (option, value) -> option.setMaxSpawn(Integer.parseInt(value))),
    MOB_PER_MINUTE(Integer.class, (option, value) -> option.setMobPerMinute(Integer.parseInt(value)));

    private final Class<?> type;
    private final BiConsumer<SpawnerOption, String> setterFunction;

    SpawnerOptionSetter(Class<?> type, BiConsumer<SpawnerOption, String> setterFunction) {
        this.type = type;
        this.setterFunction = setterFunction;
    }

    public boolean apply(SpawnerOption option, String value) {
        try {
            setterFunction.accept(option, value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Class<?> getType() {
        return type;
    }
}
