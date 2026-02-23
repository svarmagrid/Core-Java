package org.example;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class CrazyGenerics {

    public static abstract class BaseEntity implements Serializable {
        private final UUID uuid;
        private final boolean isNew;
        private final LocalDateTime createdOn;

        protected BaseEntity(UUID uuid, boolean isNew, LocalDateTime createdOn) {
            this.uuid = uuid;
            this.isNew = isNew;
            this.createdOn = createdOn;
        }

        public UUID getUuid() {
            return uuid;
        }

        public boolean isNew() {
            return isNew;
        }

        public LocalDateTime getCreatedOn() {
            return createdOn;
        }
    }

    public static class User extends BaseEntity {
        private final String name;

        public User(String name, boolean isNew) {
            super(UUID.randomUUID(), isNew, LocalDateTime.now());
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + " (new=" + isNew() + ")";
        }
    }

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

    interface CollectionRepository<T, C extends Collection<T>> {
        void save(T entity);
        C getEntityCollection();
    }

    interface ListRepository<T> extends CollectionRepository<T, List<T>> {
    }

    interface ComparableCollection<E>
            extends Collection<E>, Comparable<Collection<?>> {

        @Override
        default int compareTo(Collection<?> o) {
            return Integer.compare(this.size(), o.size());
        }
    }

    static class MyComparableCollection<E>
            extends ArrayList<E>
            implements ComparableCollection<E> {
    }

    static class CollectionUtil {

        public static <T> void print(List<T> list) {
            list.forEach(element -> System.out.println(" – " + element));
        }

        public static <T extends BaseEntity> boolean hasNewEntities(Collection<T> entities) {
            return entities.stream().anyMatch(BaseEntity::isNew);
        }

        public static <T extends BaseEntity> boolean isValidCollection(
                Collection<T> entities,
                Predicate<T> validationPredicate) {

            return entities.stream().allMatch(validationPredicate);
        }

        public static <T extends BaseEntity> boolean hasDuplicates(
                List<T> entities,
                T targetEntity) {

            long count = entities.stream()
                    .filter(e -> e.getUuid().equals(targetEntity.getUuid()))
                    .count();

            return count > 1;
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

        public static <T> T findMostRecentlyCreatedEntity(
                Collection<T> entities,
                Comparator<T> createdOnComparator) {

            return findMax(entities, createdOnComparator);
        }
    }

    /* ===================== MAIN ===================== */

    public static void main(String[] args) {

        Sourced<String> sourced = new Sourced<>("Hello", "API");
        System.out.println("Value: " + sourced.getValue());
        System.out.println("Source: " + sourced.getSource());

        Limited<Integer> limited = new Limited<>(10, 0, 100);
        System.out.println("Actual: " + limited.getActual());


        Converter<String, Integer> lengthConverter = String::length;
        System.out.println("Length: " + lengthConverter.convert("Generics"));


        MaxHolder<Integer> maxHolder = new MaxHolder<>(5);
        maxHolder.put(10);
        maxHolder.put(25);
        System.out.println("Max: " + maxHolder.getMax());

        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4));
        CollectionUtil.swap(numbers, 0, 3);
        System.out.println("After swap: " + numbers);
        System.out.println("Max from list: " +
                CollectionUtil.findMax(numbers, Integer::compareTo));

        ComparableCollection<String> col1 = new MyComparableCollection<>();
        col1.add("A");
        col1.add("B");

        ComparableCollection<String> col2 = new MyComparableCollection<>();
        col2.add("X");

        System.out.println("Compare collections by size: " +
                col1.compareTo(col2));

        User u1 = new User("Alice", true);
        User u2 = new User("Bob", false);
        User u3 = new User("Charlie", false);

        List<User> users = new ArrayList<>(List.of(u1, u2, u3));

        System.out.println("Has new entities: " +
                CollectionUtil.hasNewEntities(users));

        System.out.println("All names non-empty: " +
                CollectionUtil.isValidCollection(users,
                        user -> !user.getName().isBlank()));

        System.out.println("Has duplicates (u1): " +
                CollectionUtil.hasDuplicates(users, u1));
    }
}