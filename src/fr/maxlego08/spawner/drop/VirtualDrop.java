package fr.maxlego08.spawner.drop;

import java.util.List;

public record VirtualDrop(boolean cancelDefaultDrop, List<CustomVirtualDrop> drops) {
    public boolean isEmpty() {
        return this.drops.isEmpty();
    }
}
