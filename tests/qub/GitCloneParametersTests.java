package qub;

public interface GitCloneParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitCloneParameters.class, () ->
        {
            GitParametersDecoratorTests.test(runner, GitCloneParameters::create);
            
            runner.test("create()", (Test test) ->
            {
                final GitCloneParameters parameters = GitCloneParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("git"), parameters.getExecutablePath());
                test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                test.assertNull(parameters.getWorkingFolderPath());
                test.assertNull(parameters.getInputStream());
                test.assertNull(parameters.getOutputStreamHandler());
                test.assertNull(parameters.getErrorStreamHandler());
            });

            runner.testGroup("create(String)", () ->
            {
                final Action2<String,Throwable> createErrorTest = (String executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        test.assertThrows(() -> GitCloneParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                createErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                final Action1<String> createTest = (String executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(Path.parse(executablePath), parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                        test.assertNull(parameters.getWorkingFolderPath());
                        test.assertNull(parameters.getInputStream());
                        test.assertNull(parameters.getOutputStreamHandler());
                        test.assertNull(parameters.getErrorStreamHandler());
                    });
                };

                createTest.run("git");
                createTest.run("git.exe");
                createTest.run("relative/git");
                createTest.run("/rooted/git.exe");
            });

            runner.testGroup("create(Path)", () ->
            {
                final Action2<Path,Throwable> createErrorTest = (Path executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        test.assertThrows(() -> GitCloneParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));

                final Action1<Path> createTest = (Path executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(executablePath, parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                        test.assertNull(parameters.getWorkingFolderPath());
                        test.assertNull(parameters.getInputStream());
                        test.assertNull(parameters.getOutputStreamHandler());
                        test.assertNull(parameters.getErrorStreamHandler());
                    });
                };

                createTest.run(Path.parse("git"));
                createTest.run(Path.parse("git.exe"));
                createTest.run(Path.parse("relative/git"));
                createTest.run(Path.parse("/rooted/git.exe"));
            });

            runner.testGroup("create(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> GitCloneParameters.create((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/executable").await();
                    final GitCloneParameters parameters = GitCloneParameters.create(file);
                    test.assertNotNull(parameters);
                    test.assertEqual(file.getPath(), parameters.getExecutablePath());
                    test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });
            });

            runner.test("addQuiet()", (Test test) ->
            {
                final GitCloneParameters parameters = GitCloneParameters.create();
                final GitCloneParameters addQuietResult = parameters.addQuiet();
                test.assertSame(parameters, addQuietResult);
                test.assertEqual(Iterable.create("clone", "--quiet"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("clone", "--quiet"), parameters.getCommandArguments());
            });

            runner.test("addVerbose()", (Test test) ->
            {
                final GitCloneParameters parameters = GitCloneParameters.create();
                final GitCloneParameters addVerboseResult = parameters.addVerbose();
                test.assertSame(parameters, addVerboseResult);
                test.assertEqual(Iterable.create("clone", "--verbose"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("clone", "--verbose"), parameters.getCommandArguments());
            });

            runner.test("addProgress()", (Test test) ->
            {
                final GitCloneParameters parameters = GitCloneParameters.create();
                final GitCloneParameters addProgressResult = parameters.addProgress();
                test.assertSame(parameters, addProgressResult);
                test.assertEqual(Iterable.create("clone", "--progress"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("clone", "--progress"), parameters.getCommandArguments());
            });

            runner.testGroup("addRepository(String)", () ->
            {
                final Action2<String,Throwable> addRepositoryErrorTest = (String repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        test.assertThrows(() -> parameters.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                addRepositoryErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> addRepositoryTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        final GitCloneParameters addRepositoryResult = parameters.addRepository(repository);
                        test.assertSame(parameters, addRepositoryResult);
                        test.assertEqual(Iterable.create("clone", repository), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone", repository), parameters.getCommandArguments());
                    });
                };

                addRepositoryTest.run("repo");
                addRepositoryTest.run("https://github.com/owner/repo");
            });

            runner.testGroup("addRepository(URL)", () ->
            {
                final Action2<URL,Throwable> addRepositoryErrorTest = (URL repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        test.assertThrows(() -> parameters.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));

                final Action1<URL> addRepositoryTest = (URL repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        final GitCloneParameters addRepositoryResult = parameters.addRepository(repository);
                        test.assertSame(parameters, addRepositoryResult);
                        test.assertEqual(Iterable.create("clone", repository.toString(true)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone", repository.toString(true)), parameters.getCommandArguments());
                    });
                };

                addRepositoryTest.run(URL.parse("https://github.com/owner/repo").await());
                addRepositoryTest.run(URL.parse("https://github.com/ow ner/re po").await());
            });

            runner.testGroup("addRepository(Path)", () ->
            {
                final Action2<Path,Throwable> addRepositoryErrorTest = (Path repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        test.assertThrows(() -> parameters.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));

                final Action1<Path> addRepositoryTest = (Path repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        final GitCloneParameters addRepositoryResult = parameters.addRepository(repository);
                        test.assertSame(parameters, addRepositoryResult);
                        test.assertEqual(Iterable.create("clone", repository.toString()), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone", repository.toString()), parameters.getCommandArguments());
                    });
                };

                addRepositoryTest.run(Path.parse("relative/repo"));
                addRepositoryTest.run(Path.parse("/rooted/repo/"));
            });

            runner.testGroup("addRepository(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitCloneParameters parameters = GitCloneParameters.create();
                    test.assertThrows(() -> parameters.addRepository((Folder)null),
                        new PreConditionFailure("repository cannot be null."));
                    test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitCloneParameters parameters = GitCloneParameters.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/repo/folder/").await();
                    final GitCloneParameters addRepositoryResult = parameters.addRepository(folder);
                    test.assertSame(parameters, addRepositoryResult);
                    test.assertEqual(Iterable.create("clone", folder.toString()), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("clone", folder.toString()), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addDirectory(String)", () ->
            {
                final Action2<String,Throwable> addDirectoryErrorTest = (String directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        test.assertThrows(() -> parameters.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));
                addDirectoryErrorTest.run("", new PreConditionFailure("directory cannot be empty."));

                final Action1<String> addDirectoryTest = (String directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        final GitCloneParameters addDirectoryResult = parameters.addDirectory(directory);
                        test.assertSame(parameters, addDirectoryResult);
                        test.assertEqual(Iterable.create("clone", directory), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone", directory), parameters.getCommandArguments());
                    });
                };

                addDirectoryTest.run("repo");
                addDirectoryTest.run("https://github.com/owner/repo");
            });

            runner.testGroup("addDirectory(Path)", () ->
            {
                final Action2<Path,Throwable> addDirectoryErrorTest = (Path directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        test.assertThrows(() -> parameters.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitCloneParameters parameters = GitCloneParameters.create();
                        final GitCloneParameters addDirectoryResult = parameters.addDirectory(directory);
                        test.assertSame(parameters, addDirectoryResult);
                        test.assertEqual(Iterable.create("clone", directory.toString()), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("clone", directory.toString()), parameters.getCommandArguments());
                    });
                };

                addDirectoryTest.run(Path.parse("relative/repo"));
                addDirectoryTest.run(Path.parse("/rooted/repo/"));
            });

            runner.testGroup("addDirectory(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitCloneParameters parameters = GitCloneParameters.create();
                    test.assertThrows(() -> parameters.addDirectory((Folder)null),
                        new PreConditionFailure("directory cannot be null."));
                    test.assertEqual(Iterable.create("clone"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("clone"), parameters.getCommandArguments());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitCloneParameters parameters = GitCloneParameters.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/repo/folder/").await();
                    final GitCloneParameters addDirectoryResult = parameters.addDirectory(folder);
                    test.assertSame(parameters, addDirectoryResult);
                    test.assertEqual(Iterable.create("clone", folder.toString()), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("clone", folder.toString()), parameters.getCommandArguments());
                });
            });
        });
    }
}
