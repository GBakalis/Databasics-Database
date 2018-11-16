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
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.printf("%20s|", table.getAttributes().get(j).getArray().get(i)); //Make methods per pair of gets
			}
			System.out.println();
		}

	}

}

