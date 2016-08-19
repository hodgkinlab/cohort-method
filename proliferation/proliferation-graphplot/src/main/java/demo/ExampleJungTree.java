package demo;

import java.awt.Dimension;
import java.util.List;

import org.python.google.common.collect.Lists;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.wehi.GUI;

public class ExampleJungTree {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {

		//String root = "Root";
		
		String grandpa1 = 	"GrandPa1";
		String grandma1 = 	"GrandMa1";
		String grandpa2 = 	"GrandPa2";
		String grandma2 = 	"GrandMa2";
		
		String mum = 		"Mother";
		String dad = 		"Dad";
		
		String daughter = 	"Daughter";
		String brother = 	"Brother";
		
		DelegateForest<String, String> tree = new DelegateForest<String, String>();
		
//		tree.addVertex(mum);
		tree.addEdge("1", grandpa1, mum);
		tree.addEdge("2", grandma1, mum);
		
//		tree.addVertex(dad);
		tree.addEdge("3", grandpa2, dad);
		tree.addEdge("4", grandma2, dad);
		
//		tree.addVertex(daughter);
		tree.addEdge("5",mum, daughter);
		tree.addEdge("6",dad, daughter);
		
//		tree.addVertex(brother);
		tree.addEdge("7", mum, brother);
		tree.addEdge("8", dad, brother);
		
		int defaultXDis = 100;
		int defaultYDis = 100;
		
		TreeLayout layout = new TreeLayout<String, String>(tree,defaultXDis,defaultYDis);
		
		List<String> pathToBrother = tree.getPath(brother);
		System.out.print("root");
		for (String p : Lists.reverse(pathToBrother))
		{
			System.out.print(" -> "+p);
		}
		
		VisualizationViewer server = 
				new VisualizationViewer<String, String>(
				layout,
				new Dimension(500, 500)
				);
		
		server.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		
		GUI.gui(server);

	}
}