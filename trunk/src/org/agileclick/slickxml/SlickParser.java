package org.agileclick.slickxml;

import org.agileclick.slickxml.data.*;

/* import org.dom4j.io.SAXReader;
import org.dom4j.*; */

import org.xml.sax.helpers.*;
import org.xml.sax.*;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.*;

import org.antlr.stringtemplate.*;

public class SlickParser implements SlickXMLParser.SlickHandler
	{
	private String m_package;
	private String m_destination;
	private String m_configString;  //Configuration file as a string
	
	private StringTemplate m_baseClassTemplate;
	
	private Map<String, DataObject> m_allObjects;
	
	public SlickParser()
		{
		m_allObjects = new HashMap<String, DataObject>();
		}
	
	//---------------------------------------------------------------------------
	/* private void parseObject(Element elem, DataObject dObj)
			throws SlickParserException
		{
		Iterator propIt = elem.selectNodes("property").iterator();
		while (propIt.hasNext())
			{
			Element propElem = (Element)propIt.next();
			Property prop = new Property(propElem.attributeValue("name"));
			
			Element child = propElem.element("attribute");
			if (child != null)
				{
				prop.setAttrName(child.getText());
				}
			else
				{
				child = propElem.element("element");
				prop.setElementName(child.attributeValue("name"));
				
				Iterator conIt = child.elementIterator("condition");
				while (conIt.hasNext())
					{
					Element conElem = (Element)conIt.next();
					prop.addCondition(new Condition(conElem.attributeValue("name"),
							conElem.attributeValue("value")));
					}
					
				}
			
			dObj.addProperty(prop);
			}
			
		Iterator objIt = elem.selectNodes("object").iterator();
		while (objIt.hasNext())
			{
			Element objElem = (Element)objIt.next();
			
			String reference = objElem.attributeValue("reference");
			if (reference == null)
				{
				DataObject dObject = new DataObject(objElem.attributeValue("tag"),
						objElem.attributeValue("name"));
						
				dObj.addObject(dObject);
				m_allObjects.put(dObject.getClassName(), dObject);
				
				parseObject(objElem, dObject);
				}
			else
				{
				DataObject dObject = m_allObjects.get(reference);
				if (dObject == null)
					throw new SlickParserException("Unable to locate reference '"+reference+"'");
					
				dObj.addObject(dObject);
				}
			}
			
		}
	
	//---------------------------------------------------------------------------
	public void generateFiles(String xmlFile)
			throws DocumentException, IOException, SlickParserException, SAXException
		{
		StringTemplateGroup templateGroup = new StringTemplateGroup(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("template/Parser.java")));
				
		StringTemplate baseClassTemplate = templateGroup.getInstanceOf("baseClass");
		
		Map<String, Object> attributes = new HashMap<String, Object>();
		
		SAXReader reader = new SAXReader();
		reader.setValidation(false);
		
		Document xmldoc = reader.read(new File(xmlFile));
		
		
		Element root = xmldoc.getRootElement();
		attributes.put("package", root.attributeValue("package"));
		
		String destination = root.attributeValue("destination");
		
		Iterator parserIt = xmldoc.selectNodes("/slickxml/parser").iterator();
		while (parserIt.hasNext())
			{
			Element pElem = (Element)parserIt.next();
			
			slickxml.data.Parser parser = new slickxml.data.Parser(pElem.attributeValue("name"));
			m_allObjects.clear();
			
			parseObject(pElem, parser);
			
			parser.setAllObjects(m_allObjects.values());
			
			attributes.put("parser", parser);
			attributes.put("configuration", pElem.asXML());
			
			baseClassTemplate.setAttributes(attributes);
			
			File destFile = new File(destination);
			if (!destFile.exists())
				destFile.mkdirs();
			
			File javaFile = new File(destination, parser.getClassName() + ".java");
			FileWriter fw = new FileWriter(javaFile);
			fw.write(baseClassTemplate.toString());
			fw.close();
			System.out.println(javaFile.getPath() + " created");
			}
		} */
		
		
	//---------------------------------------------------------------------------
	public void parsedHeader(SlickXMLParser.Header entry) throws Exception
		{
		m_package = entry.getPackage();
		m_destination = entry.getDestination();
		}
		
	//---------------------------------------------------------------------------
	private void loadDataObject(DataObject myDo, List<SlickXMLParser.Property> properties,
			List<SlickXMLParser.DataObject> objects)
			throws SlickParserException
		{
		for (SlickXMLParser.Property prop : properties)
			{
			Property myProperty = new Property(prop.getName());
			
			if (prop.getAttribute() != null)
				myProperty.setAttrName(prop.getAttribute());
			else
				{
				SlickXMLParser.Element elem = prop.getFirstElement();
				if (elem == null)
					throw new SlickParserException("Missing attribute or element for property "+prop.getName());
					
				myProperty.setElementName(elem.getName());
				
				for (SlickXMLParser.Condition cond : elem.getConditionList())
					{
					myProperty.addCondition(new Condition(cond.getName(), cond.getValue()));
					}
				}
			
			myDo.addProperty(myProperty);
			}
			
		for (SlickXMLParser.DataObject dObj : objects)
			{
			if (dObj.getReference() == null)
				{
				DataObject subDo = new DataObject(dObj.getTag(), dObj.getName());
						
				myDo.addObject(subDo);
				
				m_allObjects.put(subDo.getClassName(), subDo);
				loadDataObject(subDo, dObj.getPropertyList(), dObj.getDataObjectList());
				}
			else
				{
				DataObject subDo = m_allObjects.get(dObj.getReference());
				if (subDo == null)
					throw new SlickParserException("Unable to locate reference '"+dObj.getReference()+"'");
					
				myDo.addObject(subDo);
				}
			}
			
		}
		
	//---------------------------------------------------------------------------
	public void parsedParser(SlickXMLParser.Parser entry) throws Exception
		{
		Map<String, Object> attributes = new HashMap<String, Object>();
		org.agileclick.slickxml.data.Parser parser = new org.agileclick.slickxml.data.Parser(entry.getName());
		m_allObjects.clear();
		
		loadDataObject(parser, new ArrayList<SlickXMLParser.Property>(),
				entry.getDataObjectList());
		
			
		parser.setAllObjects(m_allObjects.values());
			
		attributes.put("package", m_package);
		attributes.put("parser", parser);
		
		//Get the configuration from the config file
		Pattern pattern = Pattern.compile(".*(<parser name=\""+parser.getClassName()+
				"\".*?</parser>).*", Pattern.MULTILINE | Pattern.DOTALL);
		
		Matcher matcher = pattern.matcher(m_configString);
		if (matcher.matches())
			attributes.put("configuration", matcher.group(1));
		else
			attributes.put("configuration", "");
		
		m_baseClassTemplate.setAttributes(attributes);

		File destFile = new File(m_destination);
		if (!destFile.exists())
			destFile.mkdirs();
		
		File javaFile = new File(m_destination, parser.getClassName() + ".java");
		FileWriter fw = new FileWriter(javaFile);
		fw.write(m_baseClassTemplate.toString());
		fw.close();
		System.out.println(javaFile.getPath() + " created");
		}
	
	//---------------------------------------------------------------------------
	public void generateFilesSlick(String xmlFile)
			throws SAXException, IOException, SlickParserException
		{
		StringTemplateGroup templateGroup = new StringTemplateGroup(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("template/Parser.java")));
				
		m_baseClassTemplate = templateGroup.getInstanceOf("baseClass");
		
		//Read in the file
		byte[] buffer = new byte[(int) new File(xmlFile).length()];
		FileInputStream fileIs = new FileInputStream(xmlFile);
		fileIs.read(buffer);
		m_configString = new String(buffer);
		
		XMLReader parser = XMLReaderFactory.createXMLReader();
		SlickXMLParser handler = new SlickXMLParser(this);
		parser.setContentHandler(handler);

		InputSource source = new InputSource(new FileInputStream(xmlFile));
		parser.parse(source);
		}
		
	//---------------------------------------------------------------------------	
	public static void main(String[] args)
		{
		try
			{
			SlickParser sp = new SlickParser();
			
			//sp.generateFiles(args[0]);
			sp.generateFilesSlick(args[0]);
			}
		catch (SAXException saxe)
			{
			Exception sube = saxe.getException();
			if (sube != null)
				{
				if (sube instanceof SlickParserException)
					System.out.println(sube.getMessage());
				else
					sube.printStackTrace();
				}
			}
		catch (Exception e)
			{
			if (e instanceof SlickParserException)
				System.out.println(e.getMessage());
			else if (e.getCause() instanceof SlickParserException)
				System.out.println(e.getCause().getMessage());
			else if (e.getCause() != null)
				e.getCause().printStackTrace();
			else
				e.printStackTrace();
			}
		}
	}
