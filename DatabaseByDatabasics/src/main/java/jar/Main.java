package jar;

import java.util.Scanner;
import java.util.ArrayList;

//Create better tests for Main and then commit

public class Main {

	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please insert table name:");
		String name = input.nextLine().trim();
		Table table = new Table(name);
		ArrayList<String> test = new ArrayList<String>();
		test.add("a");
		test.add("c");
		for (int i = 0; i < 3; i++) {
			table.newAttribute("a", 1);
		}
		String[] a = {"a","b"};
		for (int i = 0; i < 2; i++) {
			table.newEntry(a);
		}
		Table.viewAttribute(name, test);

	}

}

