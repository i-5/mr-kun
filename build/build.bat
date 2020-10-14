@echo off
REM convience bat file to build with

if not "%JAVA_HOME%" == "" goto gotJavaHome
echo You must set the JAVA_HOME environment variable to point at your JDK
goto finish
:gotJavaHome

if not "%WL_HOME%" == "" goto gotWeblogicHome
echo You must set the WL_HOME environment variable to point at your Weblogic home
goto finish
:gotWeblogicHome

if not "%WL_SERVER%" == "" goto gotWLServer
echo You must set the WL_SERVER environment variable to point at your Weblogic server instance
goto finish
:gotWLServer

set _ANT_HOME=%ANT_HOME%
set ANT_HOME=ant

set _ANT_OPTS=%ANT_OPTS%
set ANT_OPTS=-Dwl_home=%WL_HOME% -Dwl_server=%WL_SERVER% %_ANT_OPTS%

set _CLASSPATH=%CLASSPATH%
set CLASSPATH=%ANT_HOME%\lib\ant.jar;%ANT_HOME%\lib\jaxp.jar;%ANT_HOME%\lib\parser.jar
set CLASSPATH=%CLASSPATH%;%ANT_HOME%\lib\optional.jar;%JAVA_HOME%\lib\tools.jar

java %ANT_OPTS% org.apache.tools.ant.Main %1 %2 %3 %4 %5 %6 %7 %8 %9

:clean

rem clean up classpath after
set CLASSPATH=%_CLASSPATH%
set _CLASSPATH=
set ANT_HOME=%_ANT_HOME%
set _ANT_OPTS=
set ANT_OPTS=%_ANT_OPTS%
set _ANT_HOME=
:finish
