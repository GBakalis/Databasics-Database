package gr.aueb.databasics.tests;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import gr.aueb.databasics.CommandLineMenu;
import gr.aueb.databasics.Database;
import gr.aueb.databasics.DatabaseUniverse;
import gr.aueb.databasics.Table;

public class DatabaseTest {
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
	public void testExistsString() {
		Assert.assertTrue("Failure : Table not found.", CommandLineMenu.getActiveDatabase().exists("Student"));
		Assert.assertFalse("Failure : Table found without existing.", CommandLineMenu.getActiveDatabase().exists("aa"));
	}

	@Test
	public void testTempTable() {
		activeDatabase.tempTable("Student", 0, "temp");
		for ( int i = 0; i < table.getAttributeNumber() - 1; i ++ ) {
			Assert.assertEquals("Wrong copy results", table.getAttributes(i).getName() 
					, activeDatabase.getTables(0).getAttributes(i).getName());
			for ( int j = 0; j < table.getAttributes(0).getArray().size(); j ++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes(i).getArray().get(j)
						, activeDatabase.getTables(0).getAttributes(i).getArray().get(j));
			}
		}
	}

	@Test
	public void testCopyTable() {
		activeDatabase.copyTable("Student", "Extra");
		for ( int i = 0; i < table.getAttributeNumber(); i ++ ) {
			Assert.assertEquals("Wrong copy results", table.getAttributes(i).getName() 
					, activeDatabase.getTables(0).getAttributes(i).getName());
			for ( int j = 0; j < table.getAttributes(0).getArray().size(); j ++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes(i).getArray().get(j)
						, activeDatabase.getTables(0).getAttributes(i).getArray().get(j));
			}
		}
	}
	
	@Test
	public void testPositionString() {
		Assert.assertEquals("Failure : Wrong table position.", activeDatabase.position("Student"), 0);
	}
	
	@Test
	public void testPositionArrayListOfString() {
		ArrayList<String> tableNames = new ArrayList<String>();
		tableNames.add(table2.getName());
		tableNames.add(table.getName());
		Assert.assertEquals("Failure : Wrong first table position.",
				activeDatabase.position(tableNames.get(0)), 1);
		Assert.assertEquals("Failure : Wrong second table position.",
				activeDatabase.position(tableNames.get(1)), 0);
	}
	@Test
	public void testCopyExistingEntry() {
		String nameC = table.getName();
		String nameP = table2.getName();
		activeDatabase.copyExistingEntry(nameC, 0, nameP, 0);
		for (int i = 1; i < table.getAttributeNumber() - 2; i++) {
			Assert.assertEquals("Wrong copy results", table.getAttributes(i).getArray().get(0)
					, table2.getAttributes(i).getArray().get(0));
			
		}
	}
	
	@Test
	public void testCopyNewEntry() {
		String nameC = table.getName();
		String nameP = table2.getName();
		activeDatabase.copyNewEntry(nameC, 0, nameP);
		for (int i = 1; i < table.getAttributeNumber() - 2; i++) {
			Assert.assertEquals("Wrong copy results", table.getAttributes(i).getArray().get(0)
					, table2.getAttributes(i).getArray().get(4));
			
		}
	}
	
	@Test
	public void testCopyExistingAttribute() {
		String nameCopy = table.getName();
		String namePaste = table2.getName();
		String attNameC = "Name";
		String attNameP = "Names";
		activeDatabase.copyExistingAttribute(nameCopy,attNameC,namePaste,attNameP);
		for (int i = 0; i < table.getAttributes(0).getArray().size(); i++) {
			Assert.assertEquals("Wrong copy results",table.getAttributes(0).getArray().get(i)
					, table2.getAttributes(0).getArray().get(i));
			
		}
	}
	
		@Test
		public void testCopyNewAttribute() {
			String nameCopy = table.getName();
			String namePaste = table2.getName();
			String attNameC = "Name";
			String attNameP = "Names";
			activeDatabase.copyNewAttribute(nameCopy,attNameC,namePaste,attNameP);
			for (int i = 0; i < table.getAttributes(1).getArray().size(); i++) {
				Assert.assertEquals("Wrong copy results",table.getAttributes(1).getArray().get(i)
						, table2.getAttributes(5).getArray().get(i));
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
			activeDatabase.copyElement(nameCopy, attNameC, lineC, namePaste, attNameP, lineP);
			Assert.assertEquals("Wrong copy results", table.getAttributes(0).getArray().get(lineC) 
					,table.getAttributes(0).getArray().get(lineP));
					
		}
		
		@Test
		public void testFindChoice() {
			int choice = activeDatabase.findChoice(0, 1);
			Assert.assertEquals("Wrong results",choice, 1);
		}
		
		@Test
		public void testConvertTypes() {
			String [] types = new String[4];
			for (int i = 1; i < table.getAttributeNumber() - 1; i++) {
				types[i - 1] = table.getAttributes(i).getType();
			}
			int [] typesNum = new int[4];
			typesNum = activeDatabase.convertTypes(types);
			Assert.assertEquals("Wrong converting results", 1, typesNum[0]);
			Assert.assertEquals("Wrong converting results", 2, typesNum[1]);
			Assert.assertEquals("Wrong converting results", 3, typesNum[2]);
			Assert.assertEquals("Wrong converting results", 5, typesNum[3]);
		}
		
		@Test
		public void testDelete() {
			activeDatabase.delete();
			Assert.assertEquals("Failure : Not deleted Database.", activeDatabase.getName(), "DB1");
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
