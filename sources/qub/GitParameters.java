package qub;

public interface GitParameters extends ChildProcessParameters
{
    static GitParameters create()
    {
        return GitParameters.create("git");
    }

    static GitParameters create(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return GitParameters.create(Path.parse(executablePath));
    }

    static GitParameters create(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return BasicGitParameters.create(executablePath);
    }

    static GitParameters create(File executable)
    {
        PreCondition.assertNotNull(executable, "executable");

        return GitParameters.create(executable.getPath());
    }

    @Override
    GitParameters insertArgument(int index, String argument);

    @Override
    default GitParameters addArgument(String argument)
    {
        return (GitParameters)ChildProcessParameters.super.addArgument(argument);
    }

    @Override
    default GitParameters addArguments(String... arguments)
    {
        return (GitParameters)ChildProcessParameters.super.addArguments(arguments);
    }

    @Override
    default GitParameters addArguments(Iterable<String> arguments)
    {
        return (GitParameters)ChildProcessParameters.super.addArguments(arguments);
    }

    Iterable<String> getGitArguments();

    GitParameters insertGitArgument(int gitArgumentIndex, String gitArgument);

    /**
     * Add a Git argument that will appear before the command.
     * @param gitArgument The Git argument that will appear before the command.
     * @return This object for method chaining.
     */
    GitParameters addGitArgument(String gitArgument);

    default GitParameters addGitArguments(String... gitArguments)
    {
        PreCondition.assertNotNull(gitArguments, "gitArguments");

        for (final String gitArgument : gitArguments)
        {
            this.addGitArgument(gitArgument);
        }

        return this;
    }

    default GitParameters addGitArguments(Iterable<String> gitArguments)
    {
        PreCondition.assertNotNull(gitArguments, "gitArguments");

        for (final String gitArgument : gitArguments)
        {
            this.addGitArgument(gitArgument);
        }

        return this;
    }

    Iterable<String> getCommandArguments();

    GitParameters insertCommandArgument(int commandArgumentIndex, String commandArgument);

    GitParameters addCommandArgument(String commandArgument);

    default GitParameters addCommandArguments(String... commandArguments)
    {
        PreCondition.assertNotNull(commandArguments, "commandArguments");

        for (final String commandArgument : commandArguments)
        {
            this.addCommandArgument(commandArgument);
        }

        return this;
    }

    default GitParameters addCommandArguments(Iterable<String> commandArguments)
    {
        PreCondition.assertNotNull(commandArguments, "commandArguments");

        for (final String commandArgument : commandArguments)
        {
            this.addCommandArgument(commandArgument);
        }

        return this;
    }

    @Override
    default GitParameters setWorkingFolderPath(String workingFolderPath)
    {
        return (GitParameters)ChildProcessParameters.super.setWorkingFolderPath(workingFolderPath);
    }

    @Override
    GitParameters setWorkingFolderPath(Path workingFolderPath);

    @Override
    default GitParameters setWorkingFolder(Folder workingFolder)
    {
        return (GitParameters)ChildProcessParameters.super.setWorkingFolder(workingFolder);
    }

    @Override
    GitParameters setInputStream(ByteReadStream inputStream);

    @Override
    GitParameters setOutputStreamHandler(Action1<ByteReadStream> outputStreamHandler);

    @Override
    default GitParameters redirectOutputTo(ByteWriteStream outputStream)
    {
        return (GitParameters)ChildProcessParameters.super.redirectOutputTo(outputStream);
    }

    @Override
    GitParameters setErrorStreamHandler(Action1<ByteReadStream> errorStreamHandler);

    @Override
    default GitParameters redirectErrorTo(ByteWriteStream errorStream)
    {
        return (GitParameters)ChildProcessParameters.super.redirectErrorTo(errorStream);
    }
}
