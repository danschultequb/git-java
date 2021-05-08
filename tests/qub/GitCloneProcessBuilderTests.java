package qub;

public interface GitCloneProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitCloneProcessBuilder.class, () ->
        {
            GitCloneArgumentsTests.test(runner, (DesktopProcess process) ->
            {
                final GitProcessBuilder gitProcessBuilder = Git.create(process).getGitProcessBuilder().await();
                return GitCloneProcessBuilder.create(gitProcessBuilder);
            });

            runner.testGroup("create(GitProcessBuilder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> GitCloneProcessBuilder.create(null),
                        new PreConditionFailure("gitProcessBuilder cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final GitProcessBuilder gitProcessBuilder = Git.create(process).getGitProcessBuilder().await();
                    final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder);
                    test.assertNotNull(cloneProcessBuilder);
                    test.assertEqual(gitProcessBuilder.getWorkingFolderPath(), cloneProcessBuilder.getWorkingFolderPath());
                    test.assertEqual(gitProcessBuilder.getExecutablePath(), cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(Iterable.create("clone"), cloneProcessBuilder.getArguments());
                    test.assertEqual(Iterable.create(), cloneProcessBuilder.getGitArguments());
                    test.assertEqual(Iterable.create(), cloneProcessBuilder.getCommandArguments());
                    test.assertEqual("/: git clone", cloneProcessBuilder.getCommand());
                });
            });

            runner.testGroup("run()",
                (TestResources resources) -> Tuple.create(resources.getProcessFactory()),
                (ProcessFactory processFactory) ->
            {
                runner.test("with invalid repository", (Test test) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .addRepository("foo");

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(128, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "fatal: repository 'foo' does not exist"),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"foo\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addRepository("https://github.com/danschultequb/git-java");

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into 'git-java'..."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with relative-path repository",
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true), resources.getCurrentFolder()),
                    (Test test, Folder temporaryFolder, Folder currentFolder) ->
                {
                    final Folder csvJavaFolder = currentFolder.getFolder("../csv-java").await();
                    final Path relativePath = csvJavaFolder.relativeTo(temporaryFolder);
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addRepository(relativePath);

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into 'csv-java'...",
                            "done."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            Strings.escapeAndQuote(relativePath)),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository and relative path directory when parent folder exists",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addRepository("https://github.com/danschultequb/git-java")
                        .addDirectory("relative-path-repo");

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into 'relative-path-repo'..."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\"",
                            "\"relative-path-repo\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository and relative path directory when parent folder exists with progress",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addProgress()
                        .addRepository("https://github.com/danschultequb/git-java")
                        .addDirectory("relative-path-repo");

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    final String stderrText = stderr.getText().await();
                    test.assertContains(stderrText, "Cloning into 'relative-path-repo'...");
                    test.assertContains(stderrText, "remote: Enumerating objects:");
                    test.assertContains(stderrText, "remote: Counting objects:");
                    test.assertContains(stderrText, "remote: Compressing objects:");
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "--progress",
                            "\"https://github.com/danschultequb/git-java\"",
                            "\"relative-path-repo\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository and relative path directory when parent folder doesn't exists",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addRepository("https://github.com/danschultequb/git-java")
                        .addDirectory("relative/path/repo");

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into 'relative/path/repo'..."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\"",
                            "\"relative/path/repo\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository and rooted path directory when parent folder exists",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Folder rootedPathRepoFolder = temporaryFolder.getFolder("rooted-path-repo").await();
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .addRepository("https://github.com/danschultequb/git-java")
                        .addDirectory(rootedPathRepoFolder);

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into '" + rootedPathRepoFolder.toString().substring(0, rootedPathRepoFolder.toString().length() - 1) + "'..."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\"",
                            Strings.escapeAndQuote(rootedPathRepoFolder)),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository and rooted path directory when parent folder doesn't exists",
                    GitTests.skipRealTests,
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Folder rootedFolder = temporaryFolder.getFolder("rooted").await();
                    final Folder rootedPathRepoFolder = rootedFolder.getFolder("path/repo").await();
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder().await()
                        .setWorkingFolder(temporaryFolder)
                        .addRepository("https://github.com/danschultequb/git-java")
                        .addDirectory(rootedPathRepoFolder);

                    final InMemoryCharacterToByteStream stdout = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryCharacterToByteStream stderr = InMemoryCharacterToByteStream.create();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(0, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "Cloning into '" + rootedPathRepoFolder.toString().substring(0, rootedPathRepoFolder.toString().length() - 1) + "'..."),
                        Strings.getLines(stderr.getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\"",
                            Strings.escapeAndQuote(rootedPathRepoFolder)),
                        cloneProcessBuilder.getArguments());
                });
            });
        });
    }
}
