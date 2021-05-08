package qub;

/**
 * A process builder that can be used to build up a "git pull" process.
 */
public class GitPullProcessBuilder extends GitCommandProcessBuilder<GitPullProcessBuilder> implements GitPullArguments<GitPullProcessBuilder>
{
    private GitPullProcessBuilder(GitProcessBuilder processBuilder)
    {
        super(processBuilder, "pull");
    }

    /**
     * Create a new GitPullProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitPullProcessBuilder will wrap.
     * @return The new GitPullProcessBuilder.
     */
    public static GitPullProcessBuilder create(GitProcessBuilder gitProcessBuilder)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        return new GitPullProcessBuilder(gitProcessBuilder);
    }
}
