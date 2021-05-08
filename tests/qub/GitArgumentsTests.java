package qub;

public interface GitArgumentsTests
{
    static <T extends GitArguments<T>> void test(TestRunner runner, Function1<DesktopProcess,T> creator)
    {
        runner.testGroup(GitArguments.class, () ->
        {
            runner.testGroup("addGitArguments(String...)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getGitArguments());

                    test.assertThrows(() -> arguments.addGitArguments((String[])null),
                        new PreConditionFailure("gitArguments cannot be null."));

                    test.assertEqual(Iterable.create(), arguments.getGitArguments());
                });

                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getGitArguments());

                    final T addGitArgumentsResult = arguments.addGitArguments();
                    test.assertSame(arguments, addGitArgumentsResult);

                    test.assertEqual(Iterable.create(), arguments.getGitArguments());
                });

                runner.test("with empty array",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getGitArguments());

                    final T addGitArgumentsResult = arguments.addGitArguments(new String[0]);
                    test.assertSame(arguments, addGitArgumentsResult);

                    test.assertEqual(Iterable.create(), arguments.getGitArguments());
                });

                runner.test("with one argument",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getGitArguments());

                    final T addGitArgumentsResult = arguments.addGitArguments("a");
                    test.assertSame(arguments, addGitArgumentsResult);

                    test.assertEqual(Iterable.create("a"), arguments.getGitArguments());
                });

                runner.test("with two arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getGitArguments());

                    final T addGitArgumentsResult = arguments.addGitArguments("a", "b");
                    test.assertSame(arguments, addGitArgumentsResult);

                    test.assertEqual(Iterable.create("a", "b"), arguments.getGitArguments());
                });
            });

            runner.test("addVersion()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addVersionResult = arguments.addVersion();
                test.assertSame(arguments, addVersionResult);
                test.assertEqual(Iterable.create("--version"), arguments.getGitArguments());
            });

            runner.test("addHelp()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addHelpResult = arguments.addHelp();
                test.assertSame(arguments, addHelpResult);
                test.assertEqual(Iterable.create("--help"), arguments.getGitArguments());
            });

            runner.testGroup("addHelp(boolean)", () ->
            {
                final Action2<Boolean,Iterable<String>> addHelpTest = (Boolean all, Iterable<String> expectedGitArguments) ->
                {
                    runner.test("with " + all,
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addHelpResult = arguments.addHelp(all);
                        test.assertSame(arguments, addHelpResult);
                        test.assertEqual(expectedGitArguments, arguments.getGitArguments());
                    });
                };

                addHelpTest.run(false, Iterable.create("--help"));
                addHelpTest.run(true, Iterable.create("--help", "--all"));
            });

            runner.testGroup("addConfigurationValue(String)", () ->
            {
                final Action2<String,Throwable> addConfigurationValueErrorTest = (String name, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addConfigurationValue(name),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                    });
                };

                addConfigurationValueErrorTest.run(null, new PreConditionFailure("name cannot be null."));
                addConfigurationValueErrorTest.run("", new PreConditionFailure("name cannot be empty."));

                final Action1<String> addConfigurationValueTest = (String name) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(name),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addConfigurationValueResult = arguments.addConfigurationValue(name);
                        test.assertSame(arguments, addConfigurationValueResult);
                        test.assertEqual(Iterable.create("-c", name), arguments.getGitArguments());
                    });
                };

                addConfigurationValueTest.run("hello");
                addConfigurationValueTest.run("user.name");
            });

            runner.testGroup("addConfigurationValue(String,String)", () ->
            {
                final Action3<String,String,Throwable> addConfigurationValueErrorTest = (String name, String value, Throwable expected) ->
                {
                    runner.test("with " + English.andList(Iterable.create(name, value).map(Strings::escapeAndQuote)),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        test.assertThrows(() -> arguments.addConfigurationValue(name, value),
                            expected);
                        test.assertEqual(Iterable.create(), arguments.getGitArguments());
                    });
                };

                addConfigurationValueErrorTest.run(null, "fake-value", new PreConditionFailure("name cannot be null."));
                addConfigurationValueErrorTest.run("", "fake-value", new PreConditionFailure("name cannot be empty."));
                addConfigurationValueErrorTest.run("fake-name", null, new PreConditionFailure("value cannot be null."));

                final Action2<String,String> addConfigurationValueTest = (String name, String value) ->
                {
                    runner.test("with " + English.andList(Iterable.create(name, value).map(Strings::escapeAndQuote)),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addConfigurationValueResult = arguments.addConfigurationValue(name, value);
                        test.assertSame(arguments, addConfigurationValueResult);
                        test.assertEqual(Iterable.create("-c", name + "=" + value), arguments.getGitArguments());
                    });
                };

                addConfigurationValueTest.run("hello", "there");
                addConfigurationValueTest.run("user.name", "Dan");
                addConfigurationValueTest.run("a", "");
            });

            runner.testGroup("addPager(boolean)", () ->
            {
                final Action2<Boolean,Iterable<String>> addPagerTest = (Boolean pager, Iterable<String> expectedGitArguments) ->
                {
                    runner.test("with " + pager,
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final T arguments = creator.run(process);
                        final T addPagerResult = arguments.addPager(pager);
                        test.assertSame(arguments, addPagerResult);
                        test.assertEqual(expectedGitArguments, arguments.getGitArguments());
                    });
                };

                addPagerTest.run(false, Iterable.create("--no-pager"));
                addPagerTest.run(true, Iterable.create("--paginate"));
            });

            runner.test("addNoOptionalLocks()",
                (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                (Test test, FakeDesktopProcess process) ->
            {
                final T arguments = creator.run(process);
                final T addNoOptionalLocksResult = arguments.addNoOptionalLocks();
                test.assertSame(arguments, addNoOptionalLocksResult);
                test.assertEqual(Iterable.create("--no-optional-locks"), arguments.getGitArguments());
            });
        });
    }
}
