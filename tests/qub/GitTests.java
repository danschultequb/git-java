package qub;

public interface GitTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Git.class, () ->
        {
            runner.testGroup("create(Process)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Git.create(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    test.assertNotNull(git);
                });
            });

            runner.testGroup("getCloneProcessBuilder(String)", () ->
            {
                final Action2<String,Throwable> getCloneProcessBuilderErrorTest = (String repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final Git git = Git.create(test.getProcess());
                        test.assertThrows(() -> git.getCloneProcessBuilder(repository).await(), expected);
                    });
                };

                getCloneProcessBuilderErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                getCloneProcessBuilderErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> getCloneProcessBuilderTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final Git git = Git.create(test.getProcess());
                        final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder(repository).await();
                        test.assertNotNull(cloneProcessBuilder);
                        test.assertEqual(repository, cloneProcessBuilder.getRepository());
                        test.assertNull(cloneProcessBuilder.getDirectory());
                    });
                };

                getCloneProcessBuilderTest.run("foo");
                getCloneProcessBuilderTest.run("https://github.com/danschultequb/git-java");
                getCloneProcessBuilderTest.run("https://github.com/danschultequb/git-java.git");
            });
        });
    }
}
