package slickxml.data;

import java.util.List;
import java.util.ArrayList;

public class Property
	{
	private String m_name;
	private boolean m_isAttribute;
	private String m_attributeName;
	
	private String m_elementName;
	private List<Condition> m_conditions;
	
	public Property(String name)
		{
		m_name = name;
		m_conditions = new ArrayList<Condition>();
		}
	
	public boolean isAttribute() { return (m_attributeName != null); }
	public String getName() { return (m_name); }
	public String getVarName() { return (m_name); }
	
	public String getAttrName() { return (m_attributeName); }
	public void setAttrName(String name) { m_attributeName = name; }
	
	public String getElementName() { return (m_elementName); }
	public void setElementName(String name) { m_elementName = name; }
	
	public boolean isConditional() { return (m_conditions.size() != 0); }
	public List<Condition> getConditions() { return (m_conditions); }
	public void addCondition(Condition cond) { m_conditions.add(cond); }
	}
