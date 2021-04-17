package org.agileclick.slickxml.data;

public class Condition
	{
	private String m_attrName;
	private String m_attrValue;
	
	public Condition(String name, String value)
		{
		m_attrName = name;
		m_attrValue = value;
		}
	
	public String getAttrName() { return (m_attrName); }
	public String getAttrValue() { return (m_attrValue); }
	}
