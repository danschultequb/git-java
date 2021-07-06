package qub;

public class GitPullParameters extends GitParametersDecorator<GitPullParameters>
{
    private GitPullParameters(Path executablePath)
    {
        super(executablePath);

        this.addCommandArgument("pull");
    }

    static GitPullParameters create()
    {
        return GitPullParameters.create("git");
    }

    static GitPullParameters create(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return GitPullParameters.create(Path.parse(executablePath));
    }

    static GitPullParameters create(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return new GitPullParameters(executablePath);
    }

    static GitPullParameters create(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return GitPullParameters.create(executableFile.getPath());
    }
}
