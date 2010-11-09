@echo off
title Aion-Unique Chat Server Console
:start
echo Starting Aion-Unique Chat Server.
echo.
REM -------------------------------------
REM Default parameters for a basic server.
java -Xms128m -Xmx128m -ea -cp ./libs/*;chatserver-0.1.jar com.aionemu.chatserver.ChatServer
REM
REM -------------------------------------

SET CLASSPATH=%OLDCLASSPATH%


if ERRORLEVEL 1 goto error
goto end
:error
echo.
echo Chat Server Terminated Abnormaly, Please Verify Your Files.
echo.
:end
echo.
echo Chat Server Terminated.
echo.
pause