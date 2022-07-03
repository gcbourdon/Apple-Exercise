# Apple-Exercise
Apple programming interview exercise

Java 18.0.1.1
Spring framework with Tomcat server

TO BUILD:

- make sure to have maven downloaded and unzipped
- set the maven folder path in the PATH environment variable
- download the latest java jdk-18.0.1.1
- set the JAVA_HOME environment variable to the path of the new jdk
- running 'mvn -v' should return the current version of maven running locally
- open port 8080 locally
- cd into Apple-Exercise/exercise (where the pom file is located)
- run 'mvn clean install'. From my experience, if this does not build successfully, either the wrong version of jdk or maven cannot be found in the environment
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

- 
  
DESIGN DECISIONS: 

- In the instruction spec, it says to provision 1 key for the system. However, it does does not specify the type of algorithm parameters to use and/or if I should use   the same  initialization vector to ensure idempotency between the encrypt/decrypt interface. I decided to generate a different IV for my algorithm parameters to inititialze the AES cipher with the secret key and different IV for each unique execution of the app.

- I decided to use 256 bit AES encryption to generate a secret key which is stored in the application.properties file. I also used base64 encoding scheme to transmit the information that is encrypted using this key. 

- In the spec, the input value for the APIs says a single number and does not specify an integer. For this reason, I decided to use floats for the input in the API request body.

- I used floating point values for the running summations, averages, and standard deviations to have more than 2 decimal points precision in the output. I also formatted each output to 3 decimal places if they need to be rounded. 

- In order to model the data appropriately based on the example input/output, I decided to create a class which contains the plain text pair of statistics (avg, stddev) and called that PlainTextStatistics. I also created another class EncryptedStatistics to model the pair of encrypted avg and stddev. Either value in the encrypted statistics pair can then be passed into the Decrypt API to get the actual single value.

- I used long for the count variable because the spec says to account for an arbitrary large input set. 

- Due to the RAM constraints noted in the spec (C), I did not store the input values in a data structure. Instead, I kept a running summation of the values and another summation of the values squared. With these summations and the count of all input numbers so far, I was able to calculate the running avg and stddev. 

- Due to the methods not being idempotent (do not return same value for same input) it was not appropriate to use path variables. Therefore, I decided to use a Map<String, Float> body in the POST requests, and Map<String, String> body in the GET request to decrypt.


REFERENCES:

- I used the following sources to get a better understanding of symmetric encryption in Java and how to use the various encryption algorithms:          

https://www.geeksforgeeks.org/symmetric-encryption-cryptography-in-java/

https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html

https://en.wikipedia.org/wiki/Initialization_vector

https://medium.com/swlh/an-introduction-to-the-advanced-encryption-standard-aes-d7b72cc8de97


- I used the following source to get familiar with the math behind calculating running average and standard deviations:

https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance


