package qub;

public interface GitInitParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitInitParameters.class, () ->
        {
            GitParametersDecoratorTests.test(runner, GitInitParameters::create);

            runner.test("create()", (Test test) ->
            {
                final GitInitParameters parameters = GitInitParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("git"), parameters.getExecutablePath());
                test.assertEqual(Iterable.create("init"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                test.assertNull(parameters.getWorkingFolderPath());
                test.assertNull(parameters.getInputStream());
                test.assertNull(parameters.getOutputStreamHandler());
                test.assertNull(parameters.getErrorStreamHandler());
            });

            runner.testGroup("create(String)", () ->
            {
                final Action2<String, Throwable> createErrorTest = (String executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        test.assertThrows(() -> GitInitParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                createErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                final Action1<String> createTest = (String executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(Path.parse(executablePath), parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
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
                final Action2<Path, Throwable> createErrorTest = (Path executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        test.assertThrows(() -> GitInitParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));

                final Action1<Path> createTest = (Path executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(executablePath, parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
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
                    test.assertThrows(() -> GitInitParameters.create((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/executable").await();
                    final GitInitParameters parameters = GitInitParameters.create(file);
                    test.assertNotNull(parameters);
                    test.assertEqual(file.getPath(), parameters.getExecutablePath());
                    test.assertEqual(Iterable.create("init"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });
            });

            runner.test("addQuiet()", (Test test) ->
            {
                final GitInitParameters parameters = GitInitParameters.create();
                final GitInitParameters addBareResult = parameters.addQuiet();
                test.assertSame(parameters, addBareResult);
                test.assertEqual(Iterable.create("init", "--quiet"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("init", "--quiet"), parameters.getCommandArguments());
            });

            runner.test("addBare()", (Test test) ->
            {
                final GitInitParameters parameters = GitInitParameters.create();
                final GitInitParameters addBareResult = parameters.addBare();
                test.assertSame(parameters, addBareResult);
                test.assertEqual(Iterable.create("init", "--bare"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("init", "--bare"), parameters.getCommandArguments());
            });

            runner.testGroup("addTemplate(String)", () ->
            {
                final Action2<String,Throwable> addTemplateErrorTest = (String templateDirectory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addTemplate(templateDirectory),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addTemplateErrorTest.run(null, new PreConditionFailure("templateDirectory cannot be null."));
                addTemplateErrorTest.run("", new PreConditionFailure("templateDirectory cannot be empty."));

                final Action1<String> addTemplateTest = (String templateDirectory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addTemplateResult = parameters.addTemplate(templateDirectory);
                        test.assertSame(parameters, addTemplateResult);
                        test.assertEqual(Iterable.create("init", "--template=" + Strings.escapeAndQuote(templateDirectory)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", "--template=" + Strings.escapeAndQuote(templateDirectory)), parameters.getCommandArguments());
                    });
                };

                addTemplateTest.run("spam");
                addTemplateTest.run("/rooted/folder/");
            });

            runner.testGroup("addTemplate(Path)", () ->
            {
                final Action2<Path,Throwable> addTemplateErrorTest = (Path templateDirectory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addTemplate(templateDirectory),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addTemplateErrorTest.run(null, new PreConditionFailure("templateDirectory cannot be null."));

                final Action1<Path> addTemplateTest = (Path templateDirectory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(templateDirectory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addTemplateResult = parameters.addTemplate(templateDirectory);
                        test.assertSame(parameters, addTemplateResult);
                        test.assertEqual(Iterable.create("init", "--template=" + Strings.escapeAndQuote(templateDirectory)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", "--template=" + Strings.escapeAndQuote(templateDirectory)), parameters.getCommandArguments());
                    });
                };

                addTemplateTest.run(Path.parse("spam"));
                addTemplateTest.run(Path.parse("/rooted/folder/"));
            });

            runner.testGroup("addTemplate(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    test.assertThrows(() -> parameters.addTemplate((Folder)null),
                        new PreConditionFailure("templateDirectory cannot be null."));
                    test.assertEqual(Iterable.create("init"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/folder/").await();
                    final GitInitParameters addTemplateResult = parameters.addTemplate(folder);
                    test.assertSame(parameters, addTemplateResult);
                    test.assertEqual(Iterable.create("init", "--template=\"/folder/\""), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init", "--template=\"/folder/\""), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addSeparateGitDir(String)", () ->
            {
                final Action2<String,Throwable> addSeparateGitDirErrorTest = (String separateGitDir, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addSeparateGitDir(separateGitDir),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addSeparateGitDirErrorTest.run(null, new PreConditionFailure("separateGitDir cannot be null."));
                addSeparateGitDirErrorTest.run("", new PreConditionFailure("separateGitDir cannot be empty."));

                final Action1<String> addSeparateGitDirTest = (String separateGitDir) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addSeparateGitDirResult = parameters.addSeparateGitDir(separateGitDir);
                        test.assertSame(parameters, addSeparateGitDirResult);
                        test.assertEqual(Iterable.create("init", "--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", "--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), parameters.getCommandArguments());
                    });
                };

                addSeparateGitDirTest.run("spam");
                addSeparateGitDirTest.run("/rooted/folder/");
            });

            runner.testGroup("addSeparateGitDir(Path)", () ->
            {
                final Action2<Path,Throwable> addSeparateGitDirErrorTest = (Path separateGitDir, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addSeparateGitDir(separateGitDir),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addSeparateGitDirErrorTest.run(null, new PreConditionFailure("separateGitDir cannot be null."));

                final Action1<Path> addSeparateGitDirTest = (Path separateGitDir) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(separateGitDir), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addSeparateGitDirResult = parameters.addSeparateGitDir(separateGitDir);
                        test.assertSame(parameters, addSeparateGitDirResult);
                        test.assertEqual(Iterable.create("init", "--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", "--separate-git-dir=" + Strings.escapeAndQuote(separateGitDir)), parameters.getCommandArguments());
                    });
                };

                addSeparateGitDirTest.run(Path.parse("spam"));
                addSeparateGitDirTest.run(Path.parse("/rooted/folder/"));
            });

            runner.testGroup("addSeparateGitDir(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    test.assertThrows(() -> parameters.addSeparateGitDir((Folder)null),
                        new PreConditionFailure("separateGitDir cannot be null."));
                    test.assertEqual(Iterable.create("init"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/folder/").await();
                    final GitInitParameters addSeparateGitDirResult = parameters.addSeparateGitDir(folder);
                    test.assertSame(parameters, addSeparateGitDirResult);
                    test.assertEqual(Iterable.create("init", "--separate-git-dir=\"/folder/\""), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init", "--separate-git-dir=\"/folder/\""), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addInitialBranch(String)", () ->
            {
                final Action2<String,Throwable> addInitialBranchErrorTest = (String initialBranch, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(initialBranch), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addInitialBranch(initialBranch),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addInitialBranchErrorTest.run(null, new PreConditionFailure("initialBranch cannot be null."));
                addInitialBranchErrorTest.run("", new PreConditionFailure("initialBranch cannot be empty."));

                final Action1<String> addInitialBranchTest = (String initialBranch) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(initialBranch), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addInitialBranchResult = parameters.addInitialBranch(initialBranch);
                        test.assertSame(parameters, addInitialBranchResult);
                        test.assertEqual(Iterable.create("init", "--initial-branch", initialBranch), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", "--initial-branch", initialBranch), parameters.getCommandArguments());
                    });
                };

                addInitialBranchTest.run("spam");
                addInitialBranchTest.run("/rooted/folder/");
            });

            runner.testGroup("addDirectory(String)", () ->
            {
                final Action2<String,Throwable> addDirectoryErrorTest = (String directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));
                addDirectoryErrorTest.run("", new PreConditionFailure("directory cannot be empty."));

                final Action1<String> addDirectoryTest = (String directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addDirectoryResult = parameters.addDirectory(directory);
                        test.assertSame(parameters, addDirectoryResult);
                        test.assertEqual(Iterable.create("init", directory), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", directory), parameters.getCommandArguments());
                    });
                };

                addDirectoryTest.run("spam");
                addDirectoryTest.run("/rooted/folder/");
            });

            runner.testGroup("addDirectory(Path)", () ->
            {
                final Action2<Path,Throwable> addDirectoryErrorTest = (Path directory, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        test.assertThrows(() -> parameters.addDirectory(directory),
                            expected);
                        test.assertEqual(Iterable.create("init"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                    });
                };

                addDirectoryErrorTest.run(null, new PreConditionFailure("directory cannot be null."));

                final Action1<Path> addDirectoryTest = (Path directory) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(directory), (Test test) ->
                    {
                        final GitInitParameters parameters = GitInitParameters.create();
                        final GitInitParameters addDirectoryResult = parameters.addDirectory(directory);
                        test.assertSame(parameters, addDirectoryResult);
                        test.assertEqual(Iterable.create("init", directory.toString()), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("init", directory.toString()), parameters.getCommandArguments());
                    });
                };

                addDirectoryTest.run(Path.parse("spam"));
                addDirectoryTest.run(Path.parse("/rooted/folder/"));
            });

            runner.testGroup("addDirectory(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    test.assertThrows(() -> parameters.addDirectory((Folder)null),
                        new PreConditionFailure("directory cannot be null."));
                    test.assertEqual(Iterable.create("init"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init"), parameters.getCommandArguments());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitInitParameters parameters = GitInitParameters.create();
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/folder/").await();
                    final GitInitParameters addDirectoryResult = parameters.addDirectory(folder);
                    test.assertSame(parameters, addDirectoryResult);
                    test.assertEqual(Iterable.create("init", folder.toString()), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("init", folder.toString()), parameters.getCommandArguments());
                });
            });
        });
    }
}
