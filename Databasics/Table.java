package Databasics;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.util.InputMismatchException;

//Break newEntry() and newAttribute() to smaller methods and merge

public class Table{

	private String name;
	private int attributeNumber;
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<Attribute> thisTable = new ArrayList<Attribute>();

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
					for (int i = 0; i < attributeNumber; i++) {
						if (thisTable.get(i).getType() == "int") Integer.parseInt(entries[i]); //changed all "attributeTypes.getType(i)" to that
						if (thisTable.get(i).getType() == "double") Double.parseDouble(entries[i]);
						if (thisTable.get(i).getType() == "date") {
							DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
							format.setLenient(false);
							format.parse(entries[i]);
						}
						if ((thisTable.get(i).getType() == "char") && (entries[i].length() != 1)) throw new NotCharacterException();
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
			thisTable.get(i).setEntryField(entries[i]);
		}

	}

	public ArrayList<Attribute> getThisTable() {
		return thisTable;
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

	public boolean checkInput (int choice, boolean correctEntry) {
		try{
			if(choice < 1 || choice >6)
				throw new WrongEntryException();
			else {
				switch(choice) {
					case 1:
						thisTable.add(new Attribute(name, "string"));
						break;
					case 2:
						thisTable.add(new Attribute(name, "char"));
						break;
					case 3:
						thisTable.add(new Attribute(name, "int"));
						break;
					case 4:
						thisTable.add(new Attribute(name, "double"));
						break;
					case 5:
						thisTable.add(new Attribute(name, "date"));
						break;
					case 6:
						thisTable.add(new Attribute(name, "obj"));
						break;
				}
			}
		} catch (WrongEntryException e) {
			System.out.println(choice + " is not a valid input.");
			correctEntry = false;
		}
		return correctEntry;
	}

	public void newAttribute() throws InputMismatchException {
		boolean correctEntry;
		attributeNumber++;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the name of the new attribute");
		String name = input.nextLine();
		int choice;
		do {
			correctEntry = true;
			choice = 0;
			System.out.println("Your attribute can be of any of the following types:\n"
					+ "1. Text\n"
					+ "2. Single letter\n"
					+ "3. Integer\n"
					+ "4. Decimal\n"
					+ "5. Date\n"
					+ "6. Other (e.g. Image)\n\n"
					+ "Insert the number that corresponds to the type you want.");
			try {
				choice = input.nextInt();
			} catch (InputMismatchException err) {
				System.out.println("This was not a number!");
				correctEntry = false;
			}

			correctEntry = checkInput(choice, correctEntry);

		} while(correctEntry == false);
	}
}