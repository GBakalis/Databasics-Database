package jar;

import java.text.ParseException;
import java.util.Scanner;

public class Menu {

	public static void CreationMenu() {
		String flag = null;
		do {
			Table table = tableCreationMenu();
			attributeCreationMenu(table);
			entryCreationMenu(table);
			System.out.println("Do you want to create another table?");
			Scanner input = new Scanner(System.in);
			flag = input.next().toLowerCase();

		} while ((flag.equals("yes")) || (!flag.equals("no")));

	}

	public static Table tableCreationMenu() {
		Table table = new Table(readTableNameMenu());
		return table;
	}

	public static String readTableNameMenu() {
		String tableName = null;
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("Please type the name of the new table.");
			tableName = input.nextLine();
			boolean ex = false;
			if (Table.exists(tableName) == true) {
				System.out.println("This table already exists.");
				ex = true;
			}
		} while (ex == true)
		return tableName;
	}

	public static void attributeCreationMenu(Table table) {
		String answer = null;
		boolean ex;
		do {
			Scanner input = new Scanner(System.in);
			String attributeName = null;
			do {
				boolean ex = false;
				System.out.println("Please type the name of the new attribute");
				attributeName = input.nextLine();
				if (Table.exists(table.getName(), attributeName)) {
					System.out.println("This attribute already exists.");
					ex = true;
				}
			} while (ex == true);
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
		String tableName = readTable();
		System.out.println("Please type in the name of the attribute you want to sort the table by.");
		String keyAttribute = readAttribute();
		System.out.println("You want to sort the table by ascending or descending order?");
		int order = readOrder();
		try {
			readTable(tableName).sortTable(keyAttribute, order);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
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

	public static String readAttribute() {
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}

	public static int readOrder() {
		Scanner input = new Scanner(System.in);
		String order = input.next().toLowerCase();
		if (order.equals("ascending"))
			return 1;
		if (order.equals("descending"))
			return -1;
		System.out
				.println("This is not a valid choice. Do you want to sort the table by ascending or descending order");
		return readOrder();
	}

}
