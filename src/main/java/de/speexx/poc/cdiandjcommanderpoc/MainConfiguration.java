/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import com.beust.jcommander.Parameter;

/** @author Sascha Kohlmann */
public class MainConfiguration {
    
    @Parameter(names = "-m")
    private String main;

    public String getMain() {
        return main;
    }
}
