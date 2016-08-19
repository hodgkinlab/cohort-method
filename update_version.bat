:: update the version of the release
call vars.bat
call mvn -f proliferation/pom.xml versions:set -DnewVersion=%version%
call mvn -f proliferation/pom.xml versions:commit