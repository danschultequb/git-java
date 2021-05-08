package qub;

/**
 * A Git implementation that invokes the Git executable found on the system's path.
 */
public class Git
{
    private final ProcessFactory processFactory;

    private Git(ProcessFactory processFactory)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");

        this.processFactory = processFactory;
    }

    /**
     * Create a new GitExecutable object that can be used to invoke "git" operations using the Git
     * executable found on the system's path.
     * @param process The process that will be used to invoke the "git" process.
     * @return The new Git object.
     */
    public static Git create(DesktopProcess process)
    {
        PreCondition.assertNotNull(process, "process");

        return Git.create(process.getProcessFactory());
    }

    /**
     * Create a new GitExecutable object that can be used to invoke "git" operations using the Git
     * executable found on the system's path.
     * @param processFactory The process factory that will be used to invoke the "git" process.
     * @return The new Git object.
     */
    public static Git create(ProcessFactory processFactory)
    {
        PreCondition.assertNotNull(processFactory, "processFactory");

        return new Git(processFactory);
    }

    /**
     * Get a ProcessBuilder that can be used to run the git executable.
     * @return A ProcessBuilder that can be used to run the git executable.
     */
    public Result<GitProcessBuilder> getGitProcessBuilder()
    {
        return Result.create(() ->
        {
            return GitProcessBuilder.create(this.processFactory.getProcessBuilder("git").await());
        });
    }

    /**
     * Get a GitCloneProcessBuilder that can be used to invoke the "git clone" operation.
     * @return The GitCloneProcessBuilder that can be used to invoke the "git clone" operation.
     */
    public Result<GitCloneProcessBuilder> getCloneProcessBuilder()
    {
        return Result.create(() ->
        {
            return GitCloneProcessBuilder.create(this.getGitProcessBuilder().await());
        });
    }

    /**
     * Get a GitPullProcessBuilder that can be used to invoke the "git pull" operation.
     * @return The GitPullProcessBuilder that can be used to invoke the "git clone" operation.
     */
    public Result<GitPullProcessBuilder> getPullProcessBuilder()
    {
        return Result.create(() ->
        {
            return GitPullProcessBuilder.create(this.getGitProcessBuilder().await());
        });
    }

    /**
     * Get a GitInitProcessBuilder that can be used to invoke the "git init" operation.
     * @return The GitInitProcessBuilder that can be used to invoke the "git init" operation.
     */
    public Result<GitInitProcessBuilder> getInitProcessBuilder()
    {
        return Result.create(() ->
        {
            return GitInitProcessBuilder.create(this.getGitProcessBuilder().await());
        });
    }
}
