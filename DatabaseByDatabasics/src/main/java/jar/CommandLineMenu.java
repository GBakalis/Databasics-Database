
package jar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.ParseException;

public class CommandLineMenu {

	/*
	 * Present the user with the options to either create or load a database. Check
	 * if the choice input is valid.
	 */
	public static void databaseChoiceMenu() {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:" + "\n1.Create new database.\n2.Load existing database");
		choice = checkChoice(1, 2);
		if (choice == 1) {
			databaseCreationMenu();
		}
		if (choice == 2) {
			// not yet created
		}
		return;
	}

	/*
	 * Create the first table. Give the user the option to add more tables to the
	 * database in its creation process. Give access to database menu.
	 */
	public static void databaseCreationMenu() {
		String flag = null;
		createTable();
		Scanner input = new Scanner(System.in);
		do {

			System.out.println("Do you want to add a table?");
			flag = input.next().toLowerCase();
			if (flag.equals("yes")) {
				addTable();
			}
		} while ((flag.equals("yes")) || (!flag.equals("no")));
		// System.out.println("Do you want to save this database?");
		viewDatabase();
		System.out.println("\nDo you want to edit your database?");
		String choice = input.next().toLowerCase();
		if (choice.equalsIgnoreCase("yes"))
			databaseMenu();
		else
			return;
	}

	/*
	 * View all the tables in the database
	 */
	public static void viewDatabase() {
		for (int i = 0; i < Table.getT().size(); i++) {
			Table.viewTable(Table.getTables(i).getName());
			System.out.println();
		}
	}

	/*
	 * Add table in database. Offer options to create a brand new table or copy an
	 * existing one. Check if the input choice is valid.
	 */
	public static void addTable() {
		int choice;
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Choose one of the below:\n1.Create a new table" + "\n2.Use an existing table from the database.");
		choice = checkChoice(1, 2);

		if (choice == 1) {
			createTable();
			return;
		}
		if (choice == 2) {
			copyTableMenu();
			return;
		}
		System.out.println("This is not a valid input. Please try again");
		addTable();
	}

	/*
	 * Create a table and fill it (attributes and entries)
	 */
	public static void createTable() {
		Table table = tableCreation();
		attributeCreationMenu(table);
		entryCreationMenu(table);
	}

	/*
	 * Check if the name the user inputs is already given to another table and if
	 * not create the table.
	 */
	public static Table tableCreation() {
		String tableName = null;
		do {
			Scanner input = new Scanner(System.in);
			System.out.print("Please type in the name of the new table.");

			tableName = input.nextLine();
			if (Table.exists(tableName) == true)
				System.out.println("This table already exists.");
		} while (Table.exists(tableName) == true);
		Table table = new Table(tableName);
		return table;
	}

	/*
	 * Check if the attribute name the user inputs is available and if yes create
	 * the attribute.
	 */
	public static String attributeCreation(Table table) {
		Scanner input = new Scanner(System.in);
		String attributeName = null;
		do {
			System.out.println("Type in the name of the new attribute");
			attributeName = input.nextLine().trim();
			if (Table.exists(table.getName(), attributeName))
				System.out.println("This attribute already exists.");
		} while (Table.exists(table.getName(), attributeName) == true);
		return attributeName;
	}

	/*
	 * Ask user if they want to create more attributes. Create attributes until user
	 * answers no.
	 */
	public static void attributeCreationMenu(Table table) {
		String answer = null;

		do {
			String attributeName = attributeCreation(table);
			Scanner input = new Scanner(System.in);
			addAttributeMenu(table, attributeName);
			System.out.println("Do you want to create another attribute?");
			answer = input.next().toLowerCase();
		} while ((answer.equalsIgnoreCase("yes")) || (!answer.equalsIgnoreCase("no")));
	}

	/*
	 * Add new attribute.
	 */
	public static void addAttributeMenu(Table table, String name) throws InputMismatchException {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		int choice = 0;
		do {
			correctEntry = true;
			System.out.println("Your attribute can be of any of the following types:\n" + "1. Text\n2. Single letter\n"
					+ "3. Integer\n" + "4. Decimal\n" + "5. Date\n" + "6. Other (e.g. Image)\n\n"
					+ "Insert the number that corresponds to the type you want.");
			choice = checkChoice(1, 8);
			correctEntry = Table.checkInput(choice, correctEntry);
		} while (correctEntry == false);
		table.newAttribute(name, choice);
	}

	/*
	 * Call method that creates new entry until user types no.
	 */
	public static void entryCreationMenu(Table table) {
		String answer = null;
		Scanner input = new Scanner(System.in);
		do {
			addEntryMenu(table);
			System.out.println("Do you want to add anoter entry?");
			answer = input.next().toLowerCase();
		} while ((answer.equalsIgnoreCase("yes")) || (!answer.equalsIgnoreCase("no")));
	}

	/*
	 * Add new entry. Read entry line, split it and check it.
	 */
	public static void addEntryMenu(Table table) {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		String[] entries;
		do {
			System.out.println("Type an entry line");
			String entry = input.nextLine();
			correctEntry = true;
			entries = entry.split(",");
			for (int i = 0; i < entries.length; i++) {
				entries[i] = entries[i].trim();
			}
			correctEntry = table.checkEntry(entries);
			if (correctEntry == false) {
				System.out.println("Please try again!");
			}
		} while (correctEntry == false);
		table.newEntry(entries);
	}

	/*
	 * Add entry (for added attribute because the previous method refuses to work in
	 * that case).
	 */
	public static void addEntry(Table table, String attName) {
		Scanner input = new Scanner(System.in);
		int index = table.attPositions(new ArrayList<String>(Arrays.asList(attName))).get(0);
		for (int i = 0; i < table.getLines(); i++) {
			System.out.printf("New input:");
			table.getAttributes().get(index).setEntryField(input.nextLine());
		}
	}

	/*
	 * Menu to copy a table. Reads an existing table name and copies the respective
	 * table to a new one.
	 */
	public static void copyTableMenu() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that you want to copy");
		nameCopy = readTable();
		namePaste = tableCreation().getName();
		Table.getT().get(0).copyTable(nameCopy, namePaste);
	}

	/*
	 * Read a table name and check if it corresponds to a table. If not try again.
	 */
	public static String readTable() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		while (Table.exists(tableName) == false) {
			System.out.println("This table does not exist. Please type an existing name.");
			tableName = input.nextLine();
		}
		return tableName;
	}

	/*
	 * Return Table type by recognizing an existing table name.
	 */
	public static Table readTable(String tableName) {
		return Table.getTables(Table.position(tableName));
	}

	/*
	 * Present options to edit database (add table, view table, delete table, choose
	 * table).
	 */
	public static void databaseMenu() {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		do {
			System.out.println("Choose one of the below:\n1.Add a new table\n2.View a table\n"
					+ "3.Delete a table\n4.Choose a table (obtain access to more options)" + "\n5.exit");
			choice = checkChoice(1, 5);
			if (choice == 1) {
				addTable();
			} else if (choice == 2) {
				viewTableMenu();
			} else if (choice == 3) {
				deleteTableMenu();
			} else if (choice == 4) {
				System.out.println("Type in the name of the table of your choice.");
				tableMenu(readTable(readTable()));
			} else if (choice == 5) {
				return;
			} else {
				System.out.println("This is not a valid intput.");
				databaseMenu();
			}
			viewDatabase();
		} while (choice != 5);
	}

	/*
	 * View a specific table.
	 */
	public static void viewTableMenu() {
		System.out.println("Please enter the name of the table that you want to view");
		String name = readTable();
		Table.viewTable(name);
	}

	/*
	 * Delete a table.
	 */
	public static void deleteTableMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that you want to delete");
		tableName = readTable();
		readTable(tableName).deleteTable(tableName);
	}

	/*
	 * Present options to edit a specific table (search, sort, view, add attribute
	 * and entry, change data, delete data).
	 */
	public static void tableMenu(Table table) {
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:\n1.Search in this table\n2.Sort this table\n"
				+ "3.Present data\n4.Add an attribute\n" + "5.New entry\n6.Change data\n7.Delete data" + "\n8.Exit");
		int choice = checkChoice(1, 8);
		if (choice == 1) {
			searchMenu(table);
		} else if (choice == 2) {
			sortMenu(table.getName());
		} else if (choice == 3) {
			menuViewAttribute(table.getName());
		} else if (choice == 4) {
			addAttributeOptions(table);
		} else if (choice == 5) {
			addEntryOptions(table);
		} else if (choice == 6) {
			changeDataOptions(table);
		} else if (choice == 7) {
			deleteMenu(table);
		} else if (choice == 8) {
			return;
		} else {
			System.out.println("This is not a valid choice");
		}
		tableMenu(table);
	}

	/*
	 * Menu for search function. Read attribute to search by.
	 */
	public static void searchMenu(Table table) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> attNames = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		for (int i = 0; i < table.getAttributeNumber(); i++) {
			System.out.print("Please enter an attribute name according to " + "which you want to search: ");
			String attName = readAttribute(table.getName());
			attNames.add(attName);
			System.out.print("Please enter an element you want to " + "search in the column previously inserted: ");
			String element = input.nextLine().trim();
			elements.add(element);
			System.out.println("Would you like to add another attribute for search?" + " (Yes / Any other key");
			String addAttribute = input.nextLine().trim();
			if (addAttribute.equalsIgnoreCase("yes")) {
				continue;
			} else {
				break;
			}
		}
		ArrayList<Integer> entryPositions = table.search(attNames, elements);
		table.viewLines(entryPositions);
	}

	/*
	 * View attributes.
	 */
	public static void menuViewAttribute(String tableName) {
		ArrayList<String> atts = new ArrayList<String>();
		boolean flag = true;
		while (flag) {
			System.out.println("Please enter an attribute of this table you want to view");
			atts.add(readAttribute(tableName));
			Scanner input = new Scanner(System.in);
			System.out.println("Do you want to view another attribute of this table?");
			System.out.println("Type 'yes' to add another one or " + "'no' to view the ones you have already entered");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			}
		}
		Table.viewAttribute(tableName, atts);
	}

	/*
	 * View lines.
	 */
	public static void menuViewLines(String tableName) {
		ArrayList<Integer> lines = new ArrayList<>();
		boolean flag = true;
		while (flag) {
			System.out.println("Please enter the number of the line you want to view");
			int line = readLines(tableName);
			lines.add(line);
			Scanner input = new Scanner(System.in);
			System.out.println("Do you want to view another line of this table?");
			System.out.println("Type 'yes' to add another one or " + "'no' to view the ones you have already entered");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			}
		}
		readTable(tableName).viewLines(lines);
	}

	/*
	 * Present to the user options of what they can view, a column or a line.
	 */
	public static void viewOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:" + "\n1.View column" + "\n2.View lines" + "\n3.Exit");
		choice = checkChoice(1, 3);
		if (choice == 1) {
			menuViewAttribute(table.getName());
		} else if (choice == 2) {
			menuViewLines(table.getName());
		} else if (choice == 3) {
			return;
		}
	}

	/*
	 * Present the user with the options they have in order to add an attribute to a
	 * table (create a new one or copy an existing one).
	 */
	public static void addAttributeOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:" + "\n1.Create a new attribute" + "\n2.Copy an existing attribute"
				+ "\n3.Exit");
		choice = checkChoice(1, 3);
		if (choice == 1) {
			String attributeName = attributeCreation(table);
			addAttributeMenu(table, attributeName);
			addEntry(table, attributeName);
		} else if (choice == 2) {
			menuCopyAttribute(table.getName());
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid input.");
			addAttributeOptions(table);
		}
	}

	/*
	 * Menu in order to copy an attribute.
	 */
	public static void menuCopyAttribute(String namePaste) {
		Scanner input = new Scanner(System.in);
		String answer = null;
		do {
			System.out.println("Please enter the name of the table that contains the attribute");
			String nameCopy = readTable();
			Table.viewTable(nameCopy);
			Table.viewTable(namePaste);
			System.out.println("Please enter the name of the attribute that you want to copy");
			String attNameC = readAttribute(nameCopy);
			System.out.println("Please enter the name of the attribute where you want to paste");
			String attNameP = input.nextLine();
			Table.getT().get(0).copyAttribute(nameCopy, attNameC, namePaste, attNameP);
			System.out.println("Do you want to copy another attribute?");
			answer = input.next().toLowerCase();
		} while (answer.equalsIgnoreCase("yes"));
	}

	/*
	 * Offer the user the option to add an entry manually, or by coping an existing
	 * one
	 */
	public static void addEntryOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out
				.println("Choose one of the below:" + "\n1.Create a new entry\n2.Copy an existing entry" + "\n3.Exit");
		choice = checkChoice(1, 3);
		if (choice == 1) {
			entryCreationMenu(table);
		} else if (choice == 2) {
			copyEntryMenu(table, 1);
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			addEntryOptions(table);
		}
	}

	/*
	 * Menu for copying an entry. Offer options of adding the entry and of replacing
	 * an existing one.
	 */
	public static void copyEntryMenu(Table table, int choice) {
		String answer = null;
		do {
			String nameCopy;
			Scanner input = new Scanner(System.in);
			System.out.println("Please enter the name of the table that contains the entry");
			nameCopy = readTable();
			Table.viewTable(nameCopy);
			System.out.println("Please enter the number of the entry that you want to copy");
			int entryNumCopy = readLines(nameCopy);
			if (choice == 1)
				copyAddEntry(table, nameCopy, entryNumCopy);
			if (choice == 2)
				copyReplaceEntry(table, nameCopy, entryNumCopy);
			System.out.println("Do you want to copy another entry?");
			answer = input.next();
		} while (answer.toLowerCase().equals("yes"));
	}

	/*
	 * Copy and adding an entry
	 */
	public static void copyAddEntry(Table table, String nameCopy, int entryNumCopy) {
		Table.getT().get(0).copyNewEntry(nameCopy, entryNumCopy, table.getName());
	}

	/*
	 * Copy and replace a table.
	 */
	public static void copyReplaceEntry(Table table, String nameCopy, int entryNumCopy) {
		System.out.println("Please enter the number of the entry that you want to replace");
		int entryNumPaste = readLines(table.getName());
		Table.getT().get(0).copyExistingEntry(nameCopy, entryNumCopy, table.getName(), entryNumPaste);

	}

	/*
	 * Read an existing line number. Check its existence and if it is a number.
	 */
	public static int readLines(String tableName) {
		Scanner input = new Scanner(System.in);
		int lineNum = -1;
		try {
			lineNum = input.nextInt();
		} catch (InputMismatchException err) {
			System.out.println("This is not a number. Please try again.");
			readLines(tableName);
		}
		while (lineNum > Table.getTables(Table.position(tableName)).getLines()) {
			System.out.println("This line does not exist. Please type an existing line.");
			lineNum = input.nextInt();
		}
		return lineNum;
	}

	/*
	 * Offer options in order to change data.
	 */
	public static void changeDataOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:" + "\n1.Change an entry manualy"
				+ "\n2.Replace elements of a line with elements from another line." + "\n3.Replace entry.\n4.Exit");
		choice = checkChoice(1, 4);
		if (choice == 1) {
			menuDataChange(table);
		} else if (choice == 2) {
			String answer = null;
			do {
				copyElementMenu(table.getName());
				System.out.println("Do you want to replace another element?");
				answer = input.next().toLowerCase();
			} while (answer.equalsIgnoreCase("yes"));
		} else if (choice == 3) {
			copyEntryMenu(table, 2);
		} else if (choice == 4) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			changeDataOptions(table);
		}
	}

	/*
	 * copy and replace an element.
	 */
	public static void copyElementMenu(String tableName) {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that " + "contains the element that you want to copy");
		nameCopy = readTable();
		Table.viewTable(nameCopy);
		System.out
				.println("Please enter the name of the attribute that " + "contains the element that you want to copy");
		String attNameC = readAttribute(nameCopy);
		System.out.println("Please enter the number of the line that " + "contains the element that you want to copy");
		int lineC = readLines(nameCopy);
		Table.viewTable(tableName);
		System.out.println("Please enter the name of the " + "attribute where you want to paste the element");
		String attNameP = readAttribute(tableName);
		System.out.println("Please enter the number of the " + "line where you want to paste the element");
		int lineP = readLines(tableName);
		Table.getT().get(0).copyElement(nameCopy, attNameC, lineC, tableName, attNameP, lineP);
	}

	/*
	 * Read an existing attribute and check its existence.
	 */
	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		while (Table.exists(tableName, attName) == false) {
			System.out.println("This attribute does not exist." + " Please type an existing name.");
			attName = input.nextLine();
		}
		return attName;
	}

	/*
	 * Menu for sort function. Read the attribute to act as key.
	 */
	public static void sortMenu(String tableName) {
		System.out.println("Please type in the name of the " + "attribute you want to sort the table by.");
		String keyAttribute = readAttribute(tableName);
		System.out.println("You want to sort the table by ascending " + "or descending order?");
		int order = readOrder();
		try {
			readTable(tableName).sortTable(keyAttribute, order);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Read the order for sort function (ascending or descending).
	 */
	public static int readOrder() {
		Scanner input = new Scanner(System.in);
		String order = input.next().toLowerCase();
		if (order.equals("ascending"))
			return 1;
		if (order.equals("descending"))
			return -1;
		System.out.println(
				"This is not a valid choice. " + "Do you want to sort the table by ascending or descending order");
		return readOrder();
	}

	/*
	 * Menu for data change function. Asks for the line number that will change and
	 * continues to do so for all the lines the user chooses.
	 */
	public static void menuDataChange(Table table) {
		String done = "yes";
		int num;
		ArrayList<String> attributes;
		ArrayList<String> values;
		do {
			Scanner input = new Scanner(System.in);
			System.out.println("Type in the number of the line you want to change");
			num = readLines(table.getName());
			attributes = readAttributes(table, num);
			values = readValues(table, num, attributes);
			table.dataChange(num, attributes, values);
			Table.viewTable(table.getName());
			System.out.println("Do you want to change another line?");
			done = input.next();
		} while (done.equals("yes"));
	}

	/*
	 * Read an array list of attributes that are used in change data function.
	 */
	public static ArrayList<String> readAttributes(Table table, int num) {
		ArrayList<String> atts = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		boolean flag = false;
		do {
			for (int i = 1; i < table.getAttributeNumber() - 1; i++) {
				System.out.println("Do you want to change the attribute " + table.getAttributes().get(i).getName());
				String choice = input.nextLine();
				if (choice.equals("yes")) {
					atts.add(table.getAttributes().get(i).getName());
					flag = true;
				}
			}
			if (flag == false) {
				System.out.print("You have to change one attribute");
			}
		} while (flag == false);
		return atts;
	}

	/*
	 * Read all the values that will change in data change function.
	 */
	public static ArrayList<String> readValues(Table table, int num, ArrayList<String> atts) {
		ArrayList<String> values = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		String ch;
		int j = 0;
		for (int i = 1; i < table.getAttributeNumber() - 1; i++) {
			if (table.getAttributes().get(i).getName().equals(atts.get(j))) {
				System.out.println("The old value is " + table.getAttributes().get(i).getArray().get(num)
						+ ".\n Type in the new value.");
				ch = input.nextLine();
				values.add(ch);
				j += 1;
			}
		}
		return values;
	}

	/*
	 * Menu for delete function. Offer options to delete an entry (1) , an attribute
	 * (2) or element (3)
	 */
	public static void deleteMenu(Table table) {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		int choice = checkChoice(1, 4);
		if (choice == 1) {
			deleteEntryMenu(table.getName());
		}
		if (choice == 2) {
			deleteAttributeMenu(table.getName());
		}
		if (choice == 3) {
			deleteElementMenu(table.getName());
		}
		if (choice == 4) {
			return;
		}
	}

	/*
	 * Print messages for delete function
	 */
	public static boolean printDeleteMessages(int choice, boolean flag, Table table) {
		Scanner input = new Scanner(System.in);
		System.out.println("Are you sure you want to delete this?");
		System.out.println("Press 'yes' to continue or 'no' to go back");
		String d = input.nextLine();
		if (!(d.equalsIgnoreCase("yes"))) {
			delete(choice, table);
			System.out.println("Do you want to delete anything else?");
			System.out.println("Press 'yes' to continue or 'no' to exit Copy Mode");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = true;
			} else {
				flag = false;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/*
	 * Print choices for delete function (1: entry, 2: attribute, 3: element)
	 */
	public static void printDeleteChoices() {
		System.out.println("What do you want to delete;");
		System.out.println("	1.An entry");
		System.out.println("	2.A whole attribute");
		System.out.println("	3.Just an element");
		System.out.println("Please enter the number of your choice");
	}

	/*
	 * Delete entry.
	 */
	public static void deleteEntryMenu(String tableName) {
		int line;
		System.out.println("Please enter the number of the entry that you want to delete");
		line = readLines(tableName);
		Table.getTables(Table.position(tableName)).deleteEntry(tableName, line - 1);
	}

	/*
	 * Delete attribute.
	 */
	public static void deleteAttributeMenu(String tableName) {
		System.out.println("Please enter the name of the attribute that you want to delete");
		String attName = readAttribute(tableName);
		Table.getTables(Table.position(tableName)).deleteAttribute(tableName, attName);
	}

	/*
	 * Delete element
	 */
	public static void deleteElementMenu(String tableName) {
		System.out.println(
				"Please enter the name of " + "the attribute that contains the element that you want to delete");
		String attName = readAttribute(tableName);
		System.out.println("Please enter the " + "number of the element line that you want to delete");
		int line = readLines(tableName);
		Table.getTables(Table.position(tableName)).deleteElement(tableName, line - 1, attName);
	}

	/*
	 * Call the other appropriate delete menu methods.
	 */
	public static void delete(int choice, Table table) {
		switch (choice) {
		case 1:
			deleteEntryMenu(table.getName());
		case 2:
			deleteAttributeMenu(table.getName());
		case 3:
			deleteElementMenu(table.getName());
		}
	}

	/*
	 * Check the correct input of an integer variable.
	 */
	public static int checkChoice(int lowLimit, int highLimit) {
		Scanner input = new Scanner(System.in);
		int choice = -1;		
		try {
			choice = input.nextInt();
		} catch (InputMismatchException err) {
			System.out.println("This is not a number. Please try again.");
			return checkChoice(lowLimit, highLimit);
		}
		if ((choice < lowLimit) || (choice > highLimit)) {
			System.out.println("This is not a valid input please try again.");
			return checkChoice( lowLimit, highLimit);
		}				
		return choice;
	}
}
