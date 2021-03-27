package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class is used to serialized version of set
 *
 * @version March 12th 2021
 */
public abstract class SendSet<T> implements Serializable {
    protected final Set<T> items;

    /**
     * Constructor for SendSet
     *
     * @param items the items to be used
     */
    public SendSet(Collection<T> items) {
        this.items = new HashSet<>(items);
    }

    /**
     * Override method for ToString
     *
     * @return string
     */
    @Override
    public String toString() {
        return "SendSet{" +
                "items=" + items +
                '}';
    }

    /**
     * Override method for equals
     *
     * @param o
     * @return if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendSet<?> sendSet = (SendSet<?>) o;
        return Objects.equals(items, sendSet.items);
    }

    /**
     * Override method for hashcode
     *
     * @return hased items
     */
    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
