package qub;

/**
 * A process builder that can be used to build up a "git" process.
 */
public class GitProcessBuilder extends ProcessBuilderDecorator<GitProcessBuilder> implements ProcessBuilder, GitArguments<GitProcessBuilder>
{
    private final List<String> gitArguments;

    private GitProcessBuilder(ProcessBuilder gitProcessBuilder)
    {
        super(gitProcessBuilder);

        this.gitArguments = List.create();
    }

    /**
     * Create a new GitProcessBuilder.
     * @param gitProcessBuilder The git ProcessBuilder that the GitProcessBuilder will wrap.
     * @return A new GitProcessBuilder.
     */
    public static GitProcessBuilder create(ProcessBuilder gitProcessBuilder)
    {
        PreCondition.assertNotNull(gitProcessBuilder, "gitProcessBuilder");

        return new GitProcessBuilder(gitProcessBuilder);
    }

    @Override
    public GitProcessBuilder addGitArguments(String... gitArguments)
    {
        PreCondition.assertNotNull(gitArguments, "gitArguments");

        this.gitArguments.addAll(gitArguments);

        return this;
    }

    @Override
    public Iterable<String> getGitArguments()
    {
        return this.gitArguments;
    }

    @Override
    public Iterable<String> getArguments()
    {
        return List.create(super.getArguments()).addAll(this.getGitArguments());
    }

    @Override
    public String getCommand()
    {
        return ProcessFactory.getCommand(this.getExecutablePath(), this.getArguments(), this.getWorkingFolderPath());
    }

    @Override
    public Result<Integer> run(Iterable<String> additionalArguments)
    {
        return super.run(List.create(this.getGitArguments()).addAll(additionalArguments));
    }

    @Override
    public Result<? extends ChildProcess> start(Iterable<String> additionalArguments)
    {
        return super.start(List.create(this.getGitArguments()).addAll(additionalArguments));
    }
}
