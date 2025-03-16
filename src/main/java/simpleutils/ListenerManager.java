package simpleutils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class ListenerManager<T extends Comparable<T>> {
    private final List<T> listeners = new AutoSortThreadSafeList<>();
    public ListenerManager() {

    }
    public boolean registerListener(T o) {
        return listeners.add(o);
    }
    public boolean unregisterListener(T o) {
        return listeners.remove(o);
    }

}
