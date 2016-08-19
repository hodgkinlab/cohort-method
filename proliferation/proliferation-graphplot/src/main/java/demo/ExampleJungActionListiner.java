package demo;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph; 
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.wehi.GUI;

import java.awt.Dimension;
import java.awt.event.MouseEvent;

public class ExampleJungActionListiner {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
        //Create a test graph
        Graph graph = new SparseGraph<String, String>();
        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.addVertex("d");
        graph.addEdge("a->b", "a", "b");
        graph.addEdge("a->c", "a", "c");
        graph.addEdge("b->c", "b", "c");
        graph.addEdge("c->d", "c", "d");
        //Metrics
        visualize(graph);
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void visualize(Graph graph) {
        //Layout graph.
        Layout layout = new CircleLayout(graph);
        layout.setSize(new Dimension(500, 500)); // sets the initial size of the space
        VisualizationViewer server = new VisualizationViewer(layout);
        server.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        //Here you add the listener
        server.addGraphMouseListener(new GraphMouseListener() {

            @Override
            public void graphClicked(Object v, MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2) {
                    System.out.println("Double clicked "+ v);
                }
                me.consume();
            }

            @Override
            public void graphPressed(Object v, MouseEvent me) {
            }

            @Override
            public void graphReleased(Object v, MouseEvent me) {
            }
        });
        
        GUI.gui(server);
        
    }
}