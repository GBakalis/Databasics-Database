package jar;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
	
	public static String readTableName() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		while (Table.exists(tableName) == false) {
			System.out.println("This table does not exist. Please type an existing name.");
			tableName = input.nextLine();
		}
		return tableName;
	}
	
	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		while (Table.exists(tableName, attName) == false) {
			System.out.println("This attribute does not exist. Please type an existing name.");
			attName = input.nextLine();
		}
		return attName;
	}
	
	public static void functionsMenu() {
		
	}
	
	public static void searchMenu(Table table) {
		Scanner input = new Scanner(System.in);
		ArrayList<String> attNames = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		for (int i = 0; i < table.getAttributeNumber(); i++) {
			System.out.print("Please insert an attribute name according to which you want to search: ");
			String attName = readAttribute(table.getName());
			attNames.add(attName);
			System.out.print("Please insert an element you want to search in the column previously inserted: ");
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
		table.search(attNames, elements);
	}
	
	public static void newEntryMenu(Table table) {
		boolean correctEntry;
		String[] entries;
		Scanner input = new Scanner(System.in);
		do {
			System.out.print("Please add a new entry:");
		    String entry = input.nextLine();
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
}
