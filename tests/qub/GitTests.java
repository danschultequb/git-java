package qub;

public interface GitTests
{
    Skip skipRealTests = null; // Skip.create();

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

            runner.test("getCloneProcessBuilder()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final Git git = Git.create(process);
                final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await();
                test.assertNotNull(cloneProcessBuilder);
                test.assertEqual(Path.parse("git"), cloneProcessBuilder.getExecutablePath());
                test.assertEqual(Iterable.create("clone"), cloneProcessBuilder.getArguments());
                test.assertEqual(Iterable.create(), cloneProcessBuilder.getGitArguments());
                test.assertEqual(Iterable.create(), cloneProcessBuilder.getCommandArguments());
                test.assertEqual("/: git clone", cloneProcessBuilder.getCommand());
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
                    test.assertEqual(Iterable.create(), pullProcessBuilder.getGitArguments());
                    test.assertEqual(Iterable.create(), pullProcessBuilder.getCommandArguments());
                    test.assertEqual("/: git pull", pullProcessBuilder.getCommand());
                }
            });

            runner.test("getInitProcessBuilder()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final Git git = Git.create(process);
                final GitInitProcessBuilder initProcessBuilder = git.getInitProcessBuilder().await();
                test.assertNotNull(initProcessBuilder);
                test.assertEqual(Path.parse("git"), initProcessBuilder.getExecutablePath());
                test.assertEqual(Iterable.create("init"), initProcessBuilder.getArguments());
                test.assertEqual(Iterable.create(), initProcessBuilder.getGitArguments());
                test.assertEqual(Iterable.create(), initProcessBuilder.getCommandArguments());
                test.assertEqual("/: git init", initProcessBuilder.getCommand());
            });
        });
    }
}
