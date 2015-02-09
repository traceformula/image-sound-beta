#!/bin/bash

# run this script from this folder (bin)

# first make sure the generated parser is up to date
cd ..
javac -g -cp src -d build/packages -Xlint:unchecked src/titan/Main.java

javac -g -cp src -d build/packages -Xlint:unchecked src/titan/test/*.java
#copy all the assets
cp src/assets/* build/packages/assets/

cp manifest.txt build/packages/manifest.txt

cd build/packages

jar cvfm image-sound-conversion.jar manifest.txt titan/Main.class titan/*

cd ../../
cd bin
