package org.openphacts.cytophacts;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Task to fetch network data from the OpenPhacts API.
 */
public class GetChildNodes extends AbstractAction 
{
	private CyAppAdapter adapter;
	private CyNetwork myNet;
	public GetChildNodes(CySwingAppAdapter adapter, CyNetwork myNet) 
	{
		super ("Get child nodes");
		this.adapter = adapter;
		this.myNet = myNet;
	}
	
	private Map<String, CyNode> idMap = new HashMap<String, CyNode>();
	
	/** returns the node for a given key, or creates a new one if it doesn't exist. */
	public CyNode createOrGet (String key)
	{
		if (idMap.containsKey(key))
		{
			return idMap.get(key);
		}
		else
		{
			CyNode node = myNet.addNode();
			idMap.put (key, node);
			return node;
		}
	}
	
	/** actually create the network */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		try
		{
			OpenPhactsMethods om = new OpenPhactsMethods();
				
			//Create table
			CyTable table = myNet.getDefaultNodeTable();
			table.createColumn("uuid", String.class, true);
			table.createColumn("uri", String.class, true);
			
			OpenPhactsMethods.getChildNodes(idMap, myNet, table);
			
			adapter.getCyNetworkManager().addNetwork(myNet);		
			
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "<html>Exception: " + ex.getClass().getName() + "</br>" + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
