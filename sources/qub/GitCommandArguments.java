package qub;

public interface GitCommandArguments<T> extends GitArguments<T>
{
    /**
     * Add the provided command arguments that will appear after the Git command.
     * @param commandArguments The command arguments that will appear after the Git command.
     * @return This object for method chaining.
     */
    T addCommandArguments(String... commandArguments);

    /**
     * Get the command arguments that will appear after the Git command.
     * @return The command arguments that will appear after the Git command.
     */
    Iterable<String> getCommandArguments();
}
