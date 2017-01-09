package fi.shoppingList.data;

import fi.shoppingList.list.MyLinkedList;
import fi.shoppingList.model.Item;

import javax.swing.*;
import java.io.*;

/**
 * This class handles opening, saving and combining of files.
 *
 * @author Akash Singh
 * @version 2016-11-27
 * @since 1.7
 */
public class FileManager extends JPanel {

    /**
     * Opens a file that contains shopping list.
     *
     * @return shopping list
     */
    public MyLinkedList<Item> open() {
        MyLinkedList<Item> items = new MyLinkedList<>();

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        int index = 1;

        // Open a file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            // Read content
            try (BufferedReader reader = new BufferedReader
                    (new FileReader(selectedFile.getPath()))) {
                String lines = reader.readLine();

                // Read line by line
                while (lines != null) {
                    String[] split = lines.split(" ");
                    Item item = new Item(split[1], Integer.parseInt(split[0]));
                    items.add(item);
                    lines = reader.readLine();
                }
            } catch (IOException e) {
                System.out.println("There was an error reading a file.");
                e.printStackTrace();
            }
        }

        return items;
    }

    /**
     * Saves the shopping list to a file.
     *
     * @param items current shopping list
     * @return true/false to tell if saving was successful or not
     */
    public boolean save(MyLinkedList<Item> items) {
        boolean result = false;

        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(getParent());

        // Save to a file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            // Write to the file
            try (BufferedWriter writer = new BufferedWriter
                    (new FileWriter(selectedFile.getPath()))) {
                int size = items.size();

                // Turn item objects to string
                for (int i = 0; i < size; i++) {
                    Item readItem = items.get(i);
                    writer.write
                            (readItem.getQuantity() + " " + readItem.getName());

                    if (i < size - 1) {
                        writer.newLine();
                    }
                }

                result = true;
            } catch (IOException e) {
                System.out.println("Could not save to a file.");
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * Combines current shopping list with a shopping list from another file.
     *
     * @param items current shopping list
     * @return combination of shopping lists
     */
    public MyLinkedList<Item> combine(MyLinkedList<Item> items) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        int index = items.size() + 1;

        // Open a file
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            // Read content
            try (BufferedReader reader = new BufferedReader
                    (new FileReader(selectedFile.getPath()))) {
                String lines = reader.readLine();

                // Read line by line
                while (lines != null) {
                    String[] split = lines.split(" ");
                    boolean combined = false;

                    Item newItem = new Item
                            (split[1], Integer.parseInt(split[0]));
                    String newItemName = newItem.getName().toLowerCase();

                    // Find items by the same name, if found
                    // add item quantities together. If not, add a new item.
                    for (int i = 0; i < items.size() && !combined; i++) {
                        Item currentItem = items.get(i);
                        String currentItemName =
                                currentItem.getName().toLowerCase();

                        if (newItemName.equals(currentItemName)) {
                            currentItem.setQuantity
                                    (currentItem.getQuantity() +
                                            newItem.getQuantity());
                            combined = true;
                        }
                    }

                    if (!combined) {
                        items.add(newItem);
                    }

                    lines = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return items;
    }
}
