package qub;

public interface GitPullProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitPullProcessBuilder.class,
            (TestResources resources) -> Tuple.create(resources.getProcessFactory()),
            (ProcessFactory processFactory) ->
        {
            runner.testGroup("create(ProcessBuilder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> GitPullProcessBuilder.create(null),
                        new PreConditionFailure("gitProcessBuilder cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Git git = Git.create(processFactory);
                    final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                    final GitPullProcessBuilder pullProcessBuilder = GitPullProcessBuilder.create(gitProcessBuilder);
                    test.assertNotNull(pullProcessBuilder);
                    test.assertEqual(
                        Path.parse("git"),
                        pullProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "pull"),
                        pullProcessBuilder.getArguments());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("in folder that isn't a git repository",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                    final GitPullProcessBuilder pullProcessBuilder = GitPullProcessBuilder.create(gitProcessBuilder)
                        .setWorkingFolder(temporaryFolder);

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    pullProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    pullProcessBuilder.redirectError(stderr);

                    test.assertEqual(128, pullProcessBuilder.run().await());
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "fatal: not a git repository (or any of the parent directories): .git"
                        ),
                        Strings.getLines(stderr.getText().await()));
                });

                runner.test("in a repository that was just cloned",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);

                    git.getCloneProcessBuilder("https://github.com/danschultequb/csv-java").await()
                        .setDirectory(temporaryFolder)
                        .run()
                        .await();

                    final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                    final GitPullProcessBuilder pullProcessBuilder = GitPullProcessBuilder.create(gitProcessBuilder)
                        .setWorkingFolder(temporaryFolder);

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    pullProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    pullProcessBuilder.redirectError(stderr);

                    test.assertEqual(0, pullProcessBuilder.run().await());
                    test.assertEqual(
                        Iterable.create(
                            "Already up to date."),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stderr.getText().await()));
                });
            });
        });
    }
}
