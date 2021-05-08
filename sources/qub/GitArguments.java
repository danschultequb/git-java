package qub;

public interface GitArguments<T>
{
    /**
     * Add the provided Git arguments that will appear before the Git command.
     * @param gitArguments The Git arguments that will appear before the Git command.
     * @return This object for method chaining.
     */
    T addGitArguments(String... gitArguments);

    /**
     * Get the Git arguments that will appear before the Git command.
     * @return The Git arguments that will appear before the Git command.
     */
    Iterable<String> getGitArguments();

    /**
     * Prints the Git suite version that the git program came from.
     * @return This object for method chaining.
     */
    default T addVersion()
    {
        return this.addGitArguments("--version");
    }

    /**
     * Prints the synopsis and a list of the most commonly used commands. If the option --all or -a
     * is given then all available commands are printed. If a Git command is named this option will
     * bring up the manual page for that command.<br/>
     * <br/>
     * Other options are available to control how the manual page is displayed. See git-help(1) for
     * more information, because git --help ... is converted internally into git help ....
     * @return This object for method chaining.
     */
    default T addHelp()
    {
        return this.addHelp(false);
    }

    /**
     * Prints the synopsis and a list of the most commonly used commands. If the option --all or -a
     * is given then all available commands are printed. If a Git command is named this option will
     * bring up the manual page for that command.<br/>
     * <br/>
     * Other options are available to control how the manual page is displayed. See git-help(1) for
     * more information, because git --help ... is converted internally into git help ....
     * @param all Whether or not to also add --all to the Git arguments.
     * @return This object for method chaining.
     */
    default T addHelp(boolean all)
    {
        final T result = this.addGitArguments("--help");
        if (all)
        {
            this.addGitArguments("--all");
        }
        return result;
    }

    /**
     * Pass a configuration parameter to the command. The value given will override values from
     * configuration files. The <name> is expected in the same format as listed by git config
     * (subkeys separated by dots).<br/>
     * <br/>
     * Note that omitting the = in git -c foo.bar ... is allowed and sets foo.bar to the boolean
     * true value (just like [foo]bar would in a config file). Including the equals but with an
     * empty value (like git -c foo.bar= ...) sets foo.bar to the empty string which git config
     * --type=bool will convert to false.
     * @param name The name of the configuration value to use in this Git process.
     * @return This object for method chaining.
     */
    default T addConfigurationValue(String name)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");

        return this.addGitArguments("-c", name);
    }

    /**
     * Pass a configuration parameter to the command. The value given will override values from
     * configuration files. The <name> is expected in the same format as listed by git config
     * (subkeys separated by dots).<br/>
     * <br/>
     * Note that omitting the = in git -c foo.bar ... is allowed and sets foo.bar to the boolean
     * true value (just like [foo]bar would in a config file). Including the equals but with an
     * empty value (like git -c foo.bar= ...) sets foo.bar to the empty string which git config
     * --type=bool will convert to false.
     * @param name The name of the configuration value to use in this Git process.
     * @param value The configuration value to use in this Git process.
     * @return This object for method chaining.
     */
    default T addConfigurationValue(String name, String value)
    {
        PreCondition.assertNotNullAndNotEmpty(name, "name");
        PreCondition.assertNotNull(value, "value");

        return this.addGitArguments("-c", name + "=" + value);
    }

    /**
     * If pager is true, then pipe all output into less (or if set, $PAGER) if standard output is a
     * terminal. This overrides the pager.<cmd> configuration options (see the "Configuration
     * Mechanism" section below). If pager is false, then do not pipe Git output into a pager.
     * @param pager Whether or not to use a pager.
     * @return This object for method chaining.
     */
    default T addPager(boolean pager)
    {
        return pager
            ? this.addGitArguments("--paginate")
            : this.addGitArguments("--no-pager");
    }

    /**
     * Do not perform optional operations that require locks. This is equivalent to setting the
     * GIT_OPTIONAL_LOCKS to 0.
     * @return This object for method chaining.
     */
    default T addNoOptionalLocks()
    {
        return this.addGitArguments("--no-optional-locks");
    }
}
