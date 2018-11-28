package Databasics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class Sorting {

	public ArrayList<Attribute> sortTable(Table table) {
		boolean flag;
		Scanner input = new Scanner(System.in);

		System.out.println("Choose the column you want to sort the table by");
		String keyAttribute = null;
		do {
			for (int i = 0; i < table.getAttributeNumber(); i++)
				System.out.println(table.getAttributes().get(i).getName() + "\n");
			flag = false;
			keyAttribute = input.nextLine();
			for (int i = 0; i < table.getAttributeNumber(); i++) {
				if (keyAttribute.equals(table.getAttributes().get(i).getName()))
					flag = true;
			}

		} while (flag == false);

		int choice = -1;

		do {
			flag = false;
			System.out.println("Choose one of the below: \n1.Ascending Order\n2.Descending Order");
			try {
				choice = input.nextInt();
			} catch (InputMismatchException err) {
				System.out.println("This was not a number!");
				flag = false;
				continue;
			}
			if ((choice == 1) || (choice == 2)) {
				flag = true;
			} else {
				System.out.println("Invalid choice. PLease choose again.");
				continue;
			}
		} while (flag == false);

		int index = -1;
		for (int i = 0; i < table.getAttributeNumber(); i++)
			if (keyAttribute.equals(table.getAttributes().get(i).getName()))
				index = i;

		// sort general
		// sort date
		if ((table.getAttributes().get(index).getType().equals("date"))) {
			return dateSort(table,index,choice);
		} else if ((table.getAttributes().get(index).getType().equals("obj"))){
			System.out.println("This column contains elements that cannot be sorted");
			return null;
		} else {
			return generalSort(table, index, choice);
		}


	}

	public ArrayList<Attribute> generalSort(Table table, int index, int order) {
		if (order == 1) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (table.getAttributes().get(index).getArray().get(j - 1)
							.compareTo(table.getAttributes().get(index).getArray().get(j)) < 0)

						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		if (order == 2) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (table.getAttributes().get(index).getArray().get(j - 1)
							.compareTo(table.getAttributes().get(index).getArray().get(j)) > 0)
						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		return table.getAttributes();

	}

	public ArrayList<Attribute> dateSort(Table table, int index, int order) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(table.getAttributes().get(index).getArray().get(1));
		if (order == 1) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (formatter.format(table.getAttributes().get(index).getArray().get(j - 1))
							.compareTo(formatter.format(table.getAttributes().get(index).getArray().get(j))) < 0) {
						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
					}
				}
		}
		if (order == 2) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (formatter.format(table.getAttributes().get(index).getArray().get(j - 1))
							.compareTo(formatter.format(table.getAttributes().get(index).getArray().get(j))) > 0)
						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		return table.getAttributes();

	}
}
