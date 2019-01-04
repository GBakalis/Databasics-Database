package gr.aueb.databasics;

import java.io.IOException;

public class WrongEntryException extends IOException {

	public WrongEntryException() { };

	public WrongEntryException(String msg) {
		super(msg);
	}
}