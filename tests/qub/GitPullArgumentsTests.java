package qub;

public interface GitPullArgumentsTests
{
    static <T extends GitPullArguments<T>> void test(TestRunner runner, Function1<DesktopProcess,T> creator)
    {
        runner.testGroup(GitPullArguments.class, () ->
        {
            GitCommandArgumentsTests.test(runner, creator);
        });
    }
}
