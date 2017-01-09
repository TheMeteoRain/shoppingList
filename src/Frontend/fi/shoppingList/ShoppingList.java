package fi.shoppingList;

import fi.shoppingList.cli.ItemList;
import fi.shoppingList.enums.Run;
import fi.shoppingList.swing.ItemTable;

import javax.swing.*;

/**
 * A Shopping List application. There are two versions of shopping lists.
 * One is command-line based. The other one have graphical interface.
 * <p>
 * Command-line-version is only local and can do three simple things.
 * 1: Add single item
 * 2: Add multiple items
 * 3: Show current shopping list items
 * <p>
 * Graphical-version of shopping list can be used as locally
 * or make remote connection to database. With local-version you can:
 * <ul>
 * <li>add items.</li>
 * <li>remove items.</li>
 * <li>modify items.</li>
 * <li>clear list.</li>
 * <li>open list from a file.</li>
 * <li>save list to a file.</li>
 * <li>combine two lists.</li>
 * </ul>
 * <p>
 * With remote connection you're able to:
 * <ul>
 * <li>refresh items (fetches current situation of items from database).</li>
 * <li>combine list</li>
 * <li>add items.</li>
 * <li>remove items.</li>
 * <li>modify items.</li>
 * </ul>
 *
 * @author Akash Singh
 * @version 2016-12-13
 * @since 1.8
 */
public class ShoppingList {

    /**
     * Program's starting point.
     *
     * <p>
     * Run program with "cli" or "gui" argument.
     *
     * @param args used to determine whether to run program as gui or cli mode
     */
    public static void main(String[] args) {
        // default, if no argument was passed
        Run run = Run.NoArgument;

        // change default, if argument was given
        if (args.length > 0) {
            if (args[0].equals("gui")) {
                run = Run.GUI;
            } else if (args[0].equals("cli")) {
                run = Run.CLI;
            }
        }

        switch (run) {
            case GUI:
                SwingUtilities.invokeLater(() -> new ItemTable());
                break;
            case CLI:
                new ItemList();
                break;
            case NoArgument:
                System.out.println("To run this program you must give " +
                        "parameter argument!");
                System.out.println("Type \"gui\" to run as GUI-version");
                System.out.println("Type \"cli\" to run as CLI-version");
                break;
        }
    }
}
