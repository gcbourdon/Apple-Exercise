# Apple-Exercise
Apple programming interview exercise

Java 18

Used Spring framework with Tomcat server

TO BUILD:
- make sure to have maven downloaded and unzipped
- set the maven path in the PATH environment variable
- make sure to have java jdk-18.0.1.1 downloaded
- set the JAVA_HOME environment variable to the path of the jdk
- running 'mvn -v' should return the current version of maven running locally
- open port 8080 on the local machine
- cd into Apple-Exercise/exercise (where the pom file is located)
- run 'mvn clean install'. If this does not build successfully, from my experience either the wrong version of jdk or maven is not in the path
- if the jar is built successfully, it should be in the target folder of the project
- finally, to run the application locally use 'mvn spring-boot:run' in the /Apple-Exercise/exercise directory
- the server will run (if port 8080) is available, and the endpoints can be reached at 'http://localhost:8080/'


DESIGN DECISIONS: 

- Used floating point values to have more than 2 decimal points precision in output

- Used longs for the count and value variables to ensure enough space to store in a single variable 

- For the post mapping, since the methods are not idempotent (do not return same value for same input) it was not appropriate to use path variables. Therefore, I decided to use a Map input 



