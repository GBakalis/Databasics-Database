package gr.aueb.databasics;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.text.ParseException;
import java.util.InputMismatchException;

public class CommandLineMenu {

	private static Table activeTable;
	private static Database activeDatabase;

	public static Table getActiveTable() {
		return activeTable;
	}

	public static void setActiveTable(Table activeTable) {
		CommandLineMenu.activeTable = activeTable;
	}

	public static Database getActiveDatabase() {
		return activeDatabase;
	}

	public static void setActiveDatabase(Database activeDatabase) {
		CommandLineMenu.activeDatabase = activeDatabase;
	}

	/*
	 * Present the user with the options to 
	 * either create or load a database. Check
	 * if the choice input is valid.
	 */
	public static void databaseChoiceMenu() {
		int choice = 0;
		for (;;) {
			System.out.println("Choose one of the following:"
					+ "\n1.Create new database.\n2.Select Database"
					+ "\n3.Delete Database\n4.Exit");
			choice = checkChoice(1, 4);
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
				readDatabase(s).delete();
			}
			if (choice == 4) {	
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
			if (!database.getIsSaved()) {
				System.out.println("Unsaved data in database "
			+ database.getName() + "!");
			unsavedData = true;
			}
		}
		return unsavedData;
	}

	/**
	 * Check if an answer is acceptable
	 * @return
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

	public static boolean cancel(String str) {
		if (str.equals("cancel")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean cancelProcedure(String str) {
		if (str.equals("cancel") || str.equals("yes")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean areYouSure() {
		System.out.println("Are you sure?");
		Scanner input = new Scanner(System.in);
		String answer = input.next().toLowerCase();
		if ((!answer.equals("yes")) && (!answer.equals("no"))) {
			System.out.println("Not a valid answer. Please try again.");
			return areYouSure();
		} else if (answer.equals("yes")) {
			return true;
		} else {
			return false;
		}
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
		viewDatabase();
		databaseMenu();
	}

	/**
	 * View all the tables in the database
	 */
	public static void viewDatabase() {
		for (int i = 0; i < activeDatabase.getTableNumber(); i++) {
			activeDatabase.getTables(i).view();
			System.out.println();
		}
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
	 * @return
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
	 * @return
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
	 *  Ask the user if they want to create more attributes. Create attributes 
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
			} while ((answer.equalsIgnoreCase("yes")) 
				|| (!answer.equalsIgnoreCase("no")));
		}
	}

	/**
	 * Add new attribute.
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
					+ "6. Other (e.g. Image)\n\nInsert "
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
			System.out.println("Do you want to add anoter entry?");
			answer = checkAnswer();
		} while ((answer.equalsIgnoreCase("yes")) 
				|| (!answer.equalsIgnoreCase("no")));
	}

	/**
	 *  Add new entry. Read entry line, split it and check it.
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
	 * Add entry (for added attribute because the previous method refuses to work in
	 * that case).
	 */
	public static void addEntry(String attName) {
		Scanner input = new Scanner(System.in);
		String str;
		int index = activeTable.attPositions
				(new ArrayList<String>(Arrays.asList(attName))).get(0);
		for (int i = 0; i < activeTable.getLines(); i++) {
			System.out.printf("New input:");
			str = input.nextLine();
			if (cancel(str)) {
				return;
			}
			activeTable.getAttributes(index).setEntryField(str);
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
	 *  If not try again.
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
	 * @return
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
	 * Return Table type by recognizing an existing table name.
	 * @param tableName
	 * @return
	 */
	public static Table readTable(String tableName) {
		Table temp = activeDatabase.getTables(
				activeDatabase.position(tableName));
		setActiveTable(temp);
		return temp;
	}
	
	/**
	 * Return the database that has the name of the parameter.
	 * @param databaseName
	 * @return
	 */
	public static Database readDatabase(String databaseName) {
		Database temp = DatabaseUniverse.getDatabases(
				DatabaseUniverse.position(databaseName));
		setActiveDatabase(temp);
		return temp;
	}

	/**
	 * Present options to edit database 
	 * (add table, view table, delete table, select table).
	 */
	public static void databaseMenu() {
		int choice = 0;
		do {
			System.out.println("Choose one of the following:"
					+ "\n1.Add a new table\n2.View a table\n3.Delete a table"
					+ "\n4.Select a table to work on"
					+ "\n5.Save database\n6.Exit");
			choice = checkChoice(1, 6);
			if (choice == -1) {
				return;
			}
			if (choice == 1) {
				addTable();
				activeDatabase.setIsSaved(false);
				viewDatabase();
			} else if (choice == 2) {
				viewTableMenu();
			} else if (choice == 3) {
				deleteTableMenu();
				activeDatabase.setIsSaved(false);
				viewDatabase();
			} else if (choice == 4) {
				System.out.println(
						"Type in the name of the table of your choice.");
				String str = readTable();
				if (cancel(str)) {
					return;
				}
				setActiveTable(readTable(str));
				tableMenu();
				viewDatabase();
			} else if (choice == 5) {
				activeDatabase.saveDatabase();
				activeDatabase.setIsSaved(true);
			} else if (choice == 6) {
				setActiveDatabase(null);
				return;
			} else {
				System.out.println("This is not a valid intput.");
				databaseMenu();
			}
		} while (choice != 6);
	}

	/**
	 * View a specific table.
	 */
	public static void viewTableMenu() {
		System.out.println(
				"Please enter the name of the table that you want to view");
		String s = readTable();
		if (cancel(s)) {
			return;
		}
		if (s.equals("all")) {
			viewDatabase();
		} else {
			readTable(s).view();
		}
	}

	/**
	 * Delete a table.
	 */
	public static void deleteTableMenu() {
		System.out.println("Please enter the name"
				+ " of the table that you want to delete");
		String s = readTable();
		if (cancel(s)) {
			return;
		}
		readTable(s).delete();
	}

	/**
	 * Present options to edit a specific table (search,
	 *  sort, view, add attribute and entry, change data, delete data).
	 */
	public static void tableMenu() {
		System.out.println("Choose one of the following:"
				+ "\n1.Search in this table\n2.Sort this table\n"
				+ "3.Present data\n4.Add an attribute\n" 
				+ "5.New entry\n6.Change data\n7.Delete data\n8.Exit" );
		int choice = checkChoice(1, 8);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			searchMenu();
		} else if (choice == 2) {
			sortMenu(activeTable.getName());
			activeDatabase.setIsSaved(false);
		} else if (choice == 3) {
			viewOptions();
		} else if (choice == 4) {
			addAttributeOptions();
			activeDatabase.setIsSaved(false);
		} else if (choice == 5) {
			addEntryOptions();
			activeDatabase.setIsSaved(false);
		} else if (choice == 6) {
			changeDataOptions();
			activeDatabase.setIsSaved(false);
		} else if (choice == 7) {
			deleteMenu();
			activeDatabase.setIsSaved(false);
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
	 * Then the method {@link jar.Table.search(
	 * ArrayList<String> attributeNames, ArrayList<String> elements)} is 
	 * called, returning the positions of the matching lines, which will 
	 * feed the {@link jar.Table.viewLines(ArrayList<Integer> entryPositions)}
	 * method, presenting the set of lines that match the search criteria.
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
	 * View attributes.
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
	 * View lines.
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
	 * Present to the user options of what they can view, a column or a line.
	 */
	public static void viewOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:" 
				+ "\n1.View column"
				+ "\n2.View lines"
				+ "\n3.Exit");
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
				+ "\n3.Exit");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			String attributeName = attributeCreation();
			if (attributeName != null) {
				addAttributeMenu(attributeName);
				addEntry(attributeName);
			}
		} else if (choice == 2) {
			menuCopyAttribute(activeTable.getName());
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid input.");
			addAttributeOptions();
		}
	}

	/**
	 * Menu needed in order to copy an attribute.
	 * @param namePaste
	 */
	public static void menuCopyAttribute(String namePaste) {
		Scanner input = new Scanner(System.in);
		String answer = null;
		do {
			System.out.println("Please enter the name of the table"
					+ " that contains the attribute");
			String nameCopy = readTable();
			if (nameCopy == null) {
				return;
			}
			activeTable.view();
			activeTable.view();
			System.out.println("Please enter the name of the attribute"
					+ " that you want to copy");
			String attNameC = readAttribute(nameCopy);
			if (attNameC == null) {
				return;
			}
			System.out.println("Please enter the name of the attribute"
					+ " where you want to paste");
			String attNameP = input.nextLine();
			if (attNameP == null) {
				return;
			}
			activeDatabase.copyAttribute(
					nameCopy, attNameC, namePaste, attNameP);
			System.out.println("Do you want to copy another attribute?");
			answer = checkAnswer();
		} while (answer.equalsIgnoreCase("yes"));
	}

	/**
	 * Offer the user the option to add an entry manually,
	 * or by coping an existing one
	 */
	public static void addEntryOptions() {
		int choice = 0;
		System.out.println("Choose one of the following:"
				+ "\n1.Create a new entry"
				+ "\n2.Copy an existing entry"
				+ "\n3.Exit");
		choice = checkChoice(1, 3);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			entryCreationMenu();
		} else if (choice == 2) {
			copyEntryMenu(1);
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
	 * @param choice
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
			activeTable.view();
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
	 * opy and add an entry
	 * @param nameCopy
	 * @param entryNumCopy
	 */
	public static void copyAddEntry(String nameCopy, int entryNumCopy) {
		activeDatabase.copyNewEntry(
				nameCopy, entryNumCopy, activeTable.getName());
	}

	/**
	 * Copy and replace a table.
	 * @param nameCopy
	 * @param entryNumCopy
	 */
	public static void copyReplaceEntry(String nameCopy, int entryNumCopy) {
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
	 * Read an existing line number. Check its existence and if it is a number.
	 * @param tableName
	 * @return
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
				+ "\n3.Replace entry.\n4.Exit");
		choice = checkChoice(1, 4);
		if (choice == -1) {
			return;
		}
		if (choice == 1) {
			menuDataChange();
		} else if (choice == 2) {
			String answer = null;
			do {
				copyElementMenu(activeTable.getName());
				System.out.println("Do you want to replace another element?");
				answer = checkAnswer();
			} while (answer.equalsIgnoreCase("yes"));
		} else if (choice == 3) {
			copyEntryMenu(2);
		} else if (choice == 4) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			changeDataOptions();
		}
	}

	/**
	 * copy and replace an element.
	 */
	public static void copyElementMenu(String tableName) {
		String nameCopy;
		System.out.println("Please enter the name of the table that "
				+ "contains the element that you want to copy");
		nameCopy = readTable();
		if (nameCopy == null) {
			return;
		}
		activeTable.view();
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
	 * Read an existing attribute and check its existence.
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
	 * Check if the name the user types corresponds to an existing attribute.
	 * If no ask again. Return the name of the attribute.
	 * @param tableName
	 * @return
	 */
	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		if (cancel(attName)) {
			return null;
		}
		while (activeTable.exists(attName) == false) {
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
	 * @param tableName
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
	 *  Read an array list of attributes that are used in change data function.
	 * @return
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
	 * @param num
	 * @param atts
	 * @return
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
	 *  an attribute(2) or an element (3)
	 */
	public static void deleteMenu() {
		printDeleteChoices();
		int choice = checkChoice(1, 3);
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
	 * Delete entry.
	 * @param tableName
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
	 * Delete attribute.
	 * @param tableName
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
	 * Delete element.
	 * @param tableName
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
	 * @param lowLimit
	 * @param highLimit
	 * @return
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
		activeDatabase.importTable(br);
	}
	
	/**
	 * Check cvs format.
	 * @param fileName
	 * @return
	 */
	public static boolean checkCsvFormat(String fileName) {
		if (fileName.endsWith(".csv")) {
			return true;
		} else {
			return false;
		}
	}
}