package fr.maxlego08.spawner.api.enums;

import fr.maxlego08.spawner.api.Spawner;
import fr.maxlego08.spawner.zcore.enums.Message;

import java.util.Comparator;

public enum Sort {

    PLACE(Message.SORT_PLACE, Comparator.comparingInt(Spawner::comparePlace).reversed()),

    PLACE_NO(Message.SORT_NO_PLACE, Comparator.comparingInt(Spawner::compareNotPlace).reversed()),

    ;

    private final Message message;
    private final Comparator<Spawner> comparator;

    Sort(Message message, Comparator<Spawner> comparator) {
        this.message = message;
        this.comparator = comparator;

    }

    /**
     * @return the name
     */
    public String getName() {
        return message.getMessage();
    }

    /**
     * @return the comparator
     */
    public Comparator<Spawner> getComparator() {
        return comparator;
    }

    public Sort next() {
        switch (this) {
            case PLACE:
                return Sort.PLACE_NO;
            case PLACE_NO:
                return Sort.PLACE;
            default:
                break;
        }
        return Sort.PLACE;
    }

}