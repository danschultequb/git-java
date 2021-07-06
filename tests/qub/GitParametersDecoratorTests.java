package qub;

public interface GitParametersDecoratorTests
{
    static <T extends GitParameters, U extends GitParametersDecorator<T>> void test(TestRunner runner, Function0<U> creator)
    {
        runner.testGroup(BasicGitParameters.class, () ->
        {
            GitParametersTests.test(runner, creator);

            runner.testGroup("setWorkingFolderPath(String)", () ->
            {
                final Action1<String> setWorkingFolderPathTest = (String workingFolderPath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(workingFolderPath), (Test test) ->
                    {
                        final U parameters = creator.run();
                        final T setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
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
                        final U parameters = creator.run();
                        final T setWorkingFolderPathResult = parameters.setWorkingFolderPath(workingFolderPath);
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
                    final U parameters = creator.run();
                    final T setWorkingFolderResult = parameters.setWorkingFolder(folder);
                    test.assertSame(parameters, setWorkingFolderResult);
                    test.assertEqual(folder.getPath(), parameters.getWorkingFolderPath());
                });
            });

            runner.testGroup("insertArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertArgumentResult = parameters.insertArgument(0, "hello");
                    test.assertSame(parameters, insertArgumentResult);
                });
            });

            runner.testGroup("addArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertArgumentResult = parameters.addArgument("hello");
                    test.assertSame(parameters, insertArgumentResult);
                });
            });

            runner.testGroup("addArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertArgumentResult = parameters.addArguments("hello");
                    test.assertSame(parameters, insertArgumentResult);
                });
            });

            runner.testGroup("addArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertArgumentResult = parameters.addArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertArgumentResult);
                });
            });

            runner.testGroup("insertGitArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertGitArgumentResult = parameters.insertGitArgument(0, "hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                });
            });

            runner.testGroup("addGitArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertGitArgumentResult = parameters.addGitArgument("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                });
            });

            runner.testGroup("addGitArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertGitArgumentResult = parameters.addGitArguments("hello");
                    test.assertSame(parameters, insertGitArgumentResult);
                });
            });

            runner.testGroup("addGitArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertGitArgumentResult = parameters.addGitArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertGitArgumentResult);
                });
            });

            runner.testGroup("insertCommandArgument(int,String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertCommandArgumentResult = parameters.insertCommandArgument(0, "hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                });
            });

            runner.testGroup("addCommandArgument(String)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertCommandArgumentResult = parameters.addCommandArgument("hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                });
            });

            runner.testGroup("addCommandArguments(String...)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertCommandArgumentResult = parameters.addCommandArguments("hello");
                    test.assertSame(parameters, insertCommandArgumentResult);
                });
            });

            runner.testGroup("addCommandArguments(Iterable<String>)", () ->
            {
                runner.test("with no existing git, command, or regular arguments", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T insertCommandArgumentResult = parameters.addCommandArguments(Iterable.create("hello"));
                    test.assertSame(parameters, insertCommandArgumentResult);
                });
            });

            runner.testGroup("setInputStream(ByteReadStream)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final U parameters = creator.run();
                    test.assertThrows(() -> parameters.setInputStream(null),
                        new PreConditionFailure("inputStream cannot be null."));
                    test.assertNull(parameters.getInputStream());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final U parameters = creator.run();
                    final InMemoryByteStream inputStream = InMemoryByteStream.create();
                    final T setInputStreamResult = parameters.setInputStream(inputStream);
                    test.assertSame(parameters, setInputStreamResult);
                    test.assertSame(inputStream, parameters.getInputStream());
                });
            });

            runner.testGroup("setOutputStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T setOutputStreamHandlerResult = parameters.setOutputStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setOutputStreamHandlerResult);
                });
            });

            runner.testGroup("redirectOutputTo(ByteWriteStream)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T redirectOutputToResult = parameters.redirectOutputTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectOutputToResult);
                });
            });

            runner.testGroup("setErrorStreamHandler(Action1<ByteReadStream>)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T setErrorStreamHandlerResult = parameters.setErrorStreamHandler((ByteReadStream outputStream) -> {});
                    test.assertSame(parameters, setErrorStreamHandlerResult);
                });
            });

            runner.testGroup("redirectErrorTo(ByteWriteStream)", () ->
            {
                runner.test("with non-null", (Test test) ->
                {
                    final U parameters = creator.run();
                    final T redirectErrorToResult = parameters.redirectErrorTo(InMemoryByteStream.create());
                    test.assertSame(parameters, redirectErrorToResult);
                });
            });
        });
    }
}
