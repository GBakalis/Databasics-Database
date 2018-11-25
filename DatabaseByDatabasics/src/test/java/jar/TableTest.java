package jar;

import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class TableTest {
	
	private Table table = new Table("Student");
	private Table table2 = new Table("Extra");
	
	@Before
	public void setUp() {
		table.newAttribute("Name", 1);
		table.newAttribute("Sex", 2);
		table.newAttribute("Age", 3);
		String[] entries1 = {"Andreas", "m", "19"};//από εδώ
		String[] entries2 = {"George", "m", "19"};
		String[] entries3 = {"Martha", "f", "21"};
		String[] entries4 = {"Kostas", "m", "20"};
		table.checkType(entries1, true);//σε αυτά θέλει newEntry μετά τη διόρθωση
		table.checkType(entries2, true);
		table.checkType(entries3, true);
		table.checkType(entries4, true);//μέχρι εδώ
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
				Table.maxLength(table.getAttributes().get(0)), 7);
		Assert.assertEquals("Failure : Wrong column width.", 
				Table.maxLength(table.getAttributes().get(1)), 3);
		Assert.assertEquals("Failure : Wrong column width.", 
				Table.maxLength(table.getAttributes().get(2)), 3);
	}

	@Test
	public void testPositionStringArrayListOfString() {
		ArrayList<String> atts = new ArrayList<String>();
		atts.add(table.getAttributes().get(2).getName());
		atts.add(table.getAttributes().get(0).getName());		
		ArrayList<Integer> attPos = Table.position("Student", atts);
		Assert.assertEquals("Failure : Wrong first column position.", 
				attPos.get(0).intValue(), 2);
		Assert.assertEquals("Failure : Wrong first column position.", 
				attPos.get(1).intValue(), 0);
	}

	@Test
	public void testPositionString() {
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

}
