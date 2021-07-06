package qub;

public abstract class GitParametersDecorator<T extends GitParameters> implements GitParameters
{
    private final GitParameters innerParameters;
    
    protected GitParametersDecorator(Path executablePath)
    {
        this.innerParameters = GitParameters.create(executablePath);
    }
    
    @Override
    public Path getExecutablePath()
    {
        return this.innerParameters.getExecutablePath();
    }

    @Override
    public Iterable<String> getArguments()
    {
        return this.innerParameters.getArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T insertArgument(int index, String argument)
    {
        this.innerParameters.insertArgument(index, argument);
        
        return (T)this;
    }

    @Override
    public Path getWorkingFolderPath()
    {
        return this.innerParameters.getWorkingFolderPath();
    }

    @Override
    public Iterable<String> getGitArguments()
    {
        return this.innerParameters.getGitArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T insertGitArgument(int gitArgumentIndex, String gitArgument)
    {
        this.innerParameters.insertGitArgument(gitArgumentIndex, gitArgument);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addGitArgument(String gitArgument)
    {
        this.innerParameters.addGitArgument(gitArgument);
        
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addGitArguments(String... gitArguments)
    {
        return (T)GitParameters.super.addGitArguments(gitArguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addGitArguments(Iterable<String> gitArguments)
    {
        return (T)GitParameters.super.addGitArguments(gitArguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addCommandArguments(String... commandArguments)
    {
        return (T)GitParameters.super.addCommandArguments(commandArguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addCommandArguments(Iterable<String> commandArguments)
    {
        return (T)GitParameters.super.addCommandArguments(commandArguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArgument(String argument)
    {
        return (T)GitParameters.super.addArgument(argument);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(String... arguments)
    {
        return (T)GitParameters.super.addArguments(arguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addArguments(Iterable<String> arguments)
    {
        return (T)GitParameters.super.addArguments(arguments);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolderPath(String workingFolderPath)
    {
        return (T)GitParameters.super.setWorkingFolderPath(workingFolderPath);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolder(Folder workingFolder)
    {
        return (T)GitParameters.super.setWorkingFolder(workingFolder);
    }

    @Override
    public Iterable<String> getCommandArguments()
    {
        return this.innerParameters.getCommandArguments();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T insertCommandArgument(int index, String commandArgument)
    {
        this.innerParameters.insertCommandArgument(index, commandArgument);

        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addCommandArgument(String commandArgument)
    {
        this.innerParameters.addCommandArgument(commandArgument);
        
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setWorkingFolderPath(Path workingFolderPath)
    {
        this.innerParameters.setWorkingFolderPath(workingFolderPath);
        
        return (T)this;
    }

    @Override
    public ByteReadStream getInputStream()
    {
        return this.innerParameters.getInputStream();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setInputStream(ByteReadStream inputStream)
    {
        this.innerParameters.setInputStream(inputStream);
        
        return (T)this;
    }

    @Override
    public Action1<ByteReadStream> getOutputStreamHandler()
    {
        return this.innerParameters.getOutputStreamHandler();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setOutputStreamHandler(Action1<ByteReadStream> outputStreamHandler)
    {
        this.innerParameters.setOutputStreamHandler(outputStreamHandler);
        
        return (T)this;
    }
    

    @Override
    @SuppressWarnings("unchecked")
    public T redirectOutputTo(ByteWriteStream outputStream)
    {
        return (T)GitParameters.super.redirectOutputTo(outputStream);
    }

    @Override
    public Action1<ByteReadStream> getErrorStreamHandler()
    {
        return this.innerParameters.getErrorStreamHandler();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setErrorStreamHandler(Action1<ByteReadStream> errorStreamHandler)
    {
        this.innerParameters.setErrorStreamHandler(errorStreamHandler);
        
        return (T)this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T redirectErrorTo(ByteWriteStream errorStream)
    {
        return (T)GitParameters.super.redirectErrorTo(errorStream);
    }
}
