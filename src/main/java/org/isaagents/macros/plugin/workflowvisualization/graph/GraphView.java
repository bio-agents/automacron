package org.isaagents.macros.plugin.workflowvisualization.graph;

import org.isaagents.isacreator.common.UIHelper;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.StrokeAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.MultiScaleLabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.StrokeLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 03/07/2012
 *         Time: 11:40
 */
public class GraphView extends JPanel {

    private static final String graph = "graph";
    private static final String nodes = "graph.nodes";
    private static final String edges = "graph.edges";
    private Display display;

    private static int backgroundColor = ColorLib.rgb(255, 255, 255);

    private Visualization m_vis;

    public GraphView(Graph g, String label, int orientation, Dimension size) {
        // create a new, empty visualization for our data
        initialiseGraphView(g, label, orientation, size);
    }

    private void initialiseGraphView(Graph g, String label, int orientation, Dimension size) {
        setSize(size);
        m_vis = new Visualization();
        m_vis.addFocusGroup("selected");
        m_vis.addFocusGroup("highlighted");

        MultiScaleLabelRenderer tr = new MultiScaleLabelRenderer("value", label.contains("image") ? label : null);
        tr.setImagePosition(Constants.TOP);
        m_vis.setRendererFactory(new DefaultRendererFactory(tr));

        // adds graph to visualization and sets renderer label field
        setGraph(g);

        int hops = Integer.MAX_VALUE;
        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);

        FontAction font = new FontAction(nodes, UIHelper.VER_8_PLAIN);

        ColorAction fill = new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(255, 255, 255));
        fill.add("ingroup('highlighted')", ColorLib.rgb(241, 242, 242));
        fill.add("ingroup('selected')", ColorLib.rgb(241, 242, 242));


        ColorAction stroke = new ColorAction(nodes, VisualItem.STROKECOLOR, backgroundColor);
        StrokeAction strokeWeight = new StrokeAction(nodes, StrokeLib.getStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        stroke.add("ingroup('highlighted')", ColorLib.rgb(224, 228, 204));
        stroke.add("ingroup('selected')", ColorLib.rgb(202, 106, 33));

        ActionList draw = new ActionList(500);
        draw.add(font);
        draw.add(filter);
        draw.add(fill);
        draw.add(stroke);
        draw.add(strokeWeight);
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(109, 110, 113)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        draw.add(new RepaintAction());

        ActionList animate = new ActionList();

        NodeLinkTreeLayout layout = new NodeLinkTreeLayout(graph);
        layout.setDepthSpacing(80);
        layout.setBreadthSpacing(70);
        layout.setOrientation(orientation);

        animate.add(new QualityControlAnimator());
        animate.add(new LocationAnimator(nodes));
        animate.add(layout);

        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);

        // set up a display to show the visualization
        display = new Display(m_vis);
        display.setSize(size);

        // main display controls
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());

        // now we run our action list
        m_vis.run("draw");
        m_vis.run("layout");

        add(display);
    }

    public Display getDisplay() {
        return display;
    }

    public void setGraph(Graph g) {
        VisualGraph vg = m_vis.addGraph(graph, g);
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.TRUE);
        VisualItem f = (VisualItem) vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
    }
}
