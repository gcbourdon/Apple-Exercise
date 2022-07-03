# Apple-Exercise
Apple programming interview exercise

Java 18

Used Spring framework with Tomcat server

TO BUILD:
- make sure to have maven downloaded and unzipped
- set the maven folder path in the PATH environment variable
- make sure to have java jdk-18.0.1.1 downloaded
- set the JAVA_HOME environment variable to the path of the jdk
- running 'mvn -v' should return the current version of maven running locally
- open port 8080 on the local machine
- cd into Apple-Exercise/exercise (where the pom file is located)
- run 'mvn clean install'. If this does not build successfully, from my experience either the wrong version of jdk or maven cannot be found in the environment
- if the jar is built successfully, it should be in the target folder of the project
- finally, to run the application locally use 'mvn spring-boot:run' in the /Apple-Exercise/exercise directory
- the server will run (if port 8080) is available, and the endpoints can be reached at 'http://localhost:8080/'

API CONSUMPTION:

- endpoint -> http://localhost:8080/PushAndRecalculate 
        An HTTP POST request which requires a JSON payload in the body with structure:
        { "value": number }

- endpoint -> http://localhost:8080/PushRecalculateAndEncrypt 
        An HTTP POST request which requires a JSON payload in the body with structure:
        { "value": number }

- endpoint -> http://localhost:8080/Decrypt 
        An HTTP GET request which requires a JSON payload in the body with structure: 
        { "cipher": "encrypted text" }


ASSUMPTIONS:
  
DESIGN DECISIONS: 

- In the instruction spec, it says to provision 1 key for the system. However, it does does not specify the type of algorithm parameters to use and/or if I should use   the same  initialization vector to ensure idempotency between the encrypt/decrypt interface. I decided to generate a different IV for my algorithm parameters to    inititialze the AES cipher with the secret key and different IV for each unique execution of the app.

- Used floating point values to have more than 2 decimal points precision in output

- Used longs for the count and value variables to ensure enough space to store in a single variable 

- For the post mapping, since the methods are not idempotent (do not return same value for same input) it was not appropriate to use path variables. Therefore, I decided to use a Map<String, Integer> body in the request



