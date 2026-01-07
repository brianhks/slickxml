package org.agileclick.slickxml.data;

import java.util.List;
import java.util.ArrayList;

public class DataObject
	{
	private String m_tag;
	private String m_name;
	private String m_bodyProperty;
	private List<Property> m_properties;
	private List<DataObject> m_objects;
	private String m_implements;
	
	public DataObject(String tag, String name)
		{
		m_tag = tag;
		m_name = name;
		
		m_properties = new ArrayList<Property>();
		m_objects = new ArrayList<DataObject>();
		}
		
	public boolean isAttributesOnly() { return (m_objects.isEmpty()); }
	public boolean isImplements() { return m_implements != null; }
	
	public String getClassName() { return (m_name); }
	public String getVarName() { return (m_name); }

	public String getImplements()
		{
		return m_implements;
		}

	public void setImplements(String anImplements)
		{
		m_implements = anImplements;
		}

	public String getBodyProperty()
		{
		return m_bodyProperty;
		}

	public void setBodyProperty(String bodyProperty)
		{
		m_bodyProperty = bodyProperty;
		}

	public List<DataObject> getObjects() { return (m_objects); }
	public void addObject(DataObject obj) { m_objects.add(obj); }
	
	public String getTag() { return (m_tag); }
	
	public List<Property> getProperties() { return (m_properties); }
	public void addProperty(Property prop) { m_properties.add(prop); }
	}
