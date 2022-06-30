# Apple-Exercise
Apple programming interview exercise

SDK: 1.8
Java 18

Uses Tomcat server on Spring framework

TO BUILD:
- set the $JAVA_HOME$ environment variable to jdk-18.0.1.1
- 
- I used Java jdk-18.0.1.1 as the SDK
- cd into Apple-Exercise/exercise (where the pom file is located)
- run 'mvn clean install'


DESIGN DECISIONS: 

- Used floating point values to have more than 2 decimal points precision in output

- Used longs for the count and value variables to ensure enough space to store in a single variable 

- For the post mapping, since the methods are not idempotent (do not return same value for same input) it was not appropriate to use path variables. Therefore, I decided to use a Map input 
