## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## To run Code

Demo run through of this set up at the Library Database Runthrough Setup video.<br>
Did not demo updating a the jdk/jre if needed. Follow that video instead: https://youtu.be/ZIYDvg3fVWA

You need vscode and mysql-connector-j downloaded. mysql-connector-j version used: 8.3.0. Update vscode to latest version too. Use JDK 22 and above. Have MYSQL installed of course.

Clone the github repository in vscode. Best keep it in Downloads/default repository for simplicity. Ours was Downloads. Otherwise, good luck.

Have MYSQL shell and Java installed in vscode.

For MYSQL Shell, create new connection:<br>
&Tab;Caption: Localhost 8.0<br>
&Tab;Host Name or IP Address: localhost<br>
&Tab;User Name: root (otherwise _YOUR_ MYSQL username)<br>
&Tab;Password: Click Store password to set the password of the connection to _YOUR_ password that you use for your MYSQL.

After everything, a DB Notebook should be created. Under the tab called Database Connections, all your sql tables that you created should have appeared.

Go back to Explorer tab with all the source code. Under the tab called Java Projects, there should be a folder already there called LibraryDatabase from when you cloned it. Another sub folder called Referenced Libraries should be present at the bottom after the other subfolders, src 100% sure and JRE System Library or JDK. Press the "+" sign on Referenced Libraries to add the mysql-connection-j jar file in the mysql-connection-j folder. If it's already added, then you're good. But you can always delete it with a minus sign to the right, and then add it in again with the plus sign.

If you know you're under JDK 22, download that from https://www.oracle.com/java/technologies/downloads/<br>
To apply it to the project, same place as Java Projects where you just added mysql-connection-j folder, right click on "LibraryDatabase" folder, go to Class Configure. Go to tab of JDK Runtime, add your JDK 22 file. You might have to close and reload up the editor first though, especially if jdk file keeps switching back to previous one even if you had already added it.

Find and change all the code in the classes:

    From: public static final String SQLPassword = "329761";
    To: Put your own MYSQL password in there instead of 329761

Best to use the search icon magnifying glass located under the explorer section. Just search "329761" to find it easily.

The classes should be: AllMembersPage, ArticlesPage, BooksPage, LibrarianPage, LibraryPage, LoginPage, MemberPage, MoviesPage, RegisterPage, TransactionPage.

In the src code, in the "database" folder, is a file with all the sql code called library. Copy the whole file and go back to the DB Notebook in MYSQL Shell.

Make sure DB Notebook is running. It should look like your MYSQL Command Line Client. And if it doesn't, you've got a local problem and need to learn how to connect your MYSQL to vscode.
Copy paste all the library file into the DB Notebook MYSQL command line that's running. If need to be 100% sure, "drop database library;" first before copy pasting the library file into the command line.

Now go back to src code and find "LoginPage". Click "Run" symbol at the top right corner and a window with LibraryDatabase and login requests should appear.

Use User1/User2/User3 for logging in as users. Lib1 or Lib2 or Lib3 for logging in as admin.<br>
123 is password for all. Unless you register and do your own usernames & passwords.
