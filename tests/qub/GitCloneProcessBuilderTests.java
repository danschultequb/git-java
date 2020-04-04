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
                        final Git git = Git.create(test.getProcess());
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        test.assertThrows(() -> GitCloneProcessBuilder.create(gitProcessBuilder, repository), expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                createErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> createTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository) + " repository", (Test test) ->
                    {
                        final Git git = Git.create(test.getProcess());
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, repository);
                        test.assertNotNull(cloneProcessBuilder);
                        test.assertEqual(repository, cloneProcessBuilder.getRepository());
                        test.assertNull(cloneProcessBuilder.getDirectory());
                    });
                };
                
                createTest.run("foo");
                createTest.run("https://github.com/danschultequb/git-java");
                createTest.run("https://github.com/danschultequb/git-java.git");
            });

            runner.testGroup("run()", () ->
            {
                runner.test("with invalid repository", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("foo").await();

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    final Integer result = cloneProcessBuilder.run().await();
                    test.assertEqual(128, result);
                    test.assertEqual(
                        Iterable.create(),
                        Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                    test.assertEqual(
                        Iterable.create(
                            "fatal: repository 'foo' does not exist"),
                        Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"foo\""),
                        cloneProcessBuilder.getArguments());
                });

                runner.test("with URL repository", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await();

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into 'git-java'..."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                    test.assertEqual(
                        Path.parse("git"),
                        cloneProcessBuilder.getExecutablePath());
                    test.assertEqual(
                        Iterable.create(
                            "clone",
                            "\"https://github.com/danschultequb/git-java\""),
                        cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        test.getProcess()
                            .getCurrentFolder().await()
                            .getFolder("git-java").await()
                            .delete().catchError(FolderNotFoundException.class).await();
                    }
                });

                runner.test("with relative-path repository", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("../csv-java").await();

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into 'csv-java'...",
                                "done."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Path.parse("git"),
                            cloneProcessBuilder.getExecutablePath());
                        test.assertEqual(
                            Iterable.create(
                                "clone",
                                "\"../csv-java\""),
                            cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        test.getProcess()
                            .getCurrentFolder().await()
                            .getFolder("csv-java").await()
                            .delete().catchError(FolderNotFoundException.class).await();
                    }
                });

                runner.test("with URL repository and relative path directory when parent folder exists", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory("relative-path-repo");

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into 'relative-path-repo'..."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Path.parse("git"),
                            cloneProcessBuilder.getExecutablePath());
                        test.assertEqual(
                            Iterable.create(
                                "clone",
                                "\"https://github.com/danschultequb/git-java\"",
                                "\"relative-path-repo\""),
                            cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        test.getProcess()
                            .getCurrentFolder().await()
                            .getFolder("relative-path-repo").await()
                            .delete().catchError(FolderNotFoundException.class).await();
                    }
                });

                runner.test("with URL repository and relative path directory when parent folder exists with progress", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory("relative-path-repo")
                        .setProgress(true);

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        final String stderrText = stderr.asCharacterReadStream().getText().await();
                        test.assertContains(stderrText, "Cloning into 'relative-path-repo'...");
                        test.assertContains(stderrText, "remote: Enumerating objects:");
                        test.assertContains(stderrText, "remote: Counting objects:");
                        test.assertContains(stderrText, "remote: Compressing objects:");
                        test.assertContains(stderrText, "Receiving objects:");
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
                    }
                    finally
                    {
                        test.getProcess()
                            .getCurrentFolder().await()
                            .getFolder("relative-path-repo").await()
                            .delete().catchError(FolderNotFoundException.class).await();
                    }
                });

                runner.test("with URL repository and relative path directory when parent folder doesn't exists", (Test test) ->
                {
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory("relative/path/repo");

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into 'relative/path/repo'..."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Path.parse("git"),
                            cloneProcessBuilder.getExecutablePath());
                        test.assertEqual(
                            Iterable.create(
                                "clone",
                                "\"https://github.com/danschultequb/git-java\"",
                                "\"relative/path/repo\""),
                            cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        test.getProcess()
                            .getCurrentFolder().await()
                            .getFolder("relative").await()
                            .delete().catchError(FolderNotFoundException.class).await();
                    }
                });

                runner.test("with URL repository and rooted path directory when parent folder exists", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
                    final Folder rootedPathRepoFolder = currentFolder.getFolder("rooted-path-repo").await();
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory(rootedPathRepoFolder.toString());

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into '" + rootedPathRepoFolder + "'..."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Path.parse("git"),
                            cloneProcessBuilder.getExecutablePath());
                        test.assertEqual(
                            Iterable.create(
                                "clone",
                                "\"https://github.com/danschultequb/git-java\"",
                                Strings.escapeAndQuote(rootedPathRepoFolder)),
                            cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        rootedPathRepoFolder.delete()
                            .catchError(FolderNotFoundException.class)
                            .await();
                    }
                });

                runner.test("with URL repository and rooted path directory when parent folder doesn't exists", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
                    final Folder rootedFolder = currentFolder.getFolder("rooted").await();
                    final Folder rootedPathRepoFolder = rootedFolder.getFolder("path/repo").await();
                    final Git git = Git.create(test.getProcess());
                    final GitCloneProcessBuilder cloneProcessBuilder = git.getCloneProcessBuilder("https://github.com/danschultequb/git-java").await()
                        .setDirectory(rootedPathRepoFolder.toString());

                    final InMemoryByteStream stdout = new InMemoryByteStream();
                    cloneProcessBuilder.redirectOutput(stdout);

                    final InMemoryByteStream stderr = new InMemoryByteStream();
                    cloneProcessBuilder.redirectError(stderr);

                    try
                    {
                        final Integer result = cloneProcessBuilder.run().await();
                        test.assertEqual(0, result);
                        test.assertEqual(
                            Iterable.create(),
                            Strings.getLines(stdout.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Iterable.create(
                                "Cloning into '" + rootedPathRepoFolder + "'..."),
                            Strings.getLines(stderr.asCharacterReadStream().getText().await()));
                        test.assertEqual(
                            Path.parse("git"),
                            cloneProcessBuilder.getExecutablePath());
                        test.assertEqual(
                            Iterable.create(
                                "clone",
                                "\"https://github.com/danschultequb/git-java\"",
                                Strings.escapeAndQuote(rootedPathRepoFolder)),
                            cloneProcessBuilder.getArguments());
                    }
                    finally
                    {
                        rootedFolder.delete()
                            .catchError(FolderNotFoundException.class)
                            .await();
                    }
                });
            });

            runner.testGroup("setProgress(Boolean)", () ->
            {
                final Action1<Boolean> setProgressTest = (Boolean progress) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(progress), (Test test) ->
                    {
                        final Git git = Git.create(test.getProcess());
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                        final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setProgress(progress);
                        test.assertSame(cloneProcessBuilder, setDirectoryResult);
                        test.assertEqual(progress, cloneProcessBuilder.getProgress());
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
                        final Git git = Git.create(test.getProcess());
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                        final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(directory);
                        test.assertSame(cloneProcessBuilder, setDirectoryResult);
                        test.assertEqual(directory, cloneProcessBuilder.getDirectory());
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
                        final Git git = Git.create(test.getProcess());
                        final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                        final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                        final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(directory);
                        test.assertSame(cloneProcessBuilder, setDirectoryResult);
                        test.assertEqual(directory, cloneProcessBuilder.getDirectory());
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
                    final Git git = Git.create(test.getProcess());
                    final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                    final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                    final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory((Folder)null);
                    test.assertSame(cloneProcessBuilder, setDirectoryResult);
                    test.assertEqual(null, cloneProcessBuilder.getDirectory());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Folder currentFolder = test.getProcess().getCurrentFolder().await();
                    final Git git = Git.create(test.getProcess());
                    final ProcessBuilder gitProcessBuilder = git.getGitProcessBuilder().await();
                    final GitCloneProcessBuilder cloneProcessBuilder = GitCloneProcessBuilder.create(gitProcessBuilder, "https://github.com/danschultequb/git-java");
                    final GitCloneProcessBuilder setDirectoryResult = cloneProcessBuilder.setDirectory(currentFolder);
                    test.assertSame(cloneProcessBuilder, setDirectoryResult);
                    test.assertEqual(currentFolder.toString(), cloneProcessBuilder.getDirectory());
                });
            });
        });
    }
}
