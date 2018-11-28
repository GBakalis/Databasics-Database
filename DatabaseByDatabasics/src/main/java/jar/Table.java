package jar;
<<<<<<< HEAD
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
=======

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.text.DateFormat;
>>>>>>> sortFunction
import java.text.ParseException;
import java.util.InputMismatchException;

/**
<<<<<<< HEAD
 * Table is the class that allows the creation and editing of tables
 * inside a database.
 * A Table object is practically a series of Attribute (might also be
 * referred to as "column") objects held inside an ArrayList of type
 * Attribute and encapsulates the basic information needed for this purpose:
=======
 * Table is the class that allows the creation and editing of tables inside a
 * database. A Table object is practically a series of Attribute (might also be
 * referred to as "column") objects held inside an ArrayList of type Attribute
 * and encapsulates the basic information needed for this purpose:
>>>>>>> sortFunction
 * <ul>
 * <li>The table's name
 * <li>The number of attributes that the table holds
 * <li>The Attribute objects which contain the content the user inputs
 * <li>An ArrayList of the Tables constructed by the user.
 * </ul>
 * <p>
<<<<<<< HEAD
 * The class is designed in such manner that it will support methods
 * inside of it and other classes externally to execute the usual
 * functions that a database has, such as:
=======
 * The class is designed in such manner that it will support methods inside of
 * it and other classes externally to execute the usual functions that a
 * database has, such as:
>>>>>>> sortFunction
 * <ul>
 * <li>View Table's content
 * <li>View a column's content
 * <li>Input data into a table
 * <li>Copy an entry to another position of the same or another table
 * <li>Move an entry to another position of the same or another table
<<<<<<< HEAD
 * <li>Copy a single field to another position of the same or another
 * table
 * <li>Move a single field to another position of the same or another
 * table
=======
 * <li>Copy a single field to another position of the same or another table
 * <li>Move a single field to another position of the same or another table
>>>>>>> sortFunction
 * <li>Delete an entry
 * <li>Delete a table
 * <li>Erase a field
 * <li>Substitute a field
 * <li>Substitute an entry
 * <li>More
 * </ul>
 * <p>
<<<<<<< HEAD
 * An important point to consider is that the methods inside the class
 * are written in such a manner that they require the data input given
 * in a strict way by the user.
=======
 * An important point to consider is that the methods inside the class are
 * written in such a manner that they require the data input given in a strict
 * way by the user.
>>>>>>> sortFunction
 *
 * @author George Bakalis
 * @author Andreas Vlachos
 */

<<<<<<< HEAD
public class Table{

	private String name;
	private int attributeNumber;
=======
public class Table {

	private String name;
	private int attributeNumber;
	private int lines;
>>>>>>> sortFunction
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * A simple constructor that only expects a name to initialize a table
	 *
<<<<<<< HEAD
	 * @param name the name to be used as a title for the table
	 */
	public Table(String name){
		this.name = name;
		attributeNumber = 0;
=======
	 * @param name
	 *            the name to be used as a title for the table
	 */
	public Table(String name) {
		this.name = name;
		attributes.add(new Attribute("#", "int"));
		attributes.add(new Attribute("Time of last edit", "Date"));
		attributeNumber = 2;
		lines = 0;
>>>>>>> sortFunction
		tables.add(this);
	}

	/**
<<<<<<< HEAD
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
			if (entries.length != attributeNumber) {
				correctEntry = false;
			} else {
				for (String entry : entries){
					System.out.print(entry + "|");
				}
				System.out.println();
				for (int i = 0; i < attributeNumber; i++) {
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
=======
	 * This method implements a simple check on the entry that the user has given on
	 * demand, in order to decide if it has the correct number and types of input.
	 * It uses existing and custom exceptions to guide the user into a correct
	 * entry, if needed. If the user does not want to insert an element to a column
	 * he should type <code>--</code> instead
	 *
	 * @param entries
	 *            an array of <code>String</code> elements, which is the user's
	 *            input
	 * @param correctEntry
	 *            a <code>boolean</code> initialized as <code>true</code>, prone to
	 *            switching to <code>false</code> if there's a wrong input
	 *
	 * @return <code>true</code> if no mistake was found; <code>false</code> if
	 *         there's a wrong input.
	 */
	public void checkEntryType(String[] entries) throws ParseException, NumberFormatException, NotCharacterException {
		for (int i = 1; i < attributeNumber - 2; i++) {
			if (attributes.get(i).getType() == "int" && !entries[i - 1].equals("--")) {
				Integer.parseInt(entries[i - 1]);
			}
			if (attributes.get(i).getType() == "double" && !entries[i - 1].equals("--")) {
				Double.parseDouble(entries[i - 1]);
			}
			if (attributes.get(i).getType() == "date" && !entries[i - 1].equals("--")) {
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				format.setLenient(false);
				format.parse(entries[i - 1]);
			}
			if ((attributes.get(i).getType() == (String) "char") && (entries[i - 1].length() != 1)
					&& !entries[i - 1].equals("--")) {
				throw new NotCharacterException();
			}
		}
	}

	public boolean checkEntry(String[] entries) {
		boolean correctEntry = true;
		try {
			if (entries.length != attributeNumber - 2) {
				correctEntry = false;
			} else {
				checkEntryType(entries);
			}
		} catch (NumberFormatException e) {
			System.err.println("Wrong entry on an Integer or Decimal column!");
			correctEntry = false;
		} catch (ParseException e) {
			System.err.println("Invalid date format in a date column!");
			correctEntry = false;
		} catch (NotCharacterException e) {
			System.err.println("Large entry on a single letter column!");
			correctEntry = false;
>>>>>>> sortFunction
		}
		return correctEntry;
	}

	/**
<<<<<<< HEAD
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
=======
	 * This method creates an entry on the user's demand. It asks for the entry,
	 * splits it on commas and holds it inside an array. Checks whether the input is
	 * valid using checkEntry(String[]) and then proceeds to pass the correct input
	 * inside the table.
	 */
	public static void newEntryMenu(Table table, String entry) {
		boolean correctEntry;
		String[] entries;
	//	Scanner input = new Scanner(System.in);
		do {
	//		System.out.print("Please add a new entry:");
	//		String entry = input.nextLine();
			entries = entry.split(",");
			for (int i = 0; i < entries.length; i++) {
				entries[i] = entries[i].trim();
			}
			correctEntry = table.checkEntry(entries);
			if (correctEntry == false) {
				System.out.println("Please try again!");
				return;
			}
		} while (correctEntry == false);
		table.newEntry(entries);

	}

	public void newEntry(String[] entries) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		attributes.get(attributeNumber - 1).setEntryField(format.format(date));

		attributes.get(0).setEntryField(String.valueOf(++lines));
		for (int i = 1; i <= entries.length; i++) {
			attributes.get(i).setEntryField(entries[i - 1]);
>>>>>>> sortFunction
		}
	}

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public int getAttributeNumber() {
		return attributeNumber;
	}

<<<<<<< HEAD
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
						attributes.add(new Attribute(name, "string"));
						break;
					case 2:
						attributes.add(new Attribute(name, "char"));
						break;
					case 3:
						attributes.add(new Attribute(name, "int"));
						break;
					case 4:
						attributes.add(new Attribute(name, "double"));
						break;
					case 5:
						attributes.add(new Attribute(name, "date"));
						break;
					case 6:
						attributes.add(new Attribute(name, "obj"));
						break;
				}
			}
=======
	public static Table getTables(int i) {
		return tables.get(i);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLines() {
		return lines;
	}

	/**
	 * This method implements a simple check on the entry that the user has given on
	 * demand, in order to decide if it is an integer between 1 and 6, as the
	 * choices indicate. It uses a custom exception to guide the user into a correct
	 * entry, if needed.
	 *
	 * @param choice
	 *            <code>int</code> containing the user's choice
	 *
	 * @return <code>true</code> if no mistake was found; <code>false</code> if
	 *         there's a wrong input.
	 */

	public static boolean checkInput(int choice, boolean correctEntry) {
		try {
			if (choice < 1 || choice > 6)
				throw new WrongEntryException();
>>>>>>> sortFunction
		} catch (WrongEntryException e) {
			System.out.println(choice + " is not a valid input.");
			correctEntry = false;
		}
		return correctEntry;
	}

	/**
<<<<<<< HEAD
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
=======
	 * This method creates an attribute (column) using a name and an integer which
	 * corresponds to the data type the attribute will hold
	 */

	public void newAttribute(String name, int choice) {
		attributeNumber++;
		switch (choice) {
		case 1:
			attributes.add(attributeNumber - 2, new Attribute(name, "string"));
			break;
		case 2:
			attributes.add(attributeNumber - 2, new Attribute(name, "char"));
			break;
		case 3:
			attributes.add(attributeNumber - 2, new Attribute(name, "int"));
			break;
		case 4:
			attributes.add(attributeNumber - 2, new Attribute(name, "double"));
			break;
		case 5:
			attributes.add(attributeNumber - 2, new Attribute(name, "date"));
			break;
		case 6:
			attributes.add(attributeNumber - 2, new Attribute(name, "obj"));
			break;
		}
	}

	public static void attributeMenu(Table table, String name) throws InputMismatchException {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		int choice = 0;
		do {
			correctEntry = true;
			System.out.println("Your attribute can be of any of the following types:\n" + "1. Text\n"
					+ "2. Single letter\n" + "3. Integer\n" + "4. Decimal\n" + "5. Date\n" + "6. Other (e.g. Image)\n\n"
>>>>>>> sortFunction
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

<<<<<<< HEAD
		} while(correctEntry == false);
=======
		} while (correctEntry == false);
		table.newAttribute(name, choice);
>>>>>>> sortFunction
	}

	public static boolean exists(String name) {
		for (Table table : tables) {
<<<<<<< HEAD
			if (table.getName() == name) {
=======
			if (table.getName().equals(name)) {
>>>>>>> sortFunction
				return true;
			}
		}
		return false;
	}

<<<<<<< HEAD
	public boolean exists(String tableName, String attributeName) {
		for (Table table : tables) {
			if (table.getName() == tableName) {
				for (Attribute attribute : attributes) {
					if (attribute.getName() == attributeName) {
						return true;
					}
=======
	public static boolean exists(String tableName, String name) {
		if (exists(tableName)) {
			int p = position(tableName);
			for (Attribute attribute : tables.get(p).getAttributes()) {
				if (attribute.getName().equals(name)) {
					return true;
>>>>>>> sortFunction
				}
			}
		}
		return false;
	}
<<<<<<< HEAD
}


=======

	public static int maxLength(Attribute att) {
		int max = att.getName().length();
		for (int i = 0; i < att.getArray().size(); i++) {
			if (att.getArray().get(i).length() > max) {
				max = att.getArray().get(i).length();
			}
		}
		return max;
	}

	public static void viewTable(ArrayList<String> tableNames) {
		ArrayList<Integer> columnLength = new ArrayList<Integer>();
		ArrayList<Integer> tablePositions = position(tableNames);
		for (int pos : tablePositions) {
			System.out.println(tables.get(pos).getName() + "\n");
			for (int i = 0; i < tables.get(pos).getAttributes().size(); i++) {
				columnLength.add(maxLength(tables.get(pos).getAttributes().get(i)));
				System.out.printf("%-" + columnLength.get(i) + "s|", tables.get(pos).getAttributes().get(i).getName());
			}
			System.out.println();
			for (int j = 0; j < tables.get(pos).getLines(); j++) {
				for (int i = 0; i < tables.get(pos).getAttributes().size(); i++) {
					System.out.printf("%-" + columnLength.get(i) + "s|",
							tables.get(pos).getAttributes().get(i).getArray().get(j));
				}
				System.out.println();
			}
		}
	}

	public static void viewAttribute(String table, ArrayList<String> attributeNames) {
		ArrayList<Integer> columnLength = new ArrayList<Integer>();
		int tablePosition = position(table);
		ArrayList<Integer> attPositions = position(table, attributeNames);
		attPositions.add(0, 0);
		System.out.println(tables.get(tablePosition).getName() + "\n");
		for (int j = 0; j < attPositions.size(); j++) {
			columnLength.add(maxLength(tables.get(tablePosition).getAttributes().get(attPositions.get(j))));
			System.out.printf("%-" + columnLength.get(j) + "s|",
					tables.get(tablePosition).getAttributes().get(attPositions.get(j)).getName());
		}
		System.out.println();
		for (int i = 0; i < tables.get(tablePosition).getLines(); i++) {
			for (int j = 0; j < attPositions.size(); j++) {
				System.out.printf("%-" + columnLength.get(j) + "s|",
						tables.get(tablePosition).getAttributes().get(attPositions.get(j)).getArray().get(i));
			}
			System.out.println();
		}
	}

	public static ArrayList<Integer> position(String tableName, ArrayList<String> atts) {
		Table table = tables.get(position(tableName));
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (String att : atts) {
			for (int i = 0; i < table.getAttributes().size(); i++) {
				if (att.equals(table.getAttributes().get(i).getName())) {
					positions.add(i);
				}
			}
		}
		return positions;
	}

	public static int position(String tableName) {
		int position = 0;
		for (int i = 0; i < tables.size(); i++) {
			if (tableName.equals(tables.get(i).getName())) {
				position = i;
				continue;
			}
		}
		return position;
	}

	public static ArrayList<Integer> position(ArrayList<String> tableNames) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (String table : tableNames) {
			for (int i = 0; i < tables.size(); i++) {
				if (table.equals(tables.get(i).getName())) {
					positions.add(i);
				}
			}
		}
		return positions;
	}

	public ArrayList<Integer> search(ArrayList<String> attributeNames, ArrayList<String> elements) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		int[] columnIndices = new int[attributeNames.size()]; // Table containing the position of each attribute name
																// given in the table
		try {
			columnIndices = matchSearchAttributes(attributeNames);
		} catch (NotMatchingAttributeException e) {
			System.err.println(e);
			return positions; // If the search fails, an empty ArrayList is returned
		}
		for (int i = 0; i < lines; i++) {
			boolean matchingRow = true;
			int k = 0; // counter for the elements arraylist
			for (int j : columnIndices) {
				if (!(elements.get(k).equals(attributes.get(j).getArray().get(i)))) {
					matchingRow = false; // At least one element of the row does not match with one of the given
											// elements
					break;
				}
				k++;
			}
			if (matchingRow == true) {
				positions.add(i);
			}
		}
		return positions;
	}

	/* Method checking if the attribute names given for search exist in the table */
	public int[] matchSearchAttributes(ArrayList<String> attributeNames) throws NotMatchingAttributeException {
		int[] columnIndices = new int[attributeNames.size()];
		for (int i = 0; i < attributeNames.size(); i++) {
			boolean correctAttribute = false;
			for (int j = 0; j < attributeNumber; j++) {
				if (attributeNames.get(i).equals(attributes.get(j).getName())) {
					correctAttribute = true;
					columnIndices[i] = j; // The name of the attribute given for search was found in column j
					break;
				}
			}
			if (correctAttribute == false) {
				throw new NotMatchingAttributeException(attributeNames.get(i) + " is not an attribute name!");
			}
		}
		return columnIndices;
	}

	@Override
	public String toString() {
		return ("name = " + name + "\n" + "attributeNumber = " + attributeNumber + "\n" + "lines = " + lines + "\n");
	}

	public ArrayList<Attribute> sortTable(Table table, String keyAttribute, int choice) throws ParseException {

		int index = position(table.getName(), new ArrayList<String>(Arrays.asList(keyAttribute))).get(0);
		if ((table.getAttributes().get(index).getType().equals("date"))
				|| (table.getAttributes().get(index).getType().equals("Time of last edit"))) {
			return dateSort(table, index, choice, returnFormater(table, index));
		} else if ((table.getAttributes().get(index).getType().equals("obj")) || (index == 0)) {
			System.out.println("This column contains elements that cannot be sorted");
			return null;
		} else {
			return generalSort(table, index, choice);
		}

	}

	protected ArrayList<Attribute> generalSort(Table table, int index, int order) {
		if (order == 1) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (table.getAttributes().get(index).getArray().get(j - 1)
							.compareTo(table.getAttributes().get(index).getArray().get(j)) > 0)

						for (int k = 1; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		if (order == 2) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++)
				for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
					if (table.getAttributes().get(index).getArray().get(j - 1)
							.compareTo(table.getAttributes().get(index).getArray().get(j)) < 0)
						for (int k = 1; k < table.getAttributeNumber(); k++)
							Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
				}
		}
		return table.getAttributes();

	}

	protected SimpleDateFormat returnFormater(Table table, int index) {
		if (index == table.getAttributeNumber() - 1)
			return new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		else
			return new SimpleDateFormat("dd:MM:yyyy");
	}

	protected ArrayList<Attribute> dateSort(Table table, int index, int order, SimpleDateFormat formatter)
			throws ParseException {
		if (order == 1) {
			for (int i = 0; i < table.getAttributes().get(index).getArray().size(); i++) {
				{
					for (int j = 1; j < table.getAttributes().get(index).getArray().size() - i; j++) {
						if (formatter.parse(table.getAttributes().get(index).getArray().get(j - 1))
								.compareTo(formatter.parse(table.getAttributes().get(index).getArray().get(j))) > 0) {
							for (int k = 1; k < table.getAttributeNumber(); k++)
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
							for (int k = 1; k < table.getAttributeNumber(); k++)
								Collections.swap(table.getAttributes().get(k).getArray(), j, j - 1);
						}
					}
				}
			}
		}
		return table.getAttributes();
	}
}
>>>>>>> sortFunction
