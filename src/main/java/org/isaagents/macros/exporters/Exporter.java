package org.isaagents.macros.exporters;

import org.isaagents.macros.gui.macro.Macro;
import org.isaagents.macros.motiffinder.Motif;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface Exporter {

    public void export(Set<Macro> macros, File export);
    public void export(Map<String, Motif> motifs, String timeTaken, File export);
}
