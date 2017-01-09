/**
 * fi.shoppingList.list My own interpretation of List.
 */
package fi.shoppingList.list;

/**
 * My own interpretation of List interface.
 *
 * @param <T> the type of elements held in this collection
 * @author Akash Singh
 * @version 2016-10-24
 * @since 1.6
 */
public interface MyList<T> {

    /**
     * Inserts the specified element at the specified index in this list.
     *
     * @param o object to add
     */
    void add(T o);

    /**
     * Removes all of the elements from this list.
     */
    void clear();

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index number
     * @return object
     */
    T get(int index);

    /**
     * Returns true or false telling if the array is empty or not.
     *
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Returns the removed object.
     *
     * @param index number
     * @return object
     */
    T remove(int index);

    /**
     * Returns true or false telling if the object was found and deleted.
     *
     * @param o object
     * @return boolean
     */
    boolean remove(T o);

    /**
     * Returns the size of the array.
     *
     * @return number.
     */
    int size();
}
