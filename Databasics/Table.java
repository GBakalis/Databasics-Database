package Databasics;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;

public class Table{

	private String name;
	private int attributeNumber;
	private ArrayList<String> attributeTypes = new ArrayList<String>();
	private ArrayList<String> attributeName = new ArrayList<String>();
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<ArrayList<String>> thisTable = new ArrayList<ArrayList<String>>();

	public Table(String name){
		this.name = name;
		attributeNumber = 0;
		tables.add(this);
	}

	public void newEntry() {
		boolean correctEntry;
		String[] entries;
		do {
			System.out.print("Please add a new entry:");
			Scanner input = new Scanner(System.in);
			String entry = input.nextLine();
			correctEntry = true;
			entries = entry.split(",");
			try {
				if (entries.length != attributeNumber) {
					correctEntry = false;
				} else {
					for (int i = 0; i <= attributeNumber; i++) {
						if (attributeTypes.get(i) == "int") Integer.parseInt(entries[i]);
						if (attributeTypes.get(i) == "double") Double.parseDouble(entries[i]);
						if (attributeTypes.get(i) == "date") {
							DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
							format.setLenient(false);
							format.parse(entries[i]);
						}
						if ((attributeTypes.get(i) == "char") && (entries[i].length() != 1)) throw new NotCharacterException();
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("Wrong entry on an Integer or Decimal column!");
				correctEntry = false;
			} catch (ParseException e) {
				System.out.println("Invalid date format in a date column!");
				correctEntry = false;
			} catch (NotCharacterException e) {
				System.out.println("Large entry on a single letter column!");
				correctEntry = false;
			} finally {
				if (correctEntry == false) {
					System.out.println("Please try again!");
				}
			}

		} while (correctEntry == false);
		for (int i = 0; i < entries.length; i++) {
			thisTable.get(i).add(entries[i]);
		}

	}

	public ArrayList<ArrayList<String>> getthisTable() {
		return thisTable;
	}

	public String getAttributeName(int i) {
		return attributeName.get(i);
	}

	public void setAttributeName(int i, String name) {
		attributeName.set(i, name);
	}

	public int getAttributeNumber() {
		return attributeNumber;
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
					attributeTypes.add("string");
					break;
				case 2:
					ArrayList<String> character = new ArrayList<String>();
					attributeTypes.add("char");
					break;
				case 3:
					thisTable.add(new ArrayList<String>());
					attributeTypes.add("int");
					break;
				case 4:
					thisTable.add(new ArrayList<String>());
					attributeTypes.add("double");
					break;
				case 5:
					thisTable.add(new ArrayList<String>());
					attributeTypes.add("date");
					break;
				case 6:
					thisTable.add(new ArrayList<String>());
					attributeTypes.add("object");
					break;
				default:
					System.out.println("Invalid input " + choice);
			}
		} while(choice < 1 || choice > 6);
	}

}
