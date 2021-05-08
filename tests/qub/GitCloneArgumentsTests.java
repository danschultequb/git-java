package qub;

public interface GitCloneArgumentsTests
{
    static <T extends GitCloneArguments<T>> void test(TestRunner runner, Function1<DesktopProcess,T> creator)
    {
        runner.testGroup(GitCloneArguments.class, () ->
        {
            GitCommandArgumentsTests.test(runner, creator);

            runner.test("addQuiet()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addQuietResult = arguments.addQuiet();
                test.assertSame(arguments, addQuietResult);
                test.assertEqual(Iterable.create(), arguments.getGitArguments());
                test.assertEqual(Iterable.create("--quiet"), arguments.getCommandArguments());
            });

            runner.test("addVerbose()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addQuietResult = arguments.addVerbose();
                test.assertSame(arguments, addQuietResult);
                test.assertEqual(Iterable.create(), arguments.getGitArguments());
                test.assertEqual(Iterable.create("--verbose"), arguments.getCommandArguments());
            });

            runner.test("addProgress()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addQuietResult = arguments.addProgress();
                test.assertSame(arguments, addQuietResult);
                test.assertEqual(Iterable.create(), arguments.getGitArguments());
                test.assertEqual(Iterable.create("--progress"), arguments.getCommandArguments());
            });

            runner.testGroup("addRepository(String)", () ->
            {
                final Action2<String,Throwable> addRepositoryErrorTest = (String repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasRepository());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));
                addRepositoryErrorTest.run("", new PreConditionFailure("repository cannot be empty."));

                final Action1<String> addRepositoryTest = (String repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addRepositoryResult = arguments.addRepository(repository);
                        test.assertSame(arguments, addRepositoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(Strings.escapeAndQuote(repository)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasRepository());
                    });
                };

                addRepositoryTest.run("hello");
                addRepositoryTest.run("/hello/there/");
                addRepositoryTest.run("https://github.com/owner/repo");

                runner.test("when repository has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process).addRepository("fake-repository");

                    test.assertThrows(() -> arguments.addRepository("not-allowed"),
                        new PreConditionFailure("this.hasRepository() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                });
            });

            runner.testGroup("addRepository(URL)", () ->
            {
                final Action2<URL,Throwable> addRepositoryErrorTest = (URL repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasRepository());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));

                final Action1<URL> addRepositoryTest = (URL repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addRepositoryResult = arguments.addRepository(repository);
                        test.assertSame(arguments, addRepositoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(Strings.escapeAndQuote(repository)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasRepository());
                    });
                };

                addRepositoryTest.run(URL.parse("https://github.com/owner/repo").await());

                runner.test("when repository has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process).addRepository("fake-repository");

                    test.assertThrows(() -> arguments.addRepository(URL.parse("https://github.com/owner/repo").await()),
                        new PreConditionFailure("this.hasRepository() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                });
            });

            runner.testGroup("addRepository(Path)", () ->
            {
                final Action2<Path,Throwable> addRepositoryErrorTest = (Path repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasRepository());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));

                final Action1<Path> addRepositoryTest = (Path repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addRepositoryResult = arguments.addRepository(repository);
                        test.assertSame(arguments, addRepositoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(Strings.escapeAndQuote(repository)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasRepository());
                    });
                };

                addRepositoryTest.run(Path.parse("hello"));
                addRepositoryTest.run(Path.parse("/hello/there/"));

                runner.test("when repository has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process).addRepository("fake-repository");

                    test.assertThrows(() -> arguments.addRepository(Path.parse("not-allowed")),
                        new PreConditionFailure("this.hasRepository() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                });
            });

            runner.testGroup("addRepository(Folder)", () ->
            {
                final Action2<Folder,Throwable> addRepositoryErrorTest = (Folder repository, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addRepository(repository),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasRepository());
                    });
                };

                addRepositoryErrorTest.run(null, new PreConditionFailure("repository cannot be null."));

                final Action1<Path> addRepositoryTest = (Path repository) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(repository),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final Folder repositoryFolder = process.getFileSystem().getFolder(repository).await();
                        final T addRepositoryResult = arguments.addRepository(repositoryFolder);
                        test.assertSame(arguments, addRepositoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(Strings.escapeAndQuote(repository)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasRepository());
                    });
                };

                addRepositoryTest.run(Path.parse("/hello/there/"));

                runner.test("when repository has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process).addRepository("fake-repository");

                    final Folder repositoryFolder = process.getFileSystem().getFolder("/not/allowed/").await();
                    test.assertThrows(() -> arguments.addRepository(repositoryFolder),
                        new PreConditionFailure("this.hasRepository() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                });
            });

            runner.testGroup("addDirectory(String)", () ->
            {
                final Action2<String,Throwable> addDirectoryErrorTest = (String directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasRepository());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));
                addDirectoryErrorTest.run("", new PreConditionFailure("directory cannot be empty."));

                final Action1<String> addDirectoryTest = (String directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process)
                            .addRepository("fake-repository");
                        final T addDirectoryResult = arguments.addDirectory(directory);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("\"fake-repository\"", Strings.escapeAndQuote(directory)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasDirectory());
                    });
                };

                addDirectoryTest.run("hello");
                addDirectoryTest.run("/hello/there/");
                addDirectoryTest.run("https://github.com/owner/repo");

                runner.test("when repository hasn't already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);

                    test.assertThrows(() -> arguments.addDirectory("/not/allowed/"),
                        new PreConditionFailure("this.hasRepository() cannot be false."));

                    test.assertFalse(arguments.hasRepository());
                    test.assertFalse(arguments.hasDirectory());
                });

                runner.test("when directory has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process)
                        .addRepository("https://github.com/owner/repo")
                        .addDirectory("fake-directory");

                    test.assertThrows(() -> arguments.addDirectory("/not/allowed/"),
                        new PreConditionFailure("this.hasDirectory() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                    test.assertTrue(arguments.hasDirectory());
                });
            });

            runner.testGroup("addDirectory(Path)", () ->
            {
                final Action2<Path,Throwable> addDirectoryErrorTest = (Path directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasDirectory());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process)
                            .addRepository("fake-repository");
                        final T addDirectoryResult = arguments.addDirectory(directory);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("\"fake-repository\"", Strings.escapeAndQuote(directory)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasDirectory());
                    });
                };

                addDirectoryTest.run(Path.parse("hello"));
                addDirectoryTest.run(Path.parse("/hello/there/"));

                runner.test("when repository hasn't already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);

                    test.assertThrows(() -> arguments.addDirectory(Path.parse("/not/allowed/")),
                        new PreConditionFailure("this.hasRepository() cannot be false."));

                    test.assertFalse(arguments.hasRepository());
                    test.assertFalse(arguments.hasDirectory());
                });

                runner.test("when directory has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process)
                        .addRepository("https://github.com/owner/repo")
                        .addDirectory("fake-directory");

                    test.assertThrows(() -> arguments.addDirectory(Path.parse("/not/allowed/")),
                        new PreConditionFailure("this.hasDirectory() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                    test.assertTrue(arguments.hasDirectory());
                });
            });

            runner.testGroup("addDirectory(Folder)", () ->
            {
                final Action2<Folder,Throwable> addDirectoryErrorTest = (Folder directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                        test.assertFalse(arguments.hasDirectory());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process)
                            .addRepository("fake-repository");
                        final Folder directoryFolder = process.getFileSystem().getFolder(directory).await();
                        final T addDirectoryResult = arguments.addDirectory(directoryFolder);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("\"fake-repository\"", Strings.escapeAndQuote(directory)), arguments.getCommandArguments());
                        test.assertTrue(arguments.hasDirectory());
                    });
                };

                addDirectoryTest.run(Path.parse("/hello/there/"));

                runner.test("when repository hasn't already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);

                    final Folder directoryFolder = process.getFileSystem().getFolder("/not/allowed/").await();
                    test.assertThrows(() -> arguments.addDirectory(directoryFolder),
                        new PreConditionFailure("this.hasRepository() cannot be false."));

                    test.assertFalse(arguments.hasRepository());
                    test.assertFalse(arguments.hasDirectory());
                });

                runner.test("when directory has already been added",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process)
                        .addRepository("https://github.com/owner/repo")
                        .addDirectory("fake-directory");

                    final Folder directoryFolder = process.getFileSystem().getFolder("/not/allowed/").await();
                    test.assertThrows(() -> arguments.addDirectory(directoryFolder),
                        new PreConditionFailure("this.hasDirectory() cannot be true."));

                    test.assertTrue(arguments.hasRepository());
                    test.assertTrue(arguments.hasDirectory());
                });
            });
        });
    }
}
