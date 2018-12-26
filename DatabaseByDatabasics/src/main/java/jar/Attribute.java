package jar;

import java.util.ArrayList;

//This class is still so simple that documents itself really.

public class Attribute {
	private String name;
	private String type;
	private ArrayList<String> array = new ArrayList<String>();

	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
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

	//The user shouldn't have the ability to change a column's type before deleting the data
	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<String> getArray() {
		return array;
	}

	public void setArray(ArrayList<String> array) {
		this.array = array;
	}


	public void setEntryField(String entry) {
		array.add(entry);
	}


	public void changeField(int lineNum, String newVal) {
        array.set(lineNum - 1, newVal);
	}

}
