package jar;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.InputMismatchException;

/**
 * Table is the class that allows the creation and editing of tables inside a
 * database. A Table object is practically a series of Attribute (might also be
 * referred to as "column") objects held inside an ArrayList of type Attribute
 * and encapsulates the basic information needed for this purpose:
 * <ul>
 * <li>The table's name
 * <li>The number of attributes that the table holds
 * <li>The Attribute objects which contain the content the user inputs
 * <li>An ArrayList of the Tables constructed by the user.
 * </ul>
 * <p>
 * The class is designed in such manner that it will support methods inside of
 * it and other classes externally to execute the usual functions that a
 * database has, such as:
 * <ul>
 * <li>View Table's content
 * <li>View a column's content
 * <li>Input data into a table
 * <li>Copy an entry to another position of the same or another table
 * <li>Move an entry to another position of the same or another table
 * <li>Copy a single field to another position of the same or another table
 * <li>Move a single field to another position of the same or another table
 * <li>Delete an entry
 * <li>Delete a table
 * <li>Erase a field
 * <li>Substitute a field
 * <li>Substitute an entry
 * <li>More
 * </ul>
 * <p>
 * An important point to consider is that the methods inside the class are
 * written in such a manner that they require the data input given in a strict
 * way by the user.
 *
 */

public class Table {

	private String name;
	private int attributeNumber;
	private int lines;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

	/**
	 * A simple constructor that only expects a name to initialize a table
	 *
	 * @param name
	 *            the name to be used as a title for the table
	 */

	public Table(String name) {
		this.name = name;
		attributes.add(new Attribute("#", "int"));
		attributes.add(new Attribute("Last Modified", "date"));
		attributeNumber = 2;
		lines = 0;
	}
	
	public boolean exists(String name) {
		for (Attribute attribute : getAllAttributes()) {
			if (attribute.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
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
		for (int i = 1; i < attributeNumber - 1; i++) {
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
		}
		return correctEntry;
	}

	/**
	 * This method creates an entry. It asks for the entry, splits it on commas and
	 * holds it inside an array. Checks whether the input is valid using
	 * checkEntry(String[]) and then proceeds to pass the correct input inside the
	 * table.
	 */

	public static void newEntryMenu(Table table, String entry) {
		boolean correctEntry;
		String[] entries;
		// Scanner input = new Scanner(System.in);
		do {
			// System.out.print("Please add a new entry:");
			// String entry = input.nextLine();
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
		}
	}

	public ArrayList<Attribute> getAllAttributes() {
		return attributes;
	}
	
	public Attribute getAttributes(int i) {
		return attributes.get(i);
	}

	public int getAttributeNumber() {
		return attributeNumber;
	}
	
	public void setAttributeNumber(int margin) {
		attributeNumber += margin;
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
	 * @param correctEntry
	 *            a <code>boolean</code> initialized as <code>true</code>, prone to
	 *            switching to <code>false</code> if there's a wrong input
	 *
	 * @return <code>true</code> if no mistake was found; <code>false</code> if
	 *         there's a wrong input.
	 */

	public static boolean checkInput(int choice, boolean correctEntry) {
		try {
			if (choice < 1 || choice > 6)
				throw new WrongEntryException();
		} catch (WrongEntryException e) {
			System.out.println(choice + " is not a valid input.");
			correctEntry = false;
		}
		return correctEntry;
	}

	/**
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

	public void copyExistingEntry(String nameCopy, int entryNumCopy, String namePaste, int entryNumPaste) throws IndexOutOfBoundsException  {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		boolean check = true;
		if (entryNumPaste > 0 && entryNumPaste <= tables.get(pasteK).getLines() - 1) {
			if (tables.get(pasteK).getAttributeNumber() == tables.get(copyK).getAttributeNumber()) {
				for (int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
					if (tables.get(pasteK).getAttributes(i).getType().equals(tables.get(copyK).getAttributes(i).getType())) {
						tables.get(pasteK).getAttributes(i).changeField(entryNumPaste - 1,tables.get(copyK).getAttributes(i).getArray().get(entryNumCopy - 1));
						Date date = new Date();
						DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
						tables.get(pasteK).getAttributes(attributeNumber - 1).changeField(entryNumPaste - 1,format.format(date));//332&335
					} else {
						check = false;
						break;
					}
				}
				if (check == false) {
					System.out.println("The copy function is not possible");
				} else {
					String[] entries = new String[tables.get(copyK).attributeNumber - 2];
					for (int i = 0; i < entries.length; i++) {
						entries[i] = tables.getAttributes(es().get(i + 1).getArray().get(entryNumCopy - 1);
					}
					tables.get(pasteK).newEntry(entries);
				}

			} else {
				System.out.println("Different number of attributes");
			}

		}
	}

	public void copyNewEntry(String nameCopy, int entryNumCopy, String namePaste) {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		boolean check = true;
		if (tables.get(pasteK).getAttributeNumber() == tables.get(copyK).getAttributeNumber()) {
			for( int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
				if (!(tables.get(pasteK).getAttributes(i).getType().equals(tables.get(copyK).getAttributes(i).getType()))) {
				check = false;
				break;
				}
			}
			if (check == false) {
				System.out.println("The copy function is not possible");
			} else {
				String[] entries = new String[tables.get(copyK).attributeNumber - 2];
				for (int i = 0; i < entries.length; i++) {
					entries[i] = tables.get(copyK).getAttributes(i + 1).getArray().get(entryNumCopy - 1);
				}
				tables.get(pasteK).newEntry(entries);
			}

		} else {
			System.out.println("Different number of attributes");
		}
	}

	public void copyAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
			int copyK = position(nameCopy);
			int attNumC = searchAttribute(copyK,attNameC);
			int pasteK = position(namePaste);
			if (this.exists(attNameP)) {
				int attNumP = searchAttribute(pasteK,attNameP);
				if (tables.get(pasteK).getAttributes(attNumP).getType().equals( tables.get(copyK).getAttributes(attNumC).getType())) {
					tables.get(pasteK).getAttributes(attNumP).setArray(tables.get(copyK).getAttributes(attNumC).getArray());
				} else {
					System.out.println("Different type of attributes");
				}
			} else {
				int choice = findChoice(copyK,attNumC);
				tables.get(pasteK).newAttribute(attNameP, choice);
				tables.get(pasteK).getAttributes(tables.get(pasteK).attributeNumber - 2).setArray(tables.get(copyK).getAttributes(attNumC).getArray());
			}
		}

	public int findChoice(int copyK,int attNumC) {
		int choice = 0;
		if (tables.get(copyK).getAttributes(attNumC).getType().equals("string") ) {
			choice = 1;
		} else if (tables.get(copyK).getAttributes(attNumC).getType().equals("char")) {
			choice = 2;
		} else if (tables.get(copyK).getAttributes(attNumC).getType().equals("int")) {
			choice = 3;
		} else if (tables.get(copyK).getAttributes(attNumC).getType().equals("double")) {
			choice = 4;
		} else if (tables.get(copyK).getAttributes(attNumC).getType().equals("date")) {
			choice = 5;
		} else {
			choice = 6;
		}
		return choice;
	}

	public void copyElement(String nameCopy, String attNameC, int lineC, String namePaste, String attNameP, int lineP) throws IndexOutOfBoundsException {
		int copyK = position(nameCopy);
		int attNumC = searchAttribute(copyK,attNameC);
		int pasteK = position(namePaste);
		int attNumP = searchAttribute(pasteK,attNameP);
		try {
			if (lineC <= tables.get(copyK).getAttributes(attNumC).getArray().size() && lineP <= tables.get(pasteK).getAttributes(attNumP).getArray().size()) {
				if (tables.get(pasteK).getAttributes(attNumP).getType().equals(tables.get(copyK).getAttributes(attNumC).getType()) ) {
					tables.get(pasteK).getAttributes(attNumP).changeField(lineP - 1,tables.get(copyK).getAttributes(attNumC).getArray().get(lineC - 1));
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
					tables.get(pasteK).getAttributes(attributeNumber - 1).changeField(lineP - 1,format.format(date));
				} else {
					System.out.println("Different type of elements");
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
		}
	}

	public int searchAttribute(int num, String attName) {
		int pos = -1;
		int i = 0;
		boolean check = true;
		while (i < this.getAttributeNumber() && check == true) {
			if (attName.equals(this.getAttributes(i).getName())) {
				pos = i;
				check = false;
			}
			i += 1;
		}
		return pos;
	}

	public void viewLines(ArrayList<Integer> entryPositions) {
		ArrayList<Integer> columnLength = new ArrayList<Integer>();
		for (int i = 0; i < attributeNumber; i++) {
			columnLength.add(getAttributes(i).maxLength());
			System.out.printf("%-" + columnLength.get(i) + "s|", attributes.get(i).getName());
		}
		System.out.println();
		for (int j : entryPositions) {
			for (int i = 0; i < attributeNumber; i++) {
				System.out.printf("%-" + columnLength.get(i) + "s|", attributes.get(i).getArray().get(j));
			}
			System.out.println();
		}
		System.out.println();
	}

	public void view() {
		ArrayList<Integer> columnLength = new ArrayList<Integer>();
		System.out.println(this.getName() + "\n");
		for (int i = 0; i < this.getAttributeNumber(); i++) {
			columnLength.add(getAttributes(i).maxLength());
			System.out.printf("%-" + columnLength.get(i) + "s|", this.getAttributes(i).getName());
		}
		System.out.println();
		for (int j = 0; j < this.getLines(); j++) {
			for (int i = 0; i < this.getAttributeNumber(); i++) {
				System.out.printf("%-" + columnLength.get(i) + "s|",
						this.getAttributes(i).getArray().get(j));
			}
			System.out.println();
		}
		System.out.println();
	}

	public void viewAttribute(ArrayList<String> attributeNames) {
		ArrayList<Integer> columnLength = new ArrayList<Integer>();
		ArrayList<Integer> attPositions = this.attPositions(attributeNames);
		attPositions.add(0, 0);
		System.out.println(this.getName() + "\n");
		for (int j = 0; j < attPositions.size(); j++) {
			columnLength.add(this.getAttributes(attPositions.get(j)).maxLength());
			System.out.printf("%-" + columnLength.get(j) + "s|",
					this.getAttributes(attPositions.get(j)).getName());
		}
		System.out.println();
		for (int i = 0; i < this.getLines(); i++) {
			for (int j = 0; j < attPositions.size(); j++) {
				System.out.printf("%-" + columnLength.get(j) + "s|",
						this.getAttributes(attPositions.get(j)).getArray().get(i));
			}
			System.out.println();
		}
	}

	public ArrayList<Integer> attPositions(ArrayList<String> atts) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (String att : atts) {
			for (int i = 0; i < this.getAttributeNumber(); i++) {
				if (att.equals(this.getAttributes(i).getName())) {
					positions.add(i);
				}
			}
		}
		return positions;
	}

	public ArrayList<Integer> search(ArrayList<String> attributeNames, ArrayList<String> elements) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		ArrayList<Integer> columnIndices = new ArrayList<Integer>();
		columnIndices = attPositions(attributeNames);
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

	public ArrayList<Attribute> sortTable(String keyAttribute, int choice) throws ParseException {
		int index = this.attPositions(new ArrayList<String>(Arrays.asList(keyAttribute))).get(0);
		if ((getAttributes(index).getType().equals("date"))
				|| (getAttributes(index).getType().equals("Time of last edit"))) {
			return dateSort(index, choice, returnFormater(index));
		} else if (getAttributes(index).getType().equals("obj") || (index == 0)) {
			System.out.println("This column contains elements that cannot be sorted");
			return null;
		} else {
			return generalSort(index, choice);
		}
	}

	// sorts table according to int, float, String or char type attribute//
	protected ArrayList<Attribute> generalSort(int index, int order) {
		for (int i = 0; i < getAttributes(index).getArray().size(); i++)
			for (int j = 1; j < getAttributes(index).getArray().size() - i; j++) {
				if (order * (getAttributes(index).getArray().get(j - 1))
						.compareTo(getAttributes(index).getArray().get(j)) > 0)

					for (int k = 1; k < getAttributeNumber(); k++)
						Collections.swap(getAttributes(k).getArray(), j, j - 1);

			}
		return getAllAttributes();
	}

	protected SimpleDateFormat returnFormater(int index) {
		if (index == attributeNumber - 1)
			return new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		else
			return new SimpleDateFormat("dd/MM/yyyy");
	}

	// sort table according to timeStamp or type date attribute//
	protected ArrayList<Attribute> dateSort(int index, int order, SimpleDateFormat formatter) throws ParseException {
		for (int i = 0; i < getAttributes(index).getArray().size(); i++) {
			for (int j = 1; j < getAttributes(index).getArray().size() - i; j++) {
				if (order * (formatter.parse(getAttributes(index).getArray().get(j - 1))
						.compareTo(formatter.parse(getAttributes(index).getArray().get(j)))) > 0) {
					for (int k = 1; k < getAttributeNumber(); k++)
						Collections.swap(getAttributes(k).getArray(), j, j - 1);
				}
			}

		}
		return getAllAttributes();
	}

	public void delete() {
		Database db = CommandLineMenu.getActiveDatabase();
		for (int i = 0; i <= db.getTableNumber(); i++) {
			if (this.equals(db.getTables(i))) {
				db.getAllTables().set(i, null);
				db.getAllTables().remove(i);
				db.setTableNumber(-1);
				break;
			}
		}
	}

	public void deleteEntry(int linePosition) {
		for (int j = 0; j < attributes.size(); j++) {
			this.getAttributes(j).getArray().remove(linePosition);
		}
		for (int i = 0; i < this.getLines() - 1 ; i++) {
			String num = String.valueOf(i + 1);
			this.getAttributes(0).changeField(i, num);
		}
		lines--;
	}

	public void deleteElement(int line_number, String attributeName) {
		ArrayList<String> atts = new ArrayList<String>();
		atts.add(attributeName);
		int number = this.attPositions(atts).get(0);
		this.getAttributes(number).getArray().set(line_number,"--");
	}

	public ArrayList<String> dataChange(int num, ArrayList<String> attrNames, ArrayList<String> newValues) {
		ArrayList<String> changedValues = new ArrayList<String>();
		for (int i = 0; i < attrNames.size(); i++) {
			for (int j = 1; j < attributes.size() - 1; j++) {
				if (attributes.get(j).getName().equals(attrNames.get(i))) {
					attributes.get(j).changeField(num, newValues.get(i));
					changedValues.add(newValues.get(i));
				}
			}
		}
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
		attributes.get(attributeNumber - 1).changeField(num, format.format(date));
		return changedValues;
	}

	public void saveTable() {
		try {
			File file = new File(name  + ".csv");
			PrintWriter pw = new PrintWriter(file);
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append('\n');
			for (int i = 1; i < attributeNumber - 2; i++) {
				sb.append(attributes.get(i).getType() + ',');
			}
			sb.append(attributes.get(attributeNumber - 2).getType() + '\n');
			for (int i = 1; i < attributeNumber - 2; i++) {
				sb.append(attributes.get(i).getName() + ',');
			}
			sb.append(attributes.get(attributeNumber - 2).getName() + '\n');
			for (int i = 0; i < lines; i++) {
				for (int j = 1; j < attributeNumber - 2; j++) {
					sb.append(attributes.get(j).getArray().get(i) + ",");
				}
				sb.append(attributes.get(attributeNumber - 2).getArray().get(i));
				sb.append('\n');
			}
			pw.write(sb.toString());
			pw.close();
			System.out.println("File saved succesfully");
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	
	 public static void importTable(BufferedReader br) {
		 String line;
		 Table table = null;
		 try {
			 String tableName = br.readLine();
			 table = new Table(tableName);
			 int[] types = convertTypes(br.readLine().split(","));
			 String[] names = br.readLine().split(",");
			 assert (types.length == names.length);
			 for (int i = 0 ; i < types.length; i++) {
				 table.newAttribute(names[i], types[i]);
			 }
			 while ((line = br.readLine()) != null) {
				 String[] entries = line.split(",");
				 table.newEntry(entries);
			 }
		 } catch (IOException e) {
			 e.printStackTrace();
			 table.delete();
		 }
		 System.out.println("Table succesfully imported!");
	 }
	
	 public static int[] convertTypes(String[] types) {
		 int[] typeNums = new int[types.length];
		 for (int i = 0 ; i < types.length; i++) {
			 if (types[i].equals("string")) {
				 typeNums[i] = 1;
			 } else if (types[i].equals("char")) {
				 typeNums[i] = 2;
			 } else if (types[i].equals("int")) {
				 typeNums[i] = 3;
			 } else if (types[i].equals("double")) {
				 typeNums[i] = 4;
			 } else if (types[i].equals("date")) {
				 typeNums[i] = 5;
			 } else {
				 typeNums[i] = 6;
			 }
		 }
		 return typeNums;
	 }
}