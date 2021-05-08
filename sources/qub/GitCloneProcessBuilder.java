package qub;

/**
 * A process builder that can be used to build up a "git clone" process.
 */
public class GitCloneProcessBuilder extends GitCommandProcessBuilder<GitCloneProcessBuilder> implements GitCloneArguments<GitCloneProcessBuilder>
{
    private boolean hasRepository;
    private boolean hasDirectory;

    private GitCloneProcessBuilder(GitProcessBuilder gitProcessBuilder)
    {
        super(gitProcessBuilder, "clone");
    }

    /**
     * Create a new GitCloneProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitCloneProcessBuilder will wrap.
     * @return A new GitCloneProcessBuilder.
     */
    public static GitCloneProcessBuilder create(GitProcessBuilder gitProcessBuilder)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        return new GitCloneProcessBuilder(gitProcessBuilder);
    }

    @Override
    public GitCloneProcessBuilder addRepository(String repository)
    {
        final GitCloneProcessBuilder result = GitCloneArguments.super.addRepository(repository);
        this.hasRepository = true;

        return result;
    }

    @Override
    public boolean hasRepository()
    {
        return this.hasRepository;
    }

    @Override
    public GitCloneProcessBuilder addDirectory(String directory)
    {
        final GitCloneProcessBuilder result = GitCloneArguments.super.addDirectory(directory);
        this.hasDirectory = true;

        return result;
    }

    @Override
    public boolean hasDirectory()
    {
        return this.hasDirectory;
    }
}
