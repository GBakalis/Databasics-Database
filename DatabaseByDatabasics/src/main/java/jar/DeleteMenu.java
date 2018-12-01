package jar;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DeleteMenu {
	Scanner input = new Scanner(System.in);
	Table table;

	public void menu() {
		boolean flag = true;
		boolean correctChoice = false;
		while (flag) {
			int choice = -1;
			do {
				correctChoice = true;
				System.out.println("What do you want to delete;");
				System.out.println("	1.A table");
				System.out.println("	2.An entry");
				System.out.println("	3.A whole attribute");
				System.out.println("	4.Just an element");
				System.out.println("Please enter the number of your choice");
				try {
					choice = input.nextInt();
				} catch (InputMismatchException err) {
					System.out.println("This was not a number!");
					correctChoice= false;
					input.next();
					continue;
				}
			}	while (correctChoice == false);

			System.out.println("Are you sure you want to delete this?");
			System.out.println("	1.Yes");
			System.out.println("	2.No");
			System.out.println("Please choose a number");
			int d = input.nextInt();
				if (d == 1) {
					delete(choice);
					System.out.println("Do you want to delete anything else?");
					System.out.println("	1.Yes");
					System.out.println("	2.No");
					System.out.println("Please choose a number");
					int ch = input.nextInt();
					if (ch == 1) {
						flag = true;
					} else {
						flag = false;
					}
				} else {
					flag = true;
				}

		}
	}

	public void tableMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that you want to delete");
		tableName = readTableName();
		Table.getTables(Table.position(tableName)).deleteTable(tableName);
	}

	public static String readTableName() {
		Scanner input = new Scanner(System.in);
		String tableName = input.nextLine();
		while (Table.exists(tableName) == false) {
			System.out.println("This table does not exist. Please type an existing name.");
			tableName = input.nextLine();
		}
		return tableName;
	}

	public static int readLine(String tableName) {
		Scanner input = new Scanner(System.in);
		int lineNum = input.nextInt();
		while (lineNum > Table.getTables(Table.position(tableName)).getLines()) {
					System.out.println("This line does not exist. Please type an existing line.");
					lineNum = input.nextInt();
		}
		return lineNum;
	}

	public void entryMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that contains the entry you want to delete");
		tableName = readTableName();
		int line;
		System.out.println("Please enter the number of the entry that you want to delete");
		line = readLine(tableName);
		Table.getTables(Table.position(tableName)).deleteEntry(tableName,line);
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

	public void attributeMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that contains the attribute you want to delete");
		tableName = readTableName();
		System.out.println("Please enter the name of the attribute that you want to delete");
		String attName = readAttribute(tableName);
		Table.getTables(Table.position(tableName)).deleteAttribute(tableName,attName);
	}

	public void elementMenu() {
		String tableName;
		System.out.println("Please enter the name of the table that contains the element that you want to delete");
		tableName = readTableName();
		System.out.println("Please enter the name of the attribute that contains the element that you want to delete");
		String attName = readAttribute(tableName);
		System.out.println("Please enter the number of the element line that you want to delete");
		int line = readLine(tableName);
		Table.getTables(Table.position(tableName)).deleteElement(tableName, line, attName);
	}


	public void delete(int choice) {
		switch (choice) {
			case 1:
				tableMenu();
			case 2:
				entryMenu();
			case 3:
				attributeMenu();
			case 4:
				elementMenu();
			}

	}

}
