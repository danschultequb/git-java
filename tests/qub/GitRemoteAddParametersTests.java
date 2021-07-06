package qub;

public interface GitRemoteAddParametersTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(GitRemoteAddParameters.class, () ->
        {
            GitParametersDecoratorTests.test(runner, GitRemoteAddParameters::create);

            runner.test("create()", (Test test) ->
            {
                final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                test.assertNotNull(parameters);
                test.assertEqual(Path.parse("git"), parameters.getExecutablePath());
                test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
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
                        test.assertThrows(() -> GitRemoteAddParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                createErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                final Action1<String> createTest = (String executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(Path.parse(executablePath), parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
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
                        test.assertThrows(() -> GitRemoteAddParameters.create(executablePath),
                            expected);
                    });
                };

                createErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));

                final Action1<Path> createTest = (Path executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create(executablePath);
                        test.assertNotNull(parameters);
                        test.assertEqual(executablePath, parameters.getExecutablePath());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
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
                    test.assertThrows(() -> GitRemoteAddParameters.create((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final InMemoryFileSystem fileSystem = InMemoryFileSystem.create();
                    final File file = fileSystem.getFile("/executable").await();
                    final GitRemoteAddParameters parameters = GitRemoteAddParameters.create(file);
                    test.assertNotNull(parameters);
                    test.assertEqual(file.getPath(), parameters.getExecutablePath());
                    test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                    test.assertEqual(Iterable.create(), parameters.getGitArguments());
                    test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
                    test.assertNull(parameters.getWorkingFolderPath());
                    test.assertNull(parameters.getInputStream());
                    test.assertNull(parameters.getOutputStreamHandler());
                    test.assertNull(parameters.getErrorStreamHandler());
                });
            });

            runner.test("addVerbose()", (Test test) ->
            {
                final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                final GitRemoteAddParameters addVerboseResult = parameters.addVerbose();
                test.assertSame(parameters, addVerboseResult);
                test.assertEqual(Iterable.create("remote", "--verbose", "add"), parameters.getArguments());
                test.assertEqual(Iterable.create(), parameters.getGitArguments());
                test.assertEqual(Iterable.create("remote", "--verbose", "add"), parameters.getCommandArguments());
            });

            runner.testGroup("addName(String)", () ->
            {
                final Action2<String,Throwable> addNameErrorTest = (String remoteName, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteName), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        test.assertThrows(() -> parameters.addName(remoteName),
                            expected);
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
                    });
                };

                addNameErrorTest.run(null, new PreConditionFailure("remoteName cannot be null."));
                addNameErrorTest.run("", new PreConditionFailure("remoteName cannot be empty."));

                final Action1<String> addNameTest = (String remoteName) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteName), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        final GitRemoteAddParameters addNameResult = parameters.addName(remoteName);
                        test.assertSame(parameters, addNameResult);
                        test.assertEqual(Iterable.create("remote", "add", remoteName), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add", remoteName), parameters.getCommandArguments());
                    });
                };

                addNameTest.run("origin");
                addNameTest.run("thing");
            });

            runner.testGroup("addUrl(String)", () ->
            {
                final Action2<String,Throwable> addUrlErrorTest = (String remoteUrl, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteUrl), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        test.assertThrows(() -> parameters.addUrl(remoteUrl),
                            expected);
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
                    });
                };

                addUrlErrorTest.run(null, new PreConditionFailure("remoteUrl cannot be null."));
                addUrlErrorTest.run("", new PreConditionFailure("remoteUrl cannot be empty."));

                final Action1<String> addUrlTest = (String remoteUrl) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteUrl), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        final GitRemoteAddParameters addNameResult = parameters.addUrl(remoteUrl);
                        test.assertSame(parameters, addNameResult);
                        test.assertEqual(Iterable.create("remote", "add", remoteUrl), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add", remoteUrl), parameters.getCommandArguments());
                    });
                };

                addUrlTest.run("origin");
                addUrlTest.run("thing");
                addUrlTest.run("https://github.com/owner/repo");
            });

            runner.testGroup("addUrl(URL)", () ->
            {
                final Action2<URL,Throwable> addUrlErrorTest = (URL remoteUrl, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteUrl), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        test.assertThrows(() -> parameters.addUrl(remoteUrl),
                            expected);
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add"), parameters.getCommandArguments());
                    });
                };

                addUrlErrorTest.run(null, new PreConditionFailure("remoteUrl cannot be null."));

                final Action1<URL> addUrlTest = (URL remoteUrl) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(remoteUrl), (Test test) ->
                    {
                        final GitRemoteAddParameters parameters = GitRemoteAddParameters.create();
                        final GitRemoteAddParameters addNameResult = parameters.addUrl(remoteUrl);
                        test.assertSame(parameters, addNameResult);
                        test.assertEqual(Iterable.create("remote", "add", remoteUrl.toString(true)), parameters.getArguments());
                        test.assertEqual(Iterable.create(), parameters.getGitArguments());
                        test.assertEqual(Iterable.create("remote", "add", remoteUrl.toString(true)), parameters.getCommandArguments());
                    });
                };

                addUrlTest.run(URL.parse("https://github.com/owner/repo").await());
                addUrlTest.run(URL.parse("https://github.com/ow ner/re po").await());
            });
        });
    }
}
