# E-Baazar

## 1 To Run
--------
*	Need to check the build path
*	Need to connect to MySQL.
*	The user name and password are 1,1 and 2 2 if you get the previous database.

## 2 Function
--------
*	Online Shopping System in JavaFX: Used RUP, MVC and Inverse of Control learned in Software Engineering, implemented an on-line shopping system which includes logging in and authorization, call back function,JUnit test and rules engine implementation. Java, JSP, Servlet and JDBC are used.

## 3 Features of the E-Bazaar MainProject Distribution
---

1.	Controller classes contain the listeners. This strategy implements the philosophy that “controllers should be in control”. Rather than placing listeners in the Gui class that triggers them – which would, in effect, place control in the Gui – we place them in the appropriate Controller class. In this way, the responsibility of handling of important events, which involves controlling the flow of the application, is given to the appropriate Controller class.

2.	Use and implementation of the Singleton Pattern. The primary controller classes, the SessionContext, and the ShoppingCartSubsystemFacade have all been implemented as singletons (see class documentation for each). The implementation style is based on the recommendation of Effective Java, 2nd edition, which explains why the use of an enum for this purpose is the best practice.

3.	Use of the Observer Pattern. The Observer Pattern allows you to register with an event-maker so that you will be informed when an event is fired from this event-maker. This pattern is used here to keep the states of the combo boxes in MaintainProductCatalog and AddEditCatalog synchronized.

4.	Use of the Callback Pattern. The LoginControl class is always invoked by one of the other Controller classes whenever the next screen requested requires user authentication. After authentication succeeds, LoginControl needs to make the next logical screen visible to the user. The initiating Controller class passes this next screen in to the LoginControl constructor (as a generic Window), so LoginControl can simply make this window visible after authentication. Howver, if this Window needs to be populated with data, LoginControl will have no way to determine what the data should be. To solve this, LoginControl provides a second constructor that allows the Controller to pass itself in, typed as a “Controller” (an interface type), having a single method doUpdate(). When it is time for LoginControl to make the next Window visible, it instead calls the interface method doUpdate() to give the original Controller class the opportunity to populate this next Window in the intended way. 

5.	Data Access Subsystem and Rules Subsystem. These subsystems support access to the databases and access to the Rules Engine, respectively. These each require their own kind of command objects. For the Data Access Subsystem, entities that need to be saved or read are encapsulated in an instance of a realization of IDbClass. For the Rules Subsystem, entities that need to be validated are encapsulated in a corresponding DynamicBean which in turn is encapsulated in a realization of IRules. (Note: All concrete instances of DynamicBean are stored in the package rulesbeans.)

6.	SimpleConnectionPool. This is an implementation of the Connection Pool pattern and supports connection reuse and access to multiple connections. This is part of the Data Access subsystem.

7.	Properties files. Configuration data for the databases and the rules engines have been stored in properties files. These files are accessed via two classes DbConfigProperties and RulesConfigProperties. To access these value objects, their respective enum’s for accessing keys need to be used (namely, DbConfigKey and RulesConfigKey).

8.	TwoKeyHashmap. This datastructure is introduced to support the idea of using either of two keys to look up a value in a table in memory. This is used to permit lookup of a Product object either by name or by product id.

9.	Utility classes. Utility classes are provided in the application layer and are recommended to be included in each of the business layer subsystems; a utility class is also provided for the Data Access subsystem. These provide convenient locations for manipulating data containers and performing other utility operations.

10.	Custom exception classes. As an implementation of the E-Bazaar error-handling strategy, there are 5 custom exception classes provided. All possible exceptions that could occur are mapped to one of these.

11.	Location of interfaces. Interfaces for a layer have been placed in a package called externalinterfaces.

12.	SessionContext. Data that needs to be globally accessible to the application are stored in the SessionContext object, which is a static wrapper for a hashtable. In particular, the fact that a user has logged in, along with the generated Customer subsystem object, are stored in the SessionContext.

13.	Resources folder. A standard way to make resources such as text files, images, and properties files, available to the application is to place them in a resources folder. This has been done for the E-Bazaar project. Text files that provide messages to the user for certain screens; the E-Bazaar splash screen image; and the data access and rules engine configuration properties files have all been placed in this folder.

14.	Logging. The jdk Logger class is being used to write logs as the application executes. The Logger class can be used to write debug statements, info statements, and to indicate warnings and errors. 
