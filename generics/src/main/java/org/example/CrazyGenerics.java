package org.example;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;

public class CrazyGenerics {

    @Data
    public static class Sourced<T> {
        private T value;
        private String source;

        public Sourced(T value, String source) {
            this.value = value;
            this.source = source;
        }
    }

    @Data
    public static class Limited<T extends Number> {
        private final T actual;
        private final T min;
        private final T max;

        public Limited(T actual, T min, T max) {
            this.actual = actual;
            this.min = min;
            this.max = max;
        }
    }

    public interface Converter<T, R> {
        R convert(T obj);
    }

    public static class MaxHolder<T extends Comparable<T>> {
        private T max;

        public MaxHolder(T max) {
            this.max = max;
        }

        public void put(T val) {
            if (max == null || val.compareTo(max) > 0) {
                max = val;
            }
        }

        public T getMax() {
            return max;
        }
    }

    interface StrictProcessor<T extends Serializable & Comparable<T>> {
        void process(T obj);
    }

    interface CollectionRepository<T , C extends Collection<T>> {
        void save(T entity);
        C getEntityCollection();
    }

    interface ListRepository<T> extends CollectionRepository<T, List<T>> {
        // No extra methods needed
    }

    interface ComparableCollection<E> extends Collection<E>, Comparable<Collection<?>> {
        @Override
        default int compareTo(Collection<?> o) {
            return Integer.compare(this.size(), o.size());
        }
    }

    static class CollectionUtil {

        public static <T> void print(List<T> list) {
            list.forEach(element -> System.out.println(" – " + element));
        }

        public static <T /*extends BaseEntity*/> boolean hasNewEntities(Collection<T> entities) {
            throw new UnsupportedOperationException("Implement using BaseEntity");
        }

        public static <T /*extends BaseEntity*/> boolean isValidCollection(Collection<T> entities, java.util.function.Predicate<T> validationPredicate) {
            // Example: return true if all entities pass the validation predicate
            // return entities.stream().allMatch(validationPredicate);
            throw new UnsupportedOperationException("Implement using BaseEntity");
        }

        public static <T /*extends BaseEntity*/> boolean hasDuplicates(List<T> entities, T targetEntity) {
            // Example: count how many times targetEntity appears in list
            // return entities.stream().filter(e -> e.getUuid().equals(targetEntity.getUuid())).count() > 1;
            throw new UnsupportedOperationException("Implement using BaseEntity");
        }

        public static <T> void swap(List<T> elements, int i, int j) {
            Objects.checkIndex(i, elements.size());
            Objects.checkIndex(j, elements.size());
            T temp = elements.get(i);
            elements.set(i, elements.get(j));
            elements.set(j, temp);
        }

        public static <T> T findMax(Iterable<T> elements, Comparator<T> comparator) {
            T max = null;
            for (T element : elements) {
                if (max == null || comparator.compare(element, max) > 0) {
                    max = element;
                }
            }
            if (max == null) {
                throw new NoSuchElementException("Iterable is empty");
            }
            return max;
        }

        public static <T> T findMostRecentlyCreatedEntity(Collection<T> entities, Comparator<T> createdOnComparator) {
            return findMax(entities, createdOnComparator);
        }
    }
}
