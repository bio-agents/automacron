package org.isaagents.macros.motiffinder;

import org.isaagents.macros.exporters.Exporter;
import org.isaagents.macros.exporters.html.HTMLExporter;
import org.isaagents.macros.graph.graphloader.Neo4JConnector;
import org.isaagents.macros.gui.DBGraph;
import org.isaagents.macros.io.graphml.GraphMLCreator;
import org.isaagents.macros.loaders.isa.ISAWorkflowLoader;
import org.isaagents.macros.utils.MotifProcessingUtils;
import org.isaagents.macros.utils.MotifSelectionAlgorithm;
import org.isaagents.macros.utils.MotifSelectionAlgorithmImpl;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static junit.framework.Assert.assertTrue;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 31/10/2012
 *         Time: 23:52
 */
public class LoadAndFindMotifTest {

    private static Neo4JConnector connector = new Neo4JConnector();
    private MotifSelectionAlgorithm motifSelectionAlgorithm;
    private Node startNode;

    @Before
    public void setUp() throws Exception {

        motifSelectionAlgorithm = new MotifSelectionAlgorithmImpl();
        GraphDatabaseService graphDatabaseService = connector.getGraphDB();
        Transaction tx = graphDatabaseService.beginTx();
        try {
            ISAWorkflowLoader loader = new ISAWorkflowLoader(graphDatabaseService);
            loader.loadFiles(new File("ProgramData/test-workflow.txt"));

            startNode = graphDatabaseService.getNodeById(0);
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tx.finish();
        }
    }


    @Test
    public void testLoadAndFindMotif() {
        MotifFinder motifFinder = new AlternativeCompleteMotifFinder(8);

        long startTime = System.currentTimeMillis();

        motifFinder.performAnalysis(new DBGraph(startNode), new GraphMLCreator(new File("data/test.xml")));
        Map<String, Motif> motifs = motifFinder.getMotifs();

        long endTime = System.currentTimeMillis();

        long totalTime = (endTime-startTime);
        System.out.println("Analysis took " + totalTime + " milliseconds.");
        printMotifsToHTML(motifs, totalTime + " ms");
        System.out.println("###############");

        System.out.println(motifs.size());
        assertTrue("Awww, motif size not as expected", motifs.size() == 51);
    }

    private void printMotifs(Map<String, Motif> motifs) {
        System.out.println(String.format("Found %d motifs.", motifs.size()));
        for (String motif : motifs.keySet()) {
            System.out.println(String.format("%s -> #%d (%d)", motif, motifs.get(motif).getCumulativeUsage(), MotifProcessingUtils.getNumberOfGroupsInMotifString(motif)));
        }
    }

    private void printMotifsToHTML(Map<String, Motif> motifs, String timeTaken) {
        Exporter exporter = new HTMLExporter();
        exporter.export(motifs, timeTaken, new File("data/html-output/"));
    }
}
