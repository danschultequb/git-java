package qub;

public interface GitTests
{
    boolean testRealDesktopProcess = true;
    boolean testFakeDesktopProcess = true;

    static Map<String,DesktopProcess> getDesktopProcesses(TestResources resources)
    {
        final MutableMap<String,DesktopProcess> result = Map.create();

        final Action1<DesktopProcess> addDesktopProcess = (DesktopProcess desktopProcess) ->
        {
            PreCondition.assertNotNull(desktopProcess, "desktopProcess");

            result.set(Types.getTypeName(desktopProcess), desktopProcess);
        };

        if (GitTests.testRealDesktopProcess)
        {
            addDesktopProcess.run(resources.getProcess());
        }

        if (GitTests.testFakeDesktopProcess)
        {
            final FakeDesktopProcess fakeDesktopProcess = resources.createFakeDesktopProcess();
            fakeDesktopProcess.getFileSystem().createRoot("C:/").await();

            final FakeChildProcessRunner fakeChildProcessRunner = fakeDesktopProcess.getChildProcessRunner();
            fakeChildProcessRunner.add(FakeChildProcessRun.create("git")
                .setAction((FakeDesktopProcess childProcess) ->
                {
                    childProcess.getOutputWriteStream()
                        .writeLines(Iterable.create(
                            "usage: git [-v | --version] [-h | --help] [-C <path>] [-c <name>=<value>]",
                            "           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]",
                            "           [-p | --paginate | -P | --no-pager] [--no-replace-objects] [--bare]",
                            "           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]",
                            "           [--super-prefix=<path>] [--config-env=<name>=<envvar>]",
                            "           <command> [<args>]",
                            "",
                            "These are common Git commands used in various situations:",
                            "",
                            "start a working area (see also: git help tutorial)",
                            "   clone     Clone a repository into a new directory",
                            "   init      Create an empty Git repository or reinitialize an existing one",
                            "",
                            "work on the current change (see also: git help everyday)",
                            "   add       Add file contents to the index",
                            "   mv        Move or rename a file, a directory, or a symlink",
                            "   restore   Restore working tree files",
                            "   rm        Remove files from the working tree and from the index",
                            "",
                            "examine the history and state (see also: git help revisions)",
                            "   bisect    Use binary search to find the commit that introduced a bug",
                            "   diff      Show changes between commits, commit and working tree, etc",
                            "   grep      Print lines matching a pattern",
                            "   log       Show commit logs",
                            "   show      Show various types of objects",
                            "   status    Show the working tree status",
                            "",
                            "grow, mark and tweak your common history",
                            "   branch    List, create, or delete branches",
                            "   commit    Record changes to the repository",
                            "   merge     Join two or more development histories together",
                            "   rebase    Reapply commits on top of another base tip",
                            "   reset     Reset current HEAD to the specified state",
                            "   switch    Switch branches",
                            "   tag       Create, list, delete or verify a tag object signed with GPG",
                            "",
                            "collaborate (see also: git help workflows)",
                            "   fetch     Download objects and refs from another repository",
                            "   pull      Fetch from and integrate with another repository or a local branch",
                            "   push      Update remote refs along with associated objects",
                            "",
                            "'git help -a' and 'git help -g' list available subcommands and some",
                            "concept guides. See 'git help <command>' or 'git help <concept>'",
                            "to read about a specific subcommand or concept.",
                            "See 'git help git' for an overview of the system."))
                        .await();
                    childProcess.setExitCode(1);
                }));
            fakeChildProcessRunner.add(FakeChildProcessRun.create("git", "--version")
                .setAction((FakeDesktopProcess childProcess) ->
                {
                    childProcess.getOutputWriteStream()
                        .writeLines(Iterable.create(
                            "git version 2.37.0.windows.1"))
                        .await();
                }));
            fakeChildProcessRunner.add(FakeChildProcessRun.create("git", "clone")
                .setAction((FakeDesktopProcess childProcess) ->
                {
                    childProcess.getErrorWriteStream()
                        .writeLines(Iterable.create(
                            "fatal: You must specify a repository to clone.",
                            "",
                            "usage: git clone [<options>] [--] <repo> [<dir>]",
                            "",
                            "    -v, --verbose         be more verbose",
                            "    -q, --quiet           be more quiet",
                            "    --progress            force progress reporting",
                            "    --reject-shallow      don't clone shallow repository",
                            "    -n, --no-checkout     don't create a checkout",
                            "    --bare                create a bare repository",
                            "    --mirror              create a mirror repository (implies bare)",
                            "    -l, --local           to clone from a local repository",
                            "    --no-hardlinks        don't use local hardlinks, always copy",
                            "    -s, --shared          setup as shared repository",
                            "    --recurse-submodules[=<pathspec>]",
                            "                          initialize submodules in the clone",
                            "    --recursive ...       alias of --recurse-submodules",
                            "    -j, --jobs <n>        number of submodules cloned in parallel",
                            "    --template <template-directory>",
                            "                          directory from which templates will be used",
                            "    --reference <repo>    reference repository",
                            "    --reference-if-able <repo>",
                            "                          reference repository",
                            "    --dissociate          use --reference only while cloning",
                            "    -o, --origin <name>   use <name> instead of 'origin' to track upstream",
                            "    -b, --branch <branch>",
                            "                          checkout <branch> instead of the remote's HEAD",
                            "    -u, --upload-pack <path>",
                            "                          path to git-upload-pack on the remote",
                            "    --depth <depth>       create a shallow clone of that depth",
                            "    --shallow-since <time>",
                            "                          create a shallow clone since a specific time",
                            "    --shallow-exclude <revision>",
                            "                          deepen history of shallow clone, excluding rev",
                            "    --single-branch       clone only one branch, HEAD or --branch",
                            "    --no-tags             don't clone any tags, and make later fetches not to follow them",
                            "    --shallow-submodules  any cloned submodules will be shallow",
                            "    --separate-git-dir <gitdir>",
                            "                          separate git dir from working tree",
                            "    -c, --config <key=value>",
                            "                          set config inside the new repository",
                            "    --server-option <server-specific>",
                            "                          option to transmit",
                            "    -4, --ipv4            use IPv4 addresses only",
                            "    -6, --ipv6            use IPv6 addresses only",
                            "    --filter <args>       object filtering",
                            "    --also-filter-submodules",
                            "                          apply partial clone filters to submodules",
                            "    --remote-submodules   any cloned submodules will use their remote-tracking branch",
                            "    --sparse              initialize sparse-checkout file to include only files at root",
                            ""))
                        .await();
                    childProcess.setExitCode(129);
                }));
            fakeChildProcessRunner.add(FakeChildProcessRun.create("git", "clone", "https://github.com/github/choosealicense.com")
                .setAction((FakeDesktopProcess childProcess) ->
                {
                    childProcess.getErrorWriteStream()
                        .writeLines(Iterable.create(
                            "Cloning into 'choosealicense.com'..."))
                        .await();
                }));

            addDesktopProcess.run(fakeDesktopProcess);
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(Git.class,
            (TestResources resources) -> Tuple.create(GitTests.getDesktopProcesses(resources)),
            (Map<String,DesktopProcess> processes) ->
        {
            runner.testGroup("create(DesktopProcess)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Git.create((DesktopProcess)null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Git git = Git.create(process);
                    test.assertNotNull(git);
                    test.assertEqual(Path.parse("git"), git.getExecutablePath());
                });
            });

            runner.testGroup("create(ChildProcessRunner)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> Git.create((ChildProcessRunner)null),
                        new PreConditionFailure("childProcessRunner cannot be null."));
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final ChildProcessRunner childProcessRunner = process.getChildProcessRunner();
                    final Git git = Git.create(childProcessRunner);
                    test.assertNotNull(git);
                    test.assertEqual(Path.parse("git"), git.getExecutablePath());
                });
            });

            runner.testGroup("setExecutablePath(String)", () ->
            {
                final Action2<String,Throwable> setExecutablePathErrorTest = (String executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final Git git = Git.create(process);
                        test.assertThrows(() -> git.setExecutablePath(executablePath),
                            expected);
                        test.assertEqual(Path.parse("git"), git.getExecutablePath());
                    });
                };

                setExecutablePathErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));
                setExecutablePathErrorTest.run("", new PreConditionFailure("executablePath cannot be empty."));

                final Action1<String> setExecutablePathTest = (String executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final Git git = Git.create(process);
                        final Git setExecutablePathResult = git.setExecutablePath(executablePath);
                        test.assertSame(git, setExecutablePathResult);
                        test.assertEqual(Path.parse(executablePath), git.getExecutablePath());
                    });
                };

                setExecutablePathTest.run("relative/git");
                setExecutablePathTest.run("/rooted/git");
            });

            runner.testGroup("setExecutablePath(Path)", () ->
            {
                final Action2<Path,Throwable> setExecutablePathErrorTest = (Path executablePath, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final Git git = Git.create(process);
                        test.assertThrows(() -> git.setExecutablePath(executablePath),
                            expected);
                        test.assertEqual(Path.parse("git"), git.getExecutablePath());
                    });
                };

                setExecutablePathErrorTest.run(null, new PreConditionFailure("executablePath cannot be null."));

                final Action1<Path> setExecutablePathTest = (Path executablePath) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(executablePath),
                        (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                        (Test test, FakeDesktopProcess process) ->
                    {
                        final Git git = Git.create(process);
                        final Git setExecutablePathResult = git.setExecutablePath(executablePath);
                        test.assertSame(git, setExecutablePathResult);
                        test.assertEqual(executablePath, git.getExecutablePath());
                    });
                };

                setExecutablePathTest.run(Path.parse("relative/git"));
                setExecutablePathTest.run(Path.parse("/rooted/git"));
            });

            runner.testGroup("setExecutablePath(File)", () ->
            {
                runner.test("with null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final Git git = Git.create(process);
                    test.assertThrows(() -> git.setExecutablePath((File)null),
                        new PreConditionFailure("executable cannot be null."));
                    test.assertEqual(Path.parse("git"), git.getExecutablePath());
                });

                runner.test("with non-null",
                    (TestResources resources) -> Tuple.create(resources.createFakeDesktopProcess()),
                    (Test test, FakeDesktopProcess process) ->
                {
                    final File file = process.getFileSystem().getFile("/rooted/git").await();
                    final Git git = Git.create(process);
                    final Git setExecutablePathResult = git.setExecutablePath(file);
                    test.assertSame(git, setExecutablePathResult);
                    test.assertEqual(file.getPath(), git.getExecutablePath());
                });
            });

            for (final MapEntry<String,DesktopProcess> processEntry : processes)
            {
                final String processType = processEntry.getKey();
                final DesktopProcess process = processEntry.getValue();

                runner.testGroup("with " + processType, () ->
                {
                    runner.testGroup("run(String...)", () ->
                    {
                        runner.test("with no arguments", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run().await();
                            test.assertEqual(1, exitCode);
                        });

                        runner.test("with null arguments", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run((String[])null),
                                new PreConditionFailure("arguments cannot be null."));
                        });

                        runner.test("with null argument", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run(new String[] { null }),
                                new PreConditionFailure("argument cannot be null."));
                        });

                        runner.test("with --version", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run("--version").await();
                            test.assertEqual(0, exitCode);
                        });
                    });

                    runner.testGroup("run(Iterable<String>)", () ->
                    {
                        runner.test("with no arguments", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run(Iterable.create()).await();
                            test.assertEqual(1, exitCode);
                        });

                        runner.test("with null arguments", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run((Iterable<String>)null),
                                new PreConditionFailure("arguments cannot be null."));
                        });

                        runner.test("with null argument", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run(Iterable.create((String)null)),
                                new PreConditionFailure("argument cannot be null."));
                        });

                        runner.test("with --version", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run(Iterable.create("--version")).await();
                            test.assertEqual(0, exitCode);
                        });
                    });

                    runner.testGroup("run(GitParameters)", () ->
                    {
                        runner.test("with null", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run((GitParameters)null),
                                new PreConditionFailure("parameters cannot be null."));
                        });

                        runner.test("with no argument", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertEqual(1, git.run(GitParameters.create()).await());
                        });

                        runner.test("with --version", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run(GitParameters.create().addArgument("--version")).await();
                            test.assertEqual(0, exitCode);
                        });

                        runner.test("with no arguments and registered output and error handlers", (Test test) ->
                        {
                            final Git git = Git.create(process);

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.run(GitParameters.create()
                                .redirectOutputTo(output)
                                .redirectErrorTo(error))
                                .await();
                            test.assertLinesEqual(
                                Iterable.create(
                                    "usage: git [-v | --version] [-h | --help] [-C <path>] [-c <name>=<value>]",
                                    "           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]",
                                    "           [-p | --paginate | -P | --no-pager] [--no-replace-objects] [--bare]",
                                    "           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]",
                                    "           [--super-prefix=<path>] [--config-env=<name>=<envvar>]",
                                    "           <command> [<args>]",
                                    "",
                                    "These are common Git commands used in various situations:",
                                    "",
                                    "start a working area (see also: git help tutorial)",
                                    "   clone     Clone a repository into a new directory",
                                    "   init      Create an empty Git repository or reinitialize an existing one",
                                    "",
                                    "work on the current change (see also: git help everyday)",
                                    "   add       Add file contents to the index",
                                    "   mv        Move or rename a file, a directory, or a symlink",
                                    "   restore   Restore working tree files",
                                    "   rm        Remove files from the working tree and from the index",
                                    "",
                                    "examine the history and state (see also: git help revisions)",
                                    "   bisect    Use binary search to find the commit that introduced a bug",
                                    "   diff      Show changes between commits, commit and working tree, etc",
                                    "   grep      Print lines matching a pattern",
                                    "   log       Show commit logs",
                                    "   show      Show various types of objects",
                                    "   status    Show the working tree status",
                                    "",
                                    "grow, mark and tweak your common history",
                                    "   branch    List, create, or delete branches",
                                    "   commit    Record changes to the repository",
                                    "   merge     Join two or more development histories together",
                                    "   rebase    Reapply commits on top of another base tip",
                                    "   reset     Reset current HEAD to the specified state",
                                    "   switch    Switch branches",
                                    "   tag       Create, list, delete or verify a tag object signed with GPG",
                                    "",
                                    "collaborate (see also: git help workflows)",
                                    "   fetch     Download objects and refs from another repository",
                                    "   pull      Fetch from and integrate with another repository or a local branch",
                                    "   push      Update remote refs along with associated objects",
                                    "",
                                    "'git help -a' and 'git help -g' list available subcommands and some",
                                    "concept guides. See 'git help <command>' or 'git help <concept>'",
                                    "to read about a specific subcommand or concept.",
                                    "See 'git help git' for an overview of the system."),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(),
                                error);
                            test.assertEqual(1, exitCode);
                        });

                        runner.test("with --version and registered output and error handlers", (Test test) ->
                        {
                            final Git git = Git.create(process);

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.run(GitParameters.create()
                                .addArgument("--version")
                                .redirectOutputTo(output)
                                .redirectErrorTo(error))
                                .await();
                            test.assertLinesEqual(
                                Iterable.create(
                                    "git version 2.37.0.windows.1"),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(),
                                error);
                            test.assertEqual(0, exitCode);
                        });
                    });

                    runner.testGroup("run(Action1<GitParameters>)", () ->
                    {
                        runner.test("with null", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.run((Action1<GitParameters>)null),
                                new PreConditionFailure("parametersSetup cannot be null."));
                        });

                        runner.test("with no setup", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertEqual(1, git.run((GitParameters parameters) -> {}).await());
                        });

                        runner.test("with --version", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final Integer exitCode = git.run((GitParameters parameters) ->
                            {
                                parameters.addArgument("--version");
                            }).await();
                            test.assertEqual(0, exitCode);
                        });

                        runner.test("with no arguments and registered output and error handlers", (Test test) ->
                        {
                            final Git git = Git.create(process);

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.run((GitParameters parameters) ->
                            {
                                parameters.redirectOutputTo(output);
                                parameters.redirectErrorTo(error);
                            }).await();
                            test.assertLinesEqual(
                                Iterable.create(
                                    "usage: git [-v | --version] [-h | --help] [-C <path>] [-c <name>=<value>]",
                                    "           [--exec-path[=<path>]] [--html-path] [--man-path] [--info-path]",
                                    "           [-p | --paginate | -P | --no-pager] [--no-replace-objects] [--bare]",
                                    "           [--git-dir=<path>] [--work-tree=<path>] [--namespace=<name>]",
                                    "           [--super-prefix=<path>] [--config-env=<name>=<envvar>]",
                                    "           <command> [<args>]",
                                    "",
                                    "These are common Git commands used in various situations:",
                                    "",
                                    "start a working area (see also: git help tutorial)",
                                    "   clone     Clone a repository into a new directory",
                                    "   init      Create an empty Git repository or reinitialize an existing one",
                                    "",
                                    "work on the current change (see also: git help everyday)",
                                    "   add       Add file contents to the index",
                                    "   mv        Move or rename a file, a directory, or a symlink",
                                    "   restore   Restore working tree files",
                                    "   rm        Remove files from the working tree and from the index",
                                    "",
                                    "examine the history and state (see also: git help revisions)",
                                    "   bisect    Use binary search to find the commit that introduced a bug",
                                    "   diff      Show changes between commits, commit and working tree, etc",
                                    "   grep      Print lines matching a pattern",
                                    "   log       Show commit logs",
                                    "   show      Show various types of objects",
                                    "   status    Show the working tree status",
                                    "",
                                    "grow, mark and tweak your common history",
                                    "   branch    List, create, or delete branches",
                                    "   commit    Record changes to the repository",
                                    "   merge     Join two or more development histories together",
                                    "   rebase    Reapply commits on top of another base tip",
                                    "   reset     Reset current HEAD to the specified state",
                                    "   switch    Switch branches",
                                    "   tag       Create, list, delete or verify a tag object signed with GPG",
                                    "",
                                    "collaborate (see also: git help workflows)",
                                    "   fetch     Download objects and refs from another repository",
                                    "   pull      Fetch from and integrate with another repository or a local branch",
                                    "   push      Update remote refs along with associated objects",
                                    "",
                                    "'git help -a' and 'git help -g' list available subcommands and some",
                                    "concept guides. See 'git help <command>' or 'git help <concept>'",
                                    "to read about a specific subcommand or concept.",
                                    "See 'git help git' for an overview of the system."),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(),
                                error);
                            test.assertEqual(1, exitCode);
                        });

                        runner.test("with --version and registered output and error handlers", (Test test) ->
                        {
                            final Git git = Git.create(process);

                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.run((GitParameters parameters) ->
                            {
                                parameters.addArgument("--version");
                                parameters.redirectOutputTo(output);
                                parameters.redirectErrorTo(error);
                            }).await();
                            test.assertLinesEqual(
                                Iterable.create(
                                    "git version 2.37.0.windows.1"),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(),
                                error);
                            test.assertEqual(0, exitCode);
                        });
                    });

                    runner.test("version()", (Test test) ->
                    {
                        final Git git = Git.create(process);
                        final VersionNumber version = git.version().await();
                        test.assertNotNull(version);
                        test.assertEqual(
                            VersionNumber.create()
                                .setMajor(2)
                                .setMinor(37)
                                .setPatch(0)
                                .setSuffix(".windows.1"),
                            version);
                    });

                    runner.testGroup("clone(GitCloneParameters)", () ->
                    {
                        runner.test("with null", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.clone((GitCloneParameters)null),
                                new PreConditionFailure("parameters cannot be null."));
                        });

                        runner.test("with no repository", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final GitCloneParameters parameters = GitCloneParameters.create()
                                .redirectOutputTo(output)
                                .redirectErrorTo(error);
                            final Integer exitCode = git.clone(parameters).await();
                            test.assertLinesEqual(
                                Iterable.create(),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(
                                    "fatal: You must specify a repository to clone.",
                                    "",
                                    "usage: git clone [<options>] [--] <repo> [<dir>]",
                                    "",
                                    "    -v, --verbose         be more verbose",
                                    "    -q, --quiet           be more quiet",
                                    "    --progress            force progress reporting",
                                    "    --reject-shallow      don't clone shallow repository",
                                    "    -n, --no-checkout     don't create a checkout",
                                    "    --bare                create a bare repository",
                                    "    --mirror              create a mirror repository (implies bare)",
                                    "    -l, --local           to clone from a local repository",
                                    "    --no-hardlinks        don't use local hardlinks, always copy",
                                    "    -s, --shared          setup as shared repository",
                                    "    --recurse-submodules[=<pathspec>]",
                                    "                          initialize submodules in the clone",
                                    "    --recursive ...       alias of --recurse-submodules",
                                    "    -j, --jobs <n>        number of submodules cloned in parallel",
                                    "    --template <template-directory>",
                                    "                          directory from which templates will be used",
                                    "    --reference <repo>    reference repository",
                                    "    --reference-if-able <repo>",
                                    "                          reference repository",
                                    "    --dissociate          use --reference only while cloning",
                                    "    -o, --origin <name>   use <name> instead of 'origin' to track upstream",
                                    "    -b, --branch <branch>",
                                    "                          checkout <branch> instead of the remote's HEAD",
                                    "    -u, --upload-pack <path>",
                                    "                          path to git-upload-pack on the remote",
                                    "    --depth <depth>       create a shallow clone of that depth",
                                    "    --shallow-since <time>",
                                    "                          create a shallow clone since a specific time",
                                    "    --shallow-exclude <revision>",
                                    "                          deepen history of shallow clone, excluding rev",
                                    "    --single-branch       clone only one branch, HEAD or --branch",
                                    "    --no-tags             don't clone any tags, and make later fetches not to follow them",
                                    "    --shallow-submodules  any cloned submodules will be shallow",
                                    "    --separate-git-dir <gitdir>",
                                    "                          separate git dir from working tree",
                                    "    -c, --config <key=value>",
                                    "                          set config inside the new repository",
                                    "    --server-option <server-specific>",
                                    "                          option to transmit",
                                    "    -4, --ipv4            use IPv4 addresses only",
                                    "    -6, --ipv6            use IPv6 addresses only",
                                    "    --filter <args>       object filtering",
                                    "    --also-filter-submodules",
                                    "                          apply partial clone filters to submodules",
                                    "    --remote-submodules   any cloned submodules will use their remote-tracking branch",
                                    "    --sparse              initialize sparse-checkout file to include only files at root",
                                    ""),
                                error);
                            test.assertEqual(129, exitCode);
                        });

                        runner.test("with repository but no directory",
                            (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                            (Test test, Folder tempFolder) ->
                        {
                            process.getFileSystem().createFolder(tempFolder.getPath()).await();

                            final Git git = Git.create(process);
                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final GitCloneParameters parameters = GitCloneParameters.create()
                                .setWorkingFolder(tempFolder)
                                .addRepository("https://github.com/github/choosealicense.com")
                                .redirectOutputTo(output)
                                .redirectErrorTo(error);
                            final Integer exitCode = git.clone(parameters).await();
                            test.assertLinesEqual(
                                Iterable.create(),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(
                                    "Cloning into 'choosealicense.com'..."),
                                error);
                            test.assertEqual(0, exitCode);
                        });
                    });

                    runner.testGroup("clone(Action1<GitCloneParameters>)", () ->
                    {
                        runner.test("with null", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            test.assertThrows(() -> git.clone((Action1<GitCloneParameters>)null),
                                new PreConditionFailure("parametersSetup cannot be null."));
                        });

                        runner.test("with no repository", (Test test) ->
                        {
                            final Git git = Git.create(process);
                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.clone((GitCloneParameters parameters) ->
                            {
                                parameters.redirectOutputTo(output);
                                parameters.redirectErrorTo(error);
                            }).await();
                            test.assertLinesEqual(
                                Iterable.create(),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(
                                    "fatal: You must specify a repository to clone.",
                                    "",
                                    "usage: git clone [<options>] [--] <repo> [<dir>]",
                                    "",
                                    "    -v, --verbose         be more verbose",
                                    "    -q, --quiet           be more quiet",
                                    "    --progress            force progress reporting",
                                    "    --reject-shallow      don't clone shallow repository",
                                    "    -n, --no-checkout     don't create a checkout",
                                    "    --bare                create a bare repository",
                                    "    --mirror              create a mirror repository (implies bare)",
                                    "    -l, --local           to clone from a local repository",
                                    "    --no-hardlinks        don't use local hardlinks, always copy",
                                    "    -s, --shared          setup as shared repository",
                                    "    --recurse-submodules[=<pathspec>]",
                                    "                          initialize submodules in the clone",
                                    "    --recursive ...       alias of --recurse-submodules",
                                    "    -j, --jobs <n>        number of submodules cloned in parallel",
                                    "    --template <template-directory>",
                                    "                          directory from which templates will be used",
                                    "    --reference <repo>    reference repository",
                                    "    --reference-if-able <repo>",
                                    "                          reference repository",
                                    "    --dissociate          use --reference only while cloning",
                                    "    -o, --origin <name>   use <name> instead of 'origin' to track upstream",
                                    "    -b, --branch <branch>",
                                    "                          checkout <branch> instead of the remote's HEAD",
                                    "    -u, --upload-pack <path>",
                                    "                          path to git-upload-pack on the remote",
                                    "    --depth <depth>       create a shallow clone of that depth",
                                    "    --shallow-since <time>",
                                    "                          create a shallow clone since a specific time",
                                    "    --shallow-exclude <revision>",
                                    "                          deepen history of shallow clone, excluding rev",
                                    "    --single-branch       clone only one branch, HEAD or --branch",
                                    "    --no-tags             don't clone any tags, and make later fetches not to follow them",
                                    "    --shallow-submodules  any cloned submodules will be shallow",
                                    "    --separate-git-dir <gitdir>",
                                    "                          separate git dir from working tree",
                                    "    -c, --config <key=value>",
                                    "                          set config inside the new repository",
                                    "    --server-option <server-specific>",
                                    "                          option to transmit",
                                    "    -4, --ipv4            use IPv4 addresses only",
                                    "    -6, --ipv6            use IPv6 addresses only",
                                    "    --filter <args>       object filtering",
                                    "    --also-filter-submodules",
                                    "                          apply partial clone filters to submodules",
                                    "    --remote-submodules   any cloned submodules will use their remote-tracking branch",
                                    "    --sparse              initialize sparse-checkout file to include only files at root",
                                    ""),
                                error);
                            test.assertEqual(129, exitCode);
                        });

                        runner.test("with repository but no directory",
                            (TestResources resources) -> Tuple.create(resources.getTemporaryFolder()),
                            (Test test, Folder tempFolder) ->
                        {
                            process.getFileSystem().createFolder(tempFolder.getPath()).await();

                            final Git git = Git.create(process);
                            final InMemoryCharacterToByteStream output = InMemoryCharacterToByteStream.create();
                            final InMemoryCharacterToByteStream error = InMemoryCharacterToByteStream.create();
                            final Integer exitCode = git.clone((GitCloneParameters parameters) ->
                            {
                                parameters.setWorkingFolder(tempFolder);
                                parameters.addRepository("https://github.com/github/choosealicense.com");
                                parameters.redirectOutputTo(output);
                                parameters.redirectErrorTo(error);
                            }).await();
                            test.assertLinesEqual(
                                Iterable.create(),
                                output);
                            test.assertLinesEqual(
                                Iterable.create(
                                    "Cloning into 'choosealicense.com'..."),
                                error);
                            test.assertEqual(0, exitCode);
                        });
                    });
                });
            }
        });
    }
}
