package qub;

public interface GitCloneArguments<T> extends GitCommandArguments<T>
{
    /**
     * Operate quietly. Progress is not reported to the standard error stream.
     * @return This object for method chaining.
     */
    default T addQuiet()
    {
        return this.addCommandArguments("--quiet");
    }

    /**
     * Run verbosely. Does not affect the reporting of progress status to the standard error
     * stream.
     * @return This object for method chaining.
     */
    default T addVerbose()
    {
        return this.addCommandArguments("--verbose");
    }

    /**
     * Progress status is reported on the standard error stream by default when it is attached to a
     * terminal, unless --quiet is specified. This flag forces progress status even if the standard
     * error stream is not directed to a terminal.
     * @return This object for method chaining.
     */
    default T addProgress()
    {
        return this.addCommandArguments("--progress");
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    default T addRepository(String repository)
    {
        PreCondition.assertNotNullAndNotEmpty(repository, "repository");
        PreCondition.assertFalse(this.hasRepository(), "this.hasRepository()");

        return this.addCommandArguments(Strings.escapeAndQuote(repository));
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    default T addRepository(URL repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.toString());
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    default T addRepository(Path repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.toString());
    }

    /**
     * The (possibly remote) repository to clone from.
     * @param repository The (possibly remote repository to clone from.
     * @return This object for method chaining.
     */
    default T addRepository(Folder repository)
    {
        PreCondition.assertNotNull(repository, "repository");

        return this.addRepository(repository.getPath());
    }

    /**
     * Get whether or not a repository has been added.
     * @return Whether or not a repository has been added.
     */
    boolean hasRepository();

    /**
     * The name of a new directory to clone into. The "humanish" part of the source repository is
     * used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The path to the directory to clone into.
     * @return This object for method chaining.
     */
    default T addDirectory(String directory)
    {
        PreCondition.assertNotNullAndNotEmpty(directory, "directory");
        PreCondition.assertTrue(this.hasRepository(), "this.hasRepository()");
        PreCondition.assertFalse(this.hasDirectory(), "this.hasDirectory()");

        return this.addCommandArguments(Strings.escapeAndQuote(directory));
    }

    /**
     * The name of a new directory to clone into. The "humanish" part of the source repository is
     * used if no directory is explicitly given (repo for /path/to/repo.git and foo for
     * host.xz:foo/.git). Cloning into an existing directory is only allowed if the directory is
     * empty.
     * @param directory The path to the directory to clone into.
     * @return This object for method chaining.
     */
    default T addDirectory(Path directory)
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
    default T addDirectory(Folder directory)
    {
        PreCondition.assertNotNull(directory, "directory");

        return this.addDirectory(directory.getPath());
    }

    /**
     * Get whether or not a directory has been added.
     * @return Whether or not a directory has been added.
     */
    boolean hasDirectory();
}
