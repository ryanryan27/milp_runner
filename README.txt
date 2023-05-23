If java and javac are not in your $PATH environment variable, you will need to modift the run.bat and compile.bat files to point them to your jdk or jre install directories.
You will also need to update the CPLEX install path in run.bat

If you don't have cplex installed UGV will still run, however you won't have access to the domination solvers.

Examples of these file locations:
"c:\Program Files\Java\jdk1.8.0_191\bin\java.exe"
"c:\Program Files\Java\jdk1.8.0_191\bin\javac.exe"
"C:\Program Files\IBM\ILOG\CPLEX_Studio129\cplex\bin\x64_win64"