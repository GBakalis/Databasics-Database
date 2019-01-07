package gr.aueb.databasics;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * This class represents the database tree. DatabaseUniverse contains
 * all databases loaded in the program. This class is not object-oriented;
 * all its methods and variables are static.
 * 
 * @author George Bakalis
 * @author Andreas Vlachos
 * 
 * @version 1.0
 */
public class DatabaseUniverse {

	private static ArrayList<Database> databases = new ArrayList<Database>();
	private static int databaseNumber = databases.size();

	public static int getDatabaseNumber() {
		return databaseNumber;
	}

	public static void setDatabaseNumber(int margin) {
		databaseNumber += margin;
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

	/**
	 * This methods creates a directory in the user's Documents folder
	 * within which the databases will be saved.
	 */
	public static void createDatabaseUniverseDir() {
		try {
			File databasesFolder = new File(System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator
					+ "DatabaseUniverse");
			if (!databasesFolder.exists()) {
				databasesFolder.mkdirs();
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder."
					+ " Please check your security settings"
					+ "to enable file saving");
		} catch (NullPointerException e) {
			System.err.println(e.getCause());
		} catch (IllegalArgumentException e) {
			System.err.print(e);
		}
	}

	/**
	 * This method is used to import all the databases and their contents,
	 * that are saved in the user's DatabaseUniverse directory within
	 * his Documents folder.
	 */
	public static void importDatabaseUniverseTree() {
		try {
			File databasesFolder = new File(System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator 
					+ "DatabaseUniverse");
			String[] databases = databasesFolder.list();
			for (String databaseName : databases) {
				importDatabase(databaseName);
			}
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder."
					+ " Please check your security settings"
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
	 * <code>importDatabaseUniverseTree</code> method.
	 * @param databaseName
	 * 			The name of database directory to name the database itself.
	 */
	public static void importDatabase(String databaseName) {
		try {
			File databaseDirectory = new File(System.getProperty("user.home")
					+ File.separator + "Documents" + File.separator
					+ "DatabaseUniverse" + File.separator + databaseName);
			if (!databaseDirectory.exists()) {
				throw new FileNotFoundException();
			}
			Database database = new Database(databaseName);
			CommandLineMenu.setActiveDatabase(database);
			File[] tableFiles = databaseDirectory.listFiles(csvFilter);
			for (File tableFile : tableFiles) {
				FileReader fr = new FileReader(tableFile);
				BufferedReader br = new BufferedReader(
						new FileReader(tableFile));
				database.importTable(br);
				fr.close();
				br.close();
			}
			database.setSaved(true);
			System.out.println("Database " + database.getName()
			+ " imported succesfully!");
		} catch (SecurityException e) {
			System.out.println("Access Denied in Documents folder."
					+ " Please check your security settings"
					+ "to enable file saving.");
		} catch (FileNotFoundException e) {
			System.out.println("Database " + databaseName + " does not exist");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			CommandLineMenu.setActiveDatabase(null);
		}
	}

	/**
	 * This method lists the names of the existing databases.
	 */
	public static void listDatabases() {
		for (Database database : databases) {
			System.out.println(database.getName());
		}
	}

	public static int position(String databaseName) {
		int position = -1;
		for (int i = 0; i < databaseNumber; i++) {
			if (databaseName.equals(databases.get(i).getName())) {
				position = i;
				continue;
			}
		}
		return position;
	}
}
