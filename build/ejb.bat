@ECHO OFF
@REM
@REM EJB deployment automation script
@REM

if not "%WL_HOME%" == "" goto gotWeblogicHome
echo You must set the WL_HOME environment variable to point at your Weblogic home
goto finish
:gotWeblogicHome

if not "%WL_SERVER%" == "" goto gotWLServer
echo You must set the WL_SERVER environment variable to point at your Weblogic server instance
goto finish
:gotWLServer

set EJB_DIR=.\java\jp\ne\sonet\mrkun\sessionEJB
set DD_DIR=.\META-INF

set BEAN_SUFFIX=
set HOME_SUFFIX=HomeIntf
set REMOTE_SUFFIX=RemoteIntf

if "" == "%JAVA_HOME%" goto nojavahome
if "" == "%WL_HOME%" goto nowlhome
if "" == "%WL_SERVER%" goto nowlserver

if exist .\build goto buildexists

if "%1" == "" goto usage

set FILECHECK=%DD_DIR%\%1\ejb-jar.xml
if not exist %FILECHECK% goto nofilecheck

set FILECHECK=%EJB_DIR%\%1%BEAN_SUFFIX%.java
if not exist %FILECHECK% goto nofilecheck

set FILECHECK=%EJB_DIR%\%1%HOME_SUFFIX%.java
if not exist %FILECHECK% goto nofilecheck

set FILECHECK=%EJB_DIR%\%1%REMOTE_SUFFIX%.java
if not exist %FILECHECK% goto nofilecheck

set MYSERVER=%WL_HOME%\%WL_SERVER%

set MYCLASSPATH=%JAVA_HOME%\jre\lib\rt.jar;%WL_HOME%\classes;%WL_HOME%\lib\weblogicaux.jar;%WL_HOME%\%WL_SERVER%\serverclasses;%WL_HOME%\%WL_SERVER%\ejbclasses;%WL_HOME%\%WL_SERVER%\servletclasses;

set DEPLOYMENT_DIR=%WL_HOME%\%WL_SERVER%\ejb
set JAR_FILE=%1.jar
set TEMP_JAR_FILE=%1.temp.jar
echo Classpath is %MYCLASSPATH%
echo Environment is set up...

@REM Create the build directory, and copy the deployment descriptors into it
if not exist .\build mkdir .\build
if not exist .\build\META-INF mkdir .\build\META-INF
copy %DD_DIR%\%1\*.xml build\META-INF
echo Created temporary subdirectories...

@REM Compile ejb classes into the build directory (jar preparation)
echo Compiling .java files...
javac -g -classpath %MYCLASSPATH% -d build %EJB_DIR%\%1%BEAN_SUFFIX%.java %EJB_DIR%\%1%HOME_SUFFIX%.java %EJB_DIR%\%1%REMOTE_SUFFIX%.java
if errorlevel 1 goto javacfailed
echo Done compiling .java files...

@REM jar everything in the build directory
echo Beginning jarring...
jar cv0f %TEMP_JAR_FILE% -C .\build .
if errorlevel 1 goto jarfailed
echo Done jarring...

@REM Run ejbc to create the deployable jar file
echo Running ejbc...
java -classpath %MYCLASSPATH% -Dweblogic.home=%WL_HOME% weblogic.ejbc -compiler javac %TEMP_JAR_FILE% %DEPLOYMENT_DIR%\%JAR_FILE%
if errorlevel 1 goto ejbcfailed
echo Done running ejbc...
echo Created %JAR_FILE% in %DEPLOYMENT_DIR%\...

@REM Optionally copy the file to the current directory.  Commented out for now.
@REM copy %DEPLOYMENT_DIR%\%JAR_FILE% .\%JAR_FILE%
@REM echo Copied %JAR_FILE% to current directory...

@REM If we got this far, we can call cleanup and end
goto cleanup

@REM Error Messages which do not require cleanup ###########################################################

goto end
:usage
echo.
echo Usage:  ejb BeanName
echo.
echo The source files and xml files for the EJB should be in the current directory.
echo This command will not operate if a .\build directory exists.
echo It will place the EJB in the %WL_HOME%\%WL_SERVER%\ejb directory.
echo.
echo Example, for a bean comprised of the files:
echo.
echo		Test.java, TestHome.java, TestBean.java
echo.
echo You would type:
echo.
echo		ejb Test
echo.

goto end
:buildexists
echo.
echo This command will delete the entire dirctory tree .\build.
echo You should move any files in this directory tree elsewhere before running this command.
echo.

goto end
:nojavahome
echo.
echo You must set the JAVA_HOME environment variable to point to your JDK.
echo.

goto end
:noj2eehome
echo.
echo You must set the J2EE_HOME environment variable to point to your Enterprise JDK.
echo.

goto end
:nowlhome
echo.
echo You must set the WL_HOME environment variable to point to your Weblogic installation.
echo.

goto end
:nowlserver
echo.
echo You must set the WL_SERVER environment variable to point to your Weblogic server instance.  This is probably 'myserver' or 'yournameserver'.
echo.

goto end
:nofilecheck
echo.
echo Cannot find file %FILECHECK%.
echo.

@REM Error Messages which do not require cleanup ###########################################################

goto end
:javacfailed
echo.
echo There were compilation errors during the javac compilation.
echo.
goto cleanup

goto end
:jarfailed
echo.
echo There were errors during the jar file creation.
echo.
goto cleanup

goto end
:ejbcfailed
echo.
echo There were compilation errors during the ejbc compilation.
echo.
goto cleanup

goto aftercleanup
:cleanup
@REM Remove temporary files
echo Removing temporary files...
del %TEMP_JAR_FILE%
rmdir /s /q .\build
:aftercleanup


:end

echo.

