package Databasics;
import java.util.ArrayList;
public class Attribute {
	private String name;
	private String type;
	private ArrayList<String> array;
	private static int attributeNumber;
	
	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
		attributeNumber++;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<String> getArray() {
		return array;
	}

	public void setArray(ArrayList<String> array) {
		this.array = array;
	}

	public static int getAttributeNumber() {
		return attributeNumber;
	}

	public static void setAttributeNumber(int attributeNumber) {
		Attribute.attributeNumber = attributeNumber;
	}
	
}
