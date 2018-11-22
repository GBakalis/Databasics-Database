package jar;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.text.ParseException;
import java.util.InputMismatchException;

/**
 * Table is the class that allows the creation and editing of tables
 * inside a database.
 * A Table object is practically a series of Attribute (might also be
 * referred to as "column") objects held inside an ArrayList of type
 * Attribute and encapsulates the basic information needed for this purpose:
 * <ul>
 * <li>The table's name
 * <li>The number of attributes that the table holds
 * <li>The Attribute objects which contain the content the user inputs
 * <li>An ArrayList of the Tables constructed by the user.
 * </ul>
 * <p>
 * The class is designed in such manner that it will support methods
 * inside of it and other classes externally to execute the usual
 * functions that a database has, such as:
 * <ul>
 * <li>View Table's content
 * <li>View a column's content
 * <li>Input data into a table
 * <li>Copy an entry to another position of the same or another table
 * <li>Move an entry to another position of the same or another table
 * <li>Copy a single field to another position of the same or another
 * table
 * <li>Move a single field to another position of the same or another
 * table
 * <li>Delete an entry
 * <li>Delete a table
 * <li>Erase a field
 * <li>Substitute a field
 * <li>Substitute an entry
 * <li>More
 * </ul>
 * <p>
 * An important point to consider is that the methods inside the class
 * are written in such a manner that they require the data input given
 * in a strict way by the user.
 *
 * @author George Bakalis
 * @author Andreas Vlachos
 */

public class Table{

	private String name;
	private int attributeNumber;
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private int lines;

	/**
	 * A simple constructor that only expects a name to initialize a table
	 *
	 * @param name the name to be used as a title for the table
	 */
	public Table(String name){
		this.name = name;
		attributeNumber = 1;
		tables.add(this);
		attributes.add(0, new Attribute("Time of last edit", "Date"));
	}

	/**
	 * This method implements a simple check on the entry that the user
	 * has given on demand, in order to decide if it has the correct
	 * number and types of input. It uses existing and custom exceptions
	 * to guide the user into a correct entry, if needed. If the user does
	 * not want to insert an element to a column he should type <code>--</code>
	 * instead
	 *
	 * @param entries      an array of <code>String</code> elements, which is the user's input
	 * @param correctEntry a <code>boolean</code> initialized as <code>true</code>, prone
	 *                     to switching to <code>false</code> if there's a wrong input
	 *
	 * @return             <code>true</code> if no mistake was found;
	 *                     <code>false</code> if there's a wrong input.
	 */
	public boolean checkType(String[] entries, boolean correctEntry) {
		try {
			if (entries.length != attributeNumber-1) {
				correctEntry = false;
			} else {
				for (String entry : entries){
					System.out.print(entry + "|");
				}
				System.out.println();
				for (int i = 0; i < attributeNumber-1; i++) {
					if (attributes.get(i).getType() == "int" && !entries[i].equals("--")) {
						Integer.parseInt(entries[i]); //changed all "attributeTypes.getType(i)" to that
					}
					if (attributes.get(i).getType() == "double" && !entries[i].equals("--")) {
						Double.parseDouble(entries[i]);
					}
					if (attributes.get(i).getType() == "date" && !entries[i].equals("--")) {
						DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						format.setLenient(false);
						format.parse(entries[i]);
					}
					if ((attributes.get(i).getType() == (String) "char") && (
							entries[i].length() != 1) && !entries[i].equals("--")) {
						throw new NotCharacterException();
					}
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
		return correctEntry;
	}

	/**
	 * This method creates an entry on the user's demand. It asks for the
	 * entry, splits it on commas and holds it inside an array.
	 * Checks whether the input is valid using checktype(String[], boolean)
	 * and then proceeds to pass the correct input inside the table.
	 */

	public void newEntry() {
		boolean correctEntry;
		String[] entries;
		do {
			System.out.print("Please add a new entry:");
			Scanner input = new Scanner(System.in);
			String entry = input.nextLine();
			correctEntry = true;
			entries = entry.split(",");

			correctEntry = checkType(entries, correctEntry);

		} while (correctEntry == false);
		for (int i = 0; i < entries.length; i++) {
			attributes.get(i).setEntryField(entries[i]);
		}
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		attributes.get(attributeNumber-1).setEntryField(format.format(date));
		lines++;
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
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

	/**
	 * This method implements a simple check on the entry that the user
	 * has given on demand, in order to decide if it is an integer between
	 * 1 and 6, as the choices indicate. It uses a custom exception to
	 * guide the user into a correct entry, if needed.
	 *
	 * @param choice       <code>int</code> containing the user's choice
	 * @param correctEntry a <code>boolean</code> initialized as <code>true</code>, prone
	 *                     to switching to <code>false</code> if there's a wrong input
	 *
	 * @return             <code>true</code> if no mistake was found;
	 *                     <code>false</code> if there's a wrong input.
	 */

	public boolean checkInput (int choice, boolean correctEntry) {
		try{
			if(choice < 1 || choice > 6)
				throw new WrongEntryException();
			else {
				switch(choice) {
				case 1:
					attributes.add(attributeNumber-2, new Attribute(name, "string"));
					break;
				case 2:
					attributes.add(attributeNumber-2, new Attribute(name, "char"));
					break;
				case 3:
					attributes.add(attributeNumber-2, new Attribute(name, "int"));
					break;
				case 4:
					attributes.add(attributeNumber-2, new Attribute(name, "double"));
					break;
				case 5:
					attributes.add(attributeNumber-2, new Attribute(name, "date"));
					break;
				case 6:
					attributes.add(attributeNumber-2, new Attribute(name, "obj"));
					break;
				}
			}
		} catch (WrongEntryException e) {
			System.out.println(choice + " is not a valid input.");
			correctEntry = false;
		}
		return correctEntry;
	}

	/**
	 * This method creates an attribute (column) on the user's demand.
	 * It asks for a name and a data type for the attribute, checks for
	 * possible <code>InputMismatchException</code> and/or an invalid int
	 * via the checkInput(int, boolean) method. If everything is correct,
	 * a new Attribute object is successfully being initialized.
	 */

	public void newAttribute() throws InputMismatchException {
		boolean correctEntry;
		attributeNumber++;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the name of the new attribute");
		String name = input.nextLine();
		int choice = 0;
		do {
			correctEntry = true;
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
				input.next();
				continue;
			}

			correctEntry = checkInput(choice, correctEntry);

		} while(correctEntry == false);
	}

	public static boolean exists(String name) {
		for (Table table : tables) {
			if (table.getName() == name) {
				return true;
			}
		}
		return false;
	}

	public boolean exists(String tableName, String attributeName) {
		for (Table table : tables) {
			if (table.getName() == tableName) {
				for (Attribute attribute : attributes) {
					if (attribute.getName() == attributeName) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public int [] matchSearchAttributes(ArrayList<String> attributeNames)
			throws NotMatchingAttributeException {
		int [] columnIndeces = new int[attributeNames.size()];
		for (int i = 0; i < attributeNames.size(); i++) {
			boolean correctAttribute = false;
			for (int j = 0; j < attributeNumber; j++) {
				if (attributeNames.get(i).equals(attributes.get(j).getName())) {
					correctAttribute = true;
					columnIndeces[i] = j; //The name of the attribute given for search was found in column j
					break;
				}
			}
			if (correctAttribute == false) {
				throw new NotMatchingAttributeException(attributeNames.get(i)+" is not an attribute name!");
			}
		}
		return columnIndeces;
	}
	
	public ArrayList<Attribute> sortTable(Table table, String keyAttribute, int choice) throws ParseException {

		int index = -1;
		try {
			index = table.matchSearchAttributes(new ArrayList<String>(Arrays.asList(keyAttribute)))[0];
		} catch (NotMatchingAttributeException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < table.getAttributeNumber(); i++)
			if (keyAttribute.equals(table.getAttributes().get(i).getName()))
				index = i;

		// sort general
		// sort date
		if ((table.getAttributes().get(index).getType().equals("date"))
				|| (table.getAttributes().get(index).getType().equals("Time of last edit"))) {
			return dateSort(table, index, choice, returnFormater(table, index));
		} else if ((table.getAttributes().get(index).getType().equals("obj"))) {
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
							.compareTo(table.getAttributes().get(index).getArray().get(j)) > 0)

						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		if (order == 2) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (table.getAttributes().get(index).getArray().get(j - 1)
							.compareTo(table.getAttributes().get(index).getArray().get(j)) < 0)
						for (int k = 0; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		return table.getAttributes();

	}

	public SimpleDateFormat returnFormater(Table table, int index) {
		if (index == table.getAttributeNumber() - 1)
			return new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		else
			return new SimpleDateFormat("dd:MM:yyyy");
	}

	public ArrayList<Attribute> dateSort(Table table, int index, int order, SimpleDateFormat formatter)
			throws ParseException {
		if (order == 1) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++) {
				{
					for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
						if (formatter.parse(table.getAttributes().get(index).getArray().get(j - 1))
								.compareTo(formatter.parse(table.getAttributes().get(index).getArray().get(j))) > 0) {
							for (int k = 0; k < table.getAttributeNumber(); k++)
								Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
						}
					}
				}
			}
		}
		if (order == 2) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++) {
				{
					for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
						if (formatter.parse(table.getAttributes().get(index).getArray().get(j - 1))
								.compareTo(formatter.parse(table.getAttributes().get(index).getArray().get(j))) < 0) {
							for (int k = 0; k < table.getAttributeNumber(); k++)
								Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
						}
					}
				}
			}
		}
		return table.getAttributes();
	}
}


