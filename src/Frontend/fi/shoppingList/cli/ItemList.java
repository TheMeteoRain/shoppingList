package fi.shoppingList.cli;

import fi.shoppingList.list.MyLinkedList;
import fi.shoppingList.model.Item;

import java.util.Scanner;

/**
 * CLI-mode of the Shopping List application.
 *
 * <p>
 * This mode can only add shopping list items and show them.
 *
 * @author Akash Singh
 * @version 2016-12-10
 * @since 1.6
 */
public class ItemList {

    /**
     * Holds all the items in the shopping list.
     */
    private MyLinkedList<Item> items = new MyLinkedList<>();

    /**
     * Greets user and call queries-method.
     */
    public ItemList() {
        System.out.println("SHOPPING LIST");
        System.out.println("Tampere University of Applied Sciences");

        queries();
    }

    /**
     * Asks continuously from user item's quantity and name.
     *
     * Adds each item to shopping list and continues queries.
     * Stops when user types "exit".
     */
    private void queries() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            // Example format
            System.out.println(
                    "Give shopping list (example: 1 milk;2 tomato;3 carrot;)");

            input = scanner.nextLine();

            // If exit was not typed
            if (!input.equals("exit")) {
                // Format input, get rid of semicolon and spaces
                input = input.replace("; ", " ");
                input = input.replace(";", " ");
                // Turn to array
                String[] split = input.split(" ");

                // Take each item quantity and name and make Item object
                for (int i = 0; i < split.length; i += 2) {
                    try {
                        Item item = new Item(
                                split[i + 1],
                                Integer.parseInt(split[i]));
                        addItem(item);
                    } catch (NumberFormatException e) {
                        System.out.println("Amount must be a number.");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Could not read input. Try again.");
                    }
                }

                // Print current situation of the Shopping List
                showList();
                System.out.println();
            }
        } while (!input.equals("exit"));
    }

    /**
     * Adds an item to the shopping list.
     *
     * <p>
     * Checks if there is already item by the same name,
     * if there is, do not make duplicate and sum the quantities together.
     * Otherwise make a new item.
     *
     * @param item item to be added to shopping list
     */
    private void addItem(Item item) {
        String itemName = item.getName().toLowerCase();
        boolean itemFoundInList = false;

        for (int i = 0; i < items.size() && !itemFoundInList; i++) {
            String currentName = items.get(i).getName().toLowerCase();

            // Check duplicate names
            if (currentName.equals(itemName)) {
                items.get(i).setQuantity(
                        items.get(i).getQuantity()
                                + item.getQuantity());
                itemFoundInList = true;
            }
        }

        // If item was not found in the shopping list add this new item
        if (!itemFoundInList) {
            items.add(item);
        }
    }

    /**
     * Prints the current situation of the Shopping List.
     */
    private void showList() {
        System.out.println("Your Shopping List now:");

        for (int i = 0; i < items.size(); i++) {
            System.out.println("  " + items.get(i).getQuantity() + " "
                    + items.get(i).getName());
        }
    }
}
