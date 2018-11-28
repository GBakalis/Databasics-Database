package jar;

import java.util.Scanner;

public class Menu {

	public static void CreationMenu() {
		String flag = null;
		do {
			Table table = tableCreatioMenu();
			attributeCreatinMenu(table);
			entryCreationMenu(table);
			System.out.println("Do you want to create another table?");
			Scanner input = new Scanner(System.in);
			flag = input.next().toLowerCase();

		} while ((flag.equals("yes")) || (!flag.equals("no")));
		
	}

	public static Table tableCreatioMenu() {
		String tableName = null;
		do {
			System.out.print("Please type in the name of the new table.");
			Scanner input = new Scanner(System.in);
			tableName = input.nextLine();
			if (Table.exists(tableName) == true)
				System.out.println("This table already exists.");
		} while (Table.exists(tableName) == true);
		Table table = new Table(tableName);
		return table;
	}

	public static void attributeCreatinMenu(Table table) {
		String answer = null;
		do {
			Scanner input = new Scanner(System.in);
			String attributeName = null;
			do {
				System.out.println("Type in the name of the new attribute");
				attributeName = input.nextLine();
				if (Table.exists(table.getName(), attributeName))
					System.out.println("This attribute already exists.");
			} while (Table.exists(table.getName(), attributeName) == true);
			Table.attributeMenu(table, attributeName);
			System.out.println("Do you want to create another attribute?");
			answer = input.next().toLowerCase();
		} while ((answer.equals("yes")) || (!answer.equals("no")));
	}

	public static void entryCreationMenu(Table table) {
		String answer = null;
		String entry = null;
		do {
			Scanner input = new Scanner(System.in);
			System.out.println("Type an entry line");
			entry = input.nextLine();
			Table.newEntryMenu(table, entry);
			System.out.println("Do you want to add anoter entry?");
			answer = input.next().toLowerCase();
		} while ((answer.equals("yes")) || (!answer.equals("no")));
	}

	public static void sortMessages() {
		System.out.println("Please type in the name of the table you want to sort");
		readTable();
		System.out.println("Please type in the name of the attribute you want to sort the table by.");
		readAttributeName();
		System.out.println("You want to sort the table by ascending or descending order?");
		readOrder();
	}

	public static Table readTable() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		while (Table.exists(tableName) == false) {
			System.out.println("This table does not exist. Please type an existing name.");
			tableName = input.nextLine();
		}
		return Table.getTables(Table.position(tableName));
	}

	public static String readAttributeName() {
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}

	public static int readOrder() {
		Scanner input = new Scanner(System.in);
		String order = input.next().toLowerCase();
		if (order.equals("ascending"))
			return 1;
		if (order.equals("descending"))
			return 2;
		System.out
				.println("This is not a valid choice. Do you want to sort the table by ascending or descending order");
		return readOrder();
	}

}
