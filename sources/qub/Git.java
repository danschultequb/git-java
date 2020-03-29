package qub;

/**
 * A Git implementation that invokes the Git executable found on the system's path.
 */
public class Git
{
    private final Process process;

    private Git(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        this.process = process;
    }

    /**
     * Create a new GitExecutable object that can be used to invoke "git" operations using the Git
     * executable found on the system's path.
     * @param process The process of the current application.
     * @return The new GitExecutable object.
     */
    public static Git create(Process process)
    {
        PreCondition.assertNotNull(process, "process");

        return new Git(process);
    }

    /**
     * Get a ProcessBuilder that can be used to run the git executable.
     * @return A ProcessBuilder that can be used to run the git executable.
     */
    public Result<ProcessBuilder> getGitProcessBuilder()
    {
        return process.getProcessBuilder("git");
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
}
