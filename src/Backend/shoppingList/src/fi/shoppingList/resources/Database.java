package fi.shoppingList.resources;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fi.shoppingList.model.Item;
import fi.shoppingList.model.Items;

/**
 * REST middleware part of the Shopping List application.
 * 
 * Every call to this class will either fetch data from database and returns
 * it to the application, or gets data from application to add/update/delete
 * data on database.
 * 
 * @author Akash Singh
 * @version 2016-11-25
 * @since 1.7
 */
@Singleton
@Path("/items")
public class Database {
	
	/**
	 * Establishes a connection to the database.
	 * 
	 * @return conn database Connection object
	 */
	private Connection connection() {
		System.out.println("-------- MySQL JDBC Connection Test ------------");

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Cannot locate MySQL JDBC Driver?");
			e.printStackTrace();
		}

		System.out.println("MySQL JDBC Driver Registered!");
		Connection conn = null;

		try {
			conn = DriverManager
			.getConnection
			("jdbc:mysql://mydb.tamk.fi/dbc5asingh52","c5asingh", "1Q2w3e4r");
		} catch (SQLException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
		}

		if (conn != null) {
			System.out.println("Connected to database!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return conn;
	}

	/**
	 * Fetches all data from database and passes it to application.
	 * 
	 * @return response holds information about called request
	 */
	@GET
	@Produces("application/xml")
	@Path("")
	public Response getItem() {
		Items items = new Items();
		List<Item> databaseItems = new LinkedList<>();

		// Open connection
		try (Connection conn = connection()) {
			
			// Fetch database items
			try (Statement statement = conn.createStatement()) {
				ResultSet rs = 
						statement.executeQuery("SELECT * FROM shoppingList");

				while (rs.next()) {
					Item databaseItem = new Item(
							rs.getInt("id"),
							rs.getString("itemName"), 
							rs.getInt("itemQuantity"));
					databaseItems.add(databaseItem);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		items.setItems(databaseItems);
		
		return Response.ok(items, MediaType.APPLICATION_XML).build();
	}
	
	/**
	 * Adds an item to the database or updates an item in database.
	 * 
	 * Goes through every item in database, if an item already
	 * exists with the same name, update it. If there is no match
	 * add a new item to the database.
	 * 
	 * @param item item to add to database or update in database
	 * @return response holds information about called request
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/xml")
	@Path("/add")
	public Response addItem(Item item) {
		boolean foundItemInDatabase = false;
		
		Response response = 
				Response
				.status(500)
				.entity("There was problem connecting to database.")
				.build();
		
		String query = "";
		
		// Open connection
		try (Connection conn = connection()) {
			
			query = "SELECT * FROM shoppingList";
			List<Item> databaseItems = new LinkedList<>();
			
			// Fetch database items
			try (Statement statement = conn.createStatement()) {
				ResultSet rs = statement.executeQuery(query);

				while (rs.next()) {
					Item databaseItem = new Item(
							rs.getInt("id"),
							rs.getString("itemName"),
							rs.getInt("itemQuantity"));
					databaseItems.add(databaseItem);
					
					String databaseItemName = 
							databaseItem.getName().toLowerCase();
					
					// Check for matching item
					if (databaseItemName.equals(item.getName().toLowerCase())) {
						foundItemInDatabase = true;
					}
				}
			}
			
			// Update old item if found
			if (foundItemInDatabase) {
				query = 
				"UPDATE shoppingList " +
				"SET itemQuantity = itemQuantity + ? " +
				"WHERE itemName = ?";
				
				try (PreparedStatement statement =
						conn.prepareStatement(query)) {
					statement.setInt(1, item.getQuantity());
					statement.setString(2, item.getName());
					
					int rowsInserted = statement.executeUpdate();
					
					if (rowsInserted > 0) {
						response = 
								Response
								.ok()
								.entity("Item " + item.getName() + 
								" was updated successfully.")
								.build();
					}
				}
			// Create new item
			} else {
				query =
				"INSERT INTO shoppingList (itemName, itemQuantity) " +
				"VALUES (?,?)";
				
				try (PreparedStatement statement =
						conn.prepareStatement(query)) {
					statement.setString(1, item.getName());
					statement.setInt(2, item.getQuantity());
					
					int rowsInserted = statement.executeUpdate();
					
					if (rowsInserted > 0) {
						response = 
								Response
								.status(201)
								.entity("Item " + item.getName() + 
								" was created successfully.")
								.build();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return response;
	}
	
	/**
	 * Deletes an item from database that is given in parameter.
	 * 
	 * Normally we would use @DELETE instead of @PUT, but invocationBuilder
	 * cannot send data when sending delete calls. Making it harder to identify
	 * what we want to delete.
	 * 
	 * @param item item to delete
	 * @return response holds information about called request
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/xml")
	@Path("/delete")
	public Response deleteItem(Item item) {
		Response response = 
				Response
				.status(500)
				.entity("There was problem connecting to database.")
				.build();
		
		String query = ("DELETE FROM shoppingList WHERE itemName = ?");
		
		// Open connection
		try (Connection conn = connection()) {
			
			// Delete item
			try (PreparedStatement statement = conn.prepareStatement(query)) {
				statement.setString(1, item.getName());
				
				int rowsInserted = statement.executeUpdate();
				
				if (rowsInserted > 0) {
					response = 
							Response
							.status(200)
							.entity("Item " + item.getName() + 
							" was deleted successfully.")
							.build();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Directly modifies given item in database.
	 * 
	 * @param item item to modify
	 * @return response holds information about called request
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/xml")
	@Path("/edit")
	public Response editItem(Item item) {
		boolean foundItemInDatabase = false;
		
		Response response = 
				Response
				.status(500)
				.entity("There was problem connecting to database.")
				.build();
		
		String query = "";
		
		// Open connection
		try (Connection conn = connection()) {
			
			query = "SELECT * FROM shoppingList";
			List<Item> databaseItems = new LinkedList<>();
			
			// Fetch database items
			try (Statement statement = conn.createStatement()) {
				ResultSet rs = statement.executeQuery(query);

				while (rs.next()) {
					Item databaseItem = new Item(
							rs.getInt("id"),
							rs.getString("itemName"),
							rs.getInt("itemQuantity"));
					databaseItems.add(databaseItem);
					
					String databaseItemName = 
							databaseItem.getName().toLowerCase();
					
					// Check for matching item
					if (databaseItemName.equals(item.getName().toLowerCase())) {
						foundItemInDatabase = true;
					}
				}
			}
			
			// Update item if matching item was found, should be 100%
			if (foundItemInDatabase) {
				
				if (item.getQuantity() == 0) {
					response = deleteItem(item);
				} else {
					query = 
					"UPDATE shoppingList " +
					"SET itemQuantity = ? " +
					"WHERE itemName = ?";
					
					try (PreparedStatement statement =
							conn.prepareStatement(query)) {
						statement.setInt(1, item.getQuantity());
						statement.setString(2, item.getName());
						
						int rowsInserted = statement.executeUpdate();
						
						if (rowsInserted > 0) {
							response = 
									Response
									.ok()
									.entity("Item " + item.getName() + 
									" was modified successfully.")
									.build();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return response;
	}
	
	/**
	 * Pushes multiple items to the database.
	 * 
	 * @param items items to push to database
	 * @return response holds information about called request
	 */
	@PUT
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/xml")
	@Path("/array")
	public Response addItems(Items items) {
		Response response = 
				Response
				.status(500)
				.entity("There was problem connecting to database.")
				.build();
		
		// Get LinkedList from Items object
		List<Item> itemList = items.getItems();
		
		String query = "";
		
		// Open connection
		try (Connection conn = connection()) {
			
			query = "SELECT * FROM shoppingList";
			List<Item> databaseItems = new LinkedList<>();
			
			// Fetch database items
			try (Statement statement = conn.createStatement()) {
				ResultSet rs = statement.executeQuery(query);
				
				while (rs.next()) {
					Item databaseItem = new Item(
							rs.getInt("id"),
							rs.getString("itemName"), 
							rs.getInt("itemQuantity"));
					databaseItems.add(databaseItem);
				}
			}
			
			// Loop given items against database items
			for (Item item : itemList) {
				boolean itemFound = false;
				Item databaseItem = databaseItems.get(0);
				
				// Database items
				for (int i = 0; i < databaseItems.size() && !itemFound; i++) {
					databaseItem = databaseItems.get(i);
					String databaseItemName =
							databaseItem.getName().toLowerCase();
					String itemName = item.getName().toLowerCase();
					
					// Check for matching items
					if (itemName.equals(databaseItemName)) {
						itemFound = true;
					}
				}
				
				// Matching item found
				// Update old item
				if (itemFound) {
					query = "UPDATE shoppingList "
							+ "SET itemQuantity = ? "
							+ "WHERE itemName = ?";
					
					try (PreparedStatement updateStmt = 
							conn.prepareStatement(query)) {
						updateStmt.setInt(1, databaseItem.getQuantity() 
								+ item.getQuantity());
						updateStmt.setString(2, databaseItem.getName());
						
						int rowsInserted = updateStmt.executeUpdate();
						
						if (rowsInserted > 0) {
							response = 
									Response
									.ok()
									.entity("List was successfully modified.")
									.build();
						}
					}
				// No matching item found
				// Create new item
				} else {
					query = "INSERT INTO "
							+ "shoppingList (itemName, itemQuantity) "
							+ "VALUES (?,?)";
					
					try (PreparedStatement insertStmt = 
							conn.prepareStatement(query)) {
						insertStmt.setString(1, item.getName());
						insertStmt.setInt(2, item.getQuantity());
						
						int rowsInserted = insertStmt.executeUpdate();
						
						if (rowsInserted > 0) {
							response = 
									Response
									.ok()
									.entity("List was successfully modified.")
									.build();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return response;
	}
}
