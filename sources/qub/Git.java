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
    public Result<ProcessBuilder> getGitProcessBuilder()
    {
        return this.processFactory.getProcessBuilder("git");
    }

    /**
     * Get a GitCloneProcessBuilder that can be used to invoke the "git clone" operation.
     * @param repository The location of the repository to clone.
     * @return The GitCloneProcessBuilder that can be used to invoke the "git clone" operation.
     */
    public Result<GitCloneProcessBuilder> getCloneProcessBuilder(String repository)
    {
        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        return Result.create(() ->
        {
            return GitCloneProcessBuilder.create(this.getGitProcessBuilder().await(), repository);
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
}
