package gr.aueb.databasics;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * Table is the class that allows the creation and editing of tables inside a
 * database. A Table object is practically a series of Attribute (might also be
 * referred to as "column") objects held inside an ArrayList of type Attribute
 * and encapsulates the basic information needed for this purpose:
 * <ul>
 * <li>The table's name
 * <li>The number of attributes that the table holds
 * <li>The Attribute objects which contain the content the user inputs
 * </ul>
 * <p>
 * The class is designed in such manner that it will support methods inside of
 * it and other classes externally to execute the usual functions that a
 * database has, such as:
 * <ul>
 * <li>View Table's content
 * <li>View columns' content
 * <li>Input data into a table
 * <li>Delete a table
 * <li>Delete an entry
 * <li>Erase a field
 * <li>Change a field
 * <li>Change an entry
 * <li>More
 * </ul>
 * <p>
 * An important point to consider is that the methods inside the class are
 * written in such a manner that they require the data input given in a strict
 * way by the user.
 *
 * @author George Bakalis
 * @author Andreas Vlachos
 * @author Artemis Doumeni
 * @author Evi Vratsanou
 * @author Kostas Kyriakidis
 * @author Martha Pontika
 * 
 * @version 1.0
 */

public class Table {

	private String name;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	private int attributeNumber;
	private int lines;

	/**
	 * A simple constructor that only expects a name to initialize a table
	 *
	 * @param name	The name to be used as a title for the table
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
	 * This method implements a check on each element of an entry line
	 * by utilizing method {@link Attribute#checkType(String)}
	 * @param entries
	 * 			An array of <code>String</code> elements that represents
	 * 			an entry line,
	 * @return
	 * 			Returns <code>true</code> if all elements match the 
	 * 			corresponding types and <code>false</code> if at least 
	 * 			one does not.
	 */
	public boolean checkEntryType(String[] entries) {
		boolean correctEntryType = true;
		for (int i = 1; i < attributeNumber - 1; i++) {
			if (!attributes.get(i).checkType(entries[i - 1])) {
				correctEntryType = false;
			}
		}
		return correctEntryType;
	}

	/**
	 * This method checks whether a set of values matches with the
	 * length and the types of the table it is called upon. The types check is implemented
	 * via the method <code>checkEntryType</code>
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
		if (entries.length != attributeNumber - 2) {
			correctEntry = false;
			System.err.println("Wrong entry size");
		} else if (!checkEntryType(entries)) {
			correctEntry = false;
		}
		return correctEntry;
	}

	/**
	 * This method creates an entry. It asks for the entry, splits it on commas and
	 * holds it inside an array. Checks whether the input is valid using
	 * {@link #checkEntry(String[])} and then proceeds to pass the correct input inside the
	 * table.
	 * 
	 * @param table
	 * 		The Table object in which the new entry will be created
	 * @param entry
	 * 		The String that contains the fields, separated by commas
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
		DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
		attributes.get(attributeNumber - 1).setEntryField(format.format(date));
		attributes.get(0).setEntryField(String.valueOf(++lines));
		for (int i = 1; i <= entries.length; i++) {
			attributes.get(i).setEntryField(entries[i - 1]);
		}
	}
	
	/**
	  * This method returns the value of the field attributes.
	  * @return
	  *	The value of the field attributes.
	  */
	public ArrayList<Attribute> getAllAttributes() {
		return attributes;
	}
	
	/**
	 * This method expects an input and returns the value of the field attributes.
         * @param i
	 *	The value used to select an attribute's element.
	 * @return
	 *	The value of the field attributes.
	 */
	public Attribute getAttributes(int i) {
		return attributes.get(i);
	}
	
	/**
	  * This method returns the value of the field attributeNumber.
	  * @return
	  *	The value of the field attributeNumber.
	  */
	public int getAttributeNumber() {
		return attributeNumber;
	}
	
	/**
	 * This method expects an input and uses it to set field attributeNumber.
	 * @param margin
	 *  	The value used to set field attributeNumber.
	 */
	public void setAttributeNumber(int margin) {
		attributeNumber += margin;
	}
	
	/**
	  * This method returns the value of the field name.
	  * @return
	  *	The value of the field name.
	  */
	public String getName() {
		return name;
	}
	
	/**
	 * This method expects an input and uses it to set field name.
	 * @param name
	 *  	The value used to set field name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * This method returns the value of the field lines.
	 * @return
	 * 	The value of the field lines.
	 */
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
			if (choice < 1 || choice > 5)
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
	 * 
	 * @param name
	 * 		The name of the Attribute object to be created
	 * @param choice
	 * 		An int which represents the data type of the Attribute to be created
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
		
		}
	/*	if (getLines()>0) {
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
			for (int i = 0 ; i < this.getLines();i++)
				this.getAttributes(attributeNumber-1).changeField(i, format.format(date));  */
		
		
	}
	
	/**
	 * This method finds the position of an attribute specified by the user in a table.
	 * @param attName
	 * 				An <code>String</code> element, containing the name of the attribute
	 * 				which position must be found.
	 * @return
	 * 			An <code>int</code> element, containing the position of this attribute
	 * 			in the table
	 */
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
	 * 
	 * @param entryPositions
	 * 			An <code>ArrayList</code> of <code>Integer</code> elements, each one
	 * 			representing the	position of a line in the attribute arraylists.
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

	/**
	 * When <code>view()</code> is called on a Table object, it views the table
	 * in a structured manner
	 */
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

	/**
	 * When it is called on a Table object, it views the specified attributes of the table
	 * in a structured manner
	 * 
	 * @param attributeNames	Contains the names of the attributes to be viewed
	 */
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

	/**
	 * Gives the positions of the specified attributes inside a table
	 * 
	 * @param atts	Contains the names of the attributes
	 * @return		Returns an <code>ArrayList</code> of integers corresponding to the
	 * 				attributes' positions
	 */
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
	 * 
	 * @param keyAttribute		A String holding the name of the Attribute to be used 
	 * 							to sort the table
	 * @param choice			1 represents ascending order, -1 represents descending 
	 * 							order
	 * @return					Returns an ArrayList of Attribute objects. It is the 
	 * 							sorted table.
	 * @throws ParseException	if parsing fails
	 */
	public ArrayList<Attribute> sortTable(String keyAttribute, int choice) throws ParseException {
		int index = this.attPositions(new ArrayList<String>(Arrays.asList(keyAttribute))).get(0);
		if ((getAttributes(index).getType().equals("date"))
				|| (getAttributes(index).getType().equals("Time of last edit"))) {
			return dateSort(index, choice, returnFormatter(index));
		} else if ((index == 0)) {
			System.out.println("This column contains elements that cannot be sorted");
			return null;
		} else {
			return generalSort(index, choice);
		}
	}

	/**
	 * Sort table according to int, double, String or char type attribute.
	 * 
	 * @param index	Shows position of Attribute
	 * @param order	1 represents ascending and -1 descending order
	 * @return		Returns an ArrayList of Attribute objects. It is the
	 * 				sorted table
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
	 * 
	 * @param index	Checks if the attribute refers to the Last Modified column
	 * @return		Returns the date format
	 */
	public SimpleDateFormat returnFormatter(int index) {
		if (index == attributeNumber - 1)
			return new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
		else
			return new SimpleDateFormat("dd/MM/yyyy");
	}

	/**
	 * Sort table according to time stamp (last column) or date type attribute.
	 * 
	 * @param index				Shows position of Attribute
	 * @param order				+1 represents ascending, -1 represents descending order of sort
	 * @param formatter			Carries the date format
	 * @return					Returns the Attributes sorted
	 * @throws ParseException 	if parsing fails
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
		boolean flag = false;
		Date date = new Date();
				DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");	
		for (int i = 0; i <= this.getAttributeNumber(); i++) {
			if (this.getAttributes(i).getName().equals(att)) {
				this.getAllAttributes().set(i, null);
				this.getAllAttributes().remove(i);
				this.setAttributeNumber(-1);
				
				flag = true;
				break;
			}
		}
		if (flag = true)
			for (int i = 0 ; i < this.getLines();i++)
				this.getAttributes(attributeNumber-1).changeField(i, format.format(date));
	}
 	
	/**
	 * This method deletes a whole entry specified by the user and
	 * reduces the table's number of lines after deleting the specified entry.
	 * The delete is parameterized by the number that indicates the position of 
	 * the entry the user wants to delete.
	 * 
	 * @param linePosition	Contains the number that indicates the position of 
	 * 						entry which will be deleted.
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
	 * 
	 * @param lineNumber	Contains the number that indicates the line of the element 
	 * 						which will be deleted.
	 * @param attributeName Contains the name of the element's attribute which will
	 * 						be deleted.
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
	 * specific line using the method {@link Attribute#changeField(int, String)}
	 * and it gives the opportunity of multiple elements change with one run.
	 * @param num
	 * 			An <code>int</code> which contains the line number of the data to be
	 *      	changed.
	 * @param attNames
	 * 			An arraylist of <code>String</code> elements, containing the names of
	 * 			the attributes where the data to be changed belong to.
	 * @param newValues
	 *      	An arraylist of <code>String</code> elements, containing the new values
	 *      	of the data to be changed.
	 * @return
	 * 			An arrraylist of <code>String</code> elements, containing the changed
	 *      	values.
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
		DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
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


}
