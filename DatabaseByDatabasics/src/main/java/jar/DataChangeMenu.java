import java.util.ArrayList;
import java.util.Scanner;

public class DataChangeMenu {
	public static void menuDataChange(Table table) {
        String done = "yes";
        int num;
        ArrayList<String> attributes;
        ArrayList<String> values;
        do {
        	Scanner input = new Scanner(System.in);
        	num = readLines(table);
        	attributes = readAttributes(table, num);
        	values = readValues(table,num,attributes);
        	table.dataChange(num, attributes, values);
        	Table.viewTable(table.getName());
        	System.out.println("Do you want to change another line?");
        	done = input.next();
        } while (done.equals("yes"));
	}

	public static int readLines(Table table) {
		Scanner input = new Scanner(System.in);
		int num;
		do {
		 System.out.println("Enter the number of the line");
		 num = input.nextInt();
		} while (num <0 && num >= table.getLines());
		 return num - 1;
	}

	public static ArrayList<String> readAttributes(Table table, int num) {
		ArrayList<String> atts = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		boolean flag = false;
		do {
			for (int i = 1; i < table.getAttributeNumber()-1; i++) {
				System.out.println("Do you want to change the attribute " + table.getAttributes().get(i).getName());
				String choice = input.nextLine();
				if (choice.equals("yes")){
					atts.add(table.getAttributes().get(i).getName());
					flag = true;
				}
			}
			if (flag == false) {
				System.out.print("You have to change one attribute");
			}
		} while (flag == false);
		return atts;
	}
	public static ArrayList<String> readValues(Table table, int num, ArrayList<String> atts) {
		ArrayList<String> values = new ArrayList<String>();
		Scanner input = new Scanner(System.in);
		String ch;
		int j = 0;
		for (int i = 1; i < table.getAttributeNumber()-1; i++) {
			if ( table.getAttributes().get(i).getName().equals(atts.get(j))) {
				System.out.println("The old value is " + table.getAttributes().get(i).getArray().get(num) + " Type in the new value.");
				ch = input.nextLine();
				values.add(ch);
				j +=1;
			}
		}
		return values;
	}
}