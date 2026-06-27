@echo off
setlocal

set "ROOT=%~dp0"
set "OUT_DIR=%ROOT%out"
set "JAR_DIR=%ROOT%dist"
set "JAR_FILE=%JAR_DIR%\pvz-java.jar"

if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
if not exist "%JAR_DIR%" mkdir "%JAR_DIR%"
mkdir "%OUT_DIR%"

javac -d "%OUT_DIR%" "%ROOT%src\pvz\*.java"
if errorlevel 1 (
    echo Build failed during compilation.
    exit /b 1
)

jar cfe "%JAR_FILE%" pvz.Main -C "%OUT_DIR%" .
if errorlevel 1 (
    echo Build failed during jar creation.
    exit /b 1
)

echo Built "%JAR_FILE%"
