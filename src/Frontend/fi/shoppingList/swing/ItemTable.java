package fi.shoppingList.swing;

import fi.shoppingList.data.Database;
import fi.shoppingList.data.FileManager;
import fi.shoppingList.list.MyLinkedList;
import fi.shoppingList.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * GUI-version of the Shopping List application.
 * <p>
 * User has a view of items at all times. User can open,
 * save and combine shopping lists. And also start a new one from scratch.
 * <p>
 * User has ability to add items and remove them. User has an option to
 * work as locally or remotely to a database.
 *
 * @author Akash Singh
 * @version 2016-11-27
 * @since 1.8
 */
public class ItemTable extends JFrame {

    /**
     * Holds all the items of the shopping list.
     */
    private MyLinkedList<Item> items = new MyLinkedList<>();

    /**
     * Formats the table.
     */
    private ItemTableModel model = new ItemTableModel(items);

    /**
     * Table.
     */
    private JTable table;

    /**
     * Button to add items to the shopping list.
     */
    private JButton addButton;

    /**
     * Button to remove items from shopping list.
     */
    private JButton removeButton;

    /**
     * Determines local-mode of the shopping list.
     */
    private JRadioButton local;

    /**
     * Determines remote-mode of the shopping list.
     */
    private JRadioButton remote;

    /**
     * Table main menu.
     */
    private JMenuBar menu;

    /**
     * Main menu option.
     */
    private JMenu file;

    /**
     * Option to open new file.
     */
    private JMenuItem fileOpen;

    /**
     * Option to save current shopping list to a file.
     */
    private JMenuItem fileSave;

    /**
     * Option to clear current shopping list.
     */
    private JMenuItem fileNew;

    /**
     * Option to combine two shopping lists.
     */
    private JMenuItem fileCombine;

    /**
     * Currently selected item in shopping list.
     */
    private Item selectedItem;

    /**
     * Currently selected item index in shopping list.
     */
    private int selectedIndex;

    /**
     * Initializes JFrame with components, listeners and general settings.
     */
    public ItemTable() {
        // table
        table = new JTable(model);

        // Set column text alignment and width
        DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0) {
                table.getColumnModel().getColumn(i).setPreferredWidth(115);
                table.getColumnModel().getColumn(i).
                        setCellRenderer(centerRenderer);
            } else {
                table.getColumnModel().getColumn(i).setPreferredWidth(500);
            }
        }

        // buttons
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");

        // menu
        menu = new JMenuBar();
        file = new JMenu("File");
        fileNew = new JMenuItem("New");
        fileOpen = new JMenuItem("Open");
        fileSave = new JMenuItem("Save");
        fileCombine = new JMenuItem("Combine");

        // radio buttons
        local = new JRadioButton("Local");
        remote = new JRadioButton("Remote", true);

        // add action listeners
        // table
        table.addMouseListener(new MyMouseListener());
        // buttons
        addButton.addActionListener(new MyButtonListener());
        removeButton.addActionListener(new MyButtonListener());
        // menu
        fileNew.addActionListener(new FileListener());
        fileOpen.addActionListener(new FileListener());
        fileSave.addActionListener(new FileListener());
        fileCombine.addActionListener(new FileListener());
        // radio buttons
        local.addActionListener(new MyButtonListener());
        remote.addActionListener(new MyButtonListener());

        // create button menu layout
        JPanel buttonMenu = new JPanel(new FlowLayout());

        // create button group and add radio buttons to it
        ButtonGroup group = new ButtonGroup();
        group.add(local);
        group.add(remote);

        // add components to file menu
        file.add(fileNew);
        file.add(fileOpen);
        file.add(fileSave);
        file.add(fileCombine);
        menu.add(file);
        setJMenuBar(menu);

        // add components to button menu
        buttonMenu.add(addButton);
        buttonMenu.add(removeButton);
        buttonMenu.add(local);
        buttonMenu.add(remote);

        // set border layout for this frame
        this.setLayout(new BorderLayout());

        // add components to this frame
        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(buttonMenu, BorderLayout.PAGE_START);

        this.setTitle("Shopping List");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fileNew.setEnabled(false);
        fileOpen.setText("Refresh");
        fileSave.setEnabled(false);
    }

    /**
     * Refreshes table and resets selected item and selected cell in table.
     *
     * <p>
     * Also fetches items from database if user is using remote option.
     * Call this when changes are made to the table or items.
     */
    private void refreshView() {
        selectedItem = null;
        table.clearSelection();

        if (remote.isSelected()) {
            items = new Database().fetchItems();
        }

        model.setItems(items);

        table.revalidate();
        table.repaint();
    }

    /**
     * Listens to all file menu items.
     */
    private class FileListener implements ActionListener {

        /**
         * File menu items changes behaviours depending on radio button.
         *
         * <p>
         * When remote is selected, all menu items are disabled
         * and user can only refresh items from database.
         *
         * @param e menu action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (remote.isSelected()) {

                // Fetch items from database
                if (e.getSource() == fileOpen) {
                    refreshView();
                }

                // Combine list to the database
                if (e.getSource() == fileCombine) {
                    MyLinkedList<Item> combineList = new FileManager().open();
                    new Database().pushItems(combineList);
                    refreshView();
                }
            } else {
                // Open shopping list file
                if (e.getSource() == fileOpen) {
                    items = new FileManager().open();
                }

                // Save shopping list to a file
                if (e.getSource() == fileSave) {
                    new FileManager().save(items);
                }

                // Clear shopping list and start a new
                if (e.getSource() == fileNew) {
                    items.clear();
                }

                // Combine another shopping list with current view of
                // shopping list
                if (e.getSource() == fileCombine) {
                    items = new FileManager().combine(items);
                }

                refreshView();
            }
        }
    }

    /**
     * Listens to all buttons.
     */
    private class MyButtonListener implements ActionListener {

        /**
         * Buttons changes behaviours depending on radio buttons.
         *
         * <p>
         * When remote is selected, all buttons directly modify database.
         * When local is selected, all buttons make changes locally.
         *
         * @param e button action
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            // Remote selected
            if (remote.isSelected()) {
                // Modify file menu items
                fileNew.setEnabled(false);
                fileOpen.setText("Refresh");
                fileSave.setEnabled(false);

                // Add item
                if (e.getSource() == addButton) {
                    Item newItem = new UserInputPane().getItem();

                    if (newItem != null) {
                        new Database().pushItem(newItem);
                        refreshView();
                    }
                }

                // Remove item
                if (e.getSource() == removeButton) {
                    if (selectedItem != null) {
                        new Database().removeItem(selectedItem);
                        refreshView();
                    } else {
                        JOptionPane.showMessageDialog(table,
                                "Select an item.",
                                "Input error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Local selected
            } else {
                // Modify file menu items
                fileNew.setEnabled(true);
                fileOpen.setText("Open");
                fileSave.setEnabled(true);

                // Add item
                if (e.getSource() == addButton) {
                    Item newItem = new UserInputPane().getItem();

                    // If item was created successfully
                    if (newItem != null) {
                        boolean itemFound = false;
                        String newItemName = newItem.getName().toLowerCase();

                        // Find if item already exists with same name
                        for (int i = 0; i < items.size() && !itemFound; i++) {
                            String itemName =
                                    items.get(i).getName().toLowerCase();

                            // If exists, add quantities together
                            if (itemName.equals(newItemName)) {
                                items.get(i).setQuantity(
                                        items.get(i).getQuantity() +
                                                newItem.getQuantity()
                                );
                                itemFound = true;
                            }
                        }

                        // If item was not found, add new item
                        if (!itemFound) {
                            items.add(newItem);
                        }

                        refreshView();
                    }
                }

                // Remove item
                if (e.getSource() == removeButton) {
                    if (selectedItem != null) {
                        items.remove(selectedIndex);
                        refreshView();
                    } else {
                        JOptionPane.showMessageDialog(table,
                                "Select an item.",
                                "Input error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    /**
     * Listens to all mouse clicks made to cells in table.
     */
    private class MyMouseListener extends MouseAdapter {

        /**
         * Registers clicks to a table cells to determine action.
         *
         * Single click to a table cell selects an item and
         * double click to a table cell opens up an edit window for that item.
         *
         * @param e mouse action
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // Single click
            if (e.getClickCount() == 1) {
                selectedIndex  = table.getSelectedRow();
                selectedItem = items.get(selectedIndex);
            }

            // Double click
            if (e.getClickCount() == 2) {
                int selectedItemQuantity = selectedItem.getQuantity();
                Item modifyItem = new UserInputPane(selectedItem).getItem();

                if (remote.isSelected()) {
                    // Modify database item
                    if (modifyItem.getQuantity() != selectedItemQuantity) {
                        new Database().editItem(modifyItem);
                        refreshView();
                    }
                } else {
                    // Modify local item
                    if (modifyItem.getQuantity() != selectedItemQuantity) {

                        if (modifyItem.getQuantity() == 0) {
                            items.remove(selectedIndex);
                        }

                        refreshView();
                    }
                }
            }
        }
    }
}
