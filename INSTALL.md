# Installations

## Requirements

- Git
- Maven
- jdk >= 1.8


## Generic installation
Clone the project with Git using the following command.

```
git clone https://github.com/Nrkz/PDLProject.git
```

Change directory to be in the project.


```
cd PDLProject
```

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

Browse directory where here is the project and select the pom.xml.

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

Import the project, select the pom.xml file and next the steps.

### Run project

Navigate in the project explorer,

```
src > main > java > pdl.wiki
```

Right-click to the class WikipediaMatrix,

```
Run 'WikipediaMatrix:main()'
```

### Run test

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

### Run project

Right-click to the Project,

```
Run 
```

And select main class for execution : pdl.wikiWikipediaMatrix.

### Run test

Right-click in the project,

```
Test
```
