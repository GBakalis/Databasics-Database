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

public class Database {

	private String name;
	private int tableNumber;
	private ArrayList<Table> tables = new ArrayList<Table>();
	
	public Database(String name) {
		this.name = name;
		tableNumber = 0;
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
}
