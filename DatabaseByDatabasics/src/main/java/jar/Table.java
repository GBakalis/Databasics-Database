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
	private int lines;
	private static ArrayList<Table> tables = new ArrayList<Table>();
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * A simple constructor that only expects a name to initialize a table
	 *
	 * @param name the name to be used as a title for the table
	 */
	public Table(String name){
		this.name = name;
		attributes.add(new Attribute("#", "int"));
		attributeNumber = 1;
		lines = 0;
		tables.add(this);
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
	public void checkEntryType(String[] entries) throws ParseException, NumberFormatException,
				NotCharacterException {
		for (int i = 1; i < attributeNumber; i++) {
			if (attributes.get(i).getType() == "int" && !entries[i-1].equals("--")) {
				Integer.parseInt(entries[i-1]);
			}
			if (attributes.get(i).getType() == "double" && !entries[i-1].equals("--")) {
				Double.parseDouble(entries[i-1]);
			}
			if (attributes.get(i).getType() == "date" && !entries[i-1].equals("--")) {
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				format.setLenient(false);
				format.parse(entries[i-1]);
			}
			if ((attributes.get(i).getType() == (String) "char") && (
					entries[i-1].length() != 1) && !entries[i-1].equals("--")) {
				throw new NotCharacterException();
			}
		}
	}

	public boolean checkEntry(String[] entries) {
		boolean correctEntry = true;
		try {
			if (entries.length != attributeNumber - 1) {
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
		} 
		return correctEntry;
	}

	/**
	 * This method creates an entry on the user's demand. It asks for the
	 * entry, splits it on commas and holds it inside an array.
	 * Checks whether the input is valid using checkEntry(String[])
	 * and then proceeds to pass the correct input inside the table.
	 */

	public static void newEntryMenu(Table table, String entry) {
		boolean correctEntry;
		String[] entries;
		//Scanner input = new Scanner(System.in);
		do {
		//	System.out.print("Please add a new entry:");
		//	String entry = input.nextLine();
			entries = entry.split(",");
			for (int i = 0; i < entries.length; i++) {
				entries[i] = entries[i].trim();
			}
			correctEntry = table.checkEntry(entries);
			if (correctEntry == false) {
				System.out.println("Please try again!");
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
			attributes.get(i).setEntryField(entries[i-1]);
		}
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

	public static ArrayList<Table> getT() {
		return tables;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getLines() {
		return lines;
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
	public static boolean checkInput(int choice, boolean correctEntry) {
		try{
			if(choice < 1 || choice > 6)
				throw new WrongEntryException();
		} catch (WrongEntryException e) {
			System.out.println(choice + " is not a valid input.");
			correctEntry = false;
		}
		return correctEntry;
	}

	/**
	 * This method creates an attribute (column) using a name and an integer 
	 * which corresponds to the data type the attribute will hold
	 */

	public void newAttribute(String name, int choice) {
		attributeNumber++;
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

	public static void attributeMenu(Table table) throws InputMismatchException {
		boolean correctEntry;
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the name of the new attribute");
		String name = input.nextLine().trim();
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
		table.newAttribute(name, choice);
	}

	public static boolean exists(String name) {
		for (Table table : tables) {
			if (table.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean exists(String tableName, String name) {
		if (exists(tableName)) {
			int p = position(tableName);
			for (Attribute attribute : tables.get(p).getAttributes()) {
				if (attribute.getName().equals(name)) {
					return true;
				}
			}
		}
		return false;
	}

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
				System.out.printf("%-" + columnLength.get(i) + "s|",
					tables.get(pos).getAttributes().get(i).getName());
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
		attPositions.add(0,0);
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
		int [] columnIndices = new int[attributeNames.size()]; //Table containing the position of each attribute name given in the table
		try {
			columnIndices = matchSearchAttributes(attributeNames);
		} catch (NotMatchingAttributeException e) {
			System.err.println(e);
			return positions; //If the search fails, an empty ArrayList is returned
		}
		for (int i = 0; i < lines; i++) {
			boolean matchingRow = true;
			int k = 0; //counter for the elements arraylist
			for (int j : columnIndices) {
					if (!(elements.get(k).equals(attributes.get(j).getArray().get(i)))) {
						matchingRow = false; //At least one element of the row does not match with one of the given elements						
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
	public int [] matchSearchAttributes(ArrayList<String> attributeNames)
			throws NotMatchingAttributeException {
		int [] columnIndices = new int[attributeNames.size()]; 
		for (int i = 0; i < attributeNames.size(); i++) {
			boolean correctAttribute = false;
			for (int j = 0; j < attributeNumber; j++) {
				if (attributeNames.get(i).equals(attributes.get(j).getName())) {
					correctAttribute = true;
					columnIndices[i] = j; //The name of the attribute given for search was found in column j
					break;
				}
			}
			if (correctAttribute == false) {
				throw new NotMatchingAttributeException(attributeNames.get(i)+" is not an attribute name!");
			}
		}
		return columnIndices;
	}

	@Override
	public String toString() {
		return ("name = " + name + "\n"
				+ "attributeNumber = " + attributeNumber + "\n"
				+ "lines = " + lines + "\n");
	}
	
	public void deleteTable(String tableName) {
		int pos=position(tableName);
		for (int i = 0; i <= getT().size(); i++) {
			if (pos == i) {
				tables.remove(i);
				break;
			}
		}
	}
	
	

	public void deleteAttribute(String tableName, String attributeName) {
		int t_pos = position(tableName);
		ArrayList<String> att = new ArrayList<String>();
		att.add(attributeName);
		ArrayList<Integer> p = position(tableName,att);
		int number = p.get(0); 
		tables.get(t_pos).attributes.remove(number);
		
	}

	public void deleteEntry(String tableName, int lineNumber) {
		int t_pos = position(tableName);
		for (int i = 0; i <= lines; i++) {
			for (int j=0; j<= attributes.size(); j++) {
				if (lineNumber == i) {
					tables.get(t_pos).getAttributes().get(j).getArray().remove(lineNumber);
					break;
				}
			}
		}
	}

	public void deleteElement(String tableName, int line_number, String attributeName) {
		int t_pos = position(tableName);
		ArrayList<String> att = new ArrayList<String>();
		att.add(attributeName);
		ArrayList<Integer> p = position(tableName,att);
		int number = p.get(0);
		tables.get(t_pos).getAttributes().get(number).getArray().set(line_number,null);
	}


	public void tempTable(String nameCopy, int copyK, String newName) {
		Table tempTab = new Table(newName);
		for (int i = 1; i < tables.get(copyK).attributeNumber ; i++) {
			int choice = 0;
			if (tables.get(copyK).getAttributes().get(i).getType().equals("string") ) {
				choice = 1;
			} else if (tables.get(copyK).getAttributes().get(i).getType().equals("char ")) {
				choice = 2;
			} else if (tables.get(copyK).getAttributes().get(i).getType().equals("int")) {
				choice = 3;
			} else if (tables.get(copyK).getAttributes().get(i).getType().equals("double")) {
				choice = 4;
			} else if (tables.get(copyK).getAttributes().get(i).getType().equals("date")) {
				choice = 5;
			} else {
				choice = 6;
			}
			String name = tables.get(copyK).getAttributes().get(i).getName();
			tempTab.newAttribute(name, choice);
			for (int j = 0;j < tables.get(copyK).getAttributes().get(i).getArray().size(); j ++) {
				System.out.println(tables.get(copyK).getAttributes().get(i).getArray().get(j));
				tempTab.getAttributes().get(i).getArray().add(tables.get(copyK).getAttributes().get(i).getArray().get(j));
			}
		}
		
	}
		

		public void copyTable(String nameCopy, String namePaste) {
			int copyK = position(nameCopy);
			int pasteK = position(namePaste);
			if (exists(namePaste) && tables.get(copyK).getAttributeNumber() == tables.get(pasteK).getAttributeNumber()) {
				tempTable(nameCopy, copyK, "temp");
				tables.set(pasteK,tables.get(tables.size() - 1));
				deleteTable("temp");
			}else {
				tempTable(nameCopy, copyK, namePaste);
			}

		}
		

		public void copyEntry(String nameCopy, int entryNumCopy, String namePaste, int entryNumPaste) throws IndexOutOfBoundsException  {
			int copyK = position(nameCopy);
			int pasteK = position(namePaste);
			boolean check = true;
			if (entryNumPaste > 0 && entryNumPaste <= tables.get(pasteK).getLines() - 1) {
				if (tables.get(pasteK).getAttributeNumber() == tables.get(copyK).getAttributeNumber()) {
					for( int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
						if (tables.get(pasteK).getAttributes().get(i).getType().equals(tables.get(copyK).getAttributes().get(i).getType())) {
							tables.get(pasteK).getAttributes().get(i).changeField(entryNumPaste - 1,tables.get(copyK).getAttributes().get(i).getArray().get(entryNumCopy - 1));
							Date date = new Date();
							DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
							tables.get(pasteK).getAttributes().get(attributeNumber - 2).setEntryField(format.format(date));
						} else {
							check = false;
							break;
						}
					}
					if (check == false)
						System.out.println("The copy function is not possible");
				} else {
					System.out.println("Different number of attributes");
				}
			}
			
		}

		public void copyAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
			int copyK = position(nameCopy);
			int attNumC = search_attribute(copyK,attNameC);
			int pasteK = position(namePaste);
			if (exists(namePaste,attNameP)) {
				int attNumP = search_attribute(pasteK,attNameP);
				if (tables.get(pasteK).getAttributes().get(attNumP).getType().equals( tables.get(copyK).getAttributes().get(attNumC).getType())) {
					tables.get(pasteK).getAttributes().get(attNumP).setArray(tables.get(copyK).getAttributes().get(attNumC).getArray());
				} else {
					System.out.println("Different type of attributes");
				}
			} else {
				attributeMenu(tables.get(pasteK));
				tables.get(pasteK).getAttributes().get(tables.get(pasteK).attributeNumber - 2).setArray(tables.get(copyK).getAttributes().get(attNumC).getArray());

			}
		}


		public void copyElement(String nameCopy, String attNameC, int lineC, String namePaste, String attNameP, int lineP) throws IndexOutOfBoundsException {
			int copyK = position(nameCopy);
			int attNumC = search_attribute(copyK,attNameC);
			int pasteK = position(namePaste);
			int attNumP = search_attribute(pasteK,attNameP);
			try {
				if (lineC <= tables.get(copyK).getAttributes().get(attNumC).getArray().size() && lineP <= tables.get(pasteK).getAttributes().get(attNumP).getArray().size()) {
					if (tables.get(pasteK).getAttributes().get(attNumP).getType().equals(tables.get(copyK).getAttributes().get(attNumC).getType()) ) {
						tables.get(pasteK).getAttributes().get(attNumP).changeField(lineP - 1,tables.get(copyK).getAttributes().get(attNumC).getArray().get(lineC - 1));
					} else {
						System.out.println("Different type of elements");
					}
				}
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Out of Bounds");
			}
			
		}

		public int search_attribute(int num,String attName) {
			int pos = -1;
			int i = 0;
			boolean check = true;
			while (i< tables.get(num).attributeNumber && check == true) {
				if (attName.equals(tables.get(num).getAttributes().get(i).getName())) {
					pos = i;
					check = false;
				}
				i += 1;
			}
			return pos;
		}



}


