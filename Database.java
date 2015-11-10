import java.net.*;
import java.util.*;

public class Database
{
	private URL url;
	private HashMap<ClassificationNode, Integer> coverages;
	private HashMap<ClassificationNode, Double> specificities;
	
	public Database(String urlString) throws MalformedURLException
	{
		url = new URL(urlString);
		coverages = new HashMap<ClassificationNode, Integer>();
		specificities = new HashMap<ClassificationNode, Double>();	
	}
	
	public double getSpecificity(ClassificationNode node)
	{
		if (specificities.containsKey(node))
		{
			return specificities.get(node);
		}
		else
		{
			return -1;
		}
	}
	
	public void setSpecificity(ClassificationNode node, double value)
	{
		specificities.put(node, value);
	}
	
	public int getCoverage(ClassificationNode node)
	{
		if (coverages.containsKey(node))
		{
			return coverages.get(node);
		}
		else
		{
			return -1;
		}
	}
	
	public void setCoverage(ClassificationNode node, int value)
	{
		coverages.put(node, value);
	}
	
	public String getURL()
	{
		return url.getHost();
	}

}
