@ECHO OFF

REM I may need a global flag variable that determines if it is necessary for me to save and export
REM Variables for File/Folder Locations
SET BookSRCs=src/book/*.java
SET LibrarySRCs=src/library/*.java
SET MenuSRCs=src/menu/*.java
SET mainSRC=src/Main.java
SET JavaDocDir=docs/javadoc

REM Move up to the root directory
CD ..

REM 1) Compile everything into the bin folder
ECHO Compiling Java source files...
javac -d bin %BookSRCs% %LibrarySRCs% %MenuSRCs% %mainSRC%

REM Use ErrorLevel to catch compilation issues immediately - This if-statement was copied from the internet. Not my own work.
IF %ERRORLEVEL% NEQ 0 (
    ECHO [!] Java compilation failed. Analyse compiler error messages!
    PAUSE
    EXIT /B %ERRORLEVEL%
)

REM 2) Generate Javadoc
REM -d: destination folder | -Xdoclint:none => For muting JavaDoc warnings | -author & -version: includes those tags
ECHO Generating documentation...
javadoc -Xdoclint:none -d %JavaDocDir% -author -version %BookSRCs% %LibrarySRCs% %MenuSRCs% %mainSRC%

REM 3) Run the Java program
ECHO Launching Application...
CLS
java -cp bin Main

REM 4) Run the Python export script using the specific 'py' launcher -- Should handle execution if I run into errors
ECHO Exporting data to Excel...
py -3.14 scripts/exportToExcel.py

ECHO Process Finished.
PAUSE