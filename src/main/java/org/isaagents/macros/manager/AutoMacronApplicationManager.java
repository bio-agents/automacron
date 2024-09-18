package org.isaagents.macros.manager;

import org.isaagents.macros.gui.MacroGraphUI;

/**
 * Created by the ISA team
 *
 * @author Eamonn Maguire (eamonnmag@gmail.com)
 *         <p/>
 *         Date: 02/10/2012
 *         Time: 13:10
 */
public class AutoMacronApplicationManager {

    private static MacroGraphUI currentApplicationInstance;

    public static MacroGraphUI getCurrentApplicationInstance() {
        return currentApplicationInstance;
    }

    public static void setCurrentApplicationInstance(MacroGraphUI currentApplicationInstance) {
        AutoMacronApplicationManager.currentApplicationInstance = currentApplicationInstance;
    }
}
