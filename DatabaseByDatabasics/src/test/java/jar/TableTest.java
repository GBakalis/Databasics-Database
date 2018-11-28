package jar;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TableTest {
	private Table  table = new Table("Student");

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
	public void testExistsString() {
		Assert.assertTrue("Failure : Table not found", Table.exists("Student"));
		Assert.assertFalse("Failure : Table found without existing", Table.exists("k"));
	}

	@Test
	public void testExistsStringString() {
		Assert.assertTrue("Failure : Attribute not found", Table.exists("Student", "Age"));
		Assert.assertFalse("Failure : Attribute found without existing", Table.exists("Student", "smt"));
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
}
