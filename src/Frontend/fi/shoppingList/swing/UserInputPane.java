package fi.shoppingList.swing;

import fi.shoppingList.model.Item;

import javax.swing.*;

/**
 * Handles user input by pop-up windows.
 *
 * @author Akash Singh
 * @version 2016-11-28
 * @since 1.7
 */
public class UserInputPane {

    /**
     * Item object to add or modify.
     */
    private Item item;

    /**
     * Window panel.
     */
    private JPanel myPanel;

    /**
     * User input field for item quantity.
     */
    private JTextField quantityField;

    /**
     * User input field for item name.
     */
    private JTextField nameField;

    /**
     * Adds a new item.
     */
    public UserInputPane() {
        initPane();
        addItem();
    }

    /**
     * Modifies an existing item.
     *
     * @param item item to modify
     */
    public UserInputPane(Item item) {
        this.item = item;

        initPane();
        modifyItem();
    }

    /**
     * Initializes pane and components.
     */
    private void initPane() {
        myPanel = new JPanel();

        quantityField = new JTextField(3);
        nameField = new JTextField(10);

        myPanel.add(new JLabel("Quantity:"));
        myPanel.add(quantityField);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameField);
    }

    /**
     * Creates a new item.
     */
    private void addItem() {
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter item quantity and name",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            boolean hitError = false;
            int quantity = 0;

            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                hitError = true;
            }

            if (name.length() == 0) {
                hitError = true;
            }

            if (name.length() <= 0 || quantity <= 0 || hitError) {
                JOptionPane.showMessageDialog(myPanel,
                        "Quantity must be a number and above 0.\n" +
                                "Name must contain characters",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                this.item = new Item(0, nameField.getText().trim(),
                        Integer.parseInt(quantityField.getText()));
            }
        }
    }

    /**
     * Modifies an existing item.
     */
    private void modifyItem() {
        quantityField.setText(item.getQuantity() + "");
        nameField.setText(item.getName());
        nameField.setEditable(false);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Modify item quantity", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int quantity = 0;

            try {
                quantity = Integer.parseInt(quantityField.getText());

                if (quantity < 0) {
                    JOptionPane.showMessageDialog(myPanel,
                            "Quantity must be a number and 0 or above 0.",
                            "Input error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    item.setQuantity(Integer.parseInt(quantityField.getText()));
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(myPanel,
                        "Quantity must be a number.",
                        "Input error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Returns created or modified item.
     *
     * @return item created or modified item.
     */
    public Item getItem() {
        return item;
    }
}
