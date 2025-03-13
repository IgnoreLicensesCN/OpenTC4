package simpleutils;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class AutoSortSynchronizedList<T extends Comparable<T>> implements List<T> {
    private final List<T> wrapped = Collections.synchronizedList(new ArrayList<>());

    @Override
    public boolean removeIf(@Nonnull Predicate<? super T> filter) {
        return wrapped.removeIf(filter);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return wrapped.contains(o);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return wrapped.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        wrapped.forEach(action);
    }

    @Nonnull
    @Override
    public Object[] toArray() {
        return wrapped.toArray();
    }

    @Nonnull
    @Override
    public <T1> T1[] toArray(@Nonnull T1[] a) {
        return wrapped.toArray(a);
    }

    @Override
    public boolean add(T t) {
        boolean added = wrapped.add(t);
        Collections.sort(this);
        return added;
    }

    @Override
    public boolean remove(Object o) {
        return wrapped.remove(o);
    }


    @Override
    public boolean containsAll(@Nonnull Collection<?> c) {
        return wrapped.containsAll(c);
    }

    @Override
    public boolean addAll(@Nonnull Collection<? extends T> c) {
        boolean changed = wrapped.addAll(c);
        Collections.sort(this);
        return changed;
    }

    @Override
    public boolean addAll(int index, @Nonnull Collection<? extends T> c) {
        boolean changed = wrapped.addAll(index, c);
        Collections.sort(this);
        return changed;
    }

    @Override
    public boolean removeAll(@Nonnull Collection<?> c) {
        return wrapped.removeAll(c);
    }

    @Override
    public boolean retainAll(@Nonnull Collection<?> c) {
        boolean changed = wrapped.retainAll(c);
        Collections.sort(this);
        return changed;
    }

    @Override
    public void replaceAll(@Nonnull UnaryOperator<T> operator) {
        wrapped.replaceAll(operator);
        Collections.sort(this);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        wrapped.sort(c);
    }

    @Override
    public void clear() {
        wrapped.clear();
    }

    @Override
    public T get(int index) {
        return wrapped.get(index);
    }

    @Override
    public T set(int index, T element) {
        T old = wrapped.set(index, element);
        Collections.sort(this);
        return old;
    }

    @Override
    public void add(int index, T element) {
        wrapped.add(index, element);
        Collections.sort(this);
    }

    @Override
    public T remove(int index) {
        return wrapped.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return wrapped.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return wrapped.lastIndexOf(o);
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator() {
        return wrapped.listIterator();
    }

    @Nonnull
    @Override
    public ListIterator<T> listIterator(int index) {
        return wrapped.listIterator(index);
    }

    @Nonnull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return wrapped.subList(fromIndex, toIndex);
    }

    @Nonnull
    @Override
    public Spliterator<T> spliterator() {
        return wrapped.spliterator();
    }

    @Nonnull
    @Override
    public Stream<T> stream() {
        return wrapped.stream();
    }

    @Nonnull
    @Override
    public Stream<T> parallelStream() {
        return wrapped.parallelStream();
    }
}
