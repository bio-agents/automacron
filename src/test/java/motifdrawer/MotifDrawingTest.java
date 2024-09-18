package motifdrawer;

import commons.TestUtils;
import org.isaagents.macros.graph.graphloader.Neo4JConnector;
import org.isaagents.macros.gui.DBGraph;
import org.isaagents.macros.gui.macro.Macro;
import org.isaagents.macros.gui.macro.renderer.*;
import org.isaagents.macros.io.graphml.GraphMLCreator;
import org.isaagents.macros.motiffinder.AlternativeCompleteMotifFinder;
import org.isaagents.macros.motiffinder.Motif;
import org.isaagents.macros.utils.MotifStatCalculator;
import org.junit.Test;
import org.neo4j.graphdb.Node;

import java.io.File;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 08/11/2012
 *         Time: 10:22
 */
public class MotifDrawingTest extends TestUtils {


    @Test
    public void testFullMotifDrawing() {

        String baseDir = System.getProperty("basedir");
        String filesPath = baseDir + "/target/test-classes/testdata/Flattened/BII-I-1:BII-S-1:a_metabolome.txt";
        File dataset = new File(filesPath);

        Neo4JConnector connector = loadGraph(dataset);

        Node startNode = connector.getGraphDB().getNodeById(0);
        // we do the analysis for different sized motifs.

        AlternativeCompleteMotifFinder motifFinder = new AlternativeCompleteMotifFinder(5);
        motifFinder.performAnalysis(new DBGraph(startNode), new GraphMLCreator(new File("data/test.xml")));

        System.out.println("There are " + motifFinder.getMotifs().size() + " motifs.");

        File[] files = new File("data/images/").listFiles();
        System.out.println("Cleaning up old files.");
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        int count = 0;
        for (Motif foundMotif : motifFinder.getMotifs().values()) {
            if (count > 10) break;
            MotifGraphRenderer mainRenderer = new MotifGraphRenderer(true);
            mainRenderer.renderMacro(foundMotif, new File("data/images/glyph-" + count + ".png"));

            RenderingFactory.populateMacroWithFiles(new Macro(foundMotif), mainRenderer.getGraph());

            count++;
        }

//        MotifStatCalculator calculator = new MotifStatCalculator();
//        calculator.analyseMotifs(motifFinder.getMotifs());
//        calculator.getOverRepresentedBlocks(motifFinder.getMotifs());

        connector.getGraphDB().shutdown();
    }

}
