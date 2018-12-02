package jar;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
	
	public static void menuAddEntry(Table table) {
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

	public static void menuAddAttribute(Table table) throws InputMismatchException {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the name of the new attribute");
		String name = input.nextLine().trim();
		int choice = 0;
		do {
			correctEntry = true;
			System.out.println("Your attribute can be of any of the following types:\n"
								+ "1. Text\n"
								+ "2. Single letter\n"
								+ "3. Integer\n"
								+ "4. Decimal\n"
								+ "5. Date\n"
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
		} while(correctEntry == false);
		table.newAttribute(name, choice);
	}

	public static void menuView() {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		boolean correctChoice;
		while (flag) {
			int choice = -1;
			do {
				correctChoice = true;
				System.out.println("What do you want to view?");
				System.out.println("	1.A table");
				System.out.println("	2.One or more attributes");
				System.out.println("Please enter the number of your choice");
				try {
					choice = input.nextInt();
					if(choice != 1 && choice != 2) {
						correctChoice = false;
						System.out.println("This was not one of the menu's choices");
					}
				} catch (InputMismatchException err) {
					System.out.println("This was not a number!");
					correctChoice = false;
					input.next();
					continue;
				}
			}while (correctChoice == false);
			view(choice);
			System.out.println("Do you want to view anything else?");
			System.out.println("Type 'yes' to continue or 'no' to exit View Mode");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			} 
		}
	}
	
	public static void view(int choice) {
		switch (choice) {
		case 1:
			menuViewTable();
			break;
		case 2:
			menuViewAttribute();
			break;
		}
	}

	public static void menuViewTable() {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter the name of the table that you want to view");
		String name = readTable();
		Table.viewTable(name);
	}
	
	public static void menuViewAttribute() {
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter the name of the table that containg the attributes you want to view");
		String tableName = readTable();

		ArrayList<String> atts = new ArrayList<String>();
		boolean flag = true;
		while(flag) {
			System.out.println("Please enter an attribute of this table you want to view");
			atts.add(readAttribute(tableName));
		
			System.out.println("Do you want to view another attribute of this table?");
			System.out.println("Type 'yes' to add another one or 'no' to view the ones you have already entered");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			} 
		}
		Table.viewAttribute(tableName, atts);
	}

	public static void menuCopy() {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		boolean correctChoice;
		while (flag) {
			int choice = -1;
			do {
				correctChoice = true;
				System.out.println("What do you want to copy?");
				System.out.println("	1.A table");
				System.out.println("	2.An entry");
				System.out.println("	3.A whole attribute");
				System.out.println("	4.Just an element");
				System.out.println("Please enter the number of your choice");
				try {
					choice = input.nextInt();
					if(choice != 1 && choice != 2 && choice != 3 && choice != 4) {
						correctChoice = false;
						System.out.println("This was not one of the menu's choices");
					}
				} catch (InputMismatchException err) {
					System.out.println("This was not a number!");
					correctChoice= false;
					input.next();
					continue;
				}
			}while (correctChoice == false);
			copy(choice);
			System.out.println("Do you want to copy anything else?");
			System.out.println("Press 'yes' to continue or 'no' to exit Copy Mode");
			String ch = input.nextLine();
			if (!(ch.equalsIgnoreCase("yes"))) {
				flag = false;
			} 
		}
	}
	
	public static void copy(int choice) {
		switch (choice) {
			case 1:
				menuCopyTable();
				break;
			case 2:
				menuCopyEntry();
				break;
			case 3:
				menuCopyAttribute();
				break;
			case 4:
				menuCopyElement();
				break;
			}
	}
	
	public static void menuCopyTable() {
		Scanner input = new Scanner(System.in);
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that you want to copy");
		nameCopy = readTable();
		
		System.out.println("Please enter the name of the table that you want to paste");
		namePaste = input.nextLine();
		Table.getT().get(0).copyTable(nameCopy,namePaste);
	}
	
	public static void menuCopyEntry() {
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
		Table.getT().get(0).copyEntry(nameCopy,entryNumCopy,namePaste,entryNumPaste);
	}
	
	public static void menuCopyAttribute() {
		Scanner input = new Scanner(System.in);
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that contains the attribute");
		nameCopy = readTable();
		Table.viewTable(nameCopy);
		
		System.out.println("Please enter the name of the attribute that you want to copy");
		String attNameC = readAttribute(nameCopy);
		
		System.out.println("Please enter the name of the table that you want to paste the attribute");
		namePaste = readTable();
		Table.viewTable(namePaste);
		
		System.out.println("Please enter the name of the attribute where you want to paste");
		String attNameP = input.nextLine();
		Table.getT().get(0).copyAttribute(nameCopy,attNameC, namePaste,attNameP);
	}
	
	public static void menuCopyElement() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that contains the element that you want to copy");
		nameCopy = readTable();
		Table.viewTable(nameCopy);
		
		System.out.println("Please enter the name of the attribute that contains the element that you want to copy");
		String attNameC = readAttribute(nameCopy);
		
		System.out.println("Please enter the number of the line that contains the element that you want to copy");
		int lineC = readLine();
		
		System.out.println("Please enter the name of the table where you want to paste the element");
		namePaste = readTable();
		Table.viewTable(namePaste);
		
		System.out.println("Please enter the name of the attribute where you want to paste the element");
		String attNameP = readAttribute(namePaste);
		
		System.out.println("Please enter the number of the line where you want to paste the element");
		int lineP = readLine();
		Table.getT().get(0).copyElement( nameCopy, attNameC, lineC, namePaste, attNameP, lineP);
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
	
	public static String readAttribute(String tableName) {
		Scanner input = new Scanner(System.in);
		String attName = input.nextLine();
		while (Table.exists(tableName, attName) == false) {
			System.out.println("This attribute does not exist. Please type an existing name.");
			attName = input.nextLine();
		}
		return attName;
	}
	
	public static int readLine() {
		Scanner input = new Scanner(System.in);
		return input.nextInt();
	}
}
