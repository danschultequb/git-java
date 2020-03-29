package qub;

/**
 * A process builder that can be used to build up a "git clone" process.
 */
public class GitCloneProcessBuilder extends ProcessBuilderDecorator<GitCloneProcessBuilder>
{
    private final String repository;

    private String directory;

    protected GitCloneProcessBuilder(ProcessBuilder processBuilder, String repository)
    {
        super(processBuilder);

        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        processBuilder.addArgument("clone");

        this.repository = repository;
    }

    public static GitCloneProcessBuilder create(ProcessBuilder processBuilder, String repository)
    {
        PreCondition.assertNotNull(processBuilder, "processBuilder");
        PreCondition.assertNotNullAndNotEmpty(repository, "repository");

        return new GitCloneProcessBuilder(processBuilder, repository);
    }

    @Override
    public Result<Integer> run()
    {
        this.addArgument(Strings.escapeAndQuote(this.repository));

        if (!Strings.isNullOrEmpty(this.directory))
        {
            this.addArgument(Strings.escapeAndQuote(this.directory));
        }

        return super.run();
    }

    public String getRepository()
    {
        return this.repository;
    }

    public String getDirectory()
    {
        return this.directory;
    }

    public GitCloneProcessBuilder setDirectory(String directory)
    {
        this.directory = directory;

        return this;
    }
}
