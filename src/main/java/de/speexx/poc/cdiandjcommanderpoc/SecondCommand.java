/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import javax.inject.Inject;

/** @author Sascha Kohlmann */
@Parameters(commandDescription = "Simple second command", commandNames = "second")
public class SecondCommand implements Command {

    @Inject @Config
    private MainConfiguration mainConfig;
    
    @Parameter(names = "-c2p")
    private String parameter;
    
    @Override
    public String execute() {
        return this.mainConfig.getMain() + this.parameter;
    }
}
