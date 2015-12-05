/* Pulic domain. No rights reserved. */
package de.speexx.poc.cdiandjcommanderpoc;

import com.beust.jcommander.JCommander;
import java.util.Map;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/** @author Sascha Kohlmann */
@Singleton
public class Application {
    
    private final static MainConfiguration MAIN_CONFIG = new MainConfiguration();

    @Produces @Config
    private MainConfiguration configuration() {
        return MAIN_CONFIG;
    }
    
    // For unit tests
    public static String result;
    
    @Inject
    private Instance<Command> commands;

    public static final void main(final String... args) {        
        final Weld weld = new Weld();
        try (final WeldContainer container = weld.initialize()) {
            result = container.select(Application.class).get().run(args);
        }
    }
    
    String run(final String... args) {
        final JCommander jc = new JCommander(MAIN_CONFIG);
        this.commands.forEach(cmd -> jc.addCommand(cmd));

        jc.parse(args);

        final Command cmd = findCommand(jc);
        return cmd != null ? cmd.execute() : null;
    }
    
    Command findCommand(final JCommander jc) {
        final String parsedCommand = jc.getParsedCommand();

        for (final Map.Entry<String, JCommander> cmdEntry : jc.getCommands().entrySet()) {
            final String name = cmdEntry.getKey();
            if (name != null && name.equals(parsedCommand)) {
                return (Command) cmdEntry.getValue().getObjects().get(0);
            }
        }
        
        return null;
    }
}
