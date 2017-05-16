# VS2017 Actor

A very simple Java program demonstrating the usage of the 0MQ library (publish/subscribe pattern) and how to use the GSON library to serialize and deserialize Java objects into JSON.

Gradle is used as the build tool.

## Installing Gradle

The easiest way to install Gradle is using [sdkman](http://sdkman.io/).

## Installing the example program

Just download or clone the repository. `cd` into the directory and create the fatjar containing all the required classes including GSON and 0MQ.

    gradle shadowJar

Start the publisher

    java -jar ./build/libs/vs2017-actor-seed-1.0-shadow.jar pub tcp://localhost:5432

and finally the subscriber

    java -jar ./build/libs/vs2017-actor-seed-1.0-shadow.jar sub tcp://localhost:5432

The subscriber should print only even number

    Tags: even
    Message: {"header":{"sender":"me"},"body":{"number":"494216","even":"true"}}
    Number received was 494216
    Tags: even
    Message: {"header":{"sender":"me"},"body":{"number":"494218","even":"true"}}
    Number received was 494218