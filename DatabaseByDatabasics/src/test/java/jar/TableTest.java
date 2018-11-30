package jar;

import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class TableTest {

	private Table table = new Table("Student");
	private String[] timeStamp = { null, null, null };

	@Before
	public void setUp() {
		table.newAttribute("Name", 1);
		table.newAttribute("Age", 3);
		table.newAttribute("Date", 5);
		String[] entries1 = { "George", "19", "01:01:2009" };
		String[] entries2 = { "Martha", "21", "01:01:2019" };
		String[] entries3 = { "Andreas", "19", "01:01:1999" };
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
		table.sortTable(table, "Name", -1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(0), "Martha");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(1), "George");
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(1).getArray().get(2), "Andreas");
	}

	@Test
	public void testReturnFormatter() {
		Assert.assertEquals("Failure: Wrong formatter String",table.returnFormater(table, 3), new SimpleDateFormat("dd:MM:yyyy") );
		Assert.assertEquals("Failure: Wrong formatter String",table.returnFormater(table, 4), new SimpleDateFormat("HH:mm:ss dd:MM:yyyy") );
	}
	
	@Test
	public void testTimeStampSort() throws ParseException {
		table.sortTable(table, "Time of last edit", 2);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(0), timeStamp[2]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(1), timeStamp[1]);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(4).getArray().get(2), timeStamp[0]);
	}
	
	@Test
	public void testAscendingDateSort() throws ParseException {
		table.sortTable(table, "Date", 1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(0),"01:01:1999"  );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(1), "01:01:2009" );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(2), "01:01:2019" );
	}
	public void testDescendingDateSort() throws ParseException {
		table.sortTable(table, "Date", -1);
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(0),"01:01:2019"  );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(1), "01:01:2009" );
		Assert.assertEquals("Failure: Not sorted", table.getAttributes().get(3).getArray().get(2), "01:01:1999" );
	}
	
	
	
	

}
