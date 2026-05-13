@echo off
REM Run script for Spring Boot application with Java 17

setlocal enabledelayedexpansion

REM Set Java 17 path
set JAVA_HOME=C:\Users\cuongmanh.vu\.jdks\ms-17.0.18
set PATH=!JAVA_HOME!\bin;!PATH!

REM Change to backend directory
cd /d "%~dp0"

echo.
echo ╔════════════════════════════════════════════════════════════════════════════╗
echo ║                  Running Spring Boot Application                          ║
echo ║                      with Java 17                                         ║
echo ╚════════════════════════════════════════════════════════════════════════════╝
echo.

echo Checking Java version...
java -version
echo.

echo Starting application...
echo ════════════════════════════════════════════════════════════════════════════
echo.

REM Run Spring Boot application
call mvn spring-boot:run

pause

