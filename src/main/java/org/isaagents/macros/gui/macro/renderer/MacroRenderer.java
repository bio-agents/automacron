package org.isaagents.macros.gui.macro.renderer;

import org.isaagents.macros.gui.macro.Macro;
import org.isaagents.macros.motiffinder.Motif;

import java.io.File;
import java.util.Map;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 03/12/2012
 *         Time: 08:11
 */
public interface MacroRenderer {


    public Map<RenderingType, File> renderMacros(Macro macro, MotifGraphInfo motifGraphInfo);
}
