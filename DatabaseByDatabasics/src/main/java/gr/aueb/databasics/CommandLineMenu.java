package gr.aueb.databasics;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.text.ParseException;
import java.util.InputMismatchException;

/**
 * A class that has all the menus that show while the program is
 * running. It holds an <code>activeTable</code> as well as an
 * <code>activeDatabase</code> based on the user's choices, in
 * order to perform the functions needed.
 * <p>
 * The menu has different levels, from the app startup, where the
 * user has control over all the databases they hold in their system
 * to a micro-level where they have control over the details of a
 * single table.
 * <p>
 * This class has all menus covering each and every one functionality
 * of the app. It performs all i/o transactions with the user. It
 * checks for restricted access on specific attributes, where functions
 * are disallowed to be performed manually by the user etc.
 * 
 * @version 1.0
 */
public class CommandLineMenu {

	private static Table activeTable;
	private static Database activeDatabase;
	
	/**
	 * This method returns the value of the field activeTable.
	 * @return
	 *	The value of the field activeTable.
	 */
	public static Table getActiveTable() {
		return activeTable;
	}
	
	/**
	 * This method expects an input and uses it to set field activeTable.
	 * @param activeTable
	 *  	The value used to set field activeTable.
	 */
	public static void setActiveTable(Table activeTable) {
		CommandLineMenu.activeTable = activeTable;
	}
	
	/**
	 * This method returns the value of the field activeDatabase.
	 * @return
	 *	The value of the field activeDatabase.
	 */
	public static Database getActiveDatabase() {
		return activeDatabase;
	}
	
	/**
	 * This method expects an input and uses it to set field activeDatabase.
	 * @param activeDatabase
	 *  	The value used to set field activeDatabase.
	 */
	public static void setActiveDatabase(Database activeDatabase) {
		CommandLineMenu.activeDatabase = activeDatabase;
	}

	/**
	 * Present the user with the options to create, select, delete a
	 * database or list all available databases
	 */
	public static void databaseChoiceMenu() {
		int choice = 0;
		for (;;) {
			System.out.println("Choose one of the following:"
					+ "\n1.Create new database.\n2.Select Database"
					+ "\n3.Delete Database"
					+ "\n4.List databases\n5.Exit");
			choice = checkChoice(1, 5);
			if (choice == 1) {
				databaseCreationMenu();
			}
			if (choice == 2) {
				System.out.println("Type in the name of the "
						+ "database of your choice.");
				String s = readDatabase();
				if (s == null) {
					continue;
				}
				setActiveDatabase(readDatabase(s));
				databaseMenu();
			}
			if (choice == 3) {
				System.out.println("Type in the name of the "
						+ "database you want to delete.");
				String s = readDatabase();
				if (s == null) {
					continue;
				}
				boolean sure = areYouSure();
				if (!sure) {
					databaseChoiceMenu();
				}
				System.out.println("Do you want to delete contents from disk?");
				String answer = checkAnswer();
				if (answer.equals("yes")) {
					DatabaseUniverse.deleteDatabaseFromDisk(s);
				}
				readDatabase(s).delete();
			}
			if (choice == 4) {
				DatabaseUniverse.listDatabases();
			}
			if (choice == 5) {
				boolean sure = areYouSure();
				if (!sure) {
					databaseChoiceMenu();
				}
				programTermination();
			}
		}
	}

	/**
	 * This method's purpose is to terminate the program if no unsaved data
	 * exist or ask the user (yes/no) whether he wants to terminate
	 * the program if unsaved data exist in one of the loaded databases.
	 */
	public static void programTermination() {
		if (!checkUnsaved()) {
			System.out.println("Terminating...");
			System.exit(0);
		}
		System.out.println("You have unsaved data. Are you sure you wish to "
				+ "proceed with termination?");
		String answer = checkAnswer();
		if (answer.equals("yes")) {
			System.out.println("Terminating...");
			System.exit(0);
		} else {
			return;
		}
	}
	
	/**
	 * This method checks if unsaved data exist in any of the databases,
	 * prints a message for each database that contains unsaved data
	 * and returns a <code>boolean</code> to indicate whether unsaved
	 * data exist in DatabaseUniverse
	 * @return
	 * 			a <code>boolean</code> variable that is returned as 
	 * 			true if unsaved data exist within at least one database,
	 * 			and false if not.
	 */
	public static boolean checkUnsaved() {
		boolean unsavedData = false;
		for (Database database : DatabaseUniverse.getAllDatabases()) {
			if (!database.isSaved()) {
				System.out.println("Unsaved data in database "
			+ database.getName() + "!");
			unsavedData = true;
			}
		}
		return unsavedData;
	}

	/**
	 * Check if an answer is acceptable
	 * 
	 * @return 	Returns the accepted answer or recursively repeats until
	 * 			an acceptable answer is given.
	 */
	public static String checkAnswer() {
		Scanner input = new Scanner(System.in);
		String answer = input.next().toLowerCase();
		if (cancel(answer)) {
			return answer;
		}
		if ((!answer.equals("yes")) && (!answer.equals("no"))) {
			System.out.println("Not a valid answer. Please try again.");
			return checkAnswer();
		}
		return answer;
	}

	/**
	 * Cancels action and returns to the previous menu at any time
	 * the word "cancel" is given as input by the user
	 * 
	 * @param str	The user's input
	 * @return		Returns <code>true</code> if the user has typed
	 * 				"cancel" or <code>false</code> if the input is
	 * 				anything else
	 */
	public static boolean cancel(String str) {
		if (str.equals("cancel")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Cancels a whole procedure (e.g. Database creation) and returns
	 * to the previous menu if the word "cancel" or "yes" is given as 
	 * input by the user, when asked if they want to completely cancel
	 * procedure in menu
	 * 
	 * @param str	The user's input
	 * @return		Returns <code>true</code> if the user has typed
	 * 				"cancel" or <code>false</code> if the input is
	 * 				anything else
	 */
	public static boolean cancelProcedure(String str) {
		if (str.equals("cancel") || str.equals("yes")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if the user is certain about an action that makes an
	 * important change (e.g. table deletion)
	 * 
	 * @return		Returns <code>true</code> if the user has typed 
	 * 				"yes" or <code>false</code> if the input is "no"
	 */
	public static boolean areYouSure() {
		System.out.println("Are you sure?");
		Scanner input = new Scanner(System.in);
		String answer = checkAnswer();
		if (answer.equalsIgnoreCase("yes"))
			return true;
		else if (answer.equalsIgnoreCase("no"))
			return false;
		else
			return areYouSure();
	}
	
	/**
	 * Create the first table. Give the user 
	 * the option to add more tables to the
	 * database in its creation process. Give access 
	 * to database menu.
	 */
	public static void databaseCreationMenu() {
		String flag = null;
		String databaseName = null;
		do {
			Scanner input = new Scanner(System.in);
			System.out.print("Please type in the name of the new database: ");
			databaseName = input.nextLine().trim();
			if (cancel(databaseName)) {
				return;
			}
			if (DatabaseUniverse.exists(databaseName) == true)
				System.out.println("This database already exists.");
		} while (DatabaseUniverse.exists(databaseName) == true);
		Database database = new Database(databaseName);
		setActiveDatabase(database);
		createTable();
		System.out.println("Do you want to add a table?");
		flag = checkAnswer();
		if (cancel(flag)) {
			boolean sure = true;
			String str;
			do {
				System.out.println("Cancel database creation?");
				str = checkAnswer();
				if (cancelProcedure(str)) {
					sure = areYouSure();
					if (sure) {
						activeDatabase.delete();
						setActiveDatabase(null);
					}
				}
			} while (!sure && cancelProcedure(str));
			return;
		}
		if (flag.equals("yes")) {
			addTable();
		}
		activeDatabase.view();
		databaseMenu();
	}

	/**
	 * Add table in database. Offer options to create a brand new table 
	 * or copy an existing one. Check if the input choice is valid.
	 */
	public static void addTable() {
		int choice;
		System.out.println(
				"Choose one of the following:\n1.Create a new table"
				+ "\n2.Copy an existing table from the database."
				+ "\n3.Import a table from a csv file");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			createTable();
			return;
		}
		if (choice == 2) {
			copyTableMenu();
			return;
		}
		if (choice == 3) {
			importTableMenu();
			return;
		}
		addTable();
	}

	/**
	 * Create a table and fill it (attributes and entries).
	 */
	public static void createTable() {
		Table table = tableCreation();
		if (table != null) {
			setActiveTable(table);
			attributeCreationMenu();
			entryCreationMenu();
		}
	}

	/**
	 * Check if the name the user inputs is already given to another table and if
	 * not create the table.
	 * 
	 * @return	Returns the Table object to be created
	 */
	public static Table tableCreation() {
		String tableName = null;
		do {
			Scanner input = new Scanner(System.in);
			System.out.print("Please type in the name of the new table: ");
			tableName = input.nextLine().trim();
			if (cancel(tableName)) {
				return null;
			}
			if (activeDatabase.exists(tableName) == true) {
				System.out.println("This table already exists.");
			}
		} while (activeDatabase.exists(tableName) == true);
		Table table = new Table(tableName);
		return table;
	}

	/**
	 * Check if the attribute name the user inputs is 
	 * available and if yes create the attribute.
	 * 
	 * @return	Returns the Attribute object's name
	 */
	public static String attributeCreation() {
		Scanner input = new Scanner(System.in);
		String attributeName = null;
		do {
			System.out.print("Type in the name of the new attribute: ");
			attributeName = input.nextLine().trim();
			if (cancel(attributeName)) {
				return null;
			}
			if (activeTable.exists(attributeName))
				System.out.println("This attribute already exists.");
		} while (activeTable.exists(attributeName) == true);
		return attributeName;
	}

	/*
	 * Ask the user if they want to create more attributes. Create attributes 
	 * until user answers no.
	 */
	/**
	 * Ask the user if they want to create more attributes. Create attributes 
	 * if they answer affirmatively.
	 */
	public static void attributeCreationMenu() {
		String answer = null;
		String attributeName = attributeCreation();
		if (attributeName != null) {
			do {
				
				addAttributeMenu(attributeName);
				System.out.println("Do you want to create another attribute?");
				answer = checkAnswer();
				if (answer.equals("yes"))	
					attributeName = attributeCreation();
			} while ((answer.equalsIgnoreCase("yes")) 
				|| (!answer.equalsIgnoreCase("no")));
		}
	}

	/**
	 * Menu for attribute creation.
	 * 
	 * @param name	The name of the new attribute
	 */
	public static void addAttributeMenu(String name) 
			throws InputMismatchException {
		boolean correctEntry;
		int choice = 0;
		do {
			correctEntry = true;
			System.out.println("Your attribute can be of any of the "
					+ "following types:\n1. Text\n2. Single letter\n"
					+ "3. Integer\n" + "4. Decimal\n" + "5. Date\n"
					+ "the number that corresponds to the type you want.");
			choice = checkChoice(1, 8);
			if (choice == -1) {
				return;
			}
			correctEntry = Table.checkInput(choice, correctEntry);
		} while (correctEntry == false);
		activeTable.newAttribute(name, choice);
	}

	/**
	 * Call method that creates new entry while the user's answer is 
	 * affirmative.
	 */
	public static void entryCreationMenu() {
		String answer = null;
		do {
			addEntryMenu();
			System.out.println("Do you want to add another entry?");
			answer = checkAnswer();
		} while ((answer.equalsIgnoreCase("yes")) 
				|| (!answer.equalsIgnoreCase("no")));
	}

	/**
	 * Add new entry. Read entry line, split it and check it.
	 */
	public static void addEntryMenu() {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		String[] entries;
		do {
			System.out.println("Type an entry line");
			String entry = input.nextLine();
			if (cancel(entry)) {
				return;
			}
			correctEntry = true;
			entries = entry.split(",");
			for (int i = 0; i < entries.length; i++) {
				entries[i] = entries[i].trim();
			}
			correctEntry = activeTable.checkEntry(entries);
			if (correctEntry == false) {
				System.out.println("Please try again!");
			}
		} while (correctEntry == false);
		activeTable.newEntry(entries);
	}

	/**
	 * Fill attribute with "--"
	 * 
	 * @param attName	The attribute's name
	 */
	public static void addVoidEntry(String attName) {
		int index = activeTable.attPositions
				(new ArrayList<String>(Arrays.asList(attName))).get(0);
		for (int i = 0; i < activeTable.getLines(); i++) {
			activeTable.getAttributes(index).setEntryField("--");
		}
			
		
	}
	/**
	 * Menu to copy a table. Reads an existing table name 
	 * and copies the respective table to a new one.
	 */
	public static void copyTableMenu() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table "
				+ "that you want to copy");
		nameCopy = readTable();
		if (cancel(nameCopy)) {
			return;
		}
		namePaste = tableCreation().getName();
		activeDatabase.copyTable(nameCopy, namePaste);
	}

	/**
	 * Read a table name and check if it corresponds to a table.
	 * If not try again.
	 * 
	 * @return	Returns the name of the table or <code>null</code> if the
	 * 			user chooses to cancel
	 */
	public static String readTable() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		if (cancel(tableName)) {
			return null;
		}
		if (tableName.equals("all")) {
			return tableName;
		}
		while (activeDatabase.exists(tableName) == false) {
			System.out.println("This table does not exist."
					+ " Please type an existing name.");
			tableName = input.nextLine();
			if (cancel(tableName)) {
				return null;
			}
			if (tableName.equals("all")) {
				return tableName;
			}
		}
		return tableName;
	}
	
	/**
	 * Read the name of a database the user types.
	 * Check if the database exists and if yes ask for
	 * another name. Return the name the user types.
	 * 
	 * @return	Returns the name of the database or <code>null</code> if the
	 * 			user chooses to cancel
	 */
	public static String readDatabase() {
		Scanner input = new Scanner(System.in);
		String databaseName = input.nextLine();
		if (cancel(databaseName)) {
			return null;
		}
		while (DatabaseUniverse.exists(databaseName) == false) {
			System.out.println("This database does not exist."
					+ " Please type an existing name.");
			databaseName = input.nextLine();
			if (cancel(databaseName)) {
				return null;
			}
		}
		return databaseName;
	}

	/**
	 * Return Table object by recognizing an existing table name.
	 * 
	 * @param tableName	The name of the table read
	 * @return			Returns a Table object with that name
	 */
	public static Table readTable(String tableName) {
		Table temp = activeDatabase.getTables(
				activeDatabase.position(tableName));
		setActiveTable(temp);
		return temp;
	}
	
	/**
	 * Return the database that has the name of the parameter.
	 * 
	 * @param databaseName	The name of the database read
	 * @return				Returns a Database object with that name
	 */
	public static Database readDatabase(String databaseName) {
		Database temp = DatabaseUniverse.getDatabases(
				DatabaseUniverse.position(databaseName));
		setActiveDatabase(temp);
		return temp;
	}

	/**
	 * Present options to edit database (add table, view table, 
	 * delete table, select table, save table, list all tables).
	 */
	public static void databaseMenu() {
		int choice = 0;
		do {
			System.out.println("Choose one of the following:"
					+ "\n1.Add a new table\n2.View a table\n3.Delete a table"
					+ "\n4.Select a table to work on"
					+ "\n5.Save database"
					+ "\n6.List tables\n7.Back");
			choice = checkChoice(1, 7);
			if (choice == -1) {
				return;
			}
			if (choice == 1) {
				boolean sure = areYouSure();
				if (!sure) {
					databaseMenu();
				}
				addTable();
				activeDatabase.setSaved(false);
				activeDatabase.view();
			} else if (choice == 2) {
				viewTableMenu();
			} else if (choice == 3) {
				deleteTableMenu();
				activeDatabase.setSaved(false);
				activeDatabase.view();
			} else if (choice == 4) {
				System.out.println(
						"Type in the name of the table of your choice.");
				String str = readTable();
				if (str == null) {
					return;
				}
				setActiveTable(readTable(str));
				tableMenu();
				activeDatabase.view();
			} else if (choice == 5) {
				activeDatabase.saveDatabase();
				activeDatabase.setSaved(true);
			} else if (choice == 6) {
				activeDatabase.listTables();
			} else if (choice == 7) {
				setActiveDatabase(null);
				return;
			} else {
				System.out.println("This is not a valid intput.");
				databaseMenu();
			}
		} while (choice != 7);
	}

	/**
	 * Menu for viewing a table
	 */
	public static void viewTableMenu() {
		System.out.println(
				"Please enter the name of the table that you want to view");
		String s = readTable();
		if (s == null) {
			return;
		}
		if (s.equals("all")) {
			activeDatabase.view();
		} else {
			readTable(s).view();
		}
	}

	/**
	 * Menu for deleting a table
	 */
	public static void deleteTableMenu() {
		System.out.println("Please enter the name"
				+ " of the table that you want to delete");
		String s = readTable();
		if (s == null) {
			return;
		}
		boolean sure = areYouSure();
		if (!sure) {
			databaseMenu();
		}
		readTable(s).delete();
	}

	/**
	 * Present options to edit a specific table (search,
	 * sort, view, add attribute and entry, change data, delete data).
	 */
	public static void tableMenu() {
		System.out.println("Choose one of the following:"
				+ "\n1.Search in this table\n2.Sort this table\n"
				+ "3.Present data\n4.Add an attribute\n" 
				+ "5.New entry\n6.Change data\n7.Delete data\n8.Back" );
		int choice = checkChoice(1, 8);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			searchMenu();
		} else if (choice == 2) {
			sortMenu(activeTable.getName());
			activeDatabase.setSaved(false);
		} else if (choice == 3) {
			viewOptions();
		} else if (choice == 4) {
			boolean sure = areYouSure();
			if (!sure) {
				tableMenu();
			}
			addAttributeOptions();
			activeDatabase.setSaved(false);
		} else if (choice == 5) {
			addEntryOptions();
			activeDatabase.setSaved(false);
		} else if (choice == 6) {
			changeDataOptions();
			activeDatabase.setSaved(false);
		} else if (choice == 7) {
			deleteMenu();
			activeDatabase.setSaved(false);
		} else if (choice == 8) {
			setActiveTable(null);
			return;
		} else {
			System.out.println("This is not a valid choice");
		}
		tableMenu();
	}

	/**
	 * This method enables the user to make use of the search function.
	 * The user can insert a set of attribute names(up to the attributes
	 * existing in the table) and a value for each one of them.
	 * Then the method {@link Table#search(ArrayList, ArrayList)} is called, 
	 * returning the positions of the matching lines, which will feed the 
	 * {@link Table#viewLines(ArrayList)} method, presenting the set of 
	 * lines that match the search criteria.
	 */
	public static void searchMenu() {
		Scanner input = new Scanner(System.in);
		ArrayList<String> attNames = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		for (int i = 0; i < activeTable.getAttributeNumber(); i++) {
			System.out.print("Please enter an attribute name according to "
					+ "which you want to search: ");
			String attName = readAttribute(activeTable.getName());
			if (attName == null) {
				return;
			}
			attNames.add(attName);
			System.out.print("Please enter an element you want to "
					+ "search in the column previously inserted: ");
			String element = input.nextLine().trim();
			if (cancel(element)) {
				return;
			}
			elements.add(element);
			System.out.println("Would you like to add another attribute for"
					+ " search? ");
			String addAttribute = checkAnswer();
			if (cancel(addAttribute)) {
				return;
			}
			if (addAttribute.equalsIgnoreCase("yes")) {
				continue;
			} else {
				break;
			}
		}
		ArrayList<Integer> entryPositions = activeTable.search(
				attNames, elements);
		activeTable.viewLines(entryPositions);
	}

	/**
	 * Menu for viewing specific attributes of a table
	 * 
	 * @param tableName	The name of the table holding the attributes
	 */
	public static void menuViewAttribute(String tableName) {
		ArrayList<String> atts = new ArrayList<String>();
		boolean flag = true;
		while (flag) {
			System.out.println("Please enter an attribute of this table"
					+ " you want to view");
			String s = readAttribute(tableName);
			if (s == null) {
				return;
			}
			atts.add(s);
			System.out.println("Do you want to view another attribute"
					+ " of this table?");
			String ch = checkAnswer();
			if (cancel(ch)) {
				return;
			}
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			}
		}
		activeTable.viewAttribute(atts);
	}

	/**
	 * Menu for vieweing specific lines of a table
	 * 
	 * @param tableName	The name of the table holding the lines to view
	 */
	public static void menuViewLines(String tableName) {
		ArrayList<Integer> lines = new ArrayList<Integer>();
		boolean flag = true;
		while (flag) {
			System.out.println("Please enter the number of the line "
					+ "you want to view");
			int pos = readLines(tableName);
			if (pos == -1) {
				return;
			}
			lines.add(pos);
			System.out.println("Do you want to view another line "
					+ "of this table?");
			String ch = checkAnswer();
			if (cancel(ch)) {
				return;
			}
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			}
		}
		readTable(tableName).viewLines(lines);
	}

	/**
	 * Present the user with the options of viewing either a column or a line.
	 */
	public static void viewOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:" 
				+ "\n1.View column"
				+ "\n2.View lines"
				+ "\n3.Back");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			menuViewAttribute(activeTable.getName());
		} else if (choice == 2) {
			menuViewLines(activeTable.getName());
		} else if (choice == 3) {
			return;
		}
	}

	/**
	 * Present the user with the options they have in order to add an attribute to a
	 * table (create a new one or copy an existing one).
	 */
	public static void addAttributeOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:"
				+ "\n1.Create a new attribute"
				+ "\n2.Copy an existing attribute"
				+ "\n3.Back");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			String attributeName = attributeCreation();
			if (attributeName != null) {
				addAttributeMenu(attributeName);
				addVoidEntry(attributeName);
			}
		} else if (choice == 2) {
			System.out.println("Choose one of the following:" 
					+"\n1.Copy an attribute" + "\n2.Cut an attribute");
			int ch = checkChoice(1,2);
			if (ch == 1) {
				copyAttributeMenu(activeTable.getName(),1);
			} else {
				cutAttributeMenu(activeTable.getName(),1);
			}
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid input.");
			addAttributeOptions();
		}
	}

	/**
	 * Menu for copying an attribute.
	 * 
	 * @param namePaste	The target table
	 * @param choice	Choice from the menu
	 */
	public static void copyAttributeMenu(String namePaste,int choice) {
		Scanner input = new Scanner(System.in);
		String answer = null;
		do {
			System.out.println("Please enter the name of the table that contains the attribute");
			String nameCopy = readTable();
			if (nameCopy == null) {
				return;
			}
			int pos = activeDatabase.position(nameCopy);
			activeDatabase.getTables(pos).view();
			System.out.println("Please enter the name of the attribute that you want to copy");
			String attNameC = readAttribute(nameCopy);
			if (attNameC == null) {
				return;
			}
			activeTable.view();
			if (choice == 1) {
				System.out.println("Please enter the name of the new attribute");
				String attNameP = input.nextLine();
				activeDatabase.copyNewAttribute(nameCopy, attNameC, namePaste, attNameP);
			} else {
				System.out.println("Please enter the name of the attribute where you want to paste");
				String attNameP = readAttribute(namePaste);
				if (attNameP == null) {
					return;
				}
				activeDatabase.copyExistingAttribute(nameCopy, attNameC, namePaste, attNameP);
			}	
			System.out.println("Do you want to copy another attribute?");
			answer = checkAnswer();
		} while (answer.equalsIgnoreCase("yes"));
	}

	/**
	 * Menu for cutting/pasting an attribute.
	 * 
	 * @param namePaste	The target table
	 * @param choice	Choice from the menu
	 */
	public static void cutAttributeMenu(String namePaste, int choice) {
		Scanner input = new Scanner(System.in);
		String answer = null;
		do {
			System.out.println("Please enter the name of the table that contains the attribute");
			String nameCopy = readTable();
			if (nameCopy == null) {
				return;
			}
			int pos = activeDatabase.position(nameCopy);
			activeDatabase.getTables(pos).view();
			System.out.println("Please enter the name of the attribute that you want to cut");
			String attNameC = readAttribute(nameCopy);
			activeTable.view();
			if (choice == 1) {
				System.out.println("Please enter the name of the new attribute");
				String attNameP = input.nextLine();
				activeDatabase.cutNewAttribute(nameCopy, attNameC, namePaste, attNameP);
			} else {
				System.out.println("Please enter the name of the attribute where you want to paste");
				String attNameP = input.nextLine();
				activeDatabase.cutExistingAttribute(nameCopy, attNameC, namePaste, attNameP);
			}	
			System.out.println("Do you want to copy another attribute?");
			answer = checkAnswer();
		} while (answer.equalsIgnoreCase("yes"));
	}
	
	/**
	 * Offer the user the option to add an entry manually,
	 * or copy an existing one
	 */
	public static void addEntryOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:"
				+ "\n1.Create a new entry"
				+ "\n2.Copy an existing entry"
				+ "\n3.Back");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			entryCreationMenu();
		} else if (choice == 2) {
			System.out.println("Choose one of the following:"
					+ "\n1.Copy an entry" + "\n2.Cut an entry");
			int ch = checkChoice(1,2);
			if (ch == 1) {
				copyEntryMenu(1);
			} else {
				cutEntryMenu(1);
			}
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			addEntryOptions();
		}
	}

	/**
	 * Offer options of adding the entry and of replacing
	 * an existing one.
	 * 
	 * @param choice	Choice frmo the menu
	 */
	public static void copyEntryMenu(int choice) {
		String answer = null;
		do {
			String nameCopy;
			System.out.println("Please enter the name of the table "
					+ "that contains the entry");
			nameCopy = readTable();
			if (nameCopy == null) {
				return;
			}
			int pos = activeDatabase.position(nameCopy);
			activeDatabase.getTables(pos).view();
			System.out.println("Please enter the number of the entry "
					+ "that you want to copy");
			int entryNumCopy = readLines(nameCopy);
			if (entryNumCopy == -1) {
				return;
			}
			if (choice == 1)
				copyAddEntry(nameCopy, entryNumCopy);
			if (choice == 2)
				copyReplaceEntry(nameCopy, entryNumCopy);
			System.out.println("Do you want to copy another entry?");
			answer = checkAnswer();
		} while (answer.toLowerCase().equals("yes"));
	}

	/**
	 * Menu for cutting/pasting an entry.
	 * 
	 * @param choice	Choice from the menu
	 */
	public static void cutEntryMenu(int choice) {
		String answer = null;
		do {
			String nameCopy;
			Scanner input = new Scanner(System.in);
			System.out.println("Please enter the name of the table that contains the entry");
			nameCopy = readTable();
			if (nameCopy == null) {
				return;
			}
			if (activeTable.getName().equals(nameCopy)) {
				System.out.println("It's not possible to cut and"
						+" paste in the same table\n");
				return;
			} else {
				int pos = activeDatabase.position(nameCopy);
				activeDatabase.getTables(pos).view();
				System.out.println("Please enter the number of the entry that you want to cut");
				int entryNumCopy = readLines(nameCopy);
				if (entryNumCopy == -1) {
					return;
				}
				if (choice == 1)
					cutAddEntry(nameCopy, entryNumCopy);
				if (choice == 2)
					cutReplaceEntry(nameCopy, entryNumCopy);
				System.out.println("Do you want to copy another entry?");
				answer = checkAnswer();
			}
		} while (answer.equals("yes"));
	}

	/**
	 * Copy and add an entry
	 * 
	 * @param nameCopy		The name of the table that contains the entry 
	 * @param entryNumCopy	Practically, the line number
	 */
	public static void copyAddEntry(String nameCopy, int entryNumCopy) {
		activeDatabase.copyNewEntry(
				nameCopy, entryNumCopy, activeTable.getName());
	}

	/**
	 * Cut and paste an entry
	 * 
	 * @param nameCopy		The name of the table that contains the entry 
	 * @param entryNumCopy	Practically, the line number
	 */
	public static void cutAddEntry(String nameCopy, int entryNumCopy) {
		activeDatabase.cutNewEntry(
				nameCopy, entryNumCopy, activeTable.getName());
	}

	/**
	 * Copy and replace an entry.
	 * 
	 * @param nameCopy		The name of the table that contains the entry
	 * @param entryNumCopy	Practically, the line number
	 */
	public static void copyReplaceEntry(String nameCopy, int entryNumCopy) {
		activeTable.view();
		System.out.println("Please enter the number of the entry "
				+ "that you want to replace");
		int entryNumPaste = readLines(activeTable.getName());
		if (entryNumPaste == -1) {
			return;
		}
		activeDatabase.copyExistingEntry(
				nameCopy, entryNumCopy, activeTable.getName(), entryNumPaste);
	}

	/**
	 * Cut and replace an entry.
	 * 
	 * @param nameCopy		The name of the table that contains the entry
	 * @param entryNumCopy	Practically, the line number
	 */
	public static void cutReplaceEntry(String nameCopy, int entryNumCopy) {
		activeTable.view();
		System.out.println("Please enter the number of the entry that you want to replace");
		int entryNumPaste = readLines(activeTable.getName());
		if (entryNumPaste == -1) {
			return;
		}
		activeDatabase.cutExistingEntry(nameCopy, entryNumCopy, activeTable.getName(), entryNumPaste);
	}

	/**
	 * Read an existing line number. Check its existence and if it is a number.
	 * 
	 * @param tableName	The name of the table under check
	 * @return			The position of the line in an ArrayList (that's why it's -1)
	 */
	public static int readLines(String tableName) {
		Scanner input = new Scanner(System.in);
		String choice = input.next();
		int lineNum = -1;
		if (cancel(choice)) {
			return lineNum;
		}
		try {
			lineNum = Integer.parseInt(choice);
		} catch (NumberFormatException e) {
			System.out.println("This is not a number. Please try again.");
		}
		while (lineNum > activeTable.getLines() || lineNum <= 0) {
			System.out.println("This line does not exist. "
					+ "Please type an existing line.");
			lineNum = input.nextInt();
		}
		return lineNum - 1;
	}

	/**
	 * Offer options in order to change data.
	 */
	public static void changeDataOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:"
				+ "\n1.Change an entry manually"
				+ "\n2.Replace elements of a line "
				+ "with elements from another line."
				+ "\n3.Replace entry."
				+ "\n4.Replace attribute.\n5.Back");
		choice = checkChoice(1, 5);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			menuDataChange();
		} else if (choice == 2) {
			String answer = null;
			do {
				System.out.println("Choose one of the following:" +"\n1.Copy an element"
									+"\n2.Cut an element");
				int ch = checkChoice(1,2);
				if (ch == 1) {
					copyElementMenu(activeTable.getName());
				} else {
					copyElementMenu(activeTable.getName());
				}
				System.out.println("Do you want to replace another element?");
				answer = checkAnswer();
			} while (answer.equalsIgnoreCase("yes"));
		} else if (choice == 3) {
			System.out.println("Choose one of the following:" +"\n1.Copy an entry"
					+"\n2.Cut an entry");
			int ch = checkChoice(1,2);
			if (ch == 1) {
				copyEntryMenu(2);
			} else {
				cutEntryMenu(2);
			}
		} else if (choice == 4) {
			System.out.println("Choose one of the following:" +"\n1.Copy an attribute"
					+"\n2.Cut an attribute");
			int ch = checkChoice(1,2);
			if (ch == 1) {
				copyAttributeMenu(activeTable.getName(), 2);
			} else {
				cutAttributeMenu(activeTable.getName(), 2);
			}
		} else if (choice == 5) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			changeDataOptions();
		}
	}

	/**
	 * Copy and replace an element.
	 * 
	 * @param tableName	The name of the target table
	 */
	public static void copyElementMenu(String tableName) {
		String nameCopy;
		System.out.println("Please enter the name of the table that "
				+ "contains the element that you want to copy");
		nameCopy = readTable();
		if (nameCopy == null) {
			return;
		}
		int pos = activeDatabase.position(nameCopy);
		activeDatabase.getTables(pos).view();
		System.out.println("Please enter the name of the attribute that "
				+ "contains the element that you want to copy");
		String attNameC = readAttribute(nameCopy);
		if (attNameC == null) {
			return;
		}
		System.out.println("Please enter the number of the line that "
				+ "contains the element that you want to copy");
		int lineC = readLines(nameCopy);
		if (lineC == -1) {
			return;
		}
		activeTable.view();
		System.out.println("Please enter the name of the "
				+ "attribute where you want to paste the element");
		String attNameP = readAttributeRestrictedPermission(tableName);
		if (attNameP == null) {
			return;
		}
		System.out.println("Please enter the number of the "
				+ "line where you want to paste the element");
		int lineP = readLines(tableName);
		if (lineP == -1) {
			return;
		}
		activeDatabase.copyElement(
				nameCopy, attNameC, lineC, tableName, attNameP, lineP);
	}

	/**
	 * Read an attribute and check its existence.
	 * 
	 * @param tableName	The name of table that is checked for the attribute
	 * @return			Returns the attribute name or <code>null</code> if the
	 * 					user cancels
	 */
	public static String readAttributeRestrictedPermission(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		if (cancel(attName)) {
			return null;
		}
		while (activeTable.exists(attName) == false 
				|| attName.equalsIgnoreCase("Last Modified") 
				|| attName.equals("#")) {
			if (activeTable.exists(attName) == false) {
				System.out.println("This attribute does not exist."
						+ " Please type an existing name.");
			} else {
				System.out.println("You do not have permission to "
						+ "access that attribute. Try again.");
			}
			attName = input.nextLine();
			if (cancel(attName)) {
				return null;
			}
		}
		return attName;
	}
	
	/**
	 * Read an attribute and check its existence.
	 * 
	 * @param tableName	The name of table that is checked for the attribute
	 * @return			Returns the attribute name or <code>null</code> if the
	 * 					user cancels
	 */
	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		int pos = activeDatabase.position(tableName);
		if (cancel(attName)) {
			return null;
		}
		while (activeDatabase.getTables(pos).exists(attName) == false) {
				System.out.println("This attribute does not exist."
						+ " Please type an existing name.");
				attName = input.nextLine();
				if (cancel(attName)) {
					return null;
				}
		}
		return attName;
	}

	/**
	 * Menu for sort function. Read the attribute to act as key.
	 * 
	 * @param tableName	The name of the table to sort
	 */
	public static void sortMenu(String tableName) {
		System.out.println("Please type in the name of the "
				+ "attribute you want to sort the table by.");
		String keyAttribute = readAttribute(tableName);
		if (keyAttribute == null) {
			return;
		}
		System.out.println("You want to sort the table by ascending "
				+ "or descending order?");
		int order = readOrder();
		if (order == 0) {
			return;
		}
		try {
			readTable(tableName).sortTable(keyAttribute, order);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read the order for sort function (ascending or descending).
	 * 
	 * @return	Returns +1 for ascending or -1 for descending
	 */
	public static int readOrder() {
		Scanner input = new Scanner(System.in);
		String order = input.next().toLowerCase();
		if (cancel(order)) {
			return 0;
		}
		if (order.equals("ascending"))
			return 1;
		if (order.equals("descending"))
			return -1;
		System.out.println(
				"This is not a valid choice. "
						+ "Do you want to sort the table by ascending or descending order");
		return readOrder();
	}

	/**
	 * Menu for data change function. Asks for the line number that will change and
	 * continues to do so for all the lines the user chooses.
	 */
	public static void menuDataChange() {
		String done = "yes";
		int num;
		ArrayList<String> attributes;
		ArrayList<String> values;
		do {
			System.out.println("Type in the number of the line"
					+ " you want to change");
			num = readLines(activeTable.getName());
			if (num == -1) {
				return;
			}
			attributes = readAttributes();
			if (attributes.isEmpty()) {
				return;
			}
			values = readValues(num, attributes);
			if (values.isEmpty()) {
				return;
			}
			activeTable.dataChange(num, attributes, values);
			activeTable.view();
			System.out.println("Do you want to change another line?");
			done = checkAnswer();
		} while (done.equals("yes"));
	}

	/**
	 * Read an ArrayList of Attributes that are used in change data function.
	 * 
	 * @return	Returns an ArrayList of Attributes or an empty ArrayList if 
	 * 			the user cancels 
	 */
	public static ArrayList<String> readAttributes() {
		ArrayList<String> atts = new ArrayList<String>();
		boolean flag = false;
		do {
			for (int i = 1; i < activeTable.getAttributeNumber() - 1; i++) {
				System.out.println("Do you want to change attribute "
						+ activeTable.getAttributes(i).getName() + "?");
				String choice = checkAnswer();
				if (cancel(choice)) {
					return new ArrayList<String>();
				}
				if (choice.equals("yes")) {
					atts.add(activeTable.getAttributes(i).getName());
					flag = true;
				}
			}
			if (flag == false) {
				System.out.print("You have to change one attribute");
			}
		} while (flag == false);
		return atts;
	}

	/**
	 * Read all the values that will change in data change function.
	 * 
	 * @param num	The line affected by the data change
	 * @param atts	The attributes affected by the data change
	 * @return		Returns the values used to change the fields or an 
	 * 				empty ArrayList if the user cancels
	 */
	public static ArrayList<String> readValues(
			int num, ArrayList<String> atts) {
		ArrayList<String> values = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		String ch;
		int j = 0;
		for (int i = 1; i < activeTable.getAttributeNumber() - 1; i++) {
			if (activeTable.getAttributes(i).getName().equals(atts.get(j))) {
				System.out.println("The old value is " 
						+ activeTable.getAttributes(i).getArray().get(num)
						+ ".\n Type in the new value.");
				ch = input.nextLine();
				if (cancel(ch)) {
					return new ArrayList<String>();
				}
				values.add(ch);
				if (++j == atts.size()) {
					break;
				}
			}
		}
		return values;
	}

	/**
	 * Menu for delete function. Offer options to delete an entry (1),
	 * an attribute(2) or an element (3)
	 */
	public static void deleteMenu() {
		printDeleteChoices();
		int choice = checkChoice(1, 3);
		boolean sure = areYouSure();
		if (!sure) {
			tableMenu();
		} 
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			deleteEntryMenu(activeTable.getName());
		}
		if (choice == 2) {
			deleteAttributeMenu(activeTable.getName());
		}
		if (choice == 3) {
			deleteElementMenu(activeTable.getName());
		}
	}

	/**
	 * Print choices for delete function (1: entry, 2: attribute, 3: element)
	 */
	public static void printDeleteChoices() {
		System.out.println("What do you want to delete;");
		System.out.println("	1.An entry");
		System.out.println("	2.A whole attribute");
		System.out.println("	3.Just an element");
		System.out.println("Please enter the number of your choice");
	}

	/**
	 * Menu for deleting an entry
	 * @param tableName	The name of the table that contains the entry to be
	 * 					deleted
	 */
	public static void deleteEntryMenu(String tableName) {
		System.out.println("Please enter the number of the entry "
				+ "that you want to delete");
		int pos = readLines(tableName);
		if (pos == -1) {
			return;
		}
		if (areYouSure()) {
			activeDatabase.getTables(activeDatabase.position(tableName))
				.deleteEntry(pos);
		}
	}

	/**
	 * Menu for deleting an attribute
	 * @param tableName	The name of the table that contains the attribute to be
	 * 					deleted
	 */
	public static void deleteAttributeMenu(String tableName) {
		System.out.println("Please enter the name of the attribute"
				+ " that you want to delete");
		String attName = readAttributeRestrictedPermission(tableName);
		if (attName == null) {
			return;
		}
		if (areYouSure()) {
			activeTable.deleteAttribute(attName);
		}
	}

	/**
	 * Menu for deleting an element
	 * @param tableName	The name of the table that contains the element to be
	 * 					deleted
	 */
	public static void deleteElementMenu(String tableName) {
		System.out.println(
				"Please enter the name of the attribute "
				+ "that contains the element that you want to delete");
		String attName = readAttributeRestrictedPermission(tableName);
		if (attName == null) {
			return;
		}
		System.out.println("Please enter the number of the element"
				+ " line that you want to delete");
		int line = readLines(tableName);
		if (line == -1) {
			return;
		}
		if (areYouSure()) {
			activeTable.deleteElement(line, attName);
		}
	}

	/**
	 * Check the correct input of an integer variable.
	 * 
	 * @param lowLimit	Lower limit of the check performed
	 * @param highLimit	Upper limit of the check performed
	 * @return			The int which is checked and valid
	 */
	public static int checkChoice(int lowLimit, int highLimit) {
		Scanner input = new Scanner(System.in);
		String choice = input.next();
		int ch = -1;
		if (cancel(choice)) {
			return ch;
		}
		try {
			ch = Integer.parseInt(choice);
		} catch (NumberFormatException err) {
			System.out.println("This is not a number. Please try again.");
			return checkChoice(lowLimit, highLimit);
		}
		if ((ch < lowLimit) || (ch > highLimit)) {
			System.out.println("This is not a valid input, please try again.");
			return checkChoice(lowLimit, highLimit);
		}
		return ch;
	}
	
	/**
	 * Read the path of a file to import and check if it corresponds to a file.
	 */
	public static void importTableMenu() {
		Scanner input = new Scanner(System.in);
		String fileName;
		boolean correctFile;
		BufferedReader br = null;
		do {
			System.out.print("Please insert the path of the file"
					+ " you want to import: ");
			fileName = input.nextLine();
			if (cancel(fileName)) {
				return;
			}
			correctFile = checkCsvFormat(fileName);
			try {
				br = new BufferedReader(new FileReader(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("Path does not correspond to a file!");
				correctFile = false;
			}
		} while (!correctFile);
		activeDatabase.importUserMadeTable(br);
	}
	
	/**
	 * Check csv format.
	 * 
	 * @param fileName	The name of the file
	 * @return			Returns <code>true</code> if the file has the correct
	 * 					format or <code>false</code> if it doesn't
	 */
	public static boolean checkCsvFormat(String fileName) {
		if (fileName.endsWith(".csv")) {
			return true;
		} else {
			return false;
		}
	}
}
