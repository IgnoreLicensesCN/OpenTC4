package simpleutils;

import thaumcraft.api.expands.warp.listeners.WarpEventListenerAfter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListenerManager<T extends Comparable<T>> {
    private final List<T> listeners = new AutoSortSynchronizedList<>();
    public ListenerManager() {

    }
    public boolean registerListener(T o) {
        return listeners.add(o);
    }
    public boolean unregisterListener(T o) {
        return listeners.remove(o);
    }

    public List<T> getListeners() {
        return Collections.unmodifiableList(listeners);
    }
}
