package qub;

/**
 * An object that allows applications to invoke Git operations.
 */
public class Git extends ChildProcessRunnerWrapper<Git,GitParameters>
{
    private Git(ChildProcessRunner childProcessRunner)
    {
        super(childProcessRunner, GitParameters::create, "git");
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

    public Result<VersionNumber> version()
    {
        return Result.create(() ->
        {
            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
            this.run((GitParameters parameters) ->
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
