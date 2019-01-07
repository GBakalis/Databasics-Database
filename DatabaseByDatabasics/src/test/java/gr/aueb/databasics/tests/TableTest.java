package gr.aueb.databasics.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

import gr.aueb.databasics.CommandLineMenu;
import gr.aueb.databasics.Database;
import gr.aueb.databasics.DatabaseUniverse;
import gr.aueb.databasics.Table;

import org.junit.Before;
import org.junit.Assert;

public class TableTest {

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
		timeStamp[0] = table.getAttributes(5).getArray().get(0);
		timeStamp[1] = table.getAttributes(5).getArray().get(1);
		timeStamp[2] = table.getAttributes(5).getArray().get(2);
		timeStamp[3] = table.getAttributes(5).getArray().get(3);
	}

	@Test
	public void testAscendingGeneralSort() throws ParseException {
		table.sortTable("Name", 1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(0), "Andreas");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(1), "George");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(2), "Kostas");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(3), "Martha");
	}

	@Test
	public void testDescendingGeneralSort() throws ParseException {
		table.sortTable("Name", -1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(0), "Martha");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(1), "Kostas");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(2), "George");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(1).getArray().get(3), "Andreas");
	}

	@Test
	public void testReturnFormatter() {
		Assert.assertEquals("Failure: Wrong formatter String", table.returnFormater(4),
				new SimpleDateFormat("dd/MM/yyyy"));
		Assert.assertEquals("Failure: Wrong formatter String", table.returnFormater(5),
				new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy"));
	}

	@Test
	public void testTimeStampSort() throws ParseException {
		table.sortTable("Last Modified", -1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(5).getArray().get(0), timeStamp[3]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(5).getArray().get(1), timeStamp[2]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(5).getArray().get(2), timeStamp[1]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(5).getArray().get(3), timeStamp[0]);
	}

	@Test
	public void testAscendingDateSort() throws ParseException {
		table.sortTable("Date", 1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(0), "01/01/1999");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(1), "01/01/2009");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(2), "01/01/2019");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(3), "01/01/2029");
	}

	@Test
	public void testDescendingDateSort() throws ParseException {
		table.sortTable("Date", -1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(0), "01/01/2029");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(1), "01/01/2019");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(2), "01/01/2009");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes(4).getArray().get(3), "01/01/1999");
	}

	
	@Test
	public void testExistsStringString() {
		Assert.assertTrue("Failure : Attribute not found.", table.exists("Sex"));
		Assert.assertFalse("Failure : Attribute found without existing.", table.exists("Uhm, no"));
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
	public void testPositionStringArrayListOfString() {
		ArrayList<String> atts = new ArrayList<String>();
		atts.add(table.getAttributes(3).getName());
		atts.add(table.getAttributes(1).getName());
		ArrayList<Integer> attPos = table.attPositions(atts);
		Assert.assertEquals("Failure : Wrong first column position.",
				attPos.get(0).intValue(), 3);
		Assert.assertEquals("Failure : Wrong first column position.", 
				attPos.get(1).intValue(), 1);
	}

	@Test
	public void testCheckEntry() {
		String[] entry1 = { "Andreas", "m", "19", "01/01/2019" };
		Assert.assertTrue("Failure : Correct entry was not accepted.", table.checkEntry(entry1));
		String[] entry2 = { "Andreas", "m", "19", "01/01/2019", "male" };
		Assert.assertFalse("Failure : Accepted large entry", table.checkEntry(entry2));
		entry1[1] = "male";
		Assert.assertFalse("Failure : Accepted wrong type entry", table.checkEntry(entry1));
		entry1[1] = "--";
		Assert.assertTrue("Failure : Correct entry with empty field not accepted", table.checkEntry(entry1));
	}
	
	@Test
	public void testNewEntry() {
		table2.newAttribute("Names", 1);
		table2.newAttribute("Sex", 2);
		table2.newAttribute("Age", 3);
		table2.newAttribute("Date", 5);
		String[] entries5 = {"Evi", "m", "19","01:01:2019"};
		String[] entries6 = {"George", "m", "19","01:01:2019"};
		table2.newEntry(entries5);
		table2.newEntry(entries6);
		for ( int i = 1; i < table.getAttributeNumber() -1; i ++) {
			Assert.assertEquals("Wrong entry results",table2.getAttributes(i).getArray().get(0)
					, entries5[i - 1]);
			Assert.assertEquals("Wrong entry results",table2.getAttributes(i).getArray().get(1)
					, entries6[i - 1]);
		}
	}
	
	@Test
	public void testCheckInput() {
		Assert.assertTrue("Failure : Correct entry was not accepted", table.checkInput(2,true));
		Assert.assertFalse("Failure : Wrong entry was accepted", table.checkInput(6,true));
		Assert.assertFalse("Failure : Wrong entry was accepted", table.checkInput(2,false));
	}
	
	@Test
	public void testNewAttribute() {
		table.newAttribute("phone", 1);
		Assert.assertEquals("Failure : Wrong name", table.getAttributes(5).getName(), "phone");
		Assert.assertEquals("Failure : Wrong type", table.getAttributes(5).getType(), "string");
	}

	@Test
	public void testSearch() {
		ArrayList<String> attributeNames = new ArrayList<String>();
		ArrayList<String> elements = new ArrayList<String>();
		attributeNames.add("Sex");
		elements.add("m");
		attributeNames.add("Age");
		elements.add("19");
		ArrayList<Integer> positionsActual = new ArrayList<Integer>();
		positionsActual = table.search(attributeNames, elements);
		ArrayList<Integer> positionsExpected = new ArrayList<Integer>();
		positionsExpected.add(0);
		positionsExpected.add(1);
		Assert.assertEquals("Wrong search results", positionsActual, positionsExpected);
	}

	@Test
	public void testDelete() {
		table.delete();
		Assert.assertEquals("Failure : Not deleted Table.", activeDatabase.getTables(0).getName(), "Extra");
	}

	@Test
	public void testDeleteAttribute() {
		table.deleteAttribute(table.getAttributes(2).getName());
		Assert.assertEquals("Failure : Not deleted Attribute.", table.getAttributes(2).getName(),
				"Age");
	}

	@Test
	public void testDeleteEntry() {
		table.deleteEntry(3);
		Assert.assertEquals("Failure : Not deleted Entry.", activeDatabase.getTables(0).getAttributes(2).getArray().get(2),
				"f");
	}

	@Test
	public void testDeleteElement() {
		table.deleteElement(1, table.getAttributes(3).getName());
		Assert.assertEquals("Failure : Not deleted Element.", "--",
				activeDatabase.getTables(0).getAttributes(3).getArray().get(1));
	}

	@Test
	public void testDataChange() {
		ArrayList<String> attNames = new ArrayList<String>();
		ArrayList<String> newValues = new ArrayList<String>();
		attNames.add("Name");
		newValues.add("Eve");
		attNames.add("Sex");
		newValues.add("f");
		table.dataChange(2, attNames, newValues);
		Assert.assertEquals("Failure : Wrong data change", newValues.get(0), table.getAttributes(1).getArray().get(2));
		Assert.assertEquals("Failure : Wrong data change", newValues.get(1), table.getAttributes(2).getArray().get(2));
	}
	
	@Test
	public void testSearchAttribute() {
		int pos = table.searchAttribute("Name");
		Assert.assertEquals("Wrong search results", 1, pos);
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
