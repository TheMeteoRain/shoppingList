package fi.shoppingList.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Item class model to represent single item in a shopping list.
 *
 * <p>
 * This class is also used when sending a single item to the database.
 * Or when receiving item from database.
 *
 * @author Akash Singh
 * @version 2016-11-23
 * @since 1.6
 */
@XmlType(propOrder = {"id", "name", "quantity"})
@XmlRootElement(name = "item")
public class Item {

    /**
     * Object id, by default -1 if it's not given.
     */
    private int id = -1;

    /**
     * The name of this object.
     */
    private String name;

    /**
     * Quantity.
     */
    private int quantity;

    /**
     * This object requires this empty constructor in order to work.
     *
     * <p>
     * This must be implemented to avoid any errors while
     * transferring objects to or from database.
     */
    public Item() {

    }

    /**
     * Creates an item with name and quantity.
     *
     * @param name     name of the item
     * @param quantity amount
     */
    public Item(String name, int quantity) {
        setName(name);
        setQuantity(quantity);
    }

    /**
     * Creates an item with id, name and quantity.
     *
     * @param id       id number
     * @param name     name of the item
     * @param quantity amount
     */
    public Item(int id, String name, int quantity) {
        setId(id);
        setName(name);
        setQuantity(quantity);
    }

    /**
     * Returns item's id, if it does not exist it will return -1.
     *
     * @return id item's id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets item id.
     *
     * @param id id to set (in integer)
     */
    @XmlAttribute(name = "id", required = true)
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets current item name.
     *
     * @return current name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets item name.
     *
     * @param name name to set
     */
    @XmlAttribute(name = "name", required = true)
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets item quantity.
     *
     * @return current quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets item quantity.
     *
     * @param quantity quantity to set (in integers)
     */
    @XmlAttribute(name = "quantity", required = true)
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Print Item as JSON in String.
     *
     * @return JSON object in String.
     */
    @Override
    public String toString() {
        return "{\"name\": \"" + getName() + "\", " +
                "\"quantity\": \"" + getQuantity() + "\"}";
    }
}
