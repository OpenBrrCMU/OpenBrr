package org.openbrr.core.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;

/**
 * 
 * @author ashutosh
 *
 */

public class HtmlParser {
	
	private String url;
	private Document doc;
	
	public HtmlParser(String _url) {
		url = _url;
		
		try {
			initializeDoc();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	private void initializeDoc() throws IOException {
		URL webUrl = new URL(url);
		URLConnection urlConn = webUrl.openConnection();
		InputStream in =  null;
		
		try {
			in = urlConn.getInputStream();
			
			Reader reader = new InputStreamReader(in);
			InputSource is = new InputSourceImpl(reader, url);
			
			//create the rendererContext
			UserAgentContext userContext = new SimpleUserAgentContext();
			DocumentBuilderImpl builder = new DocumentBuilderImpl(userContext);
			
			doc = builder.parse(is);
		} catch(IOException io) {
			io.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(in != null) {
				in.close();
			}
		}
	} */

	private void initializeDoc() throws IOException {
		URL webUrl = new URL(url);
		URLConnection urlConn = webUrl.openConnection();
		InputStream in =  null;
		OutputStream out = null;
		
		try {
			in = urlConn.getInputStream();
			Tidy tidy = new Tidy();
			
			out = new FileOutputStream("html_parser.out");
			doc = tidy.parseDOM(in, out );
		} catch(IOException io) {
			io.printStackTrace();
		} finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public List<Node> getNodes(String _path) {
		return getNodes(doc, _path);
	}
	
	@SuppressWarnings("unchecked")
	public List<Node> getNodes(Node _node, String _path) {
		List<Node> result = null;
		try {
			XPath path = new DOMXPath(_path);
			
			result = path.selectNodes(_node); 
		} catch (JaxenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Node getNode(String _path) {
		return getNode(doc, _path);
	}
	
	public Node getNode(Node _node, String _path) {
		List<Node> nodes = getNodes(_node, _path);
		Node node = null;
		
		if(nodes == null) {
			return node;
		} else if(nodes.size() == 1) {
			return nodes.get(0);
		} else {
			for(Node n : nodes) {
				System.out.println("Node : "+n.getNodeValue());
			}
			throw new RuntimeException("More than one node returned");
		}
	}
	
	
}
