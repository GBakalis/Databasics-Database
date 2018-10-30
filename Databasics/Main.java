package Databasics;

import java.util.Scanner;
import java.util.ArrayList;

//Create better tests for Main and then commit

public class Main {

	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please insert table name:");
		String name = input.nextLine();
		Table table = new Table(name);
		for (int i = 0; i < 3; i++) {
			table.newAttribute();
		}
		for (int i = 0; i < 2; i++) {
			table.newEntry();
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.println(table.getThisTable().get(i).getArray().get(j)); //Make methods per pair of gets
			}
		}

	}

}

