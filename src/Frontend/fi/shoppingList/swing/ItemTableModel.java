package fi.shoppingList.swing;

import fi.shoppingList.list.MyLinkedList;
import fi.shoppingList.model.Item;

import javax.swing.table.AbstractTableModel;

/**
 * Provides default implementations for ItemTable.
 *
 * <p>
 * Generates column amount, data types and names for ItemTable.
 *
 * @author Akash Singh
 * @version 2016-11-27
 * @since 1.6
 */
public class ItemTableModel extends AbstractTableModel {

    /**
     * Column names.
     */
    private final String[] columnNames = new String[]{
            "Quantity", "Name"
    };

    /**
     * Column data types.
     */
    private final Class[] columnClass = new Class[]{
            Integer.class, String.class
    };

    /**
     * Holds list of shopping list items.
     */
    private MyLinkedList<Item> items;

    /**
     * Initializes items with given items.
     *
     * @param items items to be shown in table
     */
    public ItemTableModel(MyLinkedList<Item> items) {
        this.items = items;
    }

    /**
     * Replaces current items with the given items.
     *
     * @param items items to be shown in table
     */
    public void setItems(MyLinkedList<Item> items) {
        this.items = items;
    }

    /**
     * Get column name at given index.
     *
     * @param column index number
     * @return column name
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * Get column data type at given index.
     *
     * @param columnIndex index number
     * @return data type
     */
    @Override
    public Class <?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    /**
     * Get the number of columns.
     *
     * @return number of columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Get the number of rows.
     *
     * @return number of rows
     */
    @Override
    public int getRowCount() {
        return items.size();
    }

    /**
     * Get value from specific column and row coordinates.
     *
     * @param rowIndex    row index
     * @param columnIndex column index
     * @return value of given row and column indexes
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Item row = items.get(rowIndex);

        if (0 == columnIndex) {
            return row.getQuantity();
        } else if (1 == columnIndex) {
            return row.getName();
        }

        return null;
    }
}
