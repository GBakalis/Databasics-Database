package gr.aueb.databasics;
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
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private int attributeNumber;
	private int lines;

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
		CommandLineMenu.getActiveDatabase().setTableNumber(1);
		CommandLineMenu.setActiveTable(this);
		CommandLineMenu.getActiveDatabase().getAllTables().add(this);
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

	/**
	 * This method checks whether a set of values matches with the
	 * length and the types of the table it is called upon. The types check is implemented
	 * via the method {@link #checkEntryType(String[])}
	 * @param entries
	 * 		An array of <code>String</code> elements, each one of the
	 * 		representing an element of the table's new line, excluding the
	 * 		default attributes' values.
	 * @return
	 * 		Returns <code>true</code> if the set of values matches with the
	 * length and the types of the table, <code>false</code> if not.
	 */
	public boolean checkEntry(String[] entries) {
		boolean correctEntry = true;
		try {
			if (entries.length != attributeNumber - 2) {
				correctEntry = false;
				System.err.println("Wrong entry size");
			} else {
				checkEntryType(entries);
			}
		} catch (NumberFormatException e) {
			System.err.println("Wrong entry on an Integer or Decimal column!");
			correctEntry = false;
		} catch (ParseException e) {
			System.err.println("Invalid date format on a date column!");
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

	/**
	 * This method inserts a line of values in the table it is called upon
	 * and initializes the Last Modified column of the line.
	 * @param entries
	 * 		An array of <code>String</code> elements, each one of the
	 * 		representing an element of the table's new line, excluding the
	 * 		default attributes' values.
	 */
	public void newEntry(String[] entries) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
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

	public int searchAttribute(String attName) {
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

	/**
	 * This method presents a set of given lines of the table upon which it is called.
	 * @param entryPositions
	 * 			An arraylist of <code>Integer</code> elements, each one representing the
	 * 			position of a line in the attribute arraylists.
	 */
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

	/**
	 * This method searches for lines which contain a set of elements specified by the user and
	 * returns their positions. The search can be parameterized by one or more attributes.
	 * There is a 1-1 relation between the values and the attributes given for search.
	 * @param attributeNames
	 * 			An arraylist of <code>String</code> elements, containing the names of
	 * 			the attributes which will be searched for the corresponding elements.
	 * @param elements
	 * 			An arraylist of <code>String</code> elements, containing the values that will be
	 * 			searched in the attribute of the corresponding position of the <code>attributeNames</code>
	 * 			arraylist.
	 * @return
	 * 			An arrraylist of <code>Integer</code> elements, containing the positions of the lines
	 * 			that match the search criteria.
	 */
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

	/**
	 * Check if the column can be sorted and if yes call the appropriate method.
	 * @param keyAttribute
	 * @param choice
	 * @return
	 * @throws ParseException
	 */
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

	/**
	 * Sort table according to int, double, String or char type attribute.
	 * @param index
	 * @param order
	 * @return
	 */
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

	/**
	 * Return the appropriate date format (simple date or time stamp).
	 * @param index
	 * @return
	 */
	public SimpleDateFormat returnFormater(int index) {
		if (index == attributeNumber - 1)
			return new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
		else
			return new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * Sort table according to time stamp (last column) or date type attribute.
	 * @param index
	 * @param order
	 * @param formatter
	 * @return
	 * @throws ParseException
	 */
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
	
	/**
	 * This method deletes a whole table specified by the user and
	 * sets the table's content to "null" after delete.
	 */
	public void delete() {
		Database db = CommandLineMenu.getActiveDatabase();
		for (int i = 0; i <= db.getTableNumber(); i++) {
			if (this.equals(db.getTables(i))) {
				db.getAllTables().set(i, null);
				db.getAllTables().remove(i);
				CommandLineMenu.getActiveDatabase().setTableNumber(-1);
				CommandLineMenu.setActiveTable(null);
				break;
			}
		}
	}
        
	/**
	 * This method deletes an attribute specified by the user and
	 * reduces the table's number of attributes after deleting the specified attribute.
	 * The delete is parameterized by the name of the attribute the user wants to delete.
	 * @param att
	 *         An <code>String</code> element, containing the name of
	 *         the attribute which will be deleted.
	 */
	public void deleteAttribute(String att) {
		for (int i = 0; i <= this.getAttributeNumber(); i++) {
			if (this.getAttributes(i).getName().equals(att)) {
				this.getAllAttributes().set(i, null);
				this.getAllAttributes().remove(i);
				this.setAttributeNumber(-1);
				break;
			}
		}
	}
 	
	/**
    	 * This method deletes a whole entry specified by the user and
	 * reduces the table's number of lines after deleting the specified entry.
	 * The delete is parameterized by the number that indicates the position of 
	 * the entry the user wants to delete.
	 * @param linePosition
	 * 		An <code>int</code> element, containing the number that
	 * 		indicates the position of entry which will be deleted.
	 */
	public void deleteEntry(int linePosition) {
		for (int j = 0; j < attributes.size(); j++) {
			this.getAttributes(j).getArray().remove(linePosition);
		}
		for (int i = linePosition; i < this.getLines() - 1 ; i++) {
			String num = String.valueOf(i + 1);
			this.getAttributes(0).changeField(i, num);
		}
		lines--;
	}
	
	/**
	 * This method deletes an element specified by the user and
	 * sets the element's content to "--" after deleting the 
	 * specified element.
	 * The delete is parameterized by the number that indicates the elelement's  
	 * line position and the the element's name of attribute the user wants to delete.
	 * @param linePosition
	 * 			An <code>int</code> element, containing the number that
	 * 			indicates the line of the element which will be deleted.
	 * @param attributeName
	 * 			An <code>String</code> element, containing the name of
	 * 			the element's attribute which will be deleted.
	 * 
	 */
	public void deleteElement(int lineNumber, String attributeName) {
		ArrayList<String> atts = new ArrayList<String>();
		atts.add(attributeName);
		int number = this.attPositions(atts).get(0);
		this.getAttributes(number).getArray().set(lineNumber,"--");
	}

	/**
	 * This method changes data which belong to one or more attributes and in one
	 * specific line using the method {@link changeField} of class {@link Attribute}
	 * and it gives the opportunity of multiple elements change with one run.
	 * @param num
	 * 			An <code>int</code> which contains the line number of the data to be
	 *      changed.
	 * @param attNames
	 * 			An arraylist of <code>String</code> elements, containing the names of
	 * 			the attributes where the data to be changed belong to.
	 * @param newValues
	 *      An arraylist of <code>String</code> elements, containing the new values
	 *      of the data to be changed.
	 * @return
	 * 			An arrraylist of <code>String</code> elements, containing the changed
	 *      values.
	 */
	public ArrayList<String> dataChange(int num, ArrayList<String> attNames, ArrayList<String> newValues) {
		ArrayList<String> changedValues = new ArrayList<String>();
		for (int i = 0; i < attNames.size(); i++) {
			for (int j = 1; j < attributes.size() - 1; j++) {
				if (attributes.get(j).getName().equals(attNames.get(i))) {
					attributes.get(j).changeField(num, newValues.get(i));
					changedValues.add(newValues.get(i));
				}
			}
		}
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("HH:mm:ss, dd/MM/yyyy");
		attributes.get(attributeNumber - 1).changeField(num, format.format(date));
		return changedValues;
	}

	/**
	 * This method saves the contents of a Table in the format of a csv file in a
	 * default location. The default attribute representing the line number
	 * is not saved in the file.
	 * The name and the first line of the file represent the name of the table.
	 * The second line of the file contains the types of the attributes, separated by commas.
	 * The third line of the file contains the names of the attributes, separated by commas.
	 * Each of the following lines contains the elements of a table line, separated by commas.
	 * @param databasePath
	 * 			A <code>String</code> representing the path of the database directory
	 * 			in which the table will be saved
	 */
	public void saveTable(String databasePath) {
		try {
			File file = new File(databasePath + File.separator + name  + ".csv");
			PrintWriter pw = new PrintWriter(file);
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append('\n');
			for (int i = 1; i < attributeNumber - 1; i++) {
				sb.append(attributes.get(i).getType() + ',');
			}
			sb.append(attributes.get(attributeNumber - 1).getType() + '\n');
			for (int i = 1; i < attributeNumber - 1; i++) {
				sb.append(attributes.get(i).getName() + ',');
			}
			sb.append(attributes.get(attributeNumber - 1).getName() + '\n');
			for (int i = 0; i < lines; i++) {
				for (int j = 1; j < attributeNumber - 1; j++) {
					sb.append(attributes.get(j).getArray().get(i) + ",");
				}
				sb.append(attributes.get(attributeNumber - 1).getArray().get(i));
				sb.append('\n');
			}
			pw.write(sb.toString());
			pw.close();
			System.out.println("File saved succesfully");
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * A method that matches the types of an attribute with a specific number
	 * so that a method can make use of the {@link #newAttribute(String, int)}
	 * method.
	 * @param types
	 * 		An array of <code>String</code> elements among "string", "char",
	 * 		"int", "double", "date" and any other type (to be matched with the object
	 * 		case), representing a type in a String format
	 * @return
	 * 		An array of <code>int</code> elements, each one representing a choice
	 * 		of type in compliance with the {@link #newAttribute(String, int)} method.
	 */
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