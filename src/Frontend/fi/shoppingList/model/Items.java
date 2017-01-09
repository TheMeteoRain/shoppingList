package fi.shoppingList.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Items class model to represent multiple items in a shopping list.
 *
 * <p>
 * Holds a list of Item objects.
 * This class is also used when sending multiple items to the database.
 * Or when receiving item from database.
 *
 * @author Akash Singh
 * @version 2016-11-23
 * @since 1.6
 */
@XmlRootElement(name = "items")
public class Items {

    /**
     * Holds list of item objects.
     */
    private List<Item> items;

    /**
     * Initializes List.
     */
    public Items() {
        items = new LinkedList<>();
    }

    /**
     * Gets the list of Item objects.
     *
     * @return items
     */
    public List<Item> getItems() {
        return this.items;
    }

    /**
     * Replace existing list with given list.
     *
     * @param items Item list
     */
    @XmlElement(name = "item")
    public void setItems(List<Item> items) {
        this.items = items;
    }
}
