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

    public boolean canApply(SpawnerOption option, Object value, Object maxValueO) {
        if (maxValueO instanceof Number maxValue && value instanceof Number number) {
            return switch (this) {
                case AUTO_KILL, AUTO_SELL -> false;
                case DISTANCE -> option.getDistance() - number.doubleValue() >= maxValue.doubleValue();
                case EXPERIENCE_MULTIPLIER -> maxValue.doubleValue() >= option.getExperienceMultiplier() + number.doubleValue();
                case LOOT_MULTIPLIER -> option.getLootMultiplier() + number.doubleValue() >= maxValue.doubleValue();
                case MAX_ENTITY -> maxValue.intValue() >= option.getMaxEntity() + number.intValue();
                case MIN_DELAY -> option.getMinDelay() - number.intValue() >= maxValue.intValue();
                case MAX_DELAY -> option.getMaxDelay() - number.intValue() >= maxValue.intValue();
                case MIN_SPAWN -> maxValue.intValue() >= option.getMinSpawn() + number.intValue();
                case MAX_SPAWN -> maxValue.intValue() >= option.getMaxSpawn() + number.intValue();
                case MOB_PER_MINUTE -> maxValue.intValue() >= option.getMobPerMinute() + number.intValue();
            };
        }
        return false;
    }

    public void apply(SpawnerOption option, Object value) {
        switch (this) {
            case AUTO_KILL, AUTO_SELL -> {
            }
            case DISTANCE ->
                    this.setterFunction.accept(option, String.valueOf(option.getDistance() + ((Number) value).doubleValue()));
            case EXPERIENCE_MULTIPLIER ->
                    this.setterFunction.accept(option, String.valueOf(option.getExperienceMultiplier() + ((Number) value).doubleValue()));
            case LOOT_MULTIPLIER ->
                    this.setterFunction.accept(option, String.valueOf(option.getLootMultiplier() + ((Number) value).doubleValue()));
            case MAX_ENTITY ->
                    this.setterFunction.accept(option, String.valueOf(option.getMaxEntity() + ((Number) value).intValue()));
            case MIN_DELAY ->
                    this.setterFunction.accept(option, String.valueOf(option.getMinDelay() - ((Number) value).intValue()));
            case MAX_DELAY ->
                    this.setterFunction.accept(option, String.valueOf(option.getMaxDelay() - ((Number) value).intValue()));
            case MIN_SPAWN ->
                    this.setterFunction.accept(option, String.valueOf(option.getMinSpawn() + ((Number) value).intValue()));
            case MAX_SPAWN ->
                    this.setterFunction.accept(option, String.valueOf(option.getMaxSpawn() + ((Number) value).intValue()));
            case MOB_PER_MINUTE ->
                    this.setterFunction.accept(option, String.valueOf(option.getMobPerMinute() + ((Number) value).intValue()));
        }
    }

    public Class<?> getType() {
        return type;
    }
}
