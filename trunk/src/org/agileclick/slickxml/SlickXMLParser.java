package org.agileclick.slickxml;

import java.util.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
	This file is a slickxml generated SAX parser.
	
	The following is the configuration used to create this file
	
<parser name="SlickXMLParser">
		<object tag="slickxml" name="Header">
			<property name="Package">
				<attribute>package</attribute>
			</property>
			<property name="Destination">
				<attribute>destination</attribute>
			</property>
		</object>
		
		<object tag="parser" name="Parser">
			<property name="Name">
				<attribute>name</attribute>
			</property>
				
			<object tag="object" name="DataObject">
				<property name="Tag">
					<attribute>tag</attribute>
				</property>
				<property name="Name">
					<attribute>name</attribute>
				</property>
				<property name="Reference">
					<attribute>reference</attribute>
				</property>
				
				<object tag="property" name="Property">
					<property name="Name">
						<attribute>name</attribute>
					</property>
					<property name="Attribute">
						<element name="attribute"/>
					</property>
					
					<object tag="element" name="Element">
						<property name="Name">
							<attribute>name</attribute>
						</property>
						
						<object tag="condition" name="Condition">
							<property name="Name">
								<attribute>name</attribute>
							</property>
							<property name="Value">
								<attribute>value</attribute>
							</property>
						</object>
					</object>
				</object>
				
				<object reference="DataObject"/>
			</object>
		</object>
		
	</parser>
*/
public class SlickXMLParser extends DefaultHandler
	{
	private SlickHandler _parserHandler;
	private StringBuilder _characterGrabber;
	
	private Header m_Header;
	private int m_HeaderRef = 0;
	private Parser m_Parser;
	private int m_ParserRef = 0;

	
	/**
		Main constructor
		
		@param handler Handler class provided by you to receive the data objects
			created as the XML is parsed.
	*/
	public SlickXMLParser(SlickHandler handler)
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
		public void parsedHeader(Header entry) throws Exception;
		public void parsedParser(Parser entry) throws Exception;

		}
		

	//========================================================================
	public class Property
		{
		private boolean _firstCall = true;
		
		private String m_Name;
		private StringBuilder m_Attribute;

		private List<Element> m_ElementList = new ArrayList<Element>();
		private Element m_Element;
		private int m_ElementRef = 0;

		public String getName() { return (m_Name); }

		public String getAttribute()
			{
			return (m_Attribute == null ? null : m_Attribute.toString());
			}


		public List<Element> getElementList() { return (m_ElementList); }
		/**
			Convenience function for getting a single value
		*/
		public Element getFirstElement() 
			{
			if (m_ElementList.size() == 0)
				return (null);
			else
				return (m_ElementList.get(0));
			}
			


		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Name = attrs.getValue("name");

				_firstCall = false;
				return;
				}
				
			if (m_Element != null) 
				{
				if (localName.equals("element"))
					m_ElementRef ++;
					
				m_Element.startElement(uri, localName, qName, attrs);
				return;
				}
				


			if (localName.equals("attribute") )
				{
				m_Attribute = new StringBuilder();
				_characterGrabber = m_Attribute;
				}

				
			if (localName.equals("element"))
				{
				m_Element = new Element();
				m_ElementList.add(m_Element);
				m_ElementRef = 1;
				
				m_Element.startElement(uri, localName, qName, attrs);
				}


			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			if ((localName.equals("element")) && ((--m_ElementRef) == 0))
				{
				m_Element = null;
				}
					
			if (m_Element != null)
				m_Element.endElement(uri, localName, qName);


			}
		}

	//========================================================================
	public class Parser
		{
		private boolean _firstCall = true;
		
		private String m_Name;

		private List<DataObject> m_DataObjectList = new ArrayList<DataObject>();
		private DataObject m_DataObject;
		private int m_DataObjectRef = 0;

		public String getName() { return (m_Name); }


		public List<DataObject> getDataObjectList() { return (m_DataObjectList); }
		/**
			Convenience function for getting a single value
		*/
		public DataObject getFirstDataObject() 
			{
			if (m_DataObjectList.size() == 0)
				return (null);
			else
				return (m_DataObjectList.get(0));
			}
			


		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Name = attrs.getValue("name");

				_firstCall = false;
				return;
				}
				
			if (m_DataObject != null) 
				{
				if (localName.equals("object"))
					m_DataObjectRef ++;
					
				m_DataObject.startElement(uri, localName, qName, attrs);
				return;
				}
				


				
			if (localName.equals("object"))
				{
				m_DataObject = new DataObject();
				m_DataObjectList.add(m_DataObject);
				m_DataObjectRef = 1;
				
				m_DataObject.startElement(uri, localName, qName, attrs);
				}


			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			if ((localName.equals("object")) && ((--m_DataObjectRef) == 0))
				{
				m_DataObject = null;
				}
					
			if (m_DataObject != null)
				m_DataObject.endElement(uri, localName, qName);


			}
		}

	//========================================================================
	public class Condition
		{
		private boolean _firstCall = true;
		
		private String m_Name;
		private String m_Value;

		public String getName() { return (m_Name); }
		public String getValue() { return (m_Value); }



		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Name = attrs.getValue("name");
				m_Value = attrs.getValue("value");

				_firstCall = false;
				return;
				}
				

				
			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			}
		}

	//========================================================================
	public class DataObject
		{
		private boolean _firstCall = true;
		
		private String m_Tag;
		private String m_Name;
		private String m_Reference;

		private List<Property> m_PropertyList = new ArrayList<Property>();
		private Property m_Property;
		private int m_PropertyRef = 0;
		private List<DataObject> m_DataObjectList = new ArrayList<DataObject>();
		private DataObject m_DataObject;
		private int m_DataObjectRef = 0;

		public String getTag() { return (m_Tag); }
		public String getName() { return (m_Name); }
		public String getReference() { return (m_Reference); }


		public List<Property> getPropertyList() { return (m_PropertyList); }
		/**
			Convenience function for getting a single value
		*/
		public Property getFirstProperty() 
			{
			if (m_PropertyList.size() == 0)
				return (null);
			else
				return (m_PropertyList.get(0));
			}
			
		public List<DataObject> getDataObjectList() { return (m_DataObjectList); }
		/**
			Convenience function for getting a single value
		*/
		public DataObject getFirstDataObject() 
			{
			if (m_DataObjectList.size() == 0)
				return (null);
			else
				return (m_DataObjectList.get(0));
			}
			


		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Tag = attrs.getValue("tag");
				m_Name = attrs.getValue("name");
				m_Reference = attrs.getValue("reference");

				_firstCall = false;
				return;
				}
				
			if (m_Property != null) 
				{
				if (localName.equals("property"))
					m_PropertyRef ++;
					
				m_Property.startElement(uri, localName, qName, attrs);
				return;
				}
				
			if (m_DataObject != null) 
				{
				if (localName.equals("object"))
					m_DataObjectRef ++;
					
				m_DataObject.startElement(uri, localName, qName, attrs);
				return;
				}
				


				
			if (localName.equals("property"))
				{
				m_Property = new Property();
				m_PropertyList.add(m_Property);
				m_PropertyRef = 1;
				
				m_Property.startElement(uri, localName, qName, attrs);
				}

			if (localName.equals("object"))
				{
				m_DataObject = new DataObject();
				m_DataObjectList.add(m_DataObject);
				m_DataObjectRef = 1;
				
				m_DataObject.startElement(uri, localName, qName, attrs);
				}


			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			if ((localName.equals("property")) && ((--m_PropertyRef) == 0))
				{
				m_Property = null;
				}
					
			if (m_Property != null)
				m_Property.endElement(uri, localName, qName);

			if ((localName.equals("object")) && ((--m_DataObjectRef) == 0))
				{
				m_DataObject = null;
				}
					
			if (m_DataObject != null)
				m_DataObject.endElement(uri, localName, qName);


			}
		}

	//========================================================================
	public class Element
		{
		private boolean _firstCall = true;
		
		private String m_Name;

		private List<Condition> m_ConditionList = new ArrayList<Condition>();
		private Condition m_Condition;
		private int m_ConditionRef = 0;

		public String getName() { return (m_Name); }


		public List<Condition> getConditionList() { return (m_ConditionList); }
		/**
			Convenience function for getting a single value
		*/
		public Condition getFirstCondition() 
			{
			if (m_ConditionList.size() == 0)
				return (null);
			else
				return (m_ConditionList.get(0));
			}
			


		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Name = attrs.getValue("name");

				_firstCall = false;
				return;
				}
				
			if (m_Condition != null) 
				{
				if (localName.equals("condition"))
					m_ConditionRef ++;
					
				m_Condition.startElement(uri, localName, qName, attrs);
				return;
				}
				


				
			if (localName.equals("condition"))
				{
				m_Condition = new Condition();
				m_ConditionList.add(m_Condition);
				m_ConditionRef = 1;
				
				m_Condition.startElement(uri, localName, qName, attrs);
				}


			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			if ((localName.equals("condition")) && ((--m_ConditionRef) == 0))
				{
				m_Condition = null;
				}
					
			if (m_Condition != null)
				m_Condition.endElement(uri, localName, qName);


			}
		}

	//========================================================================
	public class Header
		{
		private boolean _firstCall = true;
		
		private String m_Package;
		private String m_Destination;

		public String getPackage() { return (m_Package); }
		public String getDestination() { return (m_Destination); }



		//------------------------------------------------------------------------
		protected void startElement(String uri, String localName, String qName, Attributes attrs)
			{
			if (_firstCall)
				{
				m_Package = attrs.getValue("package");
				m_Destination = attrs.getValue("destination");

				_firstCall = false;
				return;
				}
				

				
			}

		//------------------------------------------------------------------------
		protected void endElement(String uri, String localName, String qName)
				throws SAXException
			{
			}
		}

		
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
		if (localName.equals("slickxml"))
			{
			Header subObject = new Header();
			subObject.startElement(uri, localName, qName, attrs);
			try
				{
				//The object only contains attributes so we will return it now
				_parserHandler.parsedHeader(subObject);
				}
			catch (Exception e)
				{
				throw new SAXException(e);
				}
			}

		if (localName.equals("parser"))
			{
			//This handles recursive nodes
			if (m_Parser != null)
				m_ParserRef ++;
			else
				{
				m_Parser = new Parser();
				m_ParserRef = 1;
				}
			}

		if (m_Parser != null)
			{
			m_Parser.startElement(uri, localName, qName, attrs);
			}


		}
	
	//------------------------------------------------------------------------
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
		{
		//Stop grabbing characters for any node.
		_characterGrabber = null;
		
		if ((localName.equals("parser")) && ((--m_ParserRef) == 0))
			{
			try
				{
				_parserHandler.parsedParser(m_Parser);
				}
			catch (Exception e)
				{
				throw new SAXException(e);
				}
				
			m_Parser = null;
			}
			
		if (m_Parser != null)
			{
			m_Parser.endElement(uri, localName, qName);
			}

		} 
	}
	
