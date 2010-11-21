package parser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;


public class XmlDocument 
{
	private static final int 		MAX_PROCESS_COUNT = 200;
	private static final String		TEMP_DIR = "xml/";
	private String 					name = null;
	private Node 					rootNode = null;
	private DocumentBuilderFactory 	dbfac = DocumentBuilderFactory.newInstance();
	private DocumentBuilder 		docBuilder = null;
	private Document 				doc = null;
	private Schema 					schema = null;
	private int 					processCounter = 0;
	private int 					tempcounter = 0;
	private String 					rootElement = null;
	
	
	public XmlDocument(String filepath, String rootelement)
	{
		this.name = filepath;
		this.rootElement = rootelement;
        try 
        {
        	docBuilder = dbfac.newDocumentBuilder();
			doc = docBuilder.newDocument();
			rootNode = doc.createElement(rootElement);
			doc.appendChild(rootNode);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}		
	}
	
	private void clear()
	{
		NodeList list = doc.getFirstChild().getChildNodes();
//		int count = 0;
		boolean havemore = list.getLength()>0;
		
		while(havemore)
		{
			Node nod = list.item(0);
			if(nod!=null)
			{
//				System.out.println(nod.getNodeName() + String.valueOf(count++));
				doc.getFirstChild().removeChild(nod);
			}
			else
				havemore = false;
		}
	}
	
	public void setSchema(File schemafile)
	{
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try {
			schema = factory.newSchema(schemafile);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	private boolean validShema()
	{
		if(schema != null)
		{
			Validator validator = schema.newValidator();
			try {
				validator.validate(new DOMSource(doc));
			} catch (SAXException e) {
				return false;
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		return true;
	}
	
	private void savetemp(int tempcount)
	{
		File out = new File(TEMP_DIR +"xmltmp" + String.valueOf(tempcount));
		FileWriter fw = null;	
		try {
			fw = new FileWriter(out);
			if(validShema())
				fw.write(createDataString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void save()
	{
		File out = new File(name);
		FileWriter fw = null;
		File dir = new File(TEMP_DIR);
		FileReader fr = null;
		  
		File[] files = dir.listFiles();
		try 
		{
			fw = new FileWriter(out);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
			fw.write("<" + rootNode.getNodeName() + ">\n");
			for(File f : files)
			{
				if(f.getName().startsWith("xmltmp"))
				{
					fr = new FileReader(f);
					BufferedReader buf = new BufferedReader(fr);
					String atm = null;
					while((atm = buf.readLine()) != null)
					{
						if(!atm.contains(rootNode.getNodeName()))
							fw.write(atm + "\n");
					}
					fr.close();
					f.delete();
				}				
			}
			fw.write("</" + rootNode.getNodeName() + ">\n");
		} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally
				{
					try {
						fw.close();
						fr.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}		
	}
	
	public String createDataString()
	{
        try 
        {
        	TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            DOMSource source = new DOMSource(doc);
//            trans.setOutputProperty(OutputKeys.METHOD,"xml");
//    	    trans.setOutputProperty(OutputKeys.VERSION, "1.0");
//    	    trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
    	    trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	    trans.setOutputProperty(OutputKeys.INDENT, "yes"); 
//    	    trans.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
        	StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
    		trans.transform(source, result);
           
	        return sw.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public Integer addElement(String nodename, String data, String commentstring, Integer parent)
	{
		Node node = doc.createElement(nodename);
		Comment comment = doc.createComment(commentstring);
		Text text = doc.createTextNode(data);
		Node parentNode = null;
		
		if(data != null)
			node.appendChild(text); //if have, add data
		if(commentstring != null)
			node.appendChild(comment);
		
		if(parent == null)
		{
			flushMemory(processCounter++);
			rootNode.appendChild(node);
			return node.hashCode();
		}
		else
		{
			parentNode = findNodeById(parent,rootNode);
			if(parentNode != null)
			{
				parentNode.appendChild(node);
				return node.hashCode();
			}
		}
		return null;
	}
		
	public void setAttribute(Integer node, String name, String value)
	{		
		Node nod = findNodeById(node,rootNode);
		((Element)nod).setAttribute(name, value);
	}
	/**
	 * find a node in tree
	 * @param hash
	 * @param where
	 * @return
	 */
	private Node findNodeById(Integer hash, Node where)
	{
		Node retNode = null;
		NodeList nodes = where.getChildNodes();
		for(int i = 0; i < nodes.getLength(); i++)
		{
			Node nod = nodes.item(i);
			
			if(nod.hashCode() == hash)
				return nod;
			
			if(nod.hasChildNodes())
			{
				retNode = findNodeById(hash,nod);
				if(retNode != null)
					return retNode;
			}
		}
		return null;
	}
	
	/**
	 * Empty memory for more performance, save data in temp file
	 * @param count
	 */
	private boolean flushMemory(int count)
	{
		if(count > MAX_PROCESS_COUNT)
		{			
			savetemp(tempcounter++);
			clear();
			processCounter = 0;
			return true;
		}
		return false;
	}
	
/*	protected void finalize() throws Throwable
	{
	  File dir = new File(TEMP_DIR);
	  
	  File[] files = dir.listFiles();
	  
	  for(File f : files)
	  {
		  if(f.getName().startsWith("xmltmp"))
		  {
			  f.delete();
		  }
	  }
	}*/
}
