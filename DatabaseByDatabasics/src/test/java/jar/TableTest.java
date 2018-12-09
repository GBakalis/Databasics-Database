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
		String[] entries1 = { "Andreas", "m", "19", "01:01:2019" };
		String[] entries2 = { "George", "m", "19", "01:01:2009" };
		String[] entries3 = { "Martha", "f", "21", "01:01:2029" };
		String[] entries4 = { "Kostas", "m", "20", "01:01:1999" };
		table.newEntry(entries1);
		table.newEntry(entries2);
		table.newEntry(entries3);
		table.newEntry(entries4);
		timeStamp[0] = table.getAttributes().get(5).getArray().get(0);
		timeStamp[1] = table.getAttributes().get(5).getArray().get(1);
		timeStamp[2] = table.getAttributes().get(5).getArray().get(2);
		timeStamp[3] = table.getAttributes().get(5).getArray().get(3);
		
		table2.newAttribute("Names", 1);
		table2.newAttribute("Sex", 2);
		table2.newAttribute("Age", 3);
		table2.newAttribute("Date", 5);
		String[] entries5 = {"Evi", "m", "19","01:01:2019"};
		String[] entries6 = {"George", "m", "19","01:01:2019"};
		String[] entries7 = {"Martha", "f", "21","01:01:2019"};
		String[] entries8 = {"Kostas", "m", "20","01:01:2019"};
		table2.newEntry(entries5);
		table2.newEntry(entries6);
		table2.newEntry(entries7);
		table2.newEntry(entries8);
	}
	
	@Test
	public void testTempTable() {
		table.tempTable("Student",0, "temp");
		for ( int i = 0; i < table.getAttributeNumber() - 1; i ++ ) {
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
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getName() , Table.getT().get(0).getAttributes().get(i).getName());
			for ( int j = 0; j < table.getAttributes().get(0).getArray().size(); j ++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes().get(i).getArray().get(j), Table.getT().get(0).getAttributes().get(i).getArray().get(j));
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
	public void testCopyExistingEntry() {
		String nameC = table.getName();
		String nameP = table2.getName();
		table.copyExistingEntry(nameC, 1, nameP, 1);
		for (int i = 1; i < table.getAttributeNumber() - 1; i++) {
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getArray().get(0), table2.getAttributes().get(i).getArray().get(0));
			
		}
	}
	
	@Test
	public void testCopyNewEntry() {
		String nameC = table.getName();
		String nameP = table2.getName();
		table.copyNewEntry(nameC, 1, nameP);
		for (int i = 1; i < table.getAttributeNumber() - 1; i++) {
			Assert.assertEquals("Wrong copy results", table.getAttributes().get(i).getArray().get(0), table2.getAttributes().get(i).getArray().get(4));
			
		}
	}
	
	@Test
	public void testFindChoice() {
		int choice = table.findChoice(0, 1);
		Assert.assertEquals("Wrong results",choice, 1);
	}
	
	@After
	public void tearDown() {
		Table.getT().clear();
	}
}
	
	