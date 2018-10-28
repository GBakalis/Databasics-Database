package testinthings;
import java.util.Scanner;
import java.util.ArrayList;
public class Main {
	public static void main(String args[]) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please insert table name:");
		String name = input.nextLine();
		Table table = new Table(name);
		for (int i = 0; i < 5; i++) {
			table.newAttribute();
		}
		for (int i = 0; i < 5; i++) {
			table.newEntry();
		}
		
	}

}
