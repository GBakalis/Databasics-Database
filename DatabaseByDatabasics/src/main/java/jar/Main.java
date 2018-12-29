package jar;
//Create better tests for Main and then commit
public class Main {

	public static void main(String args[]) {
		DatabaseUniverse.createDatabaseUniverseDir();
		DatabaseUniverse.importDatabaseUniverseTree();
		CommandLineMenu.databaseChoiceMenu();
	}
}
