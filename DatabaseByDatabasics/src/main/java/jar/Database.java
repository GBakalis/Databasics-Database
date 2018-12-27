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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public Table getTables(int i) {
		return tables.get(i);
	}

	public ArrayList<Table> getAllTables() {
		return tables;
	}
}
