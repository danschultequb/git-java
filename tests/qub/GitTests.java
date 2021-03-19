package qub;

public interface GitTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Git.class, () ->
        {
            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Git.create((DesktopProcess)null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Git git = Git.create(process);
                        test.assertNotNull(git);
                    }
                });
            });

            runner.testGroup("create(ProcessFactory)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Git.create((ProcessFactory)null),
                        new PreConditionFailure("processFactory cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Git git = Git.create(process.getProcessFactory());
                        test.assertNotNull(git);
                    }
                });
            });

            runner.testGroup("getCloneProcessBuilder(String)", () ->
            {
                final Action2<String,Throwable> getCloneProcessBuilderErrorTest = (String repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.getCloneProcessBuilder(repository).await(), expected);
                        }
                    });
                };

                getCloneProcessBuilderErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                getCloneProcessBuilderErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> getCloneProcessBuilderTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder(repository).await();
                            test.assertNotNull(cloneProcessBuilder);
                            test.assertEqual(repository, cloneProcessBuilder.getRepository());
                            test.assertNull(cloneProcessBuilder.getDirectory());
                            test.assertEqual(Path.parse("git"), cloneProcessBuilder.getExecutablePath());
                            test.assertEqual(Iterable.create("clone"), cloneProcessBuilder.getArguments());
                        }
                    });
                };

                getCloneProcessBuilderTest.run("foo");
                getCloneProcessBuilderTest.run("https://github.com/danschultequb/git-java");
                getCloneProcessBuilderTest.run("https://github.com/danschultequb/git-java.git");
            });

            runner.test("getPullProcessBuilder()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final Git git = Git.create(process);
                    final GitPullProcessBuilder pullProcessBuilder = git.getPullProcessBuilder().await();
                    test.assertNotNull(pullProcessBuilder);
                    test.assertEqual(Path.parse("git"), pullProcessBuilder.getExecutablePath());
                    test.assertEqual(Iterable.create("pull"), pullProcessBuilder.getArguments());
                }
            });
        });
    }
}
