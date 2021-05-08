package qub;

public interface GitCommandArgumentsTests
{
    static <T extends GitCommandArguments<T>> void test(TestRunner runner, Function1<DesktopProcess,T> creator)
    {
        runner.testGroup(GitCommandArguments.class, () ->
        {
            GitArgumentsTests.test(runner, creator);
            
            runner.testGroup("addCommandArguments(String...)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    
                    test.assertThrows(() -> arguments.addCommandArguments((String[])null),
                        new PreConditionFailure("commandArguments cannot be null."));
                    
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                });
                
                runner.test("with no arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    
                    final T addCommandArgumentsResult = arguments.addCommandArguments();
                    test.assertSame(arguments, addCommandArgumentsResult);
                    
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                });
                
                runner.test("with empty array",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    
                    final T addCommandArgumentsResult = arguments.addCommandArguments(new String[0]);
                    test.assertSame(arguments, addCommandArgumentsResult);
                    
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                });
                
                runner.test("with one argument",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    
                    final T addCommandArgumentsResult = arguments.addCommandArguments("a");
                    test.assertSame(arguments, addCommandArgumentsResult);
                    
                    test.assertEqual(Iterable.create("a"), arguments.getCommandArguments());
                });
                
                runner.test("with two arguments",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final T arguments = creator.run(process);
                    test.assertEqual(Iterable.create(), arguments.getCommandArguments());
                    
                    final T addCommandArgumentsResult = arguments.addCommandArguments("a", "b");
                    test.assertSame(arguments, addCommandArgumentsResult);
                    
                    test.assertEqual(Iterable.create("a", "b"), arguments.getCommandArguments());
                });
            });
        });
    }
}
