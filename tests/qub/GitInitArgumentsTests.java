package qub;

public interface GitInitArgumentsTests
{
    static <T extends GitInitArguments<T>> void test(TestRunner runner, Function1<DesktopProcess,T> creator)
    {
        runner.testGroup(GitInitArguments.class, () ->
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
            
            runner.test("addBare()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addBareResult = arguments.addBare();
                test.assertSame(arguments, addBareResult);
                test.assertEqual(Iterable.create(), arguments.getGitArguments());
                test.assertEqual(Iterable.create("--bare"), arguments.getCommandArguments());
            });

            runner.testGroup("addTemplate(String)", () ->
            {
                final Action2<String,Throwable> addTemplateErrorTest = (String templateDirectory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addTemplate(templateDirectory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addTemplateErrorTest.run(null, new PreConditionFailure("templateDirectory cannot be null."));
                addTemplateErrorTest.run("", new PreConditionFailure("templateDirectory cannot be empty."));

                final Action1<String> addTemplateTest = (String templateDirectory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addTemplateResult = arguments.addTemplate(templateDirectory);
                        test.assertSame(arguments, addTemplateResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--template=" + Strings.escapeAndQuote(templateDirectory)), arguments.getCommandArguments());
                    });
                };

                addTemplateTest.run("hello");
                addTemplateTest.run("/hello/there/");
            });

            runner.testGroup("addTemplate(Path)", () ->
            {
                final Action2<Path,Throwable> addTemplateErrorTest = (Path templateDirectory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addTemplate(templateDirectory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addTemplateErrorTest.run(null, new PreConditionFailure("templateDirectory cannot be null."));

                final Action1<Path> addTemplateTest = (Path templateDirectory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addTemplateResult = arguments.addTemplate(templateDirectory);
                        test.assertSame(arguments, addTemplateResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--template=" + Strings.escapeAndQuote(templateDirectory)), arguments.getCommandArguments());
                    });
                };

                addTemplateTest.run(Path.parse("hello"));
                addTemplateTest.run(Path.parse("/hello/there/"));
            });

            runner.testGroup("addTemplate(Folder)", () ->
            {
                final Action2<Folder,Throwable> addTemplateErrorTest = (Folder templateDirectory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addTemplate(templateDirectory),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addTemplateErrorTest.run(null, new PreConditionFailure("templateDirectory cannot be null."));

                final Action1<Path> addTemplateTest = (Path templateDirectory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final Folder templateDirectoryFolder = process.getFileSystem().getFolder(templateDirectory).await();
                        final T addTemplateResult = arguments.addTemplate(templateDirectoryFolder);
                        test.assertSame(arguments, addTemplateResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--template=" + Strings.escapeAndQuote(templateDirectory)), arguments.getCommandArguments());
                    });
                };

                addTemplateTest.run(Path.parse("/hello/there/"));
            });

            runner.testGroup("addSeparateGitDir(String)", () ->
            {
                final Action2<String,Throwable> addSeparateGitDirErrorTest = (String separateGitDir, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addSeparateGitDir(separateGitDir),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirErrorTest.run(null, new PreConditionFailure("separateGitDir cannot be null."));
                addSeparateGitDirErrorTest.run("", new PreConditionFailure("separateGitDir cannot be empty."));

                final Action1<String> addSeparateGitDirTest = (String separateGitDir) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addSeparateGitDirResult = arguments.addSeparateGitDir(separateGitDir);
                        test.assertSame(arguments, addSeparateGitDirResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirTest.run("hello");
                addSeparateGitDirTest.run("/hello/there/");
                addSeparateGitDirTest.run("https://github.com/owner/repo");
            });

            runner.testGroup("addSeparateGitDir(Path)", () ->
            {
                final Action2<Path,Throwable> addSeparateGitDirErrorTest = (Path separateGitDir, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addSeparateGitDir(separateGitDir),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirErrorTest.run(null, new PreConditionFailure("separateGitDir cannot be null."));

                final Action1<Path> addSeparateGitDirTest = (Path separateGitDir) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addSeparateGitDirResult = arguments.addSeparateGitDir(separateGitDir);
                        test.assertSame(arguments, addSeparateGitDirResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirTest.run(Path.parse("hello"));
                addSeparateGitDirTest.run(Path.parse("/hello/there/"));
            });

            runner.testGroup("addSeparateGitDir(Folder)", () ->
            {
                final Action2<Folder,Throwable> addSeparateGitDirErrorTest = (Folder separateGitDir, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addSeparateGitDir(separateGitDir),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirErrorTest.run(null, new PreConditionFailure("separateGitDir cannot be null."));

                final Action1<Path> addSeparateGitDirTest = (Path separateGitDir) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final Folder separateGitDirFolder = process.getFileSystem().getFolder(separateGitDir).await();
                        final T addSeparateGitDirResult = arguments.addSeparateGitDir(separateGitDirFolder);
                        test.assertSame(arguments, addSeparateGitDirResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), arguments.getCommandArguments());
                    });
                };

                addSeparateGitDirTest.run(Path.parse("/hello/there/"));
            });

            runner.testGroup("addInitialBranch(String)", () ->
            {
                final Action2<String,Throwable> addInitialBranchErrorTest = (String initialBranch, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(initialBranch),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addInitialBranch(initialBranch),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    });
                };

                addInitialBranchErrorTest.run(null, new PreConditionFailure("initialBranch cannot be null."));
                addInitialBranchErrorTest.run("", new PreConditionFailure("initialBranch cannot be empty."));

                final Action1<String> addInitialBranchTest = (String initialBranch) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(initialBranch),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addInitialBranchResult = arguments.addInitialBranch(initialBranch);
                        test.assertSame(arguments, addInitialBranchResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create("--initial-branch", initialBranch), arguments.getCommandArguments());
                    });
                };

                addInitialBranchTest.run("hello");
                addInitialBranchTest.run("/hello/there/");
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
                        final T arguments = creator.run(process);
                        final T addDirectoryResult = arguments.addDirectory(directory);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(directory), arguments.getCommandArguments());
                    });
                };

                addDirectoryTest.run("hello");
                addDirectoryTest.run("/hello/there/");
                addDirectoryTest.run("https://github.com/owner/repo");
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
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addDirectoryResult = arguments.addDirectory(directory);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(directory.toString()), arguments.getCommandArguments());
                    });
                };

                addDirectoryTest.run(Path.parse("hello"));
                addDirectoryTest.run(Path.parse("/hello/there/"));
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
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final Folder directoryFolder = process.getFileSystem().getFolder(directory).await();
                        final T addDirectoryResult = arguments.addDirectory(directoryFolder);
                        test.assertSame(arguments, addDirectoryResult);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                        test.assertEqual(Iterable.create(directory.toString()), arguments.getCommandArguments());
                    });
                };

                addDirectoryTest.run(Path.parse("/hello/there/"));
            });
        });
    }
}
