package jar;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CopyMenu {
	
	ArrayList<String> nameFinal = new ArrayList<String>();
	
	public void menuCopy() {
		Scanner input = new Scanner(System.in);
		boolean flag = true;
		boolean correctChoice = false;
		while (flag) {
			int choice = -1;
			do {
				correctChoice = true;
				System.out.println("What do you want to copy;");
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
			}while (correctChoice == false);
			copy(choice);
			System.out.println("Do you want to copy anything else?");
			System.out.println("	1.Yes");
			System.out.println("	2.No");
			System.out.println("Please choose a number");
			int ch = input.nextInt();
			if (ch != 1) {
				flag = false;
			} 
		}
	}
	
	public void copy(int choice) {
		switch (choice) {
			case 1:
				tableMenu();
				break;
			case 2:
				entryMenu();
				break;
			case 3:
				attributeMenu();
				break;
			case 4:
				elementMenu();
				break;
			}

	}
	
	public void tableMenu() {
		Scanner input = new Scanner(System.in);
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that you want to copy");
		nameCopy = readTableName();
		
		System.out.println("Please enter the name of the table that you want to paste");
		namePaste = input.nextLine();
		Table.getT().get(0).copyTable(nameCopy,namePaste);
	}
	
	public void entryMenu() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that contains the entry");
		nameCopy = readTableName();
		nameFinal.add(nameCopy);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
		
		System.out.println("Please enter the number of the entry that you want to copy");
		int entryNumCopy = readLine();
		
		System.out.println("Please enter the name of the table that that you want to paste the entry");
		namePaste = readTableName();
		nameFinal.add(namePaste);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
		
		System.out.println("Please enter the number of the entry that you want to paste");
		int entryNumPaste = readLine();
		Table.getT().get(0).copyEntry(nameCopy,entryNumCopy,namePaste,entryNumPaste);
	}
	
	public void attributeMenu() {
		Scanner input = new Scanner(System.in);
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that contains the attribute");
		nameCopy = readTableName();
		nameFinal.add(nameCopy);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
		
		System.out.println("Please enter the name of the attribute that you want to copy");
		String attNameC = readAttribute(nameCopy);
		
		System.out.println("Please enter the name of the table that that you want to paste the attribute");
		namePaste = readTableName();
		nameFinal.add(namePaste);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
		
		System.out.println("Please enter the name of the attribute where you want to paste");
		String attNameP = input.nextLine();
		Table.getT().get(0).copyAttribute(nameCopy,attNameC, namePaste,attNameP);
	}
	
	public void elementMenu() {
		String nameCopy;
		String namePaste;
		System.out.println("Please enter the name of the table that contains the element that you want to copy");
		nameCopy = readTableName();
		nameFinal.add(nameCopy);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
	
		System.out.println("Please enter the name of the attribute that contains the element tha you want to copy");
		String attNameC = readAttribute(nameCopy);
		
		System.out.println("Please enter the number of the line that contains the element that you want to copy");
		int lineC = readLine();
		
		System.out.println("Please enter the name of the table where you want to paste the element");
		namePaste = readTableName();
		nameFinal.add(namePaste);
		Table.viewTable(nameFinal);
		nameFinal.remove(0);
		
		System.out.println("Please enter the name of the attribute where you want to paste the element");
		String attNameP = readAttribute(namePaste);
		
		System.out.println("Please enter the number of the line where you want to paste the element");
		int lineP = readLine();
		Table.getT().get(0).copyElement( nameCopy, attNameC, lineC, namePaste, attNameP, lineP);
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

