package qub;

public interface GitInitProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitInitProcessBuilder.class, () ->
        {
            GitInitArgumentsTests.test(runner, (DesktopProcess process) ->
            {
                final GitProcessBuilder gitProcessBuilder = Git.create(process).getGitProcessBuilder().await();
                return GitInitProcessBuilder.create(gitProcessBuilder);
            });

            runner.testGroup("create(GitProcessBuilder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> GitInitProcessBuilder.create(null),
                        new PreConditionFailure("gitProcessBuilder cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final GitProcessBuilder gitProcessBuilder = Git.create(process).getGitProcessBuilder().await();
                    final GitInitProcessBuilder initProcessBuilder = GitInitProcessBuilder.create(gitProcessBuilder);
                    test.assertNotNull(initProcessBuilder);
                    test.assertEqual(gitProcessBuilder.getWorkingFolderPath(), initProcessBuilder.getWorkingFolderPath());
                    test.assertEqual(gitProcessBuilder.getExecutablePath(), initProcessBuilder.getExecutablePath());
                    test.assertEqual(Iterable.create("init"), initProcessBuilder.getArguments());
                    test.assertEqual(Iterable.create(), initProcessBuilder.getGitArguments());
                    test.assertEqual(Iterable.create(), initProcessBuilder.getCommandArguments());
                    test.assertEqual("/: git init", initProcessBuilder.getCommand());
                });
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with no additional arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder gitFolder = tempFolder.getFolder(".git").await();

                    final Integer exitCode = gitInitProcessBuilder.run().await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });

                runner.test("with directory that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder repoFolder = tempFolder.getFolder("repo").await();
                    gitInitProcessBuilder.addDirectory(repoFolder);

                    final Folder gitFolder = repoFolder.getFolder(".git").await();

                    final Integer exitCode = gitInitProcessBuilder.run().await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });

                runner.test("with directory that is already a Git repository",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder repoFolder = tempFolder.getFolder("repo").await();
                    gitInitProcessBuilder.addDirectory(repoFolder);

                    final Folder gitFolder = repoFolder.getFolder(".git").await();

                    // Create the Git repository.
                    gitInitProcessBuilder.run().await();

                    final Integer exitCode = gitInitProcessBuilder.run().await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder,
                            "Reinitialized existing Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });
            });

            runner.testGroup("start()", () ->
            {
                runner.test("with no additional arguments",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder gitFolder = tempFolder.getFolder(".git").await();

                    final ChildProcess initProcess = gitInitProcessBuilder.start().await();
                    final Integer exitCode = initProcess.await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });

                runner.test("with directory that doesn't exist",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder repoFolder = tempFolder.getFolder("repo").await();
                    gitInitProcessBuilder.addDirectory(repoFolder);

                    final Folder gitFolder = repoFolder.getFolder(".git").await();

                    final ChildProcess initProcess = gitInitProcessBuilder.start().await();
                    final Integer exitCode = initProcess.await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });

                runner.test("with directory that is already a Git repository",
                    (TestResources resources) -> Tuple.create(resources.getProcessFactory(), resources.getTemporaryFolder(true)),
                    (Test test, ProcessFactory processFactory, TemporaryFolder tempFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitInitProcessBuilder gitInitProcessBuilder = git.getInitProcessBuilder().await();

                    final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectOutput(output);

                    final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                    gitInitProcessBuilder.redirectError(error);

                    gitInitProcessBuilder.setWorkingFolder(tempFolder);

                    final Folder repoFolder = tempFolder.getFolder("repo").await();
                    gitInitProcessBuilder.addDirectory(repoFolder);

                    final Folder gitFolder = repoFolder.getFolder(".git").await();

                    // Create the Git repository.
                    gitInitProcessBuilder.run().await();

                    final ChildProcess initProcess = gitInitProcessBuilder.start().await();
                    final Integer exitCode = initProcess.await();
                    test.assertLinesEqual(
                        Iterable.create(
                            "Initialized empty Git repository in " + gitFolder,
                            "Reinitialized existing Git repository in " + gitFolder),
                        output);
                    test.assertLinesEqual(
                        Iterable.create(),
                        error);
                    test.assertEqual(0, exitCode);

                    test.assertTrue(gitFolder.exists().await());
                });
            });
        });
    }
}
