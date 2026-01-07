group slick_parser_templates;


innerClass(obj) ::=<<

//========================================================================
public class <obj.className> <if (obj.implements)>implements <obj.implements><endif>
	{
	private boolean _firstCall = true;
	
	<obj.properties:{prop | private <if (prop.attribute)>String<else>StringBuilder<endif> m_<prop.varName>;
}>
	<obj.properties:{prop | <if (!prop.attribute)>private List\<String\> m_<prop.varName>List = new ArrayList\<String\>();<endif>
}>
	<obj.objects:{subobj | private List\<<subobj.className>\> m_<subobj.varName>List = new ArrayList\<<subobj.className>\>();
private <subobj.className> m_<subobj.varName>;
private int m_<subobj.varName>Ref = 0;
}>
	<obj.properties:{prop |
<if (prop.attribute)>
public String get<prop.name>() { return (m_<prop.varName>); }

<else>

public String get<prop.name>()
	{
	return (m_<prop.varName>List.size() == 0 ? null : m_<prop.varName>List.get(0));
	}
	
public List\<String\> get<prop.name>List()
	{
	return (m_<prop.varName>List);
	}

<endif>
}>

	<obj.objects:{subobj | public List\<<subobj.className>\> get<subobj.className>List() { return (m_<subobj.varName>List); }
/**
	Convenience function for getting a single value
	@return <subobj.className>
*/
public <subobj.className> get<subobj.className>() 
	{
	if (m_<subobj.varName>List.size() == 0)
		return (null);
	else
		return (m_<subobj.varName>List.get(0));
	}
	
}>

	//------------------------------------------------------------------------
	protected void startElement(String uri, String localName, String qName, Attributes attrs)
		{
		if (_firstCall)
			{
			<obj.properties:{prop |
<if (prop.attribute)>
m_<prop.varName> = attrs.getValue("<prop.attrName>");

<endif>
<if (prop.grabBody)>
m_<prop.varName> = new StringBuilder();
_characterGrabber = m_<prop.varName>;

<endif>
}>
			_firstCall = false;
			return;
			}
			
		<obj.objects:{sobj |
if (m_<sobj.varName> != null) 
	{
	if (qName.equals("<sobj.tag>"))
		m_<sobj.varName>Ref ++;
		
	m_<sobj.varName>.startElement(uri, localName, qName, attrs);
	return;
	}
	
}>

		<obj.properties:{prop |
<if (prop.element)>
if (qName.equals("<prop.elementName>") <if (prop.conditional)>&&
		<prop.conditions:{cond | attrs.getValue("<cond.attrName>").equals("<cond.attrValue>")}; separator=" && "><endif>)
	{
	m_<prop.varName> = new StringBuilder();
	_characterGrabber = m_<prop.varName>;
	}

<endif>
}>
			
		<obj.objects:{sobj |
if (qName.equals("<sobj.tag>"))
	{
	m_<sobj.varName> = new <sobj.className>();
	m_<sobj.varName>List.add(m_<sobj.varName>);
	m_<sobj.varName>Ref = 1;
	
	m_<sobj.varName>.startElement(uri, localName, qName, attrs);
	}

}>
		}

	//------------------------------------------------------------------------
	protected void endElement(String uri, String localName, String qName)
			throws SAXException
		{
		<obj.properties:{prop |
<if (!prop.attribute)>
if (m_<prop.varName> != null)
	{
	m_<prop.varName>List.add(m_<prop.varName>.toString());
	m_<prop.varName> = null;
	}
	
<endif>
}>

		<obj.objects:{sobj |
if (m_<sobj.varName> != null)
	m_<sobj.varName>.endElement(uri, localName, qName);

if ((qName.equals("<sobj.tag>")) && ((--m_<sobj.varName>Ref) == 0))
	{
	m_<sobj.varName> = null;
	}

}>
		}
	}

>>



baseClass(parser, package) ::=<<
package <package>;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
	This file is a slickxml generated SAX parser.
	
	The following is the configuration used to create this file
	
	\<pre\>
	{@code
	<configuration>
}
	\</pre\>
*/
public class <parser.className> extends DefaultHandler
	{
	private SlickHandler _parserHandler;
	private StringBuilder _characterGrabber;
	
	<parser.objects:{obj | private <obj.className> m_<obj.varName>;
private int m_<obj.varName>Ref = 0;
}>
	
	/**
		Main constructor
		
		@param handler Handler class provided by you to receive the data objects
			created as the XML is parsed.
	*/
	public <parser.className>(SlickHandler handler)
		{
		_parserHandler = handler;
		}
		
	/**
		This is the interface implemented by you.  As the SAX parser processes the 
		XML data objects are created by the slickXML parser and passed to this 
		interface.
	*/
	public static interface SlickHandler
		{
		<parser.objects:{obj | public void parsed<obj.className>(<obj.className> entry) throws Exception;
}>
		}
		
	<parser.allObjects:innerClass(obj)>
		
	//========================================================================
	@Override
	public void characters(char[] ch, int start, int length)
		{
		if (_characterGrabber != null)
			_characterGrabber.append(ch, start, length);
		}
		
	//------------------------------------------------------------------------
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs)
			throws SAXException
		{
		<parser.objects:{obj |
<if (obj.attributesOnly)>
if (qName.equals("<obj.tag>"))
	{
	<obj.className> subObject = new <obj.className>();
	subObject.startElement(uri, localName, qName, attrs);
	try
		{
		//The object only contains attributes so we will return it now
		_parserHandler.parsed<obj.className>(subObject);
		}
	catch (Exception e)
		{
		throw new SAXException(e);
		}
	}


<else>
if (qName.equals("<obj.tag>"))
	{
	//This handles recursive nodes
	if (m_<obj.varName> != null)
		m_<obj.varName>Ref ++;
	else
		{
		m_<obj.varName> = new <obj.className>();
		m_<obj.varName>Ref = 1;
		}
	}

if (m_<obj.varName> != null)
	{
	m_<obj.varName>.startElement(uri, localName, qName, attrs);
	}


<endif>
}>
		}
	
	//------------------------------------------------------------------------
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
		{
		//Stop grabbing characters for any node.
		_characterGrabber = null;
		
		<parser.objects:{obj |
<if (!obj.attributesOnly)>
if ((qName.equals("<obj.tag>")) && ((--m_<obj.varName>Ref) == 0))
	{
	try
		{
		_parserHandler.parsed<obj.className>(m_<obj.varName>);
		}
	catch (Exception e)
		{
		throw new SAXException(e);
		}
		
	m_<obj.varName> = null;
	}
	
if (m_<obj.varName> != null)
	{
	m_<obj.varName>.endElement(uri, localName, qName);
	}

<endif>
}>
		} 
	}
	
>>
