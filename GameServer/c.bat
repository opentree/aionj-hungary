@echo off
REM ------------------------------------------------------------------
REM Author : Mystick, Based on Resp@wner (L2j)
REM Last modified : 04/08/2010
REM ------------------------------------------------------------------
REM ------------------------------------------------------------------
REM Change path without drive letter where projects are
set pom.xmlGSPath=AL-Game
set pom.xmlLSPath=AL-Login
set build.xmlCMPath=AL-Commons
REM ------------------------------------------------------------------

echo You have to edit this script to match your project folder
echo.
echo What do you want to do ?
echo For build project type b
echo For clean project type c
echo For quit the script's execution type q
echo.
:asktype1
set projecttype=x
set /p buildproject=Build project : b  / Clean project : c ?
if /i %buildproject%==b goto build
if /i %buildproject%==c goto clean
if /i %buildproject%==q goto end
goto asktype1

:build
echo Choose the project you want to build.
echo c for Commons (First step)
echo g for GameServer
echo l for LoginServer
echo q for quit
echo.
:asktype2
set buildproject=x
set /p buildproject= Commons install type : c / GameServer build type : g  / LoginServer build type : l  ?
if /i %buildproject%==g goto gsbuild
if /i %buildproject%==l goto lsbuild
if /i %buildproject%==c goto cmbuild
if /i %buildproject%==q goto end
goto asktype2

:gsbuild
echo This script allow to automatise the compilation of the GameServer
echo with Maven in command line.
echo.
echo Moving to project folder (%pom.xmlGSPath%)
cd %pom.xmlGSPath%
echo.
echo Building project
mvn assembly:assembly -Dmaven.test.skip=true
pause

:lsbuild
echo This script allow to automatise the compilation of the LoginServer
echo with Maven in command line.
echo.
echo Moving to project folder (%pom.xmlLSPath%)
cd %pom.xmlLSPath%
echo.
echo Building project
mvn assembly:assembly -Dmaven.test.skip=true
pause

:cmbuild
echo This script allow to automatise the compilation of the Commons
echo with Maven in command line.
echo.
echo Moving to project folder (%build.xmlCMPath%)
cd %build.xmlCMPath%
echo.
echo Installing project
mvn install
pause

:clean
echo Choose the project you want to clean.
echo g for GameServer
echo l for LoginServer
echo c for Commons
echo q for quit
echo.
:asktype3
set cleanproject=x
set /p cleanproject= Commons : c / GameServer clean type : g  / LoginServer clean type : l ?
if /i %cleanproject%==g goto gsclean
if /i %cleanproject%==l goto lsclean
if /i %cleanproject%==c goto cmclean
if /i %cleanproject%==q goto end
goto asktype3

:gsclean
echo This script allow to automatise the clean of the GameServer
echo with Maven in command line.
echo.
echo Moving to project folder (%pom.xmlGSPath%)
cd %pom.xmlGSPath%
echo.
echo cleaning project
mvn clean
deltree target

:lsclean
echo This script allow to automatise the clean of the LoginServer
echo with Maven in command line.
echo.
echo Moving to project folder (%pom.xmlLSPath%)
cd %pom.xmlLSPath%
echo.
echo Cleaning project
mvn clean
deltree target

:cmclean
echo This script allow to automatise the cleaning of the DataPack
echo with Ant in command line.
echo.
echo Moving to project folder (%build.xmlDPPath%)
cd %build.xmlDPPath%
echo.
echo Cleaning project
ant clean

:end
echo.
echo Script complete.
pause
