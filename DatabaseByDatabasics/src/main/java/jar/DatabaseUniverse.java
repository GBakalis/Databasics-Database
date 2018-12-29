package jar;

import java.util.ArrayList;

public class DatabaseUniverse {

	private static ArrayList<Database> databases = new ArrayList<Database>();
	private static int databaseNumber = databases.size();

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
