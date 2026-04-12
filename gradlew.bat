@ECHO OFF
SET DIR=%~dp0
IF "%DIR%"=="" SET DIR=.
SET APP_BASE_NAME=%~n0
SET APP_HOME=%DIR%
"%JAVA_HOME%\bin\java.exe" -version >NUL 2>&1
IF %ERRORLEVEL% EQU 0 GOTO execute
SET JAVA_EXE=java.exe
GOTO run
:execute
SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
:run
"%JAVA_EXE%" -Dorg.gradle.appname=%APP_BASE_NAME% -classpath "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*