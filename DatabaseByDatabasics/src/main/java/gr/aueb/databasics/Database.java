package gr.aueb.databasics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

/**
 * Database is the class that allows the creation and editing of databases.
 * A Database object is practically a series of Table objects held inside an 
 * ArrayList of type Table and encapsulates the basic information needed for 
 * this purpose:
 * <ul>
 * <li>The database's name
 * <li>The number of tables that the database holds
 * <li>The Table objects which contain the content the user inputs
 * </ul>
 * <p>
 * The class is designed in such manner that it will support methods inside of
 * it and other classes externally to execute the usual functions that a
 * database has, such as:
 * <ul>
 * <li>Delete a database
 * <li>Save a database
 * <li>Import a table
 * <li>Copy an entry to another position of the same or another table
 * <li>Cut an entry to another position of the same or another table
 * <li>Copy a single field to another position of the same or another table
 * <li>Cut a single field to another position of the same or another table
 * <li>More
 * </ul>
 * <p>
 * 
 * @author George Bakalis
 * @author Andreas Vlachos
 * @author Evi Vratsanou
 * 
 * @version 1.0
 */
public class Database {

	private String name;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private int tableNumber;
	private boolean saved;

	/**
	 * A simple constructor that only expects a name to initialize a database
	 *
	 * @param name	The name to be used as a title for the database
	 */
	public Database(String name) {
		this.name = name;
		DatabaseUniverse.getAllDatabases().add(this);
		DatabaseUniverse.setDatabaseNumber(1);
		CommandLineMenu.setActiveDatabase(this);
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int margin) {
		tableNumber += margin;
	}

	public Table getTables(int i) {
		return tables.get(i);
	}

	public ArrayList<Table> getAllTables() {
		return tables;
	}

	public boolean exists(String name) {
		for (Table table : getAllTables()) {
			if (table.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method copies a whole table to a new table that is created.
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the name of the table the user wants to copy. 
	 * @param copyK
	 * 					An <code>int</code> element which contains the position of 
	 * 					the table to be copied in the arrayList that contains all tables in this
	 * 					specific database.
	 * @param newName
	 * 					An <code>String</code> element, containing the name of the table that
	 * 					will be created.
	 */
	public void tempTable(String nameCopy, int copyK, String newName) {
		Table tempTab = new Table(newName);
		for (int i = 1; i < tables.get(copyK).getAttributeNumber() - 1; i++) {
			int choice = 0;
			if (tables.get(copyK).getAttributes(i).getType().equals("string") ) {
				choice = 1;
			} else if (tables.get(copyK).getAttributes(i).getType().equals("char")) {
				choice = 2;
			} else if (tables.get(copyK).getAttributes(i).getType().equals("int")) {
				choice = 3;
			} else if (tables.get(copyK).getAttributes(i).getType().equals("double")) {
				choice = 4;
			} else if (tables.get(copyK).getAttributes(i).getType().equals("date")) {
				choice = 5;
			} 
			String name = tables.get(copyK).getAttributes(i).getName();
			tempTab.newAttribute(name, choice);
			for (int j = 0; j < tables.get(copyK).getLines(); j++) {
				String temp = tables.get(copyK).getAttributes(i).getArray().get(j);
				tempTab.getAttributes(i).getArray().add(temp);
			}
		}
		String[] entries = new String[tables.get(copyK).getAttributeNumber() - 2];
		for (int j = 0; j < tables.get(copyK).getLines(); j++) {
			for (int i = 0; i < entries.length; i++) {
				entries[i] = tables.get(copyK).getAttributes(i + 1)
							.getArray().get(j);
			}
			tempTab.newEntry(entries);
		}
	}

	/**
	 * This method copies a table to an other, either this table exists or not,
	 * using the method {@link tempTable} of class {@link Database} in order to the 
	 * copied elements of the table not to be affected by changes in the original table.
	 * If the table the user wants to paste the original table to does not exists, a new table
	 * is created, named by the name the user gave.
	 * 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the name of the table the user wants to copy. 
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of
	 * 					the table the user wants to paste the original table. If
	 * 					this table does not exist in the database, this is the name
	 * 					of the table which will be created.
	 */
	public void copyTable(String nameCopy, String namePaste) {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		if (exists(namePaste) && tables.get(copyK).getAttributeNumber()
				== tables.get(pasteK).getAttributeNumber()) {
			tempTable(nameCopy, copyK, "temp");
			tables.set(pasteK, tables.get(tables.size() - 1));
			tables.remove(tables.size() - 1);
		} else {
			tempTable(nameCopy, copyK, namePaste);
		}
	}

	/**
	 * Gives the position of a table
	 * 
	 * @param tableName	Contains the name of the table
	 * @return			Returns the table's position in 
	 * 					<code>ArrayList&lt;Table&gt; tables</code>
	 */
	public int position(String tableName) {
		int position = 0;
		for (int i = 0; i < this.getTableNumber(); i++) {
			if (tableName.equals(this.getTables(i).getName())) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * Gives the positions of the specified tables inside a database
	 * 
	 * @param tableNames	Contains the names of the tables
	 * @return				Returns an <code>ArrayList</code> of integers corresponding to the
	 * 						tables' positions
	 */
	public ArrayList<Integer> position(ArrayList<String> tableNames) {
		ArrayList<Integer> positions = new ArrayList<Integer>();
		for (String table : tableNames) {
			for (int i = 0; i < this.getTableNumber(); i++) {
				if (table.equals(this.getTables(i).getName())) {
					positions.add(i);
				}
			}
		}
		return positions;
	}

	/**
	 * This method copies a whole entry of a table, specified by the user, replacing another entry of a table,
	 * specified by the user. 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table the user wants to copy. 
	 * @param entryNumCopy
	 * 					An <code>int</code> element, containing the number that
	 * 					indicates the position of the entry which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table the user wants to paste. 
	 * @param entryNumPaste
	 * 					An <code>int</code> element, containing the number that
	 * 					indicates the position of the entry where the other entry
	 * 					will be pasted. 
	 */
	public void copyExistingEntry(String nameCopy, int entryNumCopy, String namePaste, int entryNumPaste) {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		boolean check = true;
		if (entryNumPaste >= 0 && entryNumPaste <= tables.get(pasteK).getLines() - 1) {
			if (tables.get(pasteK).getAttributeNumber()
					== tables.get(copyK).getAttributeNumber()) {
				for (int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
					if (tables.get(pasteK).getAttributes(i).getType()
							.equals(tables.get(copyK).getAttributes(i).getType())) {
						tables.get(pasteK).getAttributes(i)
							.changeField(entryNumPaste, tables.get(copyK).getAttributes(i).getArray().get(entryNumCopy));
						Date date = new Date();
						DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
						tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1)
							.changeField(entryNumPaste, format.format(date));
					} else {
						check = false;
						break;
					}
				}
				if (check == false) {
					System.out.println("The copy function is not possible");
				}
			} else {
				System.out.println("Different number of attributes");
			}

		}
	}

	/**
	 * This method creates a new entry of a table specified by the user by copying
	 * another entry of a table.
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the entry the user wants to copy. 
	 * @param entryNumCopy
	 * 					An <code>int</code> element, containing the position of
	 * 					the entry which will be copied
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the entry. 
	 */
	public void copyNewEntry(String nameCopy, int entryNumCopy, String namePaste) {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		boolean check = true;
		if (tables.get(pasteK).getAttributeNumber() 
				== tables.get(copyK).getAttributeNumber()) {
			for( int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
				if (!(tables.get(pasteK).getAttributes(i).getType()
						.equals(tables.get(copyK).getAttributes(i).getType()))) {
				check = false;
				break;
				}
			}
			if (check == false) {
				System.out.println("The copy function is not possible");
			} else {
				String[] entries = new String[tables.get(copyK)
				                              .getAttributeNumber() - 2];
				for (int i = 0; i < entries.length; i++) {
					entries[i] = tables.get(copyK).getAttributes(i + 1)
								.getArray().get(entryNumCopy);
				}
				tables.get(pasteK).newEntry(entries);
			}
		} else {
			System.out.println("Different number of attributes");
		}
	}

	/**
	 * This method creates a new attribute in a table specified by the user, using the method {@link findChoice}
	 * by copying an other attribute of a table.
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the attribute the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code> element containing the name of the 
	 * 					attribute which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the attribute. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of
	 * 					the attribute which will be created.
	 */

	public void copyNewAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
		int copyK = position(nameCopy);
		int attNumC = tables.get(copyK).searchAttribute(attNameC);
		int pasteK = position(namePaste);
		int choice = findChoice(copyK,attNumC);
		tables.get(pasteK).newAttribute(attNameP, choice);
		ArrayList tempArray = new ArrayList();
		for (int i = 0; i < tables.get(copyK).getLines(); i ++) {
			tempArray.add(tables.get(copyK).getAttributes(attNumC).getArray().get(i));
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
			tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1)
				.changeField(i, format.format(date));
		}
		tables.get(pasteK).getAttributes(tables.get(pasteK)
				.getAttributeNumber() - 2).setArray(tempArray);
	}

	/**
	 * This method copies an attribute of a table specified by the user to
	 * an other attribute of a table, only if they have the same type of elements. 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the attribute the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code>element, containing the name of the attribute
	 * 					which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the attribute. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of the attribute
	 * 					where the user wants to paste the original attribute.
	 * 			
	 */
	public void copyExistingAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
		int copyK = position(nameCopy);
		int attNumC = tables.get(copyK).searchAttribute(attNameC);
		int pasteK = position(namePaste);
		int attNumP = tables.get(pasteK).searchAttribute(attNameP);
		if (tables.get(pasteK).getAttributes(attNumP).getType()
				.equals( tables.get(copyK).getAttributes(attNumC).getType()) && tables.get(pasteK).getLines() == tables.get(copyK).getLines()) {
			ArrayList tempArray = new ArrayList();
			for (int i = 0; i < tables.get(copyK).getLines(); i ++) {
				tempArray.add(tables.get(copyK).getAttributes(attNumC).getArray().get(i));
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
				tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1)
					.changeField(i, format.format(date));
			}
			tables.get(pasteK).getAttributes(attNumP).setArray(tempArray);
		} else {
			System.out.println("Different type of attributes or different number of lines");
		}
	}

	/**
	 * This method finds the number of the choice the user made when he
	 * created an attribute based on which is the type of the elements of 
	 * this specific attribute.
	 * @param copyK
	 * 				An <code>int</code> element, containing the position of the
	 * 				table that contains the attribute.
	 * @param attNumC
	 * 				An <code>int</code> element, containing the position of the 
	 * 				attribute in the table.
	 * @return
	 * 			An <code>int</code> element, containing the number of the choice
	 * 			the user made when he created this attribute.
	 */
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
		} 
		return choice;
	}

	/**
	 * This method copies an element of a table specified by the user to 
	 * another element of a table specified by the user, only if they 
	 * have the same type.
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the element the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code> element, containing the name of 
	 * 					the attribute that contains the element the user wants to copy. 
	 * @param lineC
	 * 					An <code>int</code> element, containing the position of the 
	 * 					entry that contains the element the user wants to copy.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the element. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of 
	 * 					the attribute where the user wants to paste the element.
	 * @param lineP
	 * 					An <code>int</code> element, containing the position of the
	 * 					entry where the user wants to paste the element.
	 */
	public void copyElement(String nameCopy, String attNameC, int lineC, String namePaste, String attNameP, int lineP) {
		int copyK = position(nameCopy);
		int attNumC = tables.get(copyK).searchAttribute(attNameC);
		int pasteK = position(namePaste);
		int attNumP = tables.get(pasteK).searchAttribute(attNameP);
		try {
			if (lineC < tables.get(copyK).getAttributes(attNumC).getArray().size() 
					&& lineP < tables.get(pasteK).getAttributes(attNumP).getArray().size()) {
				if (tables.get(pasteK).getAttributes(attNumP).getType()
						.equals(tables.get(copyK).getAttributes(attNumC).getType()) ) {
					tables.get(pasteK).getAttributes(attNumP)
						.changeField(lineP, tables.get(copyK).getAttributes(attNumC).getArray().get(lineC));
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
					tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1)
						.changeField(lineP, format.format(date));
				} else {
					System.out.println("Different type of elements");
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
		}
	}

	/**
	 * This method implements the function of cut - paste of an entry
	 * using the methods {@link Database#copyExistingEntry(String, int, String, int)}
	 * and {@link Table#deleteEntry(int)} 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table the user wants to copy. 
	 * @param entryNumCopy
	 * 					An <code>int</code> element, containing the number that
	 * 					indicates the position of the entry which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table the user wants to paste. 
	 * @param entryNumPaste
	 * 					An <code>int</code> element, containing the number that
	 * 					indicates the position of the entry where the other entry
	 * 					will be pasted. 
	 */
	public void cutExistingEntry(String nameCopy, int entryNumCopy, String namePaste, int entryNumPaste) {
		copyExistingEntry(nameCopy,entryNumCopy,namePaste,entryNumPaste);
		int pos = position(nameCopy);
		tables.get(pos).deleteEntry(entryNumCopy);
	}

	/**
	 * This method implements the function of cut an entry and creating
	 * a new one based on the first using the methods {@link Database#copyNewEntry(String, int, String)}
	 * and {@link Table#deleteEntry(int)} 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the entry the user wants to cut. 
	 * @param entryNumCopy
	 * 					An <code>int</code> element, containing the position of
	 * 					the entry which will be cut.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the entry. 
	 */
	public void cutNewEntry(String nameCopy, int entryNumCopy, String namePaste) {
		copyNewEntry(nameCopy,entryNumCopy,namePaste);
		int pos = position(nameCopy);
		tables.get(pos).deleteEntry(entryNumCopy);
	}

	/**
	 * This method implements the function cut-paste of an attribute
	 * using the methods {@link Database#copyExistingAttribute(String, String, String, String)}
	 * and {@link Table#deleteAttribute(String)} 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the attribute the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code> element, containing the name of the attribute
	 * 					which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the attribute. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of the attribute
	 * 					where the user wants to paste the original attribute.
	 */
	public void cutExistingAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
		copyExistingAttribute(nameCopy,attNameC,namePaste,attNameP);
		int pos = position(nameCopy);
		tables.get(pos).deleteAttribute(attNameC);
	}

	/**
	 * This method implements the function cut of an attribute kaicreates
	 * a new attribute based on the original attribute 
	 * using the methods {@link Database#copyNewAttribute(String, String, String, String)}
	 * and {@link Table#deleteAttribute(String)} 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the attribute the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code> element, containing the name of the attribute
	 * 					which will be copied.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the attribute. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of the attribute
	 * 					where the user wants to paste the original attribute.
	 */
	public void cutNewAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
		copyNewAttribute(nameCopy,attNameC,namePaste,attNameP);
		int pos = position(nameCopy);
		tables.get(pos).deleteAttribute(attNameC);
	}

	/**
	 * This method implements the function cut-paste of an element 
	 * using the methods {@link Database#copyElement(String, String, int, String, String, int)}
	 * and {@link Table#deleteElement(int, String)} 
	 * @param nameCopy
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table that contains the element the user wants to copy. 
	 * @param attNameC
	 * 					An <code>String</code> element, containing the name of 
	 * 					the attribute that contains the element the user wants to copy. 
	 * @param lineC
	 * 					An <code>int</code> element, containing the position of the 
	 * 					entry that contains the element the user wants to copy.
	 * @param namePaste
	 * 					An <code>String</code> element, containing the name of 
	 * 					the table where the user wants to paste the element. 
	 * @param attNameP
	 * 					An <code>String</code> element, containing the name of 
	 * 					the attribute where the user wants to paste the element.
	 * @param lineP
	 * 					An <code>int</code> element, containing the position of the
	 * 					entry where the user wants to paste the element.
	 */
	public void cutElement(String nameCopy, String attNameC, int lineC, String namePaste, String attNameP, int lineP) {
		copyElement(nameCopy,attNameC,lineC,namePaste,attNameP,lineP);
		int pos = position(nameCopy);
		tables.get(pos).deleteElement(lineC, attNameC);
	}

	/**
	 * This methods is used to save a database in the DatabaseUniverse Directory.
	 * Each database is saved as a directory with the name of the database, containing a 
	 * csv file for each table of the database. If a database already existed, all table 
	 * files are deleted, and new ones are created.
	 */
	public void saveDatabase() {
		try {
			File databaseDirectory = new File (System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator + "DatabaseUniverse"
					+ File.separator + name);
			databaseDirectory.mkdirs();
			File[] files = databaseDirectory.listFiles();
			System.gc();
			for (File file : files) {
				Files.delete(file.toPath());
			}
			for (Table table : tables) {
				table.saveTable(databaseDirectory.getPath());
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder. Please check your security settings"
					+ "to enable file saving");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.print(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * This method imports a table from a csv file, the location of which
	 * is specified by the user. The format of the file must be as follows:
	 * <ul>
	 * <li>Line one: Name of the table</li>
	 * <li>Line two: Types of the attributes separated by commas</li>
	 * <li>Line three: Names of the attributes separated by commas</li>
	 * <li>Each of the remaining lines: Values of each line, separated by commas</li>
	 * </ul>
	 * The user is responsible for the contents of each line for no checks will be
	 * conducted concerning the matching of the attribute types and the element values.
	 * @param br
	 * 			A <code>BufferedReader</code> object used to read from the file.
	 */
	public void importTable(BufferedReader br) {
		String line;
		Table table = null;
		try {
			String tableName = br.readLine();
			table = new Table(tableName);
			int[] types = convertTypes(br.readLine().split(","));
			String[] names = br.readLine().split(",");
			assert (types.length == names.length);
			for (int i = 0 ; i < types.length - 1; i++) {
				table.newAttribute(names[i], types[i]);
			}
			while ((line = br.readLine()) != null) {
				String[] entries = line.split(",");
				String[] entriesMinusLastModified = new String[entries.length - 1];
				for (int i = 0; i < entriesMinusLastModified.length; i++) {
					entriesMinusLastModified[i] = entries[i];
				}
				table.newEntry(entriesMinusLastModified);
				table.getAttributes(table.getAttributeNumber() - 1).
				changeField(table.getLines() - 1, entries[entries.length - 1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			table.delete();
		}
	}

	/**
	 * A method that matches the types of an attribute with a specific number
	 * so that a method can make use of the {@link Table#newAttribute(String, int)}
	 * method.
	 * @param types
	 * 		An array of <code>String</code> elements among "string", "char",
	 * 		"int", "double", "date" and any other type (to be matched with the object
	 * 		case), representing a type in a String format
	 * @return
	 * 		An array of <code>int</code> elements, each one representing a choice
	 * 		of type in compliance with the {@link Table#newAttribute(String, int)} method.
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
			}
		}
		return typeNums;
	}

	/**
	 * Deletes the database object it's called onto.
	 */
	public void delete() {
		for (int i = 0; i <= DatabaseUniverse.getDatabaseNumber(); i++) {
			if (this.equals(DatabaseUniverse.getDatabases(i))) {
				DatabaseUniverse.getAllDatabases().set(i, null);
				DatabaseUniverse.getAllDatabases().remove(i);
				DatabaseUniverse.setDatabaseNumber(-1);
				break;
			}
		}
	}

	/**
	 * This method lists the names of the existing tables 
	 * in a database.
	 */
	public void listTables() {
		for (Table table : tables) {
			System.out.println(table.getName());
		}
	}
}
