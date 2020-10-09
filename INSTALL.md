# Installations

## Requirements

- Git
- Maven
- jdk >= 1.8

## Environment variables
 - If you don't have Java as an environment variable, follow these instructions :
 
 From the Start menu of your computer, right-click on Computer and select Properties.

   In the Control Panel Home window, click Advanced system settings.

   In the System Properties window, click Environment Variables....

   Under System Variables, click New... to create a variable. Name the variable JAVA_HOME, enter the path to your Java JRE 8, and then click OK.

   Example path to the default JRE: C:\Program Files\Java\jre1.8.0_77.
  
  - If you don't have Maven as an environment variable, follow these instructions :
  
  From the Start menu of your computer, right-click on Computer and select Properties.

   In the Control Panel Home window, click Advanced system settings.

   In the System Properties window, click Environment Variables....

   Under System Variables, click New... to create a variable. Name the variable MAVEN_HOME, enter the path to your Maven File, and then click OK.

   Example path :  C:\Man\apache-maven-3.3.9-bin\apache-maven-3.3.9\bin

## Generic installation
Clone the project with Git using the following command.

```
git clone https://github.com/Nrkz/PDLProject.git
```

Change directory to be in the project.


```
cd PDLProject
```

There is two files in the root folder you can edit to parametrized the extractor : 
- Use wikipedia_links_list.txt file to stock the urls you need to extract. 
- Use save_path.txt file to change the saving folder (make sure to put a right path). 

### Launch the project with maven
Use the following commands.

```
mvn compile
mvn exec:java -Dexec.mainClass=pdl.wiki.WikipediaMatrix
```

### Launch test with maven
Use the following command.

```
mvn test
```

## Installation with Eclipse IDE
Try with Eclipse Version: 2019-09 R (4.13.0).

Import the project,

```
File > Import...
```

Then select,

```
Maven > Existing Maven Projects
```

Browse to the project's directory and select the pom.xml.

### Launch project

Right-click in the project,

```
Run As > Java Application
```

### Launch test
Right-click in the project, 

```
Run As > Maven test
```

## Installation with Intellij IDEA
Try with Intellij IDEA Commmunity 2019.2.3.

Import the project, select the pom.xml file and click "Next" untill you can click Finish

### Launch project

Navigate in the project explorer,

```
src > main > java > pdl.wiki
```

Right-click to the class WikipediaMatrix,

```
Run 'WikipediaMatrix:main()'
```

### Launch test

Right-click in the project, 

```
Run 'All Tests'
```

## Installation with NetBeans

Try with Apache NetBeans IDE 11.0.

```
File > Open Project...
```

And select the pom.xml file.

### Launch project

Right-click to the Project,

```
Run 
```

And select main class for execution : pdl.wikiWikipediaMatrix.

### Launch test

Right-click in the project,

```
Test
```
