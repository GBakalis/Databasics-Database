
package jar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.ParseException;

public class Menu {

	public static void databaseChoiceMenu() {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose one of the below:"
				+ "\n1.Create new database.\n2.Load existing database");
		choice = input.nextInt();
		while (!((choice == 1) || (choice == 2))) {
			System.out.println("This is not a valid choice.\n"
					+ "Choose one of the below:\n1.Create new database."
					+ "\n2.Load existing dataase");
			choice = input.nextInt();
		}
		if (choice == 1) {
			databaseCreationMenu();
		}
		if (choice == 2) {
			// not yet created
		}
		return;
	}

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

	public static void viewDatabase() {
		for (int i = 0; i < Table.getT().size(); i++)
			Table.viewTable(Table.getTables(i).getName());
		System.out.println();
	}

	public static void addTable() {

		int choice;
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Choose one of the below:\n1.Create a new table"
						+"\n2.Use an existing table from the database.");
		choice = input.nextInt();

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

	public static void createTable() {
		Table table = tableCreationMenu();
		attributeCreationMenu(table);
		entryCreationMenu(table);
	}

	public static Table tableCreationMenu() {
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

	public static void attributeCreationMenu(Table table) {
		String answer = null;

		do {
			String attributeName = attributeCreation(table);
			Scanner input = new Scanner(System.in);
			addAttributeMenu(table, attributeName);
			System.out.println("Do you want to create another attribute?");
			answer = input.next().toLowerCase();
		} while ((answer.equalsIgnoreCase("yes")) ||
				(!answer.equalsIgnoreCase("no")));
	}

	public static void addAttributeMenu(Table table, String name) throws InputMismatchException {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		int choice = 0;
		do {
			correctEntry = true;
			System.out.println("Your attribute can be of any of the following types:\n"
					+ "1. Text\n2. Single letter\n" + "3. Integer\n" + "4. Decimal\n" + "5. Date\n" 
					+ "6. Other (e.g. Image)\n\n"
					+ "Insert the number that corresponds to the type you want.");
			try {
				choice = input.nextInt();
			} catch (InputMismatchException err) {
				System.out.println("This was not a number!");
				correctEntry = false;
				input.next();
				continue;
			}
			correctEntry = Table.checkInput(choice, correctEntry);
		} while (correctEntry == false);
		table.newAttribute(name, choice);
	}

	public static void entryCreationMenu(Table table) {
		String answer = null;
		Scanner input = new Scanner(System.in);
		do {
			addEntryMenu(table);
			System.out.println("Do you want to add anoter entry?");
			answer = input.next().toLowerCase();
		} while ((answer.equalsIgnoreCase("yes")) || (!answer.equalsIgnoreCase("no")));
	}

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

	public static void addEntry(Table table, String attName) {
		Scanner input = new Scanner(System.in);
		int index = table.attPositions(new ArrayList<String>(Arrays.asList(attName))).get(0);
		for (int i = 0; i < table.getLines(); i++) {
			System.out.printf("New input:");
			table.getAttributes().get(index).setEntryField(input.nextLine());
		}
	}

	public static void copyTableMenu() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that you want to copy");
		nameCopy = readTable();
		namePaste = tableCreationMenu().getName();
		Table.getT().get(0).copyTable(nameCopy, namePaste);
	}

	public static String readTable() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		while (Table.exists(tableName) == false) {
			System.out.println("This table does not exist. Please type an existing name.");
			tableName = input.nextLine();
		}
		return tableName;
	}

	public static Table readTable(String tableName) {
		return Table.getTables(Table.position(tableName));
	}

	public static void databaseMenu() {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		do {
			System.out.println("Choose ont of the below:\n1.Add a new table\n2.View a table\n"
					+ "3.Delete a table\n4.Choose a table (obtain access to more options)"
					+ "\n5.exit");
			choice = input.nextInt();
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

	public static void viewTableMenu() {

		System.out.println("Please enter the name of the table that you want to view");
		String name = readTable();
		Table.viewTable(name);
	}

	public static void deleteTableMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that you want to delete");
		tableName = readTable();
		readTable(tableName).deleteTable(tableName);
	}

	public static void tableMenu(Table table) {
		Scanner input = new Scanner(System.in);
		System.out.println("Choose ont of the below:\n1.Search in this table\n2.Sort this table\n"
				+ "3.View a column\n4.Add an attribute\n" 
				+ "5.New entry\n6.Change data\n7.Delete data" 
				+ "\n8.Exit");
		int choice = input.nextInt();
		if ((choice < 1) && (choice > 8)) {
			System.out.println("This is not a valid choice.");
			tableMenu(table);
		}
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

	public static void searchMenu(Table table) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> attNames = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		for (int i = 0; i < table.getAttributeNumber(); i++) {
			System.out.print("Please enter an attribute name according to "
					+ "which you want to search: ");
			String attName = readAttribute(table.getName());
			attNames.add(attName);
			System.out.print("Please enter an element you want to "
					+ "search in the column previously inserted: ");
			String element = input.nextLine().trim();
			elements.add(element);
			System.out.println("Would you like to add another attribute for search?" 
			+ " (Yes / Any other key");
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

	public static void menuViewAttribute(String tableName) {

		ArrayList<String> atts = new ArrayList<String>();
		boolean flag = true;
		while (flag) {
			System.out.println("Please enter an attribute of this table you want to view");
			atts.add(readAttribute(tableName));
			Scanner input = new Scanner(System.in);
			System.out.println("Do you want to view another attribute of this table?");
			System.out.println("Type 'yes' to add another one or "
					+ "'no' to view the ones you have already entered");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			}
		}
		Table.viewAttribute(tableName, atts);
	}

	public static void addAttributeOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println(
				"Choose ont of the below:" + "\n1.Create a new attribute"
						+ "\n2.Copy an existing attribute" 
						+ "\n3.Exit");
		choice = input.nextInt();
		if (choice == 1) {
			String attributeName = attributeCreation(table);
			addAttributeMenu(table, attributeName);
			addEntry(table, attributeName);
		} else if (choice == 2) {
			menuCopyAttribute();
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid input.");
			addAttributeOptions(table);
		}
	}

	public static void menuCopyAttribute() {
		Scanner input = new Scanner(System.in);
		String answer = null;
		do {
			System.out.println("Please enter the name of the table that contains theattribute");
			String nameCopy = readTable();
			Table.viewTable(nameCopy);
			System.out.println("Please enter the name of the table that you want to"
					+ " paste the attribute");
			String namePaste = readTable();
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

	public static void addEntryOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose ont of the below:" 
				+ "\n1.Create a new entry\n2.Copy an existing entry" 
				+ "\n3.Exit");
		choice = input.nextInt();
		if (choice == 1) {
			entryCreationMenu(table);
		} else if (choice == 2) {
			copyEntryMenu();
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			addEntryOptions(table);
		}
	}

	public static void copyEntryMenu() {
		String answer = null;
		Scanner input = new Scanner(System.in);
		do {
			String nameCopy;
			String namePaste;
			System.out.println("Please enter the name of the table that contains the entry");
			nameCopy = readTable();
			Table.viewTable(nameCopy);
			System.out.println("Please enter the number of the entry that you want to copy");
			int entryNumCopy = readLine();
			System.out.println("Please enter the name of the table that you want to paste the entry");
			namePaste = readTable();
			Table.viewTable(namePaste);
			System.out.println("Please enter the number of the entry that you want to paste");
			int entryNumPaste = readLine();
			Table.getT().get(0).copyEntry(nameCopy, entryNumCopy, namePaste, entryNumPaste);
			System.out.println("Do you want to copy another entry?");
			answer = input.next().toLowerCase();
		} while (answer.equalsIgnoreCase("yes"));
	}

	public static int readLine() {
		Scanner input = new Scanner(System.in);
		return input.nextInt();
	}

	public static int readLines(String tableName) {
		Scanner input = new Scanner(System.in);
		int lineNum = input.nextInt();
		while (lineNum > Table.getTables(Table.position(tableName)).getLines()) {
			System.out.println("This line does not exist. Please type an existing line.");
			lineNum = input.nextInt();
		}
		return lineNum;
	}

	public static void changeDataOptions(Table table) {
		int choice = 0;
		Scanner input = new Scanner(System.in);
		System.out.println("Choose ont of the below:"
				+ "\n1.Change an entry manualy"
				+ "\n2.Replace elements of a line with elements from another line.\n3.Exit" 
				+ "\n3.Exit");
		choice = input.nextInt();
		if (choice == 1) {
			// not yet ready
		} else if (choice == 2) {
			String answer = null;
			do {
				copyElementMenu(table.getName());
				System.out.println("Do you want to replace another element?");
				answer = input.next().toLowerCase();
			} while (answer.equalsIgnoreCase("yes"));
		} else if (choice == 3) {
			return;
		} else {
			System.out.println("This is not a valid choice.");
			changeDataOptions(table);
		}
	}

	public static void copyElementMenu(String tableName) {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that "
				+ "contains the element that you want to copy");
		nameCopy = readTable();
		Table.viewTable(nameCopy);
		System.out.println("Please enter the name of the attribute that "
				+ "contains the element that you want to copy");
		String attNameC = readAttribute(nameCopy);
		System.out.println("Please enter the number of the line that "
				+ "contains the element that you want to copy");
		int lineC = readLine();
		System.out.println("Please enter the name of the "
				+ "table where you want to paste the element");
		namePaste = readTable();
		Table.viewTable(namePaste);
		System.out.println("Please enter the name of the "
				+ "attribute where you want to paste the element");
		String attNameP = readAttribute(namePaste);
		System.out.println("Please enter the number of the "
				+ "line where you want to paste the element");
		int lineP = readLine();
		Table.getT().get(0).copyElement(nameCopy, attNameC, lineC, namePaste, attNameP, lineP);
	}

	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		while (Table.exists(tableName, attName) == false) {
			System.out.println("This attribute does not exist."
					+ " Please type an existing name.");
			attName = input.nextLine();
		}
		return attName;
	}

	public static void sortMenu(String tableName) {
		System.out.println("Please type in the name of the "
				+ "attribute you want to sort the table by.");
		String keyAttribute = readAttribute(tableName);
		System.out.println("You want to sort the table by ascending "
				+ "or descending order?");
		int order = readOrder();
		try {
			readTable(tableName).sortTable(keyAttribute, order);
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static int readOrder() {
		Scanner input = new Scanner(System.in);
		String order = input.next().toLowerCase();
		if (order.equals("ascending"))
			return 1;
		if (order.equals("descending"))
			return -1;
		System.out.println("This is not a valid choice. " 
				+ "Do you want to sort the table by ascending or descending order");
		return readOrder();
	}

	public static void deleteMenu(Table table) {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		boolean correctChoice;
		while (flag) {
			int choice = -1;
			do {
				correctChoice = true;
				printDeleteChoices();
				try {
					choice = input.nextInt();
					if ((choice != 1) && (choice != 2) && (choice != 3)) {
						correctChoice = false;
						System.out.println("This was not one of the menu's choices");
					}
				} catch (InputMismatchException err) {
					System.out.println("This was not a number!");
					correctChoice = false;
					input.next();
					continue;
				}
			} while (correctChoice == false);
			if (choice == 1) {
				deleteEntryMenu(table.getName());
			}
			if (choice == 2) {
				deleteAttributeMenu(table.getName());
			}
			if (choice == 3) {
				deleteElementMenu(table.getName());
			}
			flag = printDeleteMessages(choice, flag, table);
		}
	}

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

	public static void printDeleteChoices() {
		System.out.println("What do you want to delete;");
		System.out.println("	1.An entry");
		System.out.println("	2.A whole attribute");
		System.out.println("	3.Just an element");
		System.out.println("Please enter the number of your choice");
	}

	public static void deleteEntryMenu(String tableName) {
		int line;
		System.out.println("Please enter the number of the entry that you want to delete");
		line = readLines(tableName);
		Table.getTables(Table.position(tableName)).deleteEntry(tableName, line - 1);
	}

	public static void deleteAttributeMenu(String tableName) {
		System.out.println("Please enter the name of the attribute that you want to delete");
		String attName = readAttribute(tableName);
		Table.getTables(Table.position(tableName)).deleteAttribute(tableName, attName);
	}

	public static void deleteElementMenu(String tableName) {
		System.out.println("Please enter the name of "
				+ "the attribute that contains the element that you want to delete");
		String attName = readAttribute(tableName);
		System.out.println("Please enter the "
				+ "number of the element line that you want to delete");
		int line = readLines(tableName);
		Table.getTables(Table.position(tableName)).deleteElement(tableName, line - 1, attName);
	}

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

}
