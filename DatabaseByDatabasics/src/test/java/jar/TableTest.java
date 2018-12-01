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
	private Table table2 = new Table("Extra");
	private String[] timeStamp = { null, null, null };

	@Before
	public void setUp() {
		table.newAttribute("Name", 1);
		table.newAttribute("Sex", 2);
		table.newAttribute("Age", 3);
		table.newAttribute("Date", 5);
		String[] entries1 = { "George", "m", "19", "01:01:2009" };
		String[] entries2 = { "Martha", "f", "21", "01:01:2019" };
		String[] entries3 = { "Andreas", "m", "19", "01:01:1999" };
		table.newEntry(entries1);
		timeStamp[0] = table.getAttributes().get(4).getArray().get(0);
		table.newEntry(entries2);
		timeStamp[1] = table.getAttributes().get(4).getArray().get(1);
		table.newEntry(entries3);
		timeStamp[2] = table.getAttributes().get(4).getArray().get(1);
	}

	@Test
	public void testAscendingGeneralSort() throws ParseException {
		table.sortTable(table, "Name", 1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(0), "Andreas");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(1), "George");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(2), "Martha");
	}

	@Test
	public void testDescendingGeneralSort() throws ParseException {
		table.sortTable(table, "Name", 2);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(0), "Martha");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(1), "George");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(2), "Andreas");
	}

	@Test
	public void testReturnFormatter() {
		Assert.assertEquals("Failure: Wrong formatter String",table.returnFormater(table, 4), new SimpleDateFormat("dd:MM:yyyy") );
		Assert.assertEquals("Failure: Wrong formatter String",table.returnFormater(table, 5), new SimpleDateFormat("HH:mm:ss dd:MM:yyyy") );
	}
	
	@Test
	public void testTimeStampSort() throws ParseException {
		table.sortTable(table, "Time of last edit", 2);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(5).getArray().get(0), timeStamp[2]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(5).getArray().get(1), timeStamp[1]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(5).getArray().get(2), timeStamp[0]);
	}
	
	@Test
	public void testAscendingDateSort() throws ParseException {
		table.sortTable(table, "Date", 1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(0),"01:01:1999"  );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(1), "01:01:2009" );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(2), "01:01:2019" );
	}
	
	@Test
	public void testDescendingDateSort() throws ParseException {
		table.sortTable(table, "Date", 2);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(0),"01:01:2019"  );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(1), "01:01:2009" );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(2), "01:01:1999" );
	}

	@Test
	public void testExistsString() {
		Assert.assertTrue("Failure : Table not found.", Table.exists("Student"));
		Assert.assertFalse("Failure : Table found without existing.", Table.exists("aa"));
	}

	@Test
	public void testExistsStringString() {
		Assert.assertTrue("Failure : Attribute not found.", Table.exists("Student", "Sex"));
		Assert.assertFalse("Failure : Attribute found without existing.",
				Table.exists("Student", "Uhm, no"));
	}

	@Test
	public void testMaxLength() {
		Assert.assertEquals("Failure : Wrong column width.",
				Table.maxLength(table.getAttributes().get(1)), 7);
		Assert.assertEquals("Failure : Wrong column width.",
				Table.maxLength(table.getAttributes().get(2)), 3);
		Assert.assertEquals("Failure : Wrong column width.",
				Table.maxLength(table.getAttributes().get(3)), 3);
	}

	@Test
	public void testPositionStringArrayListOfString() {
		ArrayList<String> atts = new ArrayList<String>();
		atts.add(table.getAttributes().get(3).getName());
		atts.add(table.getAttributes().get(1).getName());
		ArrayList<Integer> attPos = Table.position("Student", atts);
		Assert.assertEquals("Failure : Wrong first column position.",
				attPos.get(0).intValue(), 3);
		Assert.assertEquals("Failure : Wrong first column position.",
				attPos.get(1).intValue(), 1);
	}

	@Test
	public void testPositionString() {
		for (int i = 0 ; i < Table.getT().size() ; i++) {
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
		String[] entry1 = {"Andreas", "m", "19", "15:08:1999"};
		Assert.assertTrue("Failure : Correct entry was not accepted.",
				table.checkEntry(entry1));
		String[] entry2 = {"Andreas", "m", "19", "15:08:1999", "male"};
		Assert.assertFalse("Failure : Accepted large entry", table.checkEntry(entry2));
		entry1[1] = "male";
		Assert.assertFalse("Failure : Accepted wrong type entry", table.checkEntry(entry1));
		entry1[1] = "--";
		Assert.assertTrue("Failure : Correct entry with empty field not accepted",
				table.checkEntry(entry1));
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
		positionsExpected.add(2);
		Assert.assertEquals("Wrong search results", positionsActual, positionsExpected);
	}
	
	@Test
	public void testDeleteTable() {
		table.deleteTable(table.getName());
		Assert.assertEquals("Failure : Not deleted Table.",Table.getTables(0).getName(), "Extra");
	}
	
	@Test
	public void testDeleteAttribute() {
		table.deleteAttribute(table.getName(), table.getAttributes().get(2).getName());
		Assert.assertEquals("Failure : Not deleted Attribute.", Table.getTables(0).getAttributes().get(2).getName(), "Age");
		
	} 
	
	@Test
	public void testDeleteEntry() {
		table.deleteEntry(table.getName(), 2);
		Assert.assertEquals("Failure : Not deleted Entry.", Table.getTables(0).getAttributes().get(2).getArray().get(1), "m");
		
	}
	
	@Test
	public void testDeleteElement() {
		table.deleteElement(table.getName(), 1, table.getAttributes().get(3).getName());
		Assert.assertEquals("Failure : Not deleted Element.",null, Table.getTables(0).getAttributes().get(3).getArray().get(1));
	}
	
	@Test
	public void testDataChange() {
		ArrayList<String> attrNames = new ArrayList<String>();
		ArrayList<String> newValues = new ArrayList<String>();
		attrNames.add("Name");
		newValues.add("Eve");
		attrNames.add("Sex");
		newValues.add("f");
		Assert.assertEquals("Failure : Wrong data change", newValues ,table.dataChange(3, attrNames, newValues));
	}
	
	@After
	public void tearDown() {
		Table.getT().clear();
	}
}
