package gr.aueb.databasics.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.databasics.Database;
import gr.aueb.databasics.DatabaseUniverse;
import gr.aueb.databasics.Table;

public class AttributeTest {
	
	private Database db;
	private Table table;
	private String[] timeStamp = { null, null, null, null };
	private Table table2;
	private Database activeDatabase;

	@Before
	public void setUp() {
		db = new Database("DB1");
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
	public void testMaxLength() {
		Assert.assertEquals("Failure : Wrong column width."
				, table.getAttributes(1).maxLength(), 7);
		Assert.assertEquals("Failure : Wrong column width."
				, table.getAttributes(2).maxLength(), 3);
		Assert.assertEquals("Failure : Wrong column width."
				, table.getAttributes(3).maxLength(), 3);
		Assert.assertEquals("Failure : Wrong column width."
				, table.getAttributes(3).maxLength(), 3);
	}
	
	@Test
	public void testCheckType() {
		boolean value = table.getAttributes(4).checkType("student");
		Assert.assertFalse("Failure : Wrong type was accepted.", value);
		value = table.getAttributes(4).checkType("06/01/2019");
		Assert.assertTrue("Failure : Correct type was not accepted.", value);
		value = table.getAttributes(2).checkType("male");
		Assert.assertFalse("Failure :  Wrong type was accepted.", value);
	}

	@After
	public void tearDown() {
		db.getAllTables().clear();
		db.setTableNumber(-2);
		DatabaseUniverse.getAllDatabases().clear();
		DatabaseUniverse.setDatabaseNumber(-1);
		table = null;
		table2 = null;
		db = null;
		activeDatabase = null;
	}
}
