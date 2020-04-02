package qub;

/**
 * A process builder that can be used to build up a "git pull" process.
 */
public class GitPullProcessBuilder extends ProcessBuilderDecorator<GitPullProcessBuilder>
{
    private GitPullProcessBuilder(ProcessBuilder processBuilder)
    {
        super(processBuilder);

        processBuilder.addArgument("pull");
    }

    /**
     * Create a new GitPullProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitPullProcessBuilder will wrap.
     * @return The new GitPullProcessBuilder.
     */
    public static GitPullProcessBuilder create(ProcessBuilder gitProcessBuilder)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        return new GitPullProcessBuilder(gitProcessBuilder);
    }

    @Override
    public Result<Integer> run()
    {
        return super.run();
    }
}
