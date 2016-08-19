:: Delete any eclipse project files.
del /S *.project
del /S *.pydevproject
del /S *.classpath
mvn -f proliferation/pom.xml clean