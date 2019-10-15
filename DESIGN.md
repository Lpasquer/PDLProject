Design
======


## Project Architecture

WikipediaMatrix is composed of two main packages. One with main source code called src/main/java and an other one with tests class called src/test/java.

### Source code

The first package contains Extractor.java, an interface implemented by two class HTMLExtractor.java and WikiTextExtractor.java. They allow the conversion of tables from HTML and WikiText pages to CSV format. The class page.java is used to recover the title from the wikipedia page. To parse the page to be processed we use the abstract PageChecker.java class in the URL.java class. Its check the links and if there are tables in the page. WikipediaMatrix.java is the main class with the user interface.

## Diagrams

### Class diagram

![](/media/Class_Diagram.png)

### Use Case

![](/media/Use_Case.jpg)
- The user can enter a Wikipedia link in the application to retrieve the tables from this page. He can add and remove several.
- The user chooses to save the tables or not.
- The user changes the tables backup directory.

### Sequence Diagram

![](/media/Sequence_Diagram.jpg)

We have completed the sequence diagram for the Use Case « Add Link » . To begin with, we use an addLink() function and used the WikipediaMatrix class. This class requests the url of the Wikipedia page. Once the link is given, we create an Url object that itself uses the Pagechecker class. We use the urlChecker function which returns the tables present on the Wikipedia page. After using the Count Wikitable instruction, the array number is retrieved. Add the url to the Wikipediamatrix url list and return the number of array found to the user.
