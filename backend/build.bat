@echo off
REM Build script for Spring Boot project with Java 17
REM Usage: Run this file from the project directory

setlocal enabledelayedexpansion

REM Set Java 17 path
set JAVA_HOME=C:\Users\cuongmanh.vu\.jdks\ms-17.0.18
set PATH=!JAVA_HOME!\bin;!PATH!

REM Change to project directory
cd /d "D:\Book\ocp\baihoc\BaiHoc"

echo.
echo ╔════════════════════════════════════════════════════════════════════════════╗
echo ║                  Building Project with Java 17                            ║
echo ╚════════════════════════════════════════════════════════════════════════════╝
echo.

REM Display Java version
echo Checking Java version...
java -version
echo.

REM Build project
echo Building project...
echo ════════════════════════════════════════════════════════════════════════════
echo.

call mvn clean install -DskipTests -T 1C

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ════════════════════════════════════════════════════════════════════════════
    echo. Build successful!
    echo ════════════════════════════════════════════════════════════════════════════
    echo.
    echo Now you can run the application with:
    echo   mvn spring-boot:run
    echo.
    echo Or run the JAR file:
    echo   java -jar target/BaiHoc-1.0-SNAPSHOT.jar
    echo.
) else (
    echo.
    echo ════════════════════════════════════════════════════════════════════════════
    echo. Build failed! (Exit code: %ERRORLEVEL%)
    echo ════════════════════════════════════════════════════════════════════════════
    echo.
)

pause

