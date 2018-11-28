package jar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
public class TestTable {

	private Table table = new Table("Student");
	private Table table2 = new Table("Extra");

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
		
		table2.newAttribute("Names", 1);
		table2.newAttribute("Sex", 2);
		table2.newAttribute("Age", 3);
		String[] entries5 = {"Evi", "m", "19"};
		String[] entries6 = {"George", "m", "19"};
		String[] entries7 = {"Martha", "f", "21"};
		String[] entries8 = {"Kostas", "m", "20"};
		table.newEntry(entries1);
		table.newEntry(entries2);
		table.newEntry(entries3);
		table.newEntry(entries4);
	}
	
	@Test
	public void testTempTable() {
		table.tempTable("Student",0, "temp");
		for ( int i = 0; i < table.getAttributeNumber(); i ++ ) {
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getName() , Table.getT().get(2).getAttributes().get(i).getName());
			for ( int j = 0; j < table.getAttributes().get(0).getArray().size(); j ++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes().get(i).getArray().get(j), Table.getT().get(2).getAttributes().get(i).getArray().get(j));
			}
		}
		
	}
	
	@Test
	public void testCopyTable() {
		table.copyTable("Student", "Extra");
		for ( int i = 0; i < table.getAttributeNumber(); i ++ ) {
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getName() , Table.getT().get(2).getAttributes().get(i).getName());
			for ( int j = 0; j < table.getAttributes().get(0).getArray().size(); j ++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes().get(i).getArray().get(j), Table.getT().get(2).getAttributes().get(i).getArray().get(j));
			}
		}
	}
	
	@Test
	public void testCopyAttribute() {
		String nameCopy = table.getName();
		String namePaste = table2.getName();
		String attNameC = "Name";
		String attNameP = "Names";
		table.copyAttribute(nameCopy,attNameC,namePaste,attNameP);
		for (int i = 0; i < table.getAttributes().get(0).getArray().size(); i++) {
			Assert.assertEquals("Wrong copy results",table.getAttributes().get(0).getArray().get(i), table2.getAttributes().get(0).getArray().get(i));
			
		}
		


		
	}
	
	@Test
	public void testCopyElement() {
		String nameCopy = table.getName();
		String namePaste = table2.getName();
		String attNameC = "Name";
		String attNameP = "Names";
		int lineC = 1;
		int lineP = 1;
		table.copyElement(nameCopy, attNameC, lineC, namePaste, attNameP, lineP);
		Assert.assertEquals("Wrong copy results", table.getAttributes().get(0).getArray().get(lineC) ,table.getAttributes().get(0).getArray().get(lineP));
				
	}
	
	@Test
	public void testSearch_attribute() {
		int pos = table.search_attribute(0,"Name");
		Assert.assertEquals("Wrong search results", 1, pos);
	}
	
	@Test
	public void testCopyEntry() {
		String nameC = table.getName();
		String nameP = table2.getName();
		table.copyEntry(nameC, 1, nameP, 1);
		for (int i = 0; i < table.getAttributeNumber(); i++) {
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getArray().get(0), table2.getAttributes().get(i).getArray().get(0));
			
		}
	}
	
	@After
	public void tearDown() {
		Table.getT().clear();
	}
}