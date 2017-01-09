package fi.shoppingList.data;

import fi.shoppingList.list.MyLinkedList;
import fi.shoppingList.model.Item;
import fi.shoppingList.model.Items;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

/**
 * Connects to the backend of the Shopping List application.
 * <p>
 * Each call to this class either fetches data from database or
 * sends data to database.
 *
 * @author Akash Singh
 * @version 2016-11-27
 * @since 1.8
 */
public class Database {

    /**
     * Shopping List backend url.
     */
    private final String shoppingListUrl =
            "http://localhost:8080/shoppingList/items";

    /**
     * Declare an empty client.
     */
    private Client client = null;

    /**
     * Initializes Client on startup.
     */
    public Database() {
        client = ClientBuilder.newClient();
    }

    /**
     * Fetches data from database.
     *
     * @return Item objects within a MyLinkedList.
     */
    public MyLinkedList<Item> fetchItems() {
        WebTarget resource = client.target(shoppingListUrl);
        Invocation.Builder invocationBuilder =
                resource.request(MediaType.APPLICATION_XML);

        Response response = invocationBuilder.get();
        List<Item> linkedList =
                response.readEntity(new GenericType<List<Item>>() {});

        MyLinkedList<Item> myLinkedList = new MyLinkedList<>();
        linkedList.forEach (myLinkedList::add);

        return myLinkedList;
    }

    /**
     * Pushes only one item to the database.
     *
     * <p>
     * Used when new item is added.
     *
     * @param item item to be pushed to database
     */
    public void pushItem(Item item) {
        WebTarget resource = client.target(shoppingListUrl + "/add");
        Invocation.Builder invocationBuilder = resource.request();
        Entity<Item> entityItem =
                Entity.entity(item, MediaType.APPLICATION_XML);

        Response response = invocationBuilder.put(entityItem);
        responseMessage(response);
    }

    /**
     * Pushes multiple items to the database.
     *
     * @param myLinkedList list of item objects
     */
    public void pushItems(MyLinkedList<Item> myLinkedList) {
        WebTarget resource = client.target(shoppingListUrl + "/array");
        Invocation.Builder invocationBuilder = resource.request();

        // Change MyLinkedList to LinkedList, because backend
        // cannot handle MyLinkedList
        LinkedList<Item> linkedList = new LinkedList<>();

        for (int i = 0; i < myLinkedList.size(); i++) {
            linkedList.add(myLinkedList.get(i));
        }

        // Give linkedList to Items
        Items items = new Items();
        items.setItems(linkedList);

        Entity<Items> entityItem =
                Entity.entity(items, MediaType.APPLICATION_XML);
        Response response = invocationBuilder.put(entityItem);
        responseMessage(response);
    }

    /**
     * Removes only one item from database.
     *
     * <p>
     * Used when user wants to delete an item.
     *
     * @param item item to be removed from database
     */
    public void removeItem(Item item) {
        WebTarget resource = client.target(shoppingListUrl + "/delete");
        Invocation.Builder invocationBuilder = resource.request();
        Entity<Item> entityItem =
                Entity.entity(item, MediaType.APPLICATION_XML);
        Response response = invocationBuilder.put(entityItem);
        responseMessage(response);
    }

    /**
     * Modifies only one item at the database.
     *
     * <p>
     * Used when item is modified.
     *
     * @param item item to be modified in database
     */
    public void editItem(Item item) {
        WebTarget resource = client.target(shoppingListUrl + "/edit");
        Invocation.Builder invocationBuilder = resource.request();
        Entity<Item> entityItem =
                Entity.entity(item, MediaType.APPLICATION_XML);

        Response response = invocationBuilder.put(entityItem);
        responseMessage(response);
    }

    /**
     * Prints response message's status and entity.
     *
     * @param response response message
     */
    private void responseMessage(Response response) {
        int responseStatus = response.getStatus();
        String responseMessage = response.readEntity(String.class);

        System.out.println("Status code: " + responseStatus);
        System.out.println(responseMessage);
    }
}
