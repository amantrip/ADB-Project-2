

import java.util.*;

public class ClassificationNode
{
	private String name;
	private ArrayList<ClassificationNode> children;
	private String[] queries;
	private ClassificationNode parent;
	private HashSet<String> topResults;
	
	public ClassificationNode(String name, ArrayList<ClassificationNode> children,
			String[] queries, ClassificationNode parent)
	{
		this.name = name;
		if (children != null)
		{
			this.children = new ArrayList<ClassificationNode>(children);
		}
		else
		{
			this.children = new ArrayList<ClassificationNode>();
		}
		
		this.queries = queries;
		
		this.parent = parent;
		
		topResults = new HashSet<String>();
	}
	
	public void addChild(ClassificationNode node)
	{
		children.add(node);
	}
	
	public boolean hasChildren()
	{
		if (children == null || children.isEmpty())
		{
			return false;
		}
		return true;
	}
	
	public ArrayList<ClassificationNode> getChildren()
	{
		return children;
	}
	
	public String[] getQueries()
	{
		return queries;
	}
	
	public ClassificationNode getParent()
	{
		return parent;
	}
	
	public void setParent(ClassificationNode parent)
	{
		this.parent = parent;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addTopResults(HashSet<String> results)
	{
		topResults.addAll(results);
	}
	
	public HashSet<String> getTopResults()
	{
		HashSet<String> topResultsCopy = new HashSet<String>(topResults);
		return topResultsCopy;
	}
	
}
