# slickxml

Super simple way to parse long XML documents

Have you ever wanted to process a large XML document but, the thought of implementing 
the SAX callbacks seemed too tedious?  Maybe you don't even care about all the elements in the document?

With SlickXML you define data to pick out of the XML in the form of objects. 
You use SlickXML to generate a SlickXMLParser class that acts as a SAX event handler. 
You provide the SlickXMLParser a callback interface and it hands you back objects of data as the XML is being parsed.

How is this better than DOM? 
1. It uses SAX so it does not load the whole document in memory. 
1. It lets you cherry pick what parts of the XML to return in your object.

How to get started 
1. Create an xml config file  
1. Generate parser 
1. Include parser in your code

## How to run

First you need a configuration file, there is a sample one in the [source](test/parser_config.xml).

Then you run:

`java -jar slickxml.jar your_config.xml`

Thats it.

## Using Parser

YourSlickParser - this is the SlickXml parser that was generated

slickHandler - your handler for receiving the slick objects

```java 
XMLReader xmlParser = XMLReaderFactory.createXMLReader(); 
YourSlickParser genHandler = new YourSlickParser(slickHandler); 
xmlParser.setContentHandler(genHandler);

InputSource source = new InputSource(new FileInputStream(xmlFile)); 
xmlParser.parse(source);
```


## Config File

First have a look at the sample [configuration file](test/parser_config.xml) found in the project.

Also have a look at this mapping of a configuration file to the xml that it will parse.

![Mapping](config_xml_map.png)

The above example is a parser for extracting data from a TBX xml file.

### Example 1
Lets say we want to parse the following simple xml document 

```xml 
<books> 
    <book name="Lord of the Rings" author="JRR Tolken"/> 
    <book name="Fahrenheit 451" author="Ray Bradbury"/> 
    <book name="Facing Codependence" author="Pia Mellody"/> 
</books>
```

That is a great set of books! Now lets parse it, or better yet let's create a 
config file so SlickXML can create a parser for us. What we would like is for 
the parser to hand back each book to us as an object so our code can place the book in a database.

First lets start with the basic outline of the config file. 
This defines the package name to use and where to place the java file. 

```xml
<slickxml package="bookprocessor.parsers" destination="src/java/bookprocessor/parsers"> 
    ... 
</slickxml>
```

#### Parser
Now we want to add a parser for our books. The class name for the parser will be BookParser 
```xml
<slickxml package="bookprocessor.parsers" destination="src/java/bookprocessor/parsers"> 
    <parser name="BookParser"> 
    </parser> 
</slickxml>
```

#### Object
Now we will define the object to be returned to us. 
```xml
...
<object name="Book" tag="book">
</object>
``` 
The object it will return is Book or more precisely it will be an instance of `BookParser.Book` 
in the package `bookprocessor.parsers`. Notice we didn't have to specify the root element. 
The parser basically goes through the file ignoring everything until it hits an 
element that matches one of its root objects, in this case book.

#### Property
Now to define two properties on the object to contain each of the attributes 
```xml
<object ...>
    <property name="Name">
        <attribute>name</attribute>
    </property>
    <property name="Author">
        <attribute>author</attribute>
    </property>

</object>
``` 
The `property` elements tell the parser to add a member variables to the Book class. 
The value for the two member variables will come from the specified attributes 
that are on the book element in the xml. Now because this object only defines 
properties that are attributes of the start tag, the object will be returned 
immediately after the start element has been processed. In this case it isn't 
a big deal because the book elements don't contain a body but, if they did it 
would make a difference.

Entire example
```xml
<slickxml package="bookprocessor.parsers" destination="src/java/bookprocessor/parsers">
	<parser name="BookParser">
		<object name="Book" tag="book">
			<property name="Name">
				<attribute>name</attribute>
			</property>
			<property name="Author">
				<attribute>author</attribute>
			</property>
		</object>
	</parser>
</slickxml>
```

### Example 2
Now lets make that XML a little more complicated and introduce a new concept
```xml
<books> 
    <book name="Lord of the Rings" author="JRR Tolken"> 
        <isbn>123456</isbn> 
    </book> 
    <book name="Fahrenheit 451" author="Ray Bradbury"> 
        <isbn>123454</isbn> 
    </book> 
    <book name="Facing Codependence" author="Pia Mellody"> 
        <isbn>123452</isbn> 
    </book> 
</books>
```

Now there is a child element that we want to get the value of. Again we use the 
property tag but define the content as an element: 
```xml
 <object name="Book" tag="book">
	<property name="Name">
		<attribute>name</attribute>
	</property>
	<property name="Author">
		<attribute>author</attribute>
	</property>
	<property name="ISBN">
		<element name="isbn"/>
	</property>
</object>
``` 

Now if the parser hits any child element of book that equals isbn it will be added 
as a property to the Book class.  Because elements can occur more than once, 
properties that are defined using the element tag can be retrieved one of two 
ways from the returned object. In our case the Book object will have two get 
methods for ISBN: `Book.getISBN()` and `Book.getISBNList()`. The first is a 
convenience method that returns the first ISBN in the list, where the second 
returns the entire list in the order that they were parsed.

#### Condition
There is one more concept with the property element and that is conditions. 
A condition is used inside an element to check for a certain attribute before 
grabbing the text of the element. To explain this we will change up the XML a bit:
```xml
<books> 
    <book name="Lord of the Rings" author="JRR Tolken"> 
        <isbn type="old">123456</isbn> 
        <isbn type="new">123456-123</isbn> 
    </book> 
    ... 
</books>
```
Now there are two different isbn values and the difference is specified in the 
attribute `type`. We can separate those into two different properties with the 
following configuration change: 
```xml
<object name="Book" tag="book">
    ...
    <property name="ISBNOld">
        <element name="isbn">
            <condition name="type" value="old"/>
        </element>
    </property>
    <property name="ISBNNew">
        <element name="isbn">
            <condition name="type" value="new"/>
        </element>
    </property>
</object>
``` 

We can add as many conditions as we want, they will be AND'd to gether in the code.

#### Inner Object
You can define an inner object just as you did the object above, you place it within another object definition.

```xml
<object name="Book" tag="book">
    ...
    <object name="AuthorAddress" tag="address">
    ...
    </object>

</object>
``` 

In this case Book now contains a member list for AuthorAddress objects and getter 
methods just like the ones for element properties.

#### Recursive Object
Now lets get crazy with the xml. What if the book element contain within it another 
book element like this: 
```xml 
<books> 
    <book name="Lord of the Rings" author="JRR Tolken"> 
        <book name="The Two Towers" author="JRR Tolken"/> 
        <book name="Return of the King" author="JRR Tolken"/> 
    </book> 
    ... 
</books> 
```
Basically we want the parser to look for recursive objects and here is how we do it. 
```xml
<object name="Book" tag="book">
    ...
    <object reference="Book"/>

</object>
```

Now the Book class contains getters for a list of Book objects.