:: Build the jar.
del build.txt
del assembly.txt
call mvn -Dmaven.test.skip=true -f proliferation/pom.xml clean install -X> build.txt
call mvn -f proliferation/proliferation-cohort/pom.xml assembly:single -X> assembly.txt