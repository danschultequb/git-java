package qub;

public interface BasicGitParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicGitParameters.class, () ->
        {
            GitParametersTests.test(runner, () -> BasicGitParameters.create(Path.parse("git")));

            runner.testGroup("create(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> BasicGitParameters.create(null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                final Action1<Path> createTest = (Path executablePath) ->
                {
                    runner.test("with " + executablePath, (Test test) ->
                    {
                        final BasicGitParameters parameters = BasicGitParameters.create(executablePath);
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
                createTest.run(Path.parse("folder/git"));
                createTest.run(Path.parse("/folder/git"));
            });

            runner.testGroup("setWorkingFolderPath(String)", () ->
            {
                final Action1<String> setWorkingFolderPathTest = (String workingFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                        final BasicGitParameters setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
                        test.assertSame(parameters, setWorkingFolderPathResult);
                        test.assertEqual(Path.parse(workingFolderPath), parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathTest.run("/test/");
            });

            runner.testGroup("setWorkingFolderPath(Path)", () ->
            {
                final Action1<Path> setWorkingFolderPathTest = (Path workingFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                        final BasicGitParameters setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
                        test.assertSame(parameters, setWorkingFolderPathResult);
                        test.assertEqual(workingFolderPath, parameters.getWorkingFolderPath());
                    });
                };

                setWorkingFolderPathTest.run(Path.parse("/test/"));
            });

            runner.testGroup("setWorkingFolder(Folder)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final Folder folder = fileSystem.getFolder("/test/").await();
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters setWorkingFolderResult = parameters.setWorkingFolder(folder);
                    test.assertSame(parameters, setWorkingFolderResult);
                    test.assertEqual(folder.getPath(), parameters.getWorkingFolderPath());
                });
            });

            runner.testGroup("insertArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertArgumentResult = parameters.insertArgument(0, "hello");
                    test.assertSame(parameters, insertArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertArgumentResult = parameters.addArgument("hello");
                    test.assertSame(parameters, insertArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertArgumentResult = parameters.addArguments("hello");
                    test.assertSame(parameters, insertArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertArgumentResult = parameters.addArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("insertGitArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertGitArgumentResult = parameters.insertGitArgument(0, "hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertGitArgumentResult = parameters.addGitArguments("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addGitArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertGitArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getGitArguments());
                    test.assertEqual(Iterable.create(), parameters.getCommandArguments());
                });
            });

            runner.testGroup("insertCommandArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertCommandArgumentResult = parameters.addCommandArgument("hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertCommandArgumentResult = parameters.addCommandArguments("hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("addCommandArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters insertCommandArgumentResult = parameters.addCommandArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertCommandArgumentResult);
                    test.assertEqual(Iterable.create("hello"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("hello"), parameters.getCommandArguments());
                });
            });

            runner.testGroup("setOutputStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters setOutputStreamHandlerResult = parameters.setOutputStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setOutputStreamHandlerResult);
                });
            });

            runner.testGroup("redirectOutputTo(ByteWriteStream)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters redirectOutputToResult = parameters.redirectOutputTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectOutputToResult);
                });
            });

            runner.testGroup("setErrorStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters setErrorStreamHandlerResult = parameters.setErrorStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setErrorStreamHandlerResult);
                });
            });

            runner.testGroup("redirectErrorTo(ByteWriteStream)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final BasicGitParameters parameters = BasicGitParameters.create(Path.parse("git"));
                    final BasicGitParameters redirectErrorToResult = parameters.redirectErrorTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectErrorToResult);
                });
            });
        });
    }
}
