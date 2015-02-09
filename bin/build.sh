#!/bin/bash

# run this script from this folder (bin)

cd ..
javac -g -cp src -d build/packages -Xlint:unchecked src/titan/Main.java

javac -g -cp src -d build/packages -Xlint:unchecked src/titan/test/*.java
#copy all the assets
cp src/assets/* build/packages/assets/

cd bin
