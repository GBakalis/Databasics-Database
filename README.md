# Database Universe

## Prerequisites

The program runs from the cmd, using `java -jar DatabaseUniverse-1.0-SNAPSHOT`.
Notice: You have to be inside the DatabasebyDatabasics folder, where the .jar file is located.

## Program capabilities

The code has been developed linearly, with a dependency hierarchy architecture. The program is designed of a multi-level menu, which follows that hierarchy. A quick brief on the Classes' hierarchy, which also explains how the menus show when running the program:

- `DatabaseUniverse`: Handles all databases created by the user in the user's system (saving, importing, checking existence)
- `Database`: Handles all tables of a database. Performs all actions which take place in the database level (adding/deleting tables, copying/cutting and pasting data accross tables of the database, saving etc.)
- `Table`: Handles all attributes of a table. Performs all actions which take place in the table level (adding/deleting attributes, searching, viewing etc.)
- `Attribute`: Handles the data. it is mainly responsible for all the editing that takes place in the databases through our program.

Following our main classes' hierarchy, there is also a class `CommandLineMenu` which holds all the different menus that are being viewed and takes care of jumping from one level to another (two-way movement). We could succintly sum the menu up as follows:

- On start-up, the user sees a manu with choices that have to do with all the databases in his system. Again, the menus are in the same sequence as the classes
- The user can create or delete a database, list the databases, or select one to continue. When they do, the next menu has the different funcions that the user can perform on a database, including adding, viewing or deleting a table, listing all tables, saving the database or selecting a table to continue.
- After they select a table, they can search or sort the table, view, add, change or delete data, or add an attribute to the table. Whichever choice they make, another pop-up menu shows up giving them alternatives based on their previous choice. In every menu level, the user can move to an upper level or cancel their action.

For more info on the source code and its functionality, please refer to the Documentation, included in the folder of the same name.

## Built with

- [Maven](https://maven.apache.org/) \- Dependency Management
- [Eclipse](https://www.eclipse.org/) \- IDE
- Love
