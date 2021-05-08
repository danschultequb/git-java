package qub;

public interface GitInitArguments<T> extends GitCommandArguments<T>
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
     * Create a bare repository. If GIT_DIR environment is not set, it is set to the current
     * working directory.
     * @return This object for method chaining.
     */
    default T addBare()
    {
        return this.addCommandArguments("--bare");
    }

    /**
     * Specify the directory from which templates will be used. (See the "TEMPLATE DIRECTORY"
     * section below.)
     * @param templateDirectory The path to the directory that will be used as a template.
     * @return This object for method chaining.
     */
    default T addTemplate(String templateDirectory)
    {
        PreCondition.assertNotNullAndNotEmpty(templateDirectory, "templateDirectory");

        return this.addCommandArguments("--template=" + Strings.escapeAndQuote(templateDirectory));
    }

    /**
     * Specify the directory from which templates will be used. (See the "TEMPLATE DIRECTORY"
     * section below.)
     * @param templateDirectory The path to the directory that will be used as a template.
     * @return This object for method chaining.
     */
    default T addTemplate(Path templateDirectory)
    {
        PreCondition.assertNotNull(templateDirectory, "templateDirectory");

        return this.addTemplate(templateDirectory.toString());
    }

    /**
     * Specify the directory from which templates will be used. (See the "TEMPLATE DIRECTORY"
     * section below.)
     * @param templateDirectory The path to the directory that will be used as a template.
     * @return This object for method chaining.
     */
    default T addTemplate(Folder templateDirectory)
    {
        PreCondition.assertNotNull(templateDirectory, "templateDirectory");

        return this.addTemplate(templateDirectory.getPath());
    }

    /**
     * Instead of initializing the repository as a directory to either $GIT_DIR or ./.git/, create
     * a text file there containing the path to the actual repository. This file acts as
     * filesystem-agnostic Git symbolic link to the repository.<br/>
     * <br/>
     * If this is reinitialization, the repository will be moved to the specified path.
     * @param separateGitDir The path to the separate git directory.
     * @return This object for method chaining.
     */
    default T addSeparateGitDir(String separateGitDir)
    {
        PreCondition.assertNotNullAndNotEmpty(separateGitDir, "separateGitDir");

        return this.addCommandArguments("--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir));
    }

    /**
     * Instead of initializing the repository as a directory to either $GIT_DIR or ./.git/, create
     * a text file there containing the path to the actual repository. This file acts as
     * filesystem-agnostic Git symbolic link to the repository.<br/>
     * <br/>
     * If this is reinitialization, the repository will be moved to the specified path.
     * @param separateGitDir The path to the separate git directory.
     * @return This object for method chaining.
     */
    default T addSeparateGitDir(Path separateGitDir)
    {
        PreCondition.assertNotNull(separateGitDir, "separateGitDir");

        return this.addSeparateGitDir(separateGitDir.toString());
    }

    /**
     * Instead of initializing the repository as a directory to either $GIT_DIR or ./.git/, create
     * a text file there containing the path to the actual repository. This file acts as
     * filesystem-agnostic Git symbolic link to the repository.<br/>
     * <br/>
     * If this is reinitialization, the repository will be moved to the specified path.
     * @param separateGitDir The path to the separate git directory.
     * @return This object for method chaining.
     */
    default T addSeparateGitDir(Folder separateGitDir)
    {
        PreCondition.assertNotNull(separateGitDir, "separateGitDir");

        return this.addSeparateGitDir(separateGitDir.getPath());
    }

    /**
     * Use the specified name for the initial branch in the newly created repository. If not
     * specified, fall back to the default name (currently master, but this is subject to change in
     * the future; the name can be customized via the init.defaultBranch configuration variable).
     * @param initialBranch The name to use for the initial branch.
     * @return This object for method chaining.
     */
    default T addInitialBranch(String initialBranch)
    {
        PreCondition.assertNotNullAndNotEmpty(initialBranch, "initialBranch");

        return this.addCommandArguments("--initial-branch", initialBranch);
    }

    /**
     * The directory to create the new repository in.
     * @param directory The directory to create the new repository in.
     * @return This object for method chaining.
     */
    default T addDirectory(String directory)
    {
        PreCondition.assertNotNullAndNotEmpty(directory, "directory");

        return this.addCommandArguments(directory);
    }

    /**
     * The directory to create the new repository in.
     * @param directory The directory to create the new repository in.
     * @return This object for method chaining.
     */
    default T addDirectory(Path directory)
    {
        PreCondition.assertNotNull(directory, "directory");

        return this.addDirectory(directory.toString());
    }

    /**
     * The directory to create the new repository in.
     * @param directory The directory to create the new repository in.
     * @return This object for method chaining.
     */
    default T addDirectory(Folder directory)
    {
        PreCondition.assertNotNull(directory, "directory");

        return this.addDirectory(directory.getPath());
    }
}
