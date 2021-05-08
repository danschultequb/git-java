package qub;

public abstract class GitCommandProcessBuilder<T extends GitCommandProcessBuilder> extends ProcessBuilderDecorator<T> implements ProcessBuilder, GitCommandArguments<T>
{
    private final GitProcessBuilder gitProcessBuilder;
    private final String command;
    private final List<String> commandArguments;

    protected GitCommandProcessBuilder(GitProcessBuilder gitProcessBuilder, String command)
    {
        super(gitProcessBuilder);

        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        this.gitProcessBuilder = gitProcessBuilder;
        this.command = command;
        this.commandArguments = List.create();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addGitArguments(String... gitArguments)
    {
        PreCondition.assertNotNull(gitArguments, "gitArguments");

        this.gitProcessBuilder.addGitArguments(gitArguments);

        return (T)this;
    }

    @Override
    public Iterable<String> getGitArguments()
    {
        return this.gitProcessBuilder.getGitArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addCommandArguments(String... commandArguments)
    {
        PreCondition.assertNotNull(commandArguments, "commandArguments");

        this.commandArguments.addAll(commandArguments);

        return (T)this;
    }

    @Override
    public Iterable<String> getCommandArguments()
    {
        return this.commandArguments;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return List.create(super.getArguments())
            .addAll(this.getGitArguments())
            .addAll(this.command)
            .addAll(this.getCommandArguments());
    }

    @Override
    public String getCommand()
    {
        return ProcessFactory.getCommand(this.getExecutablePath(), this.getArguments(), this.getWorkingFolderPath());
    }

    @Override
    public Result<Integer> run(Iterable<String> additionalArguments)
    {
        return super.run(List.create(this.command)
            .addAll(this.getCommandArguments())
            .addAll(additionalArguments));
    }

    @Override
    public Result<? extends ChildProcess> start(Iterable<String> additionalArguments)
    {
        return super.start(List.create(this.command)
            .addAll(this.getCommandArguments())
            .addAll(additionalArguments));
    }
}
