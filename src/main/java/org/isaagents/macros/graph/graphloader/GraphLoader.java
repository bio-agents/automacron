package org.isaagents.macros.graph.graphloader;

import org.isaagents.macros.gui.DBGraph;
import org.isaagents.macros.gui.macro.Macro;
import org.isaagents.macros.io.graphml.GraphMLCreator;
import org.isaagents.macros.motiffinder.GraphTraversalImpl;
import org.isaagents.macros.motiffinder.MotifFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 09/05/2012
 *         Time: 17:46
 */
public class GraphLoader {

    private Neo4JConnector neo4JConnector;

    private Set<Node> visitedNodes;

    public GraphLoader(String graphDBLocation) {
        this(new Neo4JConnector(graphDBLocation));
    }

    public GraphLoader() {
        this(new Neo4JConnector());
    }

    public GraphLoader(Neo4JConnector connector) {
        neo4JConnector = connector;
        visitedNodes = new HashSet<Node>();
    }

    public Neo4JConnector getNeo4JConnector() {
        return neo4JConnector;
    }

    public void loadGraph(org.isaagents.macros.loaders.GraphLoader loader, File file) {
        loader.loadFiles(file);
    }

    public void printGraph(Node nodeToCheck) {

        if (!visitedNodes.contains(nodeToCheck)) {
            for (Relationship relationship : nodeToCheck.getRelationships(Direction.INCOMING)) {
                Node relatedNode = relationship.getStartNode();
                visitedNodes.add(nodeToCheck);
                printGraph(relatedNode);
            }
        }
    }
    
    public File createGraphMLForExperiment(DBGraph DBGraph) {
        return createGraphMLForExperiment(DBGraph, "");
    }

    public File createGraphMLForExperiment(DBGraph DBGraph, String additionFileInfo) {
        MotifFinder finder = new GraphTraversalImpl();
        File graphMLFile = new File(System.getProperty("java.io.tmpdir") + DBGraph.toString() + additionFileInfo + ".xml");
        finder.performAnalysis(DBGraph, new GraphMLCreator(graphMLFile));
        return graphMLFile;
    }

}
