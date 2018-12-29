package jar;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class Database {

	private String name;
	private ArrayList<Table> tables = new ArrayList<Table>();
	private int tableNumber;

	public Database(String name) {
		this.name = name;
		DatabaseUniverse.getAllDatabases().add(this);
		CommandLineMenu.setActiveDatabase(this);
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
			} else {
				choice = 6;
			}
			String name = tables.get(copyK).getAttributes(i).getName();
			tempTab.newAttribute(name, choice);
			for (int j = 0; j < tables.get(copyK).getLines(); j++) {

				System.out.println(tables.get(copyK).getAttributes(i).getArray().get(j));
				String temp = tables.get(copyK).getAttributes(i).getArray().get(j);
				tempTab.getAttributes(i).getArray().add(temp);
			}
		}
		String[] entries = new String[tables.get(copyK).getAttributeNumber() - 2];
		for (int j = 0; j < tables.get(copyK).getLines(); j++) {
			for (int i = 0; i < entries.length; i++) {
				entries[i] = tables.get(copyK).getAttributes(i + 1).getArray().get(j);
			}
			tempTab.newEntry(entries);
		}
		tempTab.delete();
	}
	
	public void copyTable(String nameCopy, String namePaste) {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		if (exists(namePaste) && tables.get(copyK).getAttributeNumber() == tables.get(pasteK).getAttributeNumber()) {
			tempTable(nameCopy, copyK, "temp");
			tables.set(pasteK, tables.get(tables.size() - 1));
		} else {
			tempTable(nameCopy, copyK, namePaste);
		}
	}
	
	public int position(String tableName) {
		int position = 0;
		for (int i = 0; i < this.getTableNumber(); i++) {
			if (tableName.equals(this.getTables(i).getName())) {
				position = i;
				continue;
			}
		}
		return position;
	}

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
	
	public void copyExistingEntry(String nameCopy, int entryNumCopy, String namePaste, int entryNumPaste) throws IndexOutOfBoundsException  {
		int copyK = position(nameCopy);
		int pasteK = position(namePaste);
		boolean check = true;
		if (entryNumPaste >= 0 && entryNumPaste <= tables.get(pasteK).getLines() - 1) {
			if (tables.get(pasteK).getAttributeNumber() == tables.get(copyK).getAttributeNumber()) {
				for (int i = 1; i < tables.get(pasteK).getAttributeNumber() - 1; i++) {
					if (tables.get(pasteK).getAttributes(i).getType().equals(tables.get(copyK).getAttributes(i).getType())) {
						tables.get(pasteK).getAttributes(i).changeField(entryNumPaste + 1,tables.get(copyK).getAttributes(i).getArray().get(entryNumCopy));
						Date date = new Date();
						DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
						tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1).changeField(entryNumPaste + 1,format.format(date));
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
				String[] entries = new String[tables.get(copyK).getAttributeNumber() - 2];
				for (int i = 0; i < entries.length; i++) {
					entries[i] = tables.get(copyK).getAttributes(i + 1).getArray().get(entryNumCopy);
				}
				tables.get(pasteK).newEntry(entries);
			}

		} else {
			System.out.println("Different number of attributes");
		}
	}

	public void copyAttribute(String nameCopy, String attNameC, String namePaste, String attNameP) {
			int copyK = position(nameCopy);
			int attNumC = tables.get(copyK).searchAttribute(attNameC);
			int pasteK = position(namePaste);
			if (this.exists(attNameP)) {
				int attNumP = tables.get(pasteK).searchAttribute(attNameP);
				if (tables.get(pasteK).getAttributes(attNumP).getType().equals( tables.get(copyK).getAttributes(attNumC).getType())) {
					tables.get(pasteK).getAttributes(attNumP).setArray(tables.get(copyK).getAttributes(attNumC).getArray());
				} else {
					System.out.println("Different type of attributes");
				}
			} else {
				int choice = findChoice(copyK,attNumC);
				tables.get(pasteK).newAttribute(attNameP, choice);
				tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 2).setArray(tables.get(copyK).getAttributes(attNumC).getArray());
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
		int attNumC = tables.get(copyK).searchAttribute(attNameC);
		int pasteK = position(namePaste);
		int attNumP = tables.get(pasteK).searchAttribute(attNameP);
		try {
			if (lineC < tables.get(copyK).getAttributes(attNumC).getArray().size() && lineP < tables.get(pasteK).getAttributes(attNumP).getArray().size()) {
				if (tables.get(pasteK).getAttributes(attNumP).getType().equals(tables.get(copyK).getAttributes(attNumC).getType()) ) {
					tables.get(pasteK).getAttributes(attNumP).changeField(lineP + 1,tables.get(copyK).getAttributes(attNumC).getArray().get(lineC));
					Date date = new Date();
					DateFormat format = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
					tables.get(pasteK).getAttributes(tables.get(pasteK).getAttributeNumber() - 1).changeField(lineP + 1,format.format(date));
				} else {
					System.out.println("Different type of elements");
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Out of Bounds");
		}
	}

	public void delete() {
		for (int i = 0; i <= DatabaseUniverse.getDatabaseNumber(); i++) {
			if (this.equals(DatabaseUniverse.getDatabases(i))) {
				DatabaseUniverse.getAllDatabases().set(i, null);
				DatabaseUniverse.getAllDatabases().remove(i);
				break;
			}
		}
	}
}
