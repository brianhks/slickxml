Have you ever wanted to process a large XML document but, the thought of implementing the SAX callbacks seemed too tedious?  Maybe you don't even care about all the elements in the document?

With SlickXML you define data to pick out of the XML in the form of objects.  You use SlickXML to generate a SlickXMLParser class that acts as a SAX event handler.  You provide the SlickXMLParser a callback interface and it hands you back objects of data as the XML is being parsed.

How is this better than DOM?
  1. It uses SAX so it does not load the whole document in memory.
  1. It lets you cherry pick what parts of the XML to return in your object.

How to get started
  1. [Create a config file](config_file.md)
  1. [Generate parser](how_to_run.md)
  1. [Include parser in your code](UsingParser.md)