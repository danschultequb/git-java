package qub;

public class BasicGitParameters extends ChildProcessParametersDecorator<BasicGitParameters> implements GitParameters
{
    private int gitArgumentCount;
    private int commandArgumentCount;

    private BasicGitParameters(Path executablePath)
    {
        super(executablePath);
    }

    public static BasicGitParameters create(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return new BasicGitParameters(executablePath);
    }

    @Override
    public Iterable<String> getGitArguments()
    {
        return this.getArguments().take(this.gitArgumentCount);
    }

    @Override
    public BasicGitParameters insertGitArgument(int gitArgumentIndex, String gitArgument)
    {
        PreCondition.assertBetween(0, gitArgumentIndex, this.getGitArguments().getCount(), "gitArgumentIndex");
        PreCondition.assertNotNullAndNotEmpty(gitArgument, "gitArgument");

        this.gitArgumentCount++;
        return super.insertArgument(gitArgumentIndex, gitArgument);
    }

    @Override
    public BasicGitParameters addGitArgument(String gitArgument)
    {
        return this.insertGitArgument(this.getGitArguments().getCount(), gitArgument);
    }

    @Override
    public BasicGitParameters addGitArguments(String... gitArguments)
    {
        return (BasicGitParameters)GitParameters.super.addGitArguments(gitArguments);
    }

    @Override
    public BasicGitParameters addGitArguments(Iterable<String> gitArguments)
    {
        return (BasicGitParameters)GitParameters.super.addGitArguments(gitArguments);
    }

    @Override
    public Iterable<String> getCommandArguments()
    {
        return this.getArguments().skip(this.gitArgumentCount).take(this.commandArgumentCount);
    }

    @Override
    public BasicGitParameters insertCommandArgument(int commandArgumentIndex, String commandArgument)
    {
        PreCondition.assertBetween(0, commandArgumentIndex, this.getCommandArguments().getCount(), "commandArgumentIndex");
        PreCondition.assertNotNullAndNotEmpty(commandArgument, "commandArgument");

        this.commandArgumentCount++;
        return super.insertArgument(this.gitArgumentCount + commandArgumentIndex, commandArgument);
    }

    @Override
    public BasicGitParameters addCommandArgument(String commandArgument)
    {
        return this.insertCommandArgument(this.getCommandArguments().getCount(), commandArgument);
    }

    @Override
    public BasicGitParameters addCommandArguments(String... commandArguments)
    {
        return (BasicGitParameters)GitParameters.super.addCommandArguments(commandArguments);
    }

    @Override
    public BasicGitParameters addCommandArguments(Iterable<String> commandArguments)
    {
        return (BasicGitParameters)GitParameters.super.addCommandArguments(commandArguments);
    }

    @Override
    public BasicGitParameters redirectOutputTo(ByteWriteStream outputStream)
    {
        return (BasicGitParameters)GitParameters.super.redirectOutputTo(outputStream);
    }

    @Override
    public BasicGitParameters redirectErrorTo(ByteWriteStream errorStream)
    {
        return (BasicGitParameters)GitParameters.super.redirectErrorTo(errorStream);
    }
}
