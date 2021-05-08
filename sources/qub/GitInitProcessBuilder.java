package qub;

/**
 * A process builder that can be used to build up a "git init" process.
 */
public class GitInitProcessBuilder extends GitCommandProcessBuilder<GitInitProcessBuilder> implements GitInitArguments<GitInitProcessBuilder>
{
    private GitInitProcessBuilder(GitProcessBuilder gitProcessBuilder)
    {
        super(gitProcessBuilder, "init");
    }

    /**
     * Create a new GitInitProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitInitProcessBuilder will wrap.
     * @return A new GitInitProcessBuilder.
     */
    public static GitInitProcessBuilder create(GitProcessBuilder gitProcessBuilder)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        return new GitInitProcessBuilder(gitProcessBuilder);
    }
}
