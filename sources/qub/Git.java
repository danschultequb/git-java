package qub;

/**
 * An object that allows applications to invoke Git operations.
 */
public class Git
{
    private final ChildProcessRunner childProcessRunner;
    private Path executablePath;
    
    private Git(ChildProcessRunner childProcessRunner)
    {
        PreCondition.assertNotNull(childProcessRunner, "childProcessRunner");
        
        this.childProcessRunner = childProcessRunner;
        this.executablePath = Path.parse("git");
    }
    
    /**
     * Create a new Git object object that can be used to invoke Git operations.
     * @param process This application's process.
     * @return The new Git object.
     */
    public static Git create(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return Git.create(process.getChildProcessRunner());
    }

    /**
     * Create a new Git object object that can be used to invoke Git operations.
     * @param childProcessRunner The child process runner that will be used to invoke Git
     *                           operations.
     * @return The new Git object.
     */
    public static Git create(ChildProcessRunner childProcessRunner)
    {
        PreCondition.assertNotNull(childProcessRunner, "childProcessRunner");

        return new Git(childProcessRunner);
    }

    public Path getExecutablePath()
    {
        return this.executablePath;
    }

    public Git setExecutablePath(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return this.setExecutablePath(Path.parse(executablePath));
    }

    public Git setExecutablePath(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        this.executablePath = executablePath;

        return this;
    }

    public Git setExecutable(File executable)
    {
        PreCondition.assertNotNull(executable, "executable");

        return this.setExecutablePath(executable.getPath());
    }

    private <T extends GitParameters> T addParameterDefaults(Function1<Path,T> creator)
    {
        return creator.run(this.executablePath);
    }

    public Result<Integer> run(String... arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(Iterable.create(arguments));
    }

    public Result<Integer> run(Iterable<String> arguments)
    {
        PreCondition.assertNotNull(arguments, "arguments");

        return this.run(this.addParameterDefaults(GitParameters::create).addArguments(arguments));
    }

    public Result<Integer> run(GitParameters parameters)
    {
        PreCondition.assertNotNull(parameters, "parameters");

        return this.childProcessRunner.run(parameters);
    }

    public Result<Integer> run(Action1<GitParameters> parametersSetup)
    {
        return this.run(GitParameters::create, parametersSetup);
    }

    private <T extends GitParameters> Result<Integer> run(Function1<Path,T> parametersCreator, Action1<T> parametersSetup)
    {
        PreCondition.assertNotNull(parametersCreator, "parametersCreator");
        PreCondition.assertNotNull(parametersSetup, "parametersSetup");

        final T parameters = this.addParameterDefaults(parametersCreator);
        parametersSetup.run(parameters);
        return this.run(parameters);
    }

    public Result<VersionNumber> version()
    {
        return Result.create(() ->
        {
            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
            this.run(parameters ->
            {
                parameters.addArgument("--version");
                parameters.redirectOutputTo(output);
            }).await();
            String outputText = output.getText().await();
            outputText = outputText.trim();
            outputText = outputText.substring("git version ".length());
            final VersionNumber result = VersionNumber.parse(outputText).await();

            PostCondition.assertNotNull(result, "result");

            return result;
        });
    }

    public Result<Integer> clone(GitCloneParameters parameters)
    {
        return this.run(parameters);
    }

    public Result<Integer> clone(Action1<GitCloneParameters> parametersSetup)
    {
        return this.run(GitCloneParameters::create, parametersSetup);
    }

    public Result<Integer> init(GitInitParameters parameters)
    {
        return this.run(parameters);
    }

    public Result<Integer> init(Action1<GitInitParameters> parametersSetup)
    {
        return this.run(GitInitParameters::create, parametersSetup);
    }

    public Result<Integer> pull(GitPullParameters parameters)
    {
        return this.run(parameters);
    }

    public Result<Integer> pull(Action1<GitPullParameters> parametersSetup)
    {
        return this.run(GitPullParameters::create, parametersSetup);
    }

    public Result<Integer> remoteAdd(GitRemoteAddParameters parameters)
    {
        return this.run(parameters);
    }

    public Result<Integer> remoteAdd(Action1<GitRemoteAddParameters> parametersSetup)
    {
        return this.run(GitRemoteAddParameters::create, parametersSetup);
    }
}
