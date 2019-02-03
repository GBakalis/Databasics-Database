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

- On start-up, the user sees a menu with choices that have to do with all the databases in his system. Again, the menus are in the same sequence as the classes
- The user can create or delete a database, list the databases, or select one to continue. When they do, the next menu has the different funcions that the user can perform on a database, including adding, viewing or deleting a table, listing all tables, saving the database or selecting a table to continue.
- After they select a table, they can search or sort the table, view, add, change or delete data, or add an attribute to the table. Whichever choice they make, another pop-up menu shows up giving them alternatives based on their previous choice. In every menu level, the user can move to an upper level or cancel their action.

Quick tips:

- To cancel an action type 'cancel' and press Enter.
- Yes or no questions are not case-sensitive.
- In an entry insertion, if you don't want to insert a value in a column, insert '--' and no checking error will occur.

File Import Format:

Case #1: If the file has been created by this application and is saved within the DatabaseUniverseTree, it should consist of the following:
First Line: Name of the table
Second Line: Type of each column (string/char/int/double/date), including type for Last Modified column (date).
Third Line: Name of each column. Last column should be named Last Modified.
Rest Lines: The elements of each column, including the timestamps. ATTENTION: A message will occur for each type mismatch, and the value will be replaced with '--' in the program.

Case #2: If the file has been created by the user, it should not be saved within the DatabaseUniverseTree and should consist of the following:
First Line: Name of the table
Second Line: Type of each column (string/char/int/double/date).
Third Line: Name of each column. 
Rest Lines: The elements of each column, including the timestamps. ATTENTION: A message will occur for each type mismatch, and the value will be replaced with '--' in the program.
For more info on the source code and its functionality, please refer to the Documentation, included in the folder of the same name.
Last Modified column will be created within the app and will produce new timestamps.

## Built with

- [Maven](https://maven.apache.org/) \- Dependency Management
- [Eclipse](https://www.eclipse.org/) \- IDE
- Love
