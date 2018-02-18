@echo off
ECHO "copy resources"
xcopy src\lib build\lib /E /Y /I
xcopy manifest.txt build /Y
ECHO "resources copied"
ECHO "compile sources"
javac -d build -sourcepath src -classpath "build;src\lib\commons-compress-1.16.1.jar;src\lib\xz-1.8.jar"  src\ru\ketty\Main.java
ECHO "sources compiled"
ECHO "create jar"
cd build
jar cfm Statistic.jar manifest.txt -C ./ .
cd ..\
ECHO "jar created"