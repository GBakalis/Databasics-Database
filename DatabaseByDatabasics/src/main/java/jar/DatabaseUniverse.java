package jar;

import java.io.FileReader;
import java.io.FilenameFilter;
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

	public int getDatabaseNumber() {
		return databaseNumber;
	}

	public ArrayList<Database> getAllDatabases() {
		return databases;
	}
	
	public Database getDatabases(int i) {
		return databases.get(i);
	}

	public boolean exists(String name) {
		for (Database db : getAllDatabases()) {
			if (db.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This methods creates a directory in the user's Documents folder
	 * within which the databases will be saved
	 */
	public void makeDatabaseUniverseDir() {
		try {
			File databasesFolder = new File (System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator + "DatabaseUniverse");
			if (!databasesFolder.exists()) {
				databasesFolder.mkdirs();
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder. Please check your security settings"
					+ "to enable file saving");
		} catch (NullPointerException e) {
			System.err.println(e.getCause());
		} catch (IllegalArgumentException e) {
			System.err.print(e);
		}
	}
	
	/**
	 * This method is used to import all the databases and their contents, that are saved
	 * in the user's DatabaseUniverse directory within his Documents folder.
	 */
	public static void importDatabaseUniverseTree() {
		try {
			File databasesFolder = new File (System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator + "DatabaseUniverse");
			String[] databases = databasesFolder.list();
			for (String databaseName : databases) {
				importDatabase(databaseName);
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder. Please check your security settings"
					+ "to enable file saving");
		} catch (NullPointerException e) {
			System.err.println(e.getCause());
		} catch (IllegalArgumentException e) {
			System.err.print(e);
		}
	}
	
	public static FilenameFilter csvFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			String lowercaseName = name.toLowerCase();
			if (lowercaseName.endsWith(".csv")) {
				return true;
			} else {
				return false;
			}
		}
	};
	
	/**
	 * This method is used to import a single database and is called by
	 * {@link #importDatabaseUniverseTree()} 
	 * @param databaseName
	 * 			The name of database directory to name the database itself.
	 */
	public static void importDatabase(String databaseName) {
		try {
			File databaseDirectory = new File (System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator + "DatabaseUniverse"
					+ File.separator + databaseName);
			if (!databaseDirectory.exists()) {
				throw new FileNotFoundException();
			}
			Database database = new Database(databaseName);
			databases.add(database);
			databaseNumber++;
			File[] tableFiles = databaseDirectory.listFiles(csvFilter);
			for (File tableFile : tableFiles) {
				BufferedReader br = new BufferedReader(new FileReader(tableFile));
				database.importTable(br);
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder. Please check your security settings"
					+ "to enable file saving.");
		} catch (FileNotFoundException e) {
			System.out.println("Database " + databaseName + " does not exist");
		} catch (NullPointerException e) {
			System.err.println(e.getCause());
		} catch (IllegalArgumentException e) {
			System.err.print(e);
		}
	}
}
