package demo;

import java.awt.Dimension;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.wehi.GUI;

public class ExampleJungHeirachyTree {


	
	public static void main(String[] args) {

		try {
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			final DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			final Document document = docBuilder.newDocument();
			final Element svgelem = document.createElement("svg");
			document.appendChild(svgelem);

			final Graph<String, String> graph = createGraph();
			final VisualizationImageServer<String, String> server = createServer(graph);

			GUI.gui(server);



		} catch (final ParserConfigurationException exception) {

		}

	}

	private static  VisualizationImageServer<String, String> createServer(
			final Graph<String, String> aGraph) {
		final Layout<String, String> layout = new FRLayout<String, String>(
				aGraph);

		layout.setSize(new Dimension(300, 300));
		final VisualizationImageServer<String, String> vv = new VisualizationImageServer<String, String>(
				layout, new Dimension(350, 350));
		vv.getRenderContext().setVertexLabelTransformer(
				new ToStringLabeller<String>());
		return vv;
	}

	private static Graph<String, String> createGraph() {
		final Graph<String, String> graph = new DirectedSparseMultigraph<String, String>();
		final String vertex1 = "IE";
		final String vertex2 = "P1";
		final String vertex3 = "P2";
		final String vertex4 = "P3";
		final String vertex5 = "FE";

		graph.addVertex(vertex1);
		graph.addVertex(vertex2);
		graph.addVertex(vertex3);
		graph.addVertex(vertex4);
		graph.addVertex(vertex5);

		graph.addEdge("1", vertex1, vertex2, EdgeType.DIRECTED);
		graph.addEdge("2", vertex2, vertex3, EdgeType.DIRECTED);
		graph.addEdge("3", vertex3, vertex5, EdgeType.DIRECTED);
		graph.addEdge("4", vertex1, vertex4, EdgeType.DIRECTED);
		graph.addEdge("5", vertex4, vertex5, EdgeType.DIRECTED);
		return graph;
	}
}
