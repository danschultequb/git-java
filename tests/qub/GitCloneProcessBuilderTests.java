package qub;

public interface GitCloneProcessBuilderTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitCloneProcessBuilder.class, () ->
        {
            runner.testGroup("create(ProcessBuilder,String)", () ->
            {
                runner.test("with null ProcessBuilder", (Test test) ->
                {
                    test.assertThrows(() -> GitCloneProcessBuilder.create(null, "foo"),
                        new PreConditionFailure("gitProcessBuilder cannot be null."));
                });

                final Action2<String,Throwable> createErrorTest = (String repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository) + " repository", (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                            test.assertThrows(() -> GitCloneProcessBuilder.create(gitProcessBuilder, repository), expected);
                        }
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                createErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> createTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository) + " repository", (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                            final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, repository);
                            test.assertNotNull(cloneProcessBuilder);
                            test.assertEqual(repository, cloneProcessBuilder.getRepository());
                            test.assertNull(cloneProcessBuilder.getDirectory());
                        }
                    });
                };
                
                createTest.run("foo");
                createTest.run("https://github.com/danschultequb/git-java");
                createTest.run("https://github.com/danschultequb/git-java.git");
            });

            runner.testGroup("run()",
                (TestResources resources) -> Tuple.create(resources.getProcessFactory()),
                (ProcessFactory processFactory) ->
            {
                runner.test("with invalid repository", (Test test) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("foo").await();

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setWorkingFolder(temporaryFolder);

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
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder(relativePath.toString()).await()
                        .setWorkingFolder(temporaryFolder);

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setWorkingFolder(temporaryFolder)
                        .setDirectory("relative-path-repo");

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setWorkingFolder(temporaryFolder)
                        .setDirectory("relative-path-repo")
                        .setProgress(true);

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setWorkingFolder(temporaryFolder)
                        .setDirectory("relative/path/repo");

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Folder rootedPathRepoFolder = temporaryFolder.getFolder("rooted-path-repo").await();
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory(rootedPathRepoFolder.toString());

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
                    (TestResources resources) -> Tuple.create(resources.getTemporaryFolder(true)),
                    (Test test, Folder temporaryFolder) ->
                {
                    final Folder rootedFolder = temporaryFolder.getFolder("rooted").await();
                    final Folder rootedPathRepoFolder = rootedFolder.getFolder("path/repo").await();
                    final Git git = Git.create(processFactory);
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setWorkingFolder(temporaryFolder)
                        .setDirectory(rootedPathRepoFolder.toString());

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

            runner.testGroup("setProgress(Boolean)", () ->
            {
                final Action1<Boolean> setProgressTest = (Boolean progress) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(progress), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                            final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                            final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setProgress(progress);
                            test.assertSame(cloneProcessBuilder, setDirectoryResult);
                            test.assertEqual(progress, cloneProcessBuilder.getProgress());
                        }
                    });
                };

                setProgressTest.run(null);
                setProgressTest.run(false);
                setProgressTest.run(true);
            });

            runner.testGroup("setDirectory(String)", () ->
            {
                final Action1<String> setDirectoryTest = (String directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                            final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                            final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(directory);
                            test.assertSame(cloneProcessBuilder, setDirectoryResult);
                            test.assertEqual(directory, cloneProcessBuilder.getDirectory());
                        }
                    });
                };

                setDirectoryTest.run(null);
                setDirectoryTest.run("");
                setDirectoryTest.run("hello");
                setDirectoryTest.run("/hello");
                setDirectoryTest.run("/hello/");
            });

            runner.testGroup("setDirectory(Path)", () ->
            {
                final Action1<Path> setDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final Git git = Git.create(process);
                            final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                            final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                            final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(directory);
                            test.assertSame(cloneProcessBuilder, setDirectoryResult);
                            test.assertEqual(directory, cloneProcessBuilder.getDirectory());
                        }
                    });
                };

                setDirectoryTest.run(null);
                setDirectoryTest.run(Path.parse("hello"));
                setDirectoryTest.run(Path.parse("/hello"));
                setDirectoryTest.run(Path.parse("/hello/"));
            });

            runner.testGroup("setDirectory(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Git git = Git.create(process);
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                        final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory((Folder)null);
                        test.assertSame(cloneProcessBuilder, setDirectoryResult);
                        test.assertEqual(null, cloneProcessBuilder.getDirectory());
                    }
                });

                runner.test("with non-null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Folder currentFolder = process.getCurrentFolder();
                        final Git git = Git.create(process);
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                        final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(currentFolder);
                        test.assertSame(cloneProcessBuilder, setDirectoryResult);
                        test.assertEqual(currentFolder.toString(), cloneProcessBuilder.getDirectory());
                    }
                });
            });
        });
    }
}
