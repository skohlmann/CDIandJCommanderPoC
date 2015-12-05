/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import javax.inject.Inject;

/** @author Sascha Kohlmann */
@Parameters(commandDescription = "Simple first command", commandNames = "first")
public class FirstCommand implements Command {

    @Inject @Config
    private MainConfiguration mainConfig;
    
    @Parameter(names = "-c1p")
    private String parameter;
    
    @Override
    public String execute() {
        return this.mainConfig.getMain() + this.parameter;
    }
}
