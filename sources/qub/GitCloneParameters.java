package qub;

public class GitCloneParameters extends GitParametersDecorator<GitCloneParameters>
{
    private GitCloneParameters(Path executablePath)
    {
        super(executablePath);

        this.addCommandArgument("clone");
    }

    public static GitCloneParameters create()
    {
        return GitCloneParameters.create("git");
    }

    public static GitCloneParameters create(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return GitCloneParameters.create(Path.parse(executablePath));
    }

    public static GitCloneParameters create(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return new GitCloneParameters(executablePath);
    }

    public static GitCloneParameters create(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return GitCloneParameters.create(executableFile.getPath());
    }
    
    /**
     * Operate quietly. Progress is not reported to the standard error stream.
     * @return This object for method chaining.
     */
    public GitCloneParameters addQuiet()
    {
        return this.addCommandArgument("--quiet");
    }

    /**
     * Run verbosely. Does not affect the reporting of progress status to the standard error
     * stream.
     * @return This object for method chaining.
     */
    public GitCloneParameters addVerbose()
    {
        return this.addCommandArgument("--verbose");
    }

    /**
     * Progress status is reported on the standard error stream by default when it is attached to a
     * terminal, unless --quiet is specified. This flag forces progress status even if the standard
     * error stream is not directed to a terminal.
     * @return This object for method chaining.
     */
    public GitCloneParameters addProgress()
    {
        return this.addCommandArgument("--progress");
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    public GitCloneParameters addRepository(String repository)
    {
        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        return this.addCommandArgument(repository);
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    public GitCloneParameters addRepository(URL repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.toString(true));
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    public GitCloneParameters addRepository(Path repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.toString());
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    public GitCloneParameters addRepository(Folder repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.getPath());
    }

    /**
     * The name of a new directory to clone into. The "humanish" part of the source repository is
     * used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The path to the directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneParameters addDirectory(String directory)
    {
        PreCondition.assertNotNullAndNotEmpty(directory, "directory");

        return this.addCommandArgument(directory);
    }

    /**
     * The name of a new directory to clone into. The "humanish" part of the source repository is
     * used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The path to the directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneParameters addDirectory(Path directory)
    {
        PreCondition.assertNotNull(directory, "directory");

        return this.addDirectory(directory.toString());
    }

    /**
     * The name of a new directory to clone into. The "humanish" part of the source repository is
     * used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The directory to clone into.
     * @return This object for method chaining.
     */
    public GitCloneParameters addDirectory(Folder directory)
    {
        PreCondition.assertNotNull(directory, "directory");

        return this.addDirectory(directory.getPath());
    }
}
