import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import fi.shoppingList.resources.*;

/**
 * Shopping List backend application with single database module.
 * 
 * @author Akash Singh
 * @version 2016-11-23
 * @since 1.6
 */
@ApplicationPath("")
public class ShoppingList extends Application {
    @Override
    public Set < Class <?>> getClasses() {
        final Set < Class <?>> classes = new HashSet < Class <?>>();
        classes.add(Database.class);
        return classes;
    }
}
