import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;

public class Table{

	private String name;
	private int attributeNumber;
	private ArrayList<String> attributeName = new ArrayList<String>();
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<Object> thisTable = new ArrayList<Object>();

	public Table(String name){
		this.name = name;
		attributeNumber = 0;
		tables.add(this);
	}

	public String getAttributeName(int i) {
		return attributeName.get(i);
	}

	public void setAttributeName(int i, String name) {
		attributeName.set(i, name);
	}

	public int getAttributeNumber() {
		return attributeNumber
	}

	public static Table getTables(int i){
		return tables.get(i);
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public void newAttribute(){
		attributeNumber++;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of the new attribute");
		String name = sc.nextLine();
		int choice;
		do {
			System.out.println("Your attribute can be of any of the following types:\n"
					+ "1. Text\n"
					+ "2. Single letter\n"
					+ "3. Integer\n"
					+ "4. Decimal\n"
					+ "5. Date\n"
					+ "6. Other (e.g. Image)\n\n"
					+ "Insert the number that corresponds to the type you want.");
			choice = sc.nextInt();

			switch(choice) {
				case 1:
					thisTable.add(new ArrayList<String>());
					break;
				case 2:
					thisTable.add(new ArrayList<Character>());
					break;
				case 3:
					thisTable.add(new ArrayList<Integer>());
					break;
				case 4:
					thisTable.add(new ArrayList<Double>());
					break;
				case 5:
					thisTable.add(new ArrayList<Date>());
					break;
				case 6:
					thisTable.add(new ArrayList<Object>());
					break;
				default:
					System.out.println("Invalid input " + choice);
			}
		} while(choice < 1 || choice > 6);
	}

}