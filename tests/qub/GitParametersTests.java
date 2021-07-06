package qub;

public interface GitParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitParameters.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final GitParameters parameters = GitParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("git"), parameters.getExecutablePath());
                test.assertEqual(Iterable.create(), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create(), parameters.getCommandArguments());
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
                        test.assertThrows(() -> GitParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                createErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                final Action1<String> createTest = (String executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitParameters parameters = GitParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(Path.parse(executablePath), parameters.getExecutablePath());
                        test.assertEqual(Iterable.create(), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create(), parameters.getCommandArguments());
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
                        test.assertThrows(() -> GitParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));

                final Action1<Path> createTest = (Path executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitParameters parameters = GitParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(executablePath, parameters.getExecutablePath());
                        test.assertEqual(Iterable.create(), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create(), parameters.getCommandArguments());
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
                    test.assertThrows(() -> GitParameters.create((File)null),
                        new PreConditionFailure("executable cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/executable").await();
                    final GitParameters parameters = GitParameters.create(file);
                    test.assertNotNull(parameters);
                    test.assertEqual(file.getPath(), parameters.getExecutablePath());
                    test.assertEqual(Iterable.create(), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });
            });
        });
    }
    
    static void test(TestRunner runner, Function0<? extends GitParameters> creator)
    {
        runner.testGroup(GitParameters.class, () ->
        {
            runner.test("getExecutablePath()", (Test test) ->
            {
                final GitParameters parameters = creator.run();
                test.assertNotNull(parameters.getExecutablePath());
            });

            runner.testGroup("setWorkingFolderPath(String)", () ->
            {
                final Action2<String,Throwable> setWorkingFolderPathErrorTest = (String workingFolderPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        test.assertThrows(() -> parameters.setWorkingFolderPath(workingFolderPath),
                            expected);
                        test.assertNull(parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathErrorTest.run(null, new PreConditionFailure("workingFolderPath cannot be null."));
                setWorkingFolderPathErrorTest.run("", new PreConditionFailure("workingFolderPath cannot be empty."));
                setWorkingFolderPathErrorTest.run("relative/folder/", new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));

                final Action1<String> setWorkingFolderPathTest = (String workingFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final GitParameters setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
                        test.assertSame(parameters, setWorkingFolderPathResult);
                        test.assertEqual(Path.parse(workingFolderPath), parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathTest.run("/test/");
            });

            runner.testGroup("setWorkingFolderPath(Path)", () ->
            {
                final Action2<Path,Throwable> setWorkingFolderPathErrorTest = (Path workingFolderPath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        test.assertThrows(() -> parameters.setWorkingFolderPath(workingFolderPath),
                            expected);
                        test.assertNull(parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathErrorTest.run(null, new PreConditionFailure("workingFolderPath cannot be null."));
                setWorkingFolderPathErrorTest.run(Path.parse("relative/folder/"), new PreConditionFailure("workingFolderPath.isRooted() cannot be false."));

                final Action1<Path> setWorkingFolderPathTest = (Path workingFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final GitParameters setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
                        test.assertSame(parameters, setWorkingFolderPathResult);
                        test.assertEqual(workingFolderPath, parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathTest.run(Path.parse("/test/"));
            });

            runner.testGroup("setWorkingFolder(Folder)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.setWorkingFolder(null),
                        new PreConditionFailure("workingFolder cannot be null."));
                    test.assertNull(parameters.getWorkingFolderPath());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/test/").await();
                    final GitParameters parameters = creator.run();
                    final GitParameters setWorkingFolderResult = parameters.setWorkingFolder(folder);
                    test.assertSame(parameters, setWorkingFolderResult);
                    test.assertEqual(folder.getPath(), parameters.getWorkingFolderPath());
                });
            });
            
            runner.testGroup("insertGitArgument(int,String)", () ->
            {
                final Action3<Integer,String,Throwable> insertGitArgumentErrorTest = (Integer gitArgumentIndex, String gitArgument, Throwable expected) ->
                {
                    runner.test("with " + English.andList(gitArgumentIndex, Strings.escapeAndQuote(gitArgument)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();
                        test.assertThrows(() -> parameters.insertGitArgument(gitArgumentIndex, gitArgument),
                            expected);
                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                insertGitArgumentErrorTest.run(-1, "hello", new PreConditionFailure("gitArgumentIndex (-1) must be equal to 0."));
                insertGitArgumentErrorTest.run(1, "hello", new PreConditionFailure("gitArgumentIndex (1) must be equal to 0."));
                insertGitArgumentErrorTest.run(0, null, new PreConditionFailure("gitArgument cannot be null."));
                insertGitArgumentErrorTest.run(0, "", new PreConditionFailure("gitArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.insertGitArgument(0, "hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.insertGitArgument(0, "hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.insert(0, "hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.insertGitArgument(0, "hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.insert(0, "hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertGitArgumentResult = parameters.insertGitArgument(0, "hello");

                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(0, "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.insert(0, "hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArgument(String)", () ->
            {
                final Action2<String,Throwable> addGitArgumentErrorTest = (String gitArgument, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(gitArgument), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();
                        test.assertThrows(() -> parameters.addGitArgument(gitArgument),
                            expected);
                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                addGitArgumentErrorTest.run(null, new PreConditionFailure("gitArgument cannot be null."));
                addGitArgumentErrorTest.run("", new PreConditionFailure("gitArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArguments(String...)", () ->
            {
                final Action2<String[],Throwable> addGitArgumentErrorTest = (String[] gitArguments, Throwable expected) ->
                {
                    runner.test("with " + (gitArguments == null ? null : Iterable.create(gitArguments)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> existingGitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();
                        test.assertThrows(() -> parameters.addGitArguments(gitArguments),
                            expected);
                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(existingGitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                addGitArgumentErrorTest.run(null, new PreConditionFailure("gitArguments cannot be null."));
                addGitArgumentErrorTest.run(new String[] { null }, new PreConditionFailure("gitArgument cannot be null."));
                addGitArgumentErrorTest.run(new String[] { "" }, new PreConditionFailure("gitArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArguments("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArguments("hello", "there");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments.addAll("hello", "there"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArguments("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();
                    final GitParameters insertGitArgumentResult = parameters.addGitArguments("hello", "there");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments.addAll("hello", "there"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArguments(Iterable<String>)", () ->
            {
                final Action2<Iterable<String>,Throwable> addGitArgumentErrorTest = (Iterable<String> gitArguments, Throwable expected) ->
                {
                    runner.test("with " + (gitArguments == null ? null : Iterable.create(gitArguments)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> existingGitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();
                        test.assertThrows(() -> parameters.addGitArguments(gitArguments),
                            expected);
                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(existingGitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                addGitArgumentErrorTest.run(null, new PreConditionFailure("gitArguments cannot be null."));
                addGitArgumentErrorTest.run(Iterable.create((String)null), new PreConditionFailure("gitArgument cannot be null."));
                addGitArgumentErrorTest.run(Iterable.create(""), new PreConditionFailure("gitArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello"));

                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello", "there"));

                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments.addAll("hello", "there"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello"));

                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments.add("hello"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello", "there"));

                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments.addAll("hello", "there"), parameters.getGitArguments());
                    test.assertEqual(commandArguments, parameters.getCommandArguments());
                });
            });

            runner.testGroup("insertCommandArgument(int,String)", () ->
            {
                final Action3<Integer,String,Throwable> insertCommandArgumentErrorTest = (Integer commandArgumentIndex, String commandArgument, Throwable expected) ->
                {
                    runner.test("with " + English.andList(commandArgumentIndex, Strings.escapeAndQuote(commandArgument)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();

                        test.assertThrows(() -> parameters.insertCommandArgument(commandArgumentIndex, commandArgument),
                            expected);

                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                insertCommandArgumentErrorTest.run(0, null, new PreConditionFailure("commandArgument cannot be null."));
                insertCommandArgumentErrorTest.run(0, "", new PreConditionFailure("commandArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");

                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.insert(0, "hello"), parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");

                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.insert(0, "hello"), parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");

                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.insert(0, "hello"), parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");

                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.insert(0, "hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArgument(String)", () ->
            {
                final Action2<String,Throwable> addCommandArgumentErrorTest = (String commandArgument, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(commandArgument), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> commandArguments = parameters.getCommandArguments().toList();

                        test.assertThrows(() -> parameters.addCommandArgument(commandArgument),
                            expected);

                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(commandArguments, parameters.getCommandArguments());
                    });
                };

                addCommandArgumentErrorTest.run(null, new PreConditionFailure("commandArgument cannot be null."));
                addCommandArgumentErrorTest.run("", new PreConditionFailure("commandArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentResult = parameters.addCommandArgument("hello");

                    test.assertSame(parameters, addCommandArgumentResult);
                    test.assertEqual(arguments.add("hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentResult = parameters.addCommandArgument("hello");

                    test.assertSame(parameters, addCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount() + commandArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentResult = parameters.addCommandArgument("hello");

                    test.assertSame(parameters, addCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount() + commandArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentResult = parameters.addCommandArgument("hello");

                    test.assertSame(parameters, addCommandArgumentResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount() + commandArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArguments(String...)", () ->
            {
                final Action2<String[],Throwable> addCommandArgumentErrorTest = (String[] commandArguments, Throwable expected) ->
                {
                    runner.test("with " + (commandArguments == null ? null : Iterable.create(commandArguments)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> existingCommandArguments = parameters.getCommandArguments().toList();

                        test.assertThrows(() -> parameters.addCommandArguments(commandArguments),
                            expected);

                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(existingCommandArguments, parameters.getCommandArguments());
                    });
                };

                addCommandArgumentErrorTest.run(null, new PreConditionFailure("commandArguments cannot be null."));
                addCommandArgumentErrorTest.run(new String[] { null }, new PreConditionFailure("commandArgument cannot be null."));
                addCommandArgumentErrorTest.run(new String[] { "" }, new PreConditionFailure("commandArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments("hello");

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.add("hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments("hello", "there");

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount() + commandArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.addAll("hello", "there"), parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments("hello");

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.insert(gitArguments.getCount() + commandArguments.getCount(), "hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments("hello", "there");

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount() + commandArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.addAll("hello", "there"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArguments(Iterable<String>)", () ->
            {
                final Action2<Iterable<String>,Throwable> addCommandArgumentErrorTest = (Iterable<String> commandArguments, Throwable expected) ->
                {
                    runner.test("with " + (commandArguments == null ? null : Iterable.create(commandArguments)), (Test test) ->
                    {
                        final GitParameters parameters = creator.run();
                        final List<String> arguments = parameters.getArguments().toList();
                        final List<String> gitArguments = parameters.getGitArguments().toList();
                        final List<String> existingCommandArguments = parameters.getCommandArguments().toList();

                        test.assertThrows(() -> parameters.addCommandArguments(commandArguments),
                            expected);

                        test.assertEqual(arguments, parameters.getArguments());
                        test.assertEqual(gitArguments, parameters.getGitArguments());
                        test.assertEqual(existingCommandArguments, parameters.getCommandArguments());
                    });
                };

                addCommandArgumentErrorTest.run(null, new PreConditionFailure("commandArguments cannot be null."));
                addCommandArgumentErrorTest.run(Iterable.create((String)null), new PreConditionFailure("commandArgument cannot be null."));
                addCommandArgumentErrorTest.run(Iterable.create(""), new PreConditionFailure("commandArgument cannot be empty."));

                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments(Iterable.create("hello"));

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.add("hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing regular arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments(Iterable.create("hello", "there"));

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.insertAll(gitArguments.getCount() + commandArguments.getCount(), Iterable.create("hello", "there")), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.addAll("hello", "there"), parameters.getCommandArguments());
                });

                runner.test("with existing command arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addCommandArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters addCommandArgumentsResult = parameters.addCommandArguments(Iterable.create("hello"));

                    test.assertSame(parameters, addCommandArgumentsResult);
                    test.assertEqual(arguments.add("hello"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.add("hello"), parameters.getCommandArguments());
                });

                runner.test("with existing git arguments", (Test test) ->
                {
                    final GitParameters parameters = creator.run()
                        .addGitArguments("abc", "def");
                    final List<String> arguments = parameters.getArguments().toList();
                    final List<String> gitArguments = parameters.getGitArguments().toList();
                    final List<String> commandArguments = parameters.getCommandArguments().toList();

                    final GitParameters insertCommandArgumentResult = parameters.addCommandArguments(Iterable.create("hello", "there"));

                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(arguments.addAll("hello", "there"), parameters.getArguments());
                    test.assertEqual(gitArguments, parameters.getGitArguments());
                    test.assertEqual(commandArguments.addAll("hello", "there"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("setInputStream(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.setInputStream(null),
                        new PreConditionFailure("inputStream cannot be null."));
                    test.assertNull(parameters.getInputStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final InMemoryByteStream inputStream = InMemoryByteStream.create();
                    final GitParameters setInputStreamResult = parameters.setInputStream(inputStream);
                    test.assertSame(parameters, setInputStreamResult);
                    test.assertSame(inputStream, parameters.getInputStream());
                });
            });

            runner.testGroup("setOutputStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.setOutputStreamHandler(null),
                        new PreConditionFailure("outputStreamHandler cannot be null."));
                    test.assertNull(parameters.getOutputStreamHandler());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final GitParameters setOutputStreamHandlerResult = parameters.setOutputStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setOutputStreamHandlerResult);
                });
            });

            runner.testGroup("redirectOutputTo(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.redirectOutputTo(null),
                        new PreConditionFailure("outputStream cannot be null."));
                    test.assertNull(parameters.getOutputStreamHandler());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final GitParameters redirectOutputToResult = parameters.redirectOutputTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectOutputToResult);
                });
            });

            runner.testGroup("setErrorStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.setErrorStreamHandler(null),
                        new PreConditionFailure("errorStreamHandler cannot be null."));
                    test.assertNull(parameters.getErrorStreamHandler());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final GitParameters setErrorStreamHandlerResult = parameters.setErrorStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setErrorStreamHandlerResult);
                });
            });

            runner.testGroup("redirectErrorTo(ByteWriteStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    test.assertThrows(() -> parameters.redirectErrorTo(null),
                        new PreConditionFailure("errorStream cannot be null."));
                    test.assertNull(parameters.getErrorStreamHandler());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final GitParameters parameters = creator.run();
                    final GitParameters redirectErrorToResult = parameters.redirectErrorTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectErrorToResult);
                });
            });
        });
    }
}
