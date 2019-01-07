package gr.aueb.databasics;

public class Main {

	public static void main(String args[]) {
		DatabaseUniverse.createDatabaseUniverseDir();
		DatabaseUniverse.importDatabaseUniverseTree();
		CommandLineMenu.databaseChoiceMenu();
	}
}
