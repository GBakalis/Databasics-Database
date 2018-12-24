package jar;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class Display implements Runnable {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu editMenu;
	private JMenu fileMenu;

	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		SwingUtilities.invokeLater(new Display());
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {

		// initialize frame//
		frame = new JFrame("Database by Databasics");
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBounds(500, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Create menu bar//
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		// add file menu//
		menuBar.add(fileMenu());

		// add edit menu//
		menuBar.add(editMenu());

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmInfo = new JMenuItem("Info");
		mnHelp.add(mntmInfo);

	}

	// Create file menu//
	public JMenu fileMenu() {
		fileMenu = new JMenu("File");
		JMenuItem menuItemNewDatabase = new JMenuItem("New database");

		fileMenu.add(menuItemNewDatabase);

		JMenuItem menuItemOpenExistingDatabase = new JMenuItem("Open existing database");
		fileMenu.add(menuItemOpenExistingDatabase);

		JMenuItem menuItemClose = new JMenuItem("Close");
		fileMenu.add(menuItemClose);

		JMenuItem menuItemExit = new JMenuItem("Exit");

		fileMenu.add(menuItemExit);
		return fileMenu;
	}

	public JMenu editMenu() {
		editMenu = new JMenu("Edit");

		JMenuItem menuItemAddATable = new JMenuItem("Add a table");
		editMenu.add(menuItemAddATable);

		JMenuItem menuItemViewASpecific = new JMenuItem("View a specific table");
		editMenu.add(menuItemViewASpecific);

		JMenuItem menuItemNewMenuItem = new JMenuItem("Delete a table");
		editMenu.add(menuItemNewMenuItem);

		editMenu.add(tableMenu());
		return editMenu;

	}

	// Create table menu//
	public JMenu tableMenu() {

		JMenu menuChooseTable = new JMenu("Choose a table");

		menuChooseTable.add(viewMenu());

		JMenuItem menuItemSearch = new JMenuItem("Search ");
		menuChooseTable.add(menuItemSearch);

		JMenuItem menuItemSort = new JMenuItem("Sort");
		menuChooseTable.add(menuItemSort);

		menuChooseTable.add(addMenu());

		menuChooseTable.add(dataChangeMenu());

		menuChooseTable.add(deleteMenu());

		return menuChooseTable;
	}

	// menu view//
	public JMenu viewMenu() {
		JMenu menuView = new JMenu("Present data");

		JMenuItem menuItemViewColumn = new JMenuItem("View attrinute");
		menuView.add(menuItemViewColumn);

		JMenuItem menuItemViewLines = new JMenuItem("View lines");
		menuView.add(menuItemViewLines);

		return menuView;
	}

	// menu add //
	public JMenu addMenu() {
		JMenu menuAdd = new JMenu("Add");
		menuAdd.add(addAttributeMenu());
		menuAdd.add(addEntryMenu());

		// menuChooseTable.add(menuAdd);
		return menuAdd;

	}

	// add attribute menu//
	public JMenu addAttributeMenu() {
		JMenu menuAddAttribute = new JMenu("Add Attribute");

		JMenuItem menuItemCreateAtt = new JMenuItem("Create new attribute");
		menuAddAttribute.add(menuItemCreateAtt);

		JMenuItem menuItemCopyAtt = new JMenuItem("Copy an existing attribute");
		menuAddAttribute.add(menuItemCopyAtt);

		return menuAddAttribute;
	}

	// add entry menu//
	public JMenu addEntryMenu() {
		JMenu menuAddEntry = new JMenu("Add Entry");

		JMenuItem menuItemCreateEntry = new JMenuItem("Create new entry");
		menuAddEntry.add(menuItemCreateEntry);

		JMenuItem menuItemCopyEntry = new JMenuItem("Copy an existing attribute");
		menuAddEntry.add(menuItemCopyEntry);

		return menuAddEntry;

	}

	// change data menu//
	public JMenu dataChangeMenu() {

		JMenu menuChangeData = new JMenu("Change data");

		JMenuItem menuItemChangeManually = new JMenuItem("Change data manually");
		menuChangeData.add(menuItemChangeManually);

		JMenuItem menuItemReplaceElements = new JMenuItem("Replace elements of a line with elements from another line");
		menuChangeData.add(menuItemReplaceElements);

		JMenuItem menuItemReplaceEntry = new JMenuItem("Replace a line with another one");
		menuChangeData.add(menuItemReplaceEntry);

		return menuChangeData;
	}

	// delete menu//
	public JMenu deleteMenu() {
		JMenu menuDelete = new JMenu("Delete data");

		JMenuItem menuItemDeleteEntry = new JMenuItem("Delete a line");
		menuDelete.add(menuItemDeleteEntry);

		JMenuItem menuItemDeleteColumn = new JMenuItem("Delete a column");
		menuDelete.add(menuItemDeleteColumn);

		JMenuItem menuItemDeleteElement = new JMenuItem("Delete an element");
		menuDelete.add(menuItemDeleteElement);

		return menuDelete;
	}

}
