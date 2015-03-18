YourSlickParser - this is the SlickXml parser that was generated

slickHandler - your handler for receiving the slick objects

```
XMLReader xmlParser = XMLReaderFactory.createXMLReader();
YourSlickParser genHandler = new YourSlickParser(slickHandler);
xmlParser.setContentHandler(genHandler);

InputSource source = new InputSource(new FileInputStream(xmlFile));
xmlParser.parse(source);
```