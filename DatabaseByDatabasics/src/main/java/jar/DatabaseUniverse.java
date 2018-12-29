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

public class DatabaseUniverse {

	private static int databaseNumber;
	private static ArrayList<Database> databases = new ArrayList<Database>();

	public static int getDatabaseNumber() {
		return databaseNumber;
	}

	public static ArrayList<Database> getAllDatabases() {
		return databases;
	}
	
	public static Database getDatabases(int i) {
		return databases.get(i);
	}

	public static boolean exists(String name) {
		for (Database db : getAllDatabases()) {
			if (db.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
