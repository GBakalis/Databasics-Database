package jar;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;


public class TableTest {
	
	private Table table = new Table("Student");
	private Table table2 = new Table("Extra");
	
	@Before
	public void setUp()  {
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
		table.deleteEntry(table.getName(), 3);
		Assert.assertEquals("Failure : Not deleted Entry.", Table.getTables(0).getAttributes().get(2).getArray().get(3), "m");
		
	}
	
	@Test
	public void testDeleteElement() {
		table.deleteElement(table.getName(), 1, table.getAttributes().get(3).getName());
		Assert.assertEquals("Failure : Not deleted Element.",null, Table.getTables(0).getAttributes().get(3).getArray().get(1));
	}
	
	
	
	@After
	public void tearDown() {
		Table.getT().clear();
		
		
	}
	
}
