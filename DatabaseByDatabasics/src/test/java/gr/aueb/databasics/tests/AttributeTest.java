package gr.aueb.databasics.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.databasics.Database;
import gr.aueb.databasics.Table;

public class AttributeTest {
	
	private Database db = new Database("DB1");
	private Table table = new Table("Student");
	private String[] timeStamp = { null, null, null, null };
	private Table table2 = new Table("Extra");
	private Database activeDatabase;

	@Before
	public void setUp() {
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

}
