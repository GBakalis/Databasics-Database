package jar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class TableTest {

	private Table table = new Table("Student");
	private String[] timeStamp = { null, null, null, null };
	private Table table2 = new Table("Extra");

	@Before
	public void setUp() {
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
				new SimpleDateFormat("HH:mm:ss dd:MM:yyyy"));
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
	public void testExistsString() {
		Assert.assertTrue("Failure : Table not found.", CommandLineMenu.getActiveDatabase().exists("Student"));
		Assert.assertFalse("Failure : Table found without existing.", CommandLineMenu.getActiveDatabase().exists("aa"));
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
	//MUST CREATE DATABASE TO MAKE TESTS AFTER I FINISH THE ARHITECTURE
	public void testPositionString() {
		for (int i = 0; i < CommandLineMenu.getActiveDatabase().getTableNumber(); i++) {
			System.out.println(Table.getTables(i).toString());
		}
		Assert.assertEquals("Failure : Wrong table position.", Table.position("Student"), 0);
	}

	@Test
	public void testPositionArrayListOfString() {
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames.add(table2.getName());
		tableNames.add(table.getName());
		Assert.assertEquals("Failure : Wrong first table position.",
				Table.position(tableNames.get(0)), 1);
		Assert.assertEquals("Failure : Wrong second table position.",
				Table.position(tableNames.get(1)), 0);
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
	public void testDeleteTable() {
		table.deleteTable(table.getName());
		Assert.assertEquals("Failure : Not deleted Table.", Table.getTables(0).getName(), "Extra");
	}

	@Test
	public void testDeleteAttribute() {
		table.deleteAttribute(table.getName(), table.getAttributes(2).getName());
		Assert.assertEquals("Failure : Not deleted Attribute.", Table.getTables(0).getAttributes(2).getName(),
				"Age");

	}

	@Test
	public void testDeleteEntry() {
		table.deleteEntry(table.getName(), 3);
		Assert.assertEquals("Failure : Not deleted Entry.", Table.getTables(0).getAttributes(2).getArray().get(2),
				"f");

	}

	@Test
	public void testDeleteElement() {
		table.deleteElement(table.getName(), 1, table.getAttributes(3).getName());
		Assert.assertEquals("Failure : Not deleted Element.", "--",
				Table.getTables(0).getAttributes(3).getArray().get(1));
	}

	@Test
	public void testDataChange() {
		ArrayList<String> attrNames = new ArrayList<String>();
		ArrayList<String> newValues = new ArrayList<String>();
		attrNames.add("Name");
		newValues.add("Eve");
		attrNames.add("Sex");
		newValues.add("f");
		Assert.assertEquals("Failure : Wrong data change", newValues, table.dataChange(3, attrNames, newValues));
	}

	@After
	public void tearDown() {
		Table.getT().clear();
		ArrayList<Table> test = new ArrayList<Table>();
		Assert.assertEquals("Failure : Not cleared.", Table.getT(), test);
	}
}
