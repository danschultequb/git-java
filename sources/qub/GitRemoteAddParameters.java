package qub;

public class GitRemoteAddParameters extends GitParametersDecorator<GitRemoteAddParameters>
{
    private GitRemoteAddParameters(Path executablePath)
    {
        super(executablePath);

        this.addCommandArguments("remote", "add");
    }

    static GitRemoteAddParameters create()
    {
        return GitRemoteAddParameters.create("git");
    }

    static GitRemoteAddParameters create(String executablePath)
    {
        PreCondition.assertNotNullAndNotEmpty(executablePath, "executablePath");

        return GitRemoteAddParameters.create(Path.parse(executablePath));
    }

    static GitRemoteAddParameters create(Path executablePath)
    {
        PreCondition.assertNotNull(executablePath, "executablePath");

        return new GitRemoteAddParameters(executablePath);
    }

    static GitRemoteAddParameters create(File executableFile)
    {
        PreCondition.assertNotNull(executableFile, "executableFile");

        return GitRemoteAddParameters.create(executableFile.getPath());
    }

    public GitRemoteAddParameters addVerbose()
    {
        return this.insertCommandArgument(1, "--verbose");
    }

    public GitRemoteAddParameters addName(String remoteName)
    {
        PreCondition.assertNotNullAndNotEmpty(remoteName, "remoteName");

        return this.addCommandArgument(remoteName);
    }

    public GitRemoteAddParameters addUrl(String remoteUrl)
    {
        PreCondition.assertNotNullAndNotEmpty(remoteUrl, "remoteUrl");

        return this.addCommandArgument(remoteUrl);
    }

    public GitRemoteAddParameters addUrl(URL remoteUrl)
    {
        PreCondition.assertNotNull(remoteUrl, "remoteUrl");

        return this.addUrl(remoteUrl.toString(true));
    }
}
