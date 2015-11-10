import java.util.*;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.*;
import java.io.*;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DatabaseClassifier 
{	
	public static void main(String[] args) throws Exception
	{
		ClassificationNode rootNode = PopulateCategoryNodes();
		
		Scanner userInput = new Scanner(System.in);
		Database database = getDatabaseInput(userInput);
		int coverageThreshold = getCoverageInput(userInput);
		double specificityThreshold = getSpecificityInput(userInput);
		userInput.close();
		
		try
		{
			//Part 1
			System.out.println("Classifying...");
			HashSet<ClassificationNode> results = classify(rootNode, database, coverageThreshold,
					specificityThreshold, specificity(database, rootNode));
			
			System.out.println();
			System.out.println("Classification:");
			for (ClassificationNode node : results)
			{
				printAncestors(node);
				System.out.println();
			}
			
			//Part 2 starts
			System.out.println("Extracting topic content summaries...");
			PrintWriter writer; 
			int count;
			for (ClassificationNode node : results)
			{

				while (node.getParent() != null)
				{	
					node.getParent().addTopResults(node.getTopResults());
					node = node.getParent();
					HashMap<String, Integer> res = probeD(node.getTopResults());
					writer = new PrintWriter(node.getName()+"-"+database.getURL()+".txt", "UTF-8");
					Object[] keys = res.keySet().toArray();
					Arrays.sort(keys);
					for(Object key: keys){

						count = res.get(key);
						writer.println(key+"#"+count);

					}	
					writer.close();
					//System.out.println(node.getName());
					//System.out.println(node.getTopResults());
					//System.out.println("--------------");
				}

			}
			
			// TO DO - Extract terms from top documents (part 2b)
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
		
	}
	
	private static HashMap probeD(HashSet<String> topResults)throws Exception
	{
		//HashSet<String> all = new HashSet<String>();
		HashMap<String, Integer> all = new HashMap<String, Integer>();
		Set<String> content;
		int count = 0;
		//Set all;
		for(String url: topResults){
				System.out.println("Fetching: "+url);
				if(!url.endsWith(".pdf") && !url.endsWith(".ppt") && !url.endsWith(".pptx")){
					content = getWordsLynx.runLynx(url);		
					//System.out.println(content);
					for(String word:content){

						if(all.containsKey(word)){
							count = all.get(word);
							count = count + 1;
							all.put(word, count);
						}else{
							all.put(word, 1);
						}
					}
				}
		}

		//System.out.println(all);
		return all;
	}

	private static void printAncestors(ClassificationNode node)
	{
		if (node.getParent() != null)
		{
			printAncestors(node.getParent());
			System.out.print("/");
		}
		System.out.print(node.getName());
	}
	
	private static Database getDatabaseInput(Scanner userInput)
	{
		String databaseURL;
		boolean validDB = false;
		Database database = null;
		
		while (!validDB)
		{
			System.out.print("Please enter the url of the database: ");
			databaseURL = userInput.next( );
			try
			{
				database = new Database("http://" + databaseURL);
			}
			catch (MalformedURLException e)
			{
				System.out.println("Invalid URL given");
				continue;
			}
			validDB = true;
		}
		
		return database;		
	}
	
	private static int getCoverageInput(Scanner userInput)
	{
		String coverageThresholdInput;
		boolean validCT = false;
		int coverageThreshold = 0;
		
		while (!validCT)
		{
			System.out.print("Please enter the coverage threshold (ct >= 1): ");
			coverageThresholdInput = userInput.next( );
			try
			{
				coverageThreshold = Integer.valueOf(coverageThresholdInput);
				
			}
			catch (NumberFormatException e)
			{
				coverageThreshold = 0;
			}
			
			if (coverageThreshold < 1)
			{
				System.out.println("Invalid value given");
			}
			else
			{
				validCT = true;
			}
		}
		
		return coverageThreshold;
	}
	
	private static double getSpecificityInput(Scanner userInput)
	{
		String specificityThresholdInput;
		boolean validST = false;
		double specificityThreshold = -1;
		
		while (!validST)
		{
			System.out.print("Please enter the specificity threshold(0 <= st <= 1): ");
			specificityThresholdInput = userInput.next( );
			try
			{
				specificityThreshold = Double.valueOf(specificityThresholdInput);
				
			}
			catch (NumberFormatException e)
			{
				specificityThreshold = -1;
			}
			
			if (specificityThreshold > 1 || specificityThreshold < 0)
			{
				System.out.println("Invalid value given");
			}
			else
			{
				validST = true;
			}
		}
		
		return specificityThreshold;
	}
	
	public static ClassificationNode PopulateCategoryNodes()
	{
		ClassificationNode root = new ClassificationNode("Root", null, null, null);
		
		String computerQueries = "cpu,java,module,multimedia,perl,vb,agp+card,application+windows,applet+code,array+code,audio+file,avi+file,bios,buffer+code,bytes+code,shareware,card+drivers,card+graphics,card+pc,pc+windows";
		String[] computerQueriesList = computerQueries.split(",");
		ClassificationNode computers = new ClassificationNode("Computers", null, computerQueriesList, root);
		String hardwareQueries = "bios,motherboard,board+fsb,board+overclocking,fsb+overclocking,bios+controller+ide,cables+drive+floppy";
		String[] hardwareQueriesList = hardwareQueries.split(",");
		computers.addChild(new ClassificationNode("Hardware", null, hardwareQueriesList, computers));
		String programmingQueries = "actionlistener,algorithms,alias,alloc,ansi,api,applet,argument,array,binary,boolean,borland,char,class,code,compile,compiler,component,container,controls,cpan,java,perl";
		String[] programmingQueriesList = programmingQueries.split(",");
		computers.addChild(new ClassificationNode("Programming", null, programmingQueriesList, computers));
		String healthQueries = "acupuncture,aerobic,aerobics,aids,cancer,cardiology,cholesterol,diabetes,diet,fitness,hiv,insulin,nurse,squats,treadmill,walkers,calories+fat,carb+carbs,doctor+health,doctor+medical,eating+fat,fat+muscle,health+medicine,health+nutritional,hospital+medical,hospital+patients,medical+patient,medical+treatment,patients+treatment";
		String[] healthQueriesList = healthQueries.split(",");
		ClassificationNode health = new ClassificationNode("Health", null, healthQueriesList, root);
		String diseasesQueries = "aids,cancer,dental,diabetes,hiv,cardiology,aspirin+cardiologist,aspirin+cholesterol,blood+heart,blood+insulin,cholesterol+doctor,cholesterol+lipitor,heart+surgery,radiation+treatment";
		String[] diseasesQueriesList = diseasesQueries.split(",");
		health.addChild(new ClassificationNode("Diseases", null, diseasesQueriesList, health));
		String fitnessQueries = "aerobic,fat,fitness,walking,workout,acid+diets,bodybuilding+protein,calories+protein,calories+weight,challenge+walk,dairy+milk,eating+protein,eating+weight,exercise+protein,exercise+weight";
		String[] fitnessQueriesList = fitnessQueries.split(",");
		health.addChild(new ClassificationNode("Fitness", null, fitnessQueriesList, health));
		String sportsQueries = "laker,ncaa,pacers,soccer,teams,wnba,nba,avg+league,avg+nba,ball+league,ball+referee,ball+team,blazers+game,championship+team,club+league,fans+football,game+league";
		String[] sportsQueriesList = sportsQueries.split(",");
		ClassificationNode sports = new ClassificationNode("Sports", null, sportsQueriesList, root);
		String basketballQueries = "nba,pacers,kobe,laker,shaq,blazers,knicks,sabonis,shaquille,laettner,wnba,rebounds,dunks";
		String[] basketballQueriesList = basketballQueries.split(",");
		sports.addChild(new ClassificationNode("Basketball", null, basketballQueriesList, sports));
		String soccerQueries = "uefa,leeds,bayern,bundesliga,premiership,lazio,mls,hooliganism,juventus,liverpool,fifa";
		String[] soccerQueriesList = soccerQueries.split(",");
		sports.addChild(new ClassificationNode("Soccer", null, soccerQueriesList, sports));
		root.addChild(computers);
		root.addChild(health);
		root.addChild(sports);
		return root;
	}
	
	public static HashSet<ClassificationNode> classify(ClassificationNode node, Database database,
			int coverageThreshold, double specificityThreshold, double eSpecificity) throws IOException
	{
		HashSet<ClassificationNode> resultSet = new HashSet<ClassificationNode>();
		if (!node.hasChildren())
		{
			resultSet.add(node);
			return resultSet;
		}
		
		ArrayList<ClassificationNode> children = node.getChildren();
		for (ClassificationNode child : children)
		{
			if (coverage(database, child) >= coverageThreshold &&
					specificity(database, child) >= specificityThreshold)
			{
				resultSet.addAll(classify(child, database, coverageThreshold,
						specificityThreshold, specificity(database, child)));
			}
			System.out.println("Specificity for category:" + child.getName() + " is " + specificity(database, child));
			System.out.println("Coverage for category:" + child.getName() + " is " + coverage(database, child));
		}
		
		if (resultSet.isEmpty())
		{
			resultSet.add(node);
		}
		return resultSet;
		
	}
	
	public static int coverage(Database database, ClassificationNode node) throws IOException
	{
		int coverage = 0;
		if ((coverage = database.getCoverage(node)) != -1)
		{
			return coverage;
		}
		
		for (String query : node.getQueries())
		{
			String host = database.getURL();
			String bingUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27site%3a" + host + "%20" + query + "%27&$top=10&$format=Atom";
			String accountKey = "DhYpklKgt6GsLAFyq1lz7dX3KSwG/LCWxUdBNPhreJk=";
			byte[] accountKeyBytes = Base64.encode((accountKey + ":" + accountKey));
			String accountKeyEnc = new String(accountKeyBytes);
			URL url = new URL(bingUrl);
			URLConnection urlConnection = url.openConnection();
			urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

			InputStream inputStream = (InputStream) urlConnection.getContent();		
			byte[] contentRaw = new byte[urlConnection.getContentLength()];
			inputStream.read(contentRaw);
			String content = new String(contentRaw);
			
			HashSet<String> topFour = extractTopFour(content);
			if (topFour != null && !topFour.isEmpty())
			{
				node.getParent().addTopResults(topFour);
			}
			
			int index = content.lastIndexOf("<d:WebTotal m:type=\"Edm.Int64\">");
			int index2 = content.lastIndexOf("</d:WebTotal>");
			
			int count = Integer.parseInt(content.substring(index + 31, index2));
			coverage += count;
		}
		
		database.setCoverage(node, coverage);
		return coverage;
	}
	
	public static double specificity(Database database, ClassificationNode node) throws IOException
	{
		double specificity;
		if ((specificity = database.getSpecificity(node)) != -1)
		{
			return specificity;
		}
		
		if (node.getName() == "Root")
		{
			database.setSpecificity(node, 1);
			return 1;
		}
			
		double parentSpecificity = specificity(database, node.getParent());
		
		int coverage = coverage(database, node);
	
		ArrayList<ClassificationNode> children = node.getParent().getChildren();
		
		double totalCoverage = 0;
		for (ClassificationNode child : children)
		{
			int childCoverage = coverage(database, child);
			totalCoverage += childCoverage;
		}
		
		return parentSpecificity * coverage / totalCoverage;
	}
	
	private static HashSet<String> extractTopFour(String xmlContent)
	{
		HashSet<String> topFour = new HashSet<String>();
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	InputSource is = new InputSource(new StringReader(xmlContent));
	     	Document result = builder.parse(is);
	     	NodeList nl = result.getElementsByTagName("d:Url");
	     	for(int i = 0; i < nl.getLength(); i++){
	     		if(i < 4){
		     		Node n = nl.item(i);
					//System.out.println("xmlContent -----");
					//System.out.println(result.getElementsByTagName("entry").getTextContent());	
					//System.out.println(n.getTextContent());
					topFour.add(n.getTextContent());	
					//System.out.println("xmlContent ------");
				}else{
					break;
				}
			}	
		}catch(Exception e){
			return null;
		}
		return topFour;
	}



}


















