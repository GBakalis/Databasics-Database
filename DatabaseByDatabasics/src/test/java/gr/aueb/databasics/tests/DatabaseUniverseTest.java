package gr.aueb.databasics.tests;

import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.databasics.Database;
import gr.aueb.databasics.Table;
import gr.aueb.databasics.DatabaseUniverse;

public class DatabaseUniverseTest {
	private DatabaseUniverse du = new DatabaseUniverse();
	private Database db = new Database("DB1");
	private Table table = new Table("Student");
	private String[] timeStamp = { null, null, null, null };
	private Table table2 = new Table("Extra");
	private Database activeDatabase;

	@Before
	public void setUp() {
		Database db = new Database("DB1");
		table = new Table("Student");
		table2 = new Table("Extra");
		activeDatabase = db;
		table.newAttribute("Name", 1);
		table.newAttribute("Sex", 2);
		table.newAttribute("Age", 3);
		table.newAttribute("Date", 5);
		String[] entries1 = { "Andreas", "m", "19", "01/01/2019" };
		String[] entries2 = { "George", "m", "19", "01/01/2009" };
		String[] entries3 = { "Martha", "f", "21", "01/01/2029" };
		String[] entries4 = { "Kostas", "m", "20", "01/01/1999" };
		table.newEntry(entries1);
		table.newEntry(entries2);
		table.newEntry(entries3);
		table.newEntry(entries4);
	}
	
	@Test
	public void testExistsStringString() {
		Assert.assertTrue("Failure : Database not found.", du.exists("DB1"));
		Assert.assertFalse("Failure : Database found without existing.", du.exists("Uhm, no"));
	}
	
	@Test
	public void testPositionString() {
		System.out.println("Existing databases " + du.getDatabaseNumber());
		Assert.assertEquals("Failure : Wrong database position.", du.position("DB1"), 0);
	}

	@After
	public void tearDown() {
		du.getAllDatabases().clear();
		du.setDatabaseNumber(-1);
		db.getAllTables().clear();
		db.setTableNumber(-2);
		table = null;
		db = null;
		table2 = null;
	}
}
