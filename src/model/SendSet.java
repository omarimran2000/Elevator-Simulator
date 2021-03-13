package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class SendSet <T> implements Serializable {
    protected final Set<T> items;

    public SendSet(Collection<T> items) {
        this.items = new HashSet<>(items);
    }

    @Override
    public String toString() {
        return "SendSet{" +
                "items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendSet<?> sendSet = (SendSet<?>) o;
        return Objects.equals(items, sendSet.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
