package gr.aueb.databasics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Attribute is a class that represents a column of a table and
 * its characteristics (name, type, elements).
 * The type can be one of the following:
 * <ul>
 * <li>"string"
 * <li>"char"
 * <li>"int"
 * <li>"double"
 * <li>"date"
 * </ul>
 * The elements are saved within an <code>ArrayList&lt;String&gt;</code>
 * 
 * @version 1.0
 */

public class Attribute {

	private String name;
	private String type;
	private ArrayList<String> array = new ArrayList<String>();

	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	/**
         * This method returns the value of the field name.
         * @return
         * 	The value of the field name.
         */
	public String getName() {
		return name;
	}
	
	/**
	 * This method expects an input and uses it to set field name.
	 * @param name
	 *  	The value used to set field name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * This method returns the value of the field type.
	 * @return
	 *	The value of the field type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * This method expects an input and uses it to set field type.
	 * @param type
	 *  	The value used to set field type.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * This method returns the value of the field array.
	 * @return
	 *	The value of the field array.
	 */
	public ArrayList<String> getArray() {
		return array;
	}
	
	/**
	 * This method expects an input and uses it to set field array.
	 * @param array
	 *  	The value used to set field array.
	 */
	public void setArray(ArrayList<String> array) {
		this.array = array;
	}

	/**
	 * This method expects an input and uses it to set field array.
	 * @param entry
	 *  	The value used to set field array.
	 */
	public void setEntryField(String entry) {
		array.add(entry);
	}

	/**
	 * This method changes the value of the specified element as long as
	 * its type matches with the attribute type
	 * 
	 * @param linePos
	 *      An <code>int</code> which contains the line
	 *      number of the element whose value will be changed.
	 * @param newVal
	 *      A <code>String</code> which contains
	 *      the new value used for the change.
	 */
	public void changeField(int linePos, String newVal) {
        if (checkType(newVal)) {
        	array.set(linePos, newVal);
        } 
	}
	
	/**
	 * This method examines if an element matches with this attribute's type.
	 * A set of parses and exceptions is utilized so as to examine whether
	 * the element matches or not. If the parse fails(the element doesn't
	 * match), the exception is caught within a block that prints an
	 * appropriate message and sets the returned <code>boolean</code>
	 * as <code>false</code>
	 * @param value
	 * 			The element to be examined for type matching
	 * @return
	 * 			Returns <code>true</code> if the element matches with the type
	 * 			and <code>false</code> if it does not.
	 */
	public boolean checkType(String value) {
		boolean correctType = true;
		try {
			if (this.getType().equals("int") && !value.equals("--")) {
				Integer.parseInt(value);
			}
			if (this.getType().equals("double") && !value.equals("--")) {
				Double.parseDouble(value);
			}
			if (this.getType().equals("date") && !value.equals("--")) {
				DateFormat format;
				if (this.getName().equals("Last Modified")) {
					format = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
				} else {
					format = new SimpleDateFormat("dd/MM/yyyy");
				}
				format.setLenient(false);
				format.parse(value);
			}
			if ((this.getType().equals("char")) && (value.length() != 1)
					&& !value.equals("--")) {
				throw new NotCharacterException();
			}
		} catch (NumberFormatException e) {
			System.out.println("Wrong entry on an Integer or Decimal column!");
			correctType = false;
		} catch (ParseException e) {
			System.out.println("Invalid date format on a date column!");
			correctType = false;
		} catch (NotCharacterException e) {
			System.out.println("Large entry on a single letter column!");
			correctType = false;
		}
		return correctType;
	}

	/**
	 * This method calculates the length of the longest element (attribute name
	 * included) in the attribute column
	 * @return
	 * 			Returns an <code>int</code> indicating the length of the 
	 * 			longest String in the attribute.
	 */
	public int maxLength() {
		int max = this.getName().length();
		for (int i = 0; i < this.getArray().size(); i++) {
			if (this.getArray().get(i).length() > max) {
				max = this.getArray().get(i).length();
			}
		}
		return max;
	}
}
