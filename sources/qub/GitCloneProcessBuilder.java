package qub;

/**
 * A process builder that can be used to build up a "git clone" process.
 */
public class GitCloneProcessBuilder extends ProcessBuilderDecorator<GitCloneProcessBuilder>
{
    private final String repository;

    private Boolean progress;
    private String directory;

    private GitCloneProcessBuilder(ProcessBuilder gitProcessBuilder, String repository)
    {
        super(gitProcessBuilder);

        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        gitProcessBuilder.addArgument("clone");

        this.repository = repository;
    }

    /**
     * Create a new GitCloneProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitCloneProcessBuilder will wrap.
     * @param repository The location of the repository to clone.
     * @return A new GitCloneProcessBuilder.
     */
    public static GitCloneProcessBuilder create(ProcessBuilder gitProcessBuilder, String repository)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");
        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        return new GitCloneProcessBuilder(gitProcessBuilder, repository);
    }

    @Override
    public Result<Integer> run()
    {
        if (Booleans.isTrue(this.progress))
        {
            this.addArgument("--progress");
        }

        this.addArgument(Strings.escapeAndQuote(this.repository));

        if (!Strings.isNullOrEmpty(this.directory))
        {
            this.addArgument(Strings.escapeAndQuote(this.directory));
        }

        return super.run();
    }

    /**
     * Get the (possibly remote) repository to clone from. See the GIT URLS section below for more
     * information on specifying repositories.
     * @return The (possibly remote) repository to clone from.
     */
    public String getRepository()
    {
        return this.repository;
    }

    /**
     * Progress status is reported on the standard error stream by default when it is attached to a
     * terminal, unless --quiet is specified. This flag forces progress status even if the standard
     * error stream is not directed to a terminal.
     * @return Whether or not progress status will be forced to be reported.
     */
    public Boolean getProgress()
    {
        return this.progress;
    }

    /**
     * Progress status is reported on the standard error stream by default when it is attached to a
     * terminal, unless --quiet is specified. This flag forces progress status even if the standard
     * error stream is not directed to a terminal.
     * @param progress Whether or not progress status will be forced to be reported.
     * @return This object for method chaining.
     */
    public GitCloneProcessBuilder setProgress(Boolean progress)
    {
        this.progress = progress;
        return this;
    }

    /**
     * Get the name of a new directory to clone into. The "humanish" part of the source repository
     * is used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @return The name of a new directory to clone into.
     */
    public String getDirectory()
    {
        return this.directory;
    }

    /**
     * Set the name of a new directory to clone into. The "humanish" part of the source repository
     * is used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The name of a new directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneProcessBuilder setDirectory(String directory)
    {
        this.directory = directory;

        return this;
    }

    /**
     * Set the name of a new directory to clone into. The "humanish" part of the source repository
     * is used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The name of a new directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneProcessBuilder setDirectory(Path directory)
    {
        return this.setDirectory(directory == null ? null : directory.toString());
    }

    /**
     * Set the name of a new directory to clone into. The "humanish" part of the source repository
     * is used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The name of a new directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneProcessBuilder setDirectory(Folder directory)
    {
        return this.setDirectory(directory == null ? null : directory.toString());
    }
}
