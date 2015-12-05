#JCommander with CDI over Weld SE
A proof of concept to use the incredible [JCommander](http://jcommander.org/) API from Cédric Beust together with CDI in a Java SE environment.

The goal was to use the commands API with the [`@Parameters`](http://jcommander.org/apidocs/com/beust/jcommander/Parameters.html) annotation together with CDIs natural plugin API [`Instance`](http://docs.oracle.com/javaee/6/api/javax/enterprise/inject/Instance.html).

This means, a lot of sub commands with different parameters can be executed by a main command from command line. The parameters of the main command should be injected to the sub command implementation. The sub command can have own, different parameters.

An example for such command system is `git`.

**Example**
> git -c author=sascha.kohlmann@example.com commit -am "a commit"
#Implementation
##The main command configuration
The class with the `main` method must contain `@Produces` annotated method which returns a `static` variable with the only `MainConfiguration` instance.

	public class Application {
    
	    private final static MainConfiguration MAIN_CONFIG = new MainConfiguration();

	    @Produces @Config
	    private MainConfiguration configuration() {
	        return MAIN_CONFIG;
	    }
	    
	    public static final void main(final String... args) {
		    // do something
	    }
    }

The `main`method contains only the initialization of the Weld-SE container and runs the application. 

	public static final void main(final String... args) {        
        final Weld weld = new Weld();
        try (final WeldContainer container = weld.initialize()) {
            result = container.select(Main.class).get().run(args);
        }
    }
    
    String run(final String... args) {
	    // the application
    }

### `MainConfiguration`
The `MainConfiguration`class is a straight forward data holder. The configuration field are annotated with `@Parameter`.

	public class MainConfiguration {
    
	    @Parameter(names = "-m")
	    private String main;

	    public String getMain() { return main; }
	}

##The sub commands
Sub commands implements a common command API like the following:

	public interface Command {
		String execute();
	}
Implementations of the command must have at least the `@Parameters` type annotation. They may have sub command specific parameters and the parameter data of the main command.

	@Parameters(commandDescription = "Simple sub command", commandNames = "subcmd")
	public class FirstCommand implements Command {

	    @Inject @Config
	    private MainConfiguration mainConfig;
    
	    @Parameter(names = "-p")
	    private String parameter;
    
	    @Override
	    public String execute() {
		    return this.mainConfig.getMain() + this.parameter;
	    }
	}
After the CDI initialization all sub command with the injection point for the `MainConfiguration`contains now the static instance from the main class. But the configuration is yet not initialized. So having a method with `@PostConstruct` annotated, using the main configuration will not work. Also using the sub command specific parameters in such a method will not work. 

The JCommander initialization follows in the next step. But before, we must enhance the `Application` class with the CDI plugin API.

###`Instance<Command>`
The Application class gets an injectable `Ìnstance<Command>` field. This field will be filled by CDI after the Weld initialization with all available `Command` implementations.

	public class Main {
    
		@Inject
	    private Instance<Command> commands;	    
	    
	    public static final void main(final String... args) {
		    // do something
	    }
    }


##JCommander initialization
The `run`method initialize the JCommander with the following steps:

1. Create a new JCommander instance with the static `MAIN_CONFIG`: `JCommander jc = new JCommander(MAIN_CONFIG);`
2. Add all instances of `private Instance<Command> commands` to the JCommander instance: `this.commands.forEach(cmd -> jc.addCommand(cmd));`

Afterwards, parse the command line arguments with the JCommander instance.

	jc.parse(args);

That's it. 

##Execute the sub command
The last step is now to get the parsed sub command name an fetch the sub command implementation from JCommander. Then call the `execute()` method.

    final String parsedCommand = jc.getParsedCommand();

    for (final Map.Entry<String, JCommander> cmdEntry : jc.getCommands().entrySet()) {
        final String name = cmdEntry.getKey();
        if (name != null && name.equals(parsedCommand)) {
            ((Command) cmdEntry.getValue().getObjects().get(0)).execute();
        }
    }

#Testing
Testing ist straight forward:

    @Test
    public void test_sub_command_execution() {
        Application.main("-m", "main", "subcmd", "-p", "sub");
        assertThat(Application.result, is(equalTo("mainsub")));
    }

#Conclusion
Using CDI and JCommander with complex commands in a Java SE environment is quite simple. Using the CDI natural plugin API (`Instance`) is also very simple. Together this is a strong duo to simplify the development of Java command line tools.