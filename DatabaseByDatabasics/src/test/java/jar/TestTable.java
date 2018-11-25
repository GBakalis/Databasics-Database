package jar;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class TestTable {

	private Table table = new Table("Student");

	@Before
	public void setUp() {
		table.newAttribute("Name", 1);
		table.newAttribute("Sex", 2);
		table.newAttribute("Age", 3);
		String[] entries1 = {"Andreas", "m", "19"};
		String[] entries2 = {"George", "m", "19"};
		String[] entries3 = {"Martha", "f", "21"};
		String[] entries4 = {"Kostas", "m", "20"};
		table.newEntry(entries1);
		table.newEntry(entries2);
		table.newEntry(entries3);
		table.newEntry(entries4);
	}

	@Test
	public void testCheckEntry() {
		String[] entry1 = {"Andreas", "m", "19"};
		Assert.assertTrue("Failure : Correct entry was not accepted.",
				table.checkEntry(entry1));
		String[] entry2 = {"Andreas", "m", "19", "male"};
		Assert.assertFalse("Failure : Accepted large entry", table.checkEntry(entry2));
		entry1[1] = "male";
		Assert.assertFalse("Failure : Accepted wrong type entry", table.checkEntry(entry1));
		entry1[1] = "--";
		Assert.assertTrue("Failure : Correct entry with empty field not accepted",
				table.checkEntry(entry1));
	}
	
	@Test
	public void testExistsString() {
		Table a = new Table("a");
		Table b = new Table("b");
		Assert.assertTrue("Failure : Table not found.", Table.exists("b"));
		Assert.assertFalse("Failure : Table found without existing.", Table.exists("aa"));
	}

	@Test
	public void testExistsStringString() {
		Table a = new Table("a");
		a.newAttribute("att1", 1);
		a.newAttribute("att2", 3);
		Assert.assertTrue("Failure : Attribute not found.", Table.exists("a", "att1"));
		Assert.assertFalse("Failure : Attribute found without existing.", Table.exists("a", "Uhm, no"));
	}

	@Test
	public void testMaxLength() {
		Table a = new Table("a");
		a.newAttribute("att1", 1);

	}

	@Test
	public void testViewTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testViewAttribute() {
		fail("Not yet implemented");
	}

	@Test
	public void testPositionStringArrayListOfString() {
		fail("Not yet implemented");
	}

	@Test
	public void testPositionString() {
		fail("Not yet implemented");
	}

	@Test
	public void testPositionArrayListOfString() {
		fail("Not yet implemented");
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

}
