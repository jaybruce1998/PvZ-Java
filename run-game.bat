@echo off

set /p GAME_SPEED=Enter game speed multiplier (example: 1.0): 
if "%GAME_SPEED%"=="" set "GAME_SPEED=1.0"

java -jar "pvz-java.jar" %GAME_SPEED%
