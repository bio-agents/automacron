package org.isaagents.macros.plugin;

import org.apache.log4j.Logger;
import org.isaagents.isacreator.common.CommonMouseAdapter;
import org.isaagents.isacreator.gui.AssaySpreadsheet;
import org.isaagents.isacreator.managers.ApplicationManager;
import org.isaagents.isacreator.model.Assay;
import org.isaagents.isacreator.plugins.host.service.PluginMenu;
import org.isaagents.isacreator.spreadsheet.Spreadsheet;

import org.isaagents.macros.plugin.glyphbasedworkflow.WorkflowGraphCreator;

import org.isaagents.macros.plugin.workflowvisualization.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Set;


public class WorkflowVisualizationPlugin implements PluginMenu {

    static {
        UIManager.put("MenuItemUI", "org.isaagents.isacreator.common.CustomMenuItemUI");
        UIManager.put("MenuUI", "org.isaagents.isacreator.common.CustomMenuUI");
    }

    private Logger log = Logger.getLogger(WorkflowVisualizationPlugin.class.getName());
    private JMenu workflowVisMenu;
    private JMenuItem selectedSamplesMenuItem, allMenuItem;

    public WorkflowVisualizationPlugin() {
        createSelectedSamplesMenuItem();
        createAllSamplesMenuItem();

        workflowVisMenu = new JMenu("Workflow Visualization");

        workflowVisMenu.add(selectedSamplesMenuItem);
        workflowVisMenu.add(allMenuItem);

        createFilterForWorkflow();
    }

    private void createFilterForWorkflow() {
        workflowVisMenu.addMouseListener(new CommonMouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
                super.mouseEntered(mouseEvent);
                Spreadsheet currentSpreadsheet = getCurrentSpreadsheet();

                selectedSamplesMenuItem.setEnabled(currentSpreadsheet != null);
                allMenuItem.setEnabled(currentSpreadsheet != null);
            }
        });
    }

    private void createSelectedSamplesMenuItem() {
        selectedSamplesMenuItem = new JMenuItem(new AbstractAction("View workflow for selected samples") {

            public void actionPerformed(ActionEvent e) {
                try {
                ApplicationManager.getCurrentApplicationInstance();

                WorkflowGraphCreator workflowGraphCreator = new WorkflowGraphCreator();
                Spreadsheet currentSpreadsheet = getCurrentSpreadsheet();
                if (currentSpreadsheet != null) {
                    Set<String> selectedSamples = currentSpreadsheet.getSpreadsheetFunctions().getValuesInSelectedRowsForColumn("Sample Name");
                    WorkflowInformation workflowInformation = workflowGraphCreator.createGraphMLFileForExperiment(true, selectedSamples);
                    new WorkflowVisualization(workflowInformation).createGUI();
                } else {

                    JOptionPane.showMessageDialog(null, "Sorry, this function is only available when focused on an Assay!", "Error occurred", JOptionPane.ERROR_MESSAGE);
                }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Sorry, we were unable to complete that operation. Please try saving the ISA-Tab and trying again!", "Error occurred", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void createAllSamplesMenuItem() {
        allMenuItem = new JMenuItem(new AbstractAction("View workflow for all samples") {

            public void actionPerformed(ActionEvent e) {
                try {
                    ApplicationManager.getCurrentApplicationInstance();

                    WorkflowGraphCreator workflowGraphCreator = new WorkflowGraphCreator();
                    Spreadsheet currentSpreadsheet = getCurrentSpreadsheet();
                    if (currentSpreadsheet != null) {
                        WorkflowInformation workflowInformation = workflowGraphCreator.createGraphMLFileForExperiment(true);
                        new WorkflowVisualization(workflowInformation).createGUI();
                    } else {

                        JOptionPane.showMessageDialog(null, "Sorry, this function is only available when focused on an Assay!", "Error occurred", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Sorry, we were unable to complete that operation. Please try saving the ISA-Tab and trying again!", "Error occurred", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private Spreadsheet getCurrentSpreadsheet() {
        if (ApplicationManager.getCurrentApplicationInstance() != null) {
            if (ApplicationManager.getScreenInView() != null && ApplicationManager.getScreenInView() instanceof Assay) {
                return ((AssaySpreadsheet) ApplicationManager.getUserInterfaceForISASection((Assay) ApplicationManager.getScreenInView())).getSpreadsheet();
            }
        }
        return null;
    }

    public JMenu removeMenu(JMenu menu) {

        menu.remove(workflowVisMenu);

        return menu;
    }

    public JMenu addMenu(JMenu menu) {
        menu.add(workflowVisMenu);

        return menu;
    }
}