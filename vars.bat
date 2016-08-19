:: Stores come common values in variables for use with other scripts
(
set /p version=
)<version.version
echo %version%

set workbench_jar=proliferation-workbench-%version%-jar-with-dependencies.jar
set workbench_jar_abs=proliferation\proliferation-workbench\target\%workbench_jar%

set cohort_jar=proliferation-cohort-%version%-jar-with-dependencies.jar
set cohort_jar_abs=proliferation\proliferation-cohort\target\%cohort_jar%