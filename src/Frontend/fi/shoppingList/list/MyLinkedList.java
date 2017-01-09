package fi.shoppingList.list;

/**
 * My own interpretation of LinkedList, only one way linked.
 *
 * @param <T> the type of elements held in this collection
 * @author Akash Singh
 * @version 2016-10-24
 * @since 1.6
 */
public class MyLinkedList<T> implements MyList<T> {

    /**
     * Holds information about the element and next element.
     */
    private class Node {

        /**
         * Element containing information.
         */
        private T element;

        /**
         * Points to the next element.
         */
        private Node next;

        /**
         * Places information to the element on creation.
         *
         * @param o element
         */
        public Node(T o) {
            this.element = o;
            this.next = null;
        }
    }

    /**
     * First node of the list.
     */
    private Node first;

    /**
     * Last node of the list.
     */
    private Node last;

    /**
     * Represents the size of the list.
     */
    private int index;

    /**
     * Initializes the list with zero size.
     */
    public MyLinkedList() {
        index = 0;
    }

    /**
     * Inserts the specified element at the specified index in this list.
     *
     * @see MyList#add(Object) add
     */
    @Override
    public void add(T o) {
        Node n = new Node(o);

        if (first == null) {
            first = n;
        } else {
            last.next = n;
        }

        last = n;
        index++;
    }

    /**
     * Removes all of the elements from this list.
     *
     * @see MyList#clear() clear
     */
    @Override
    public void clear() {
        first = last = null;
        index = 0;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @see MyList#get(int) get
     */
    @Override
    public T get(int index) {
        verifyIndex(index);
        Node n = first;

        for (int i = 0; i < index; i++) {
            n = n.next;
        }

        return n.element;
    }

    /**
     * Returns true or false telling if the array is empty or not.
     *
     * @see MyList#isEmpty() isEmpty
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the removed object.
     *
     * @see MyList#remove(int) remove
     */
    @Override
    public T remove(int index) {
        verifyIndex(index);
        Node prev = null;
        Node n = first;

        for (int i = 0; i < index; i++) {
            prev = n;
            n = n.next;
        }

        if (n == first) {
            first = first.next;
        } else {
            prev.next = n.next;
        }

        if (n == last) {
            last = prev;
        }

        this.index--;
        return (T) n;
    }

    /**
     * Returns true or false telling if the object was found and deleted.
     *
     * @see MyList#remove(Object) get
     */
    @Override
    public boolean remove(T o) {
        boolean removed = false;
        Node prev = null;
        Node n = first;

        for (int i = 0; i < size() && !removed; i++) {
            prev = n;
            n = n.next;

            if (n.element.equals(o)) {
                prev.next = n.next;
                removed = true;
                this.index--;
            }
        }

        return removed;
    }

    /**
     * Returns the size of the array.
     *
     * @see MyList#size() size
     */
    @Override
    public int size() {
        return this.index;
    }

    /**
     * Verifies given index number that it does not go out of bounds.
     *
     * @param index index number
     * @throws IndexOutOfBoundsException if index is out of bounds.
     */
    private void verifyIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }
    }
}
