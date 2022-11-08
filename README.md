# Splendor
____
## Introduction
This is a board game called Splendor. It has a [client](#splendor) and a [server](https://github.com/clitile/SplendorServer). Before playing it, you can read the [rule](https://www.notion.so/ooohwen/Splendor-03e460cff7e14d81a0a20ed10feb0878) to learn more about it.
## How to play
Get the latest [release](https://github.com/clitile/SplendorClient/releases/tag/Splendor1.2.0)  
### Windows 10 / 11
- Download [SplendorClient_x64_win.zip](https://github.com/clitile/SplendorClient/releases/download/Splendor1.2.0/SplendorClient_x64_win.zip)
- unzip it
- run Splendor.exe
### Linux
- Download [SplendorClient_x64_linux.tar.gz](https://github.com/clitile/SplendorClient/releases/download/Splendor1.2.0/SplendorClient_x64_linux.tar.gz)
- unzip it
- open terminal in that directory
- run ```chmod +x run.sh && ./run.sh```
### MacOS
- Download [SplendorClient_x64_linux.tar.gz](https://github.com/clitile/SplendorClient/releases/download/Splendor1.2.0/SplendorClient_x64_linux.tar.gz)
- unzip it
- open terminal in that directory
- run ```chmod +x run.sh && ./run.sh```
## Build yourself
- install [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html) maven 3.8
- Download our [codes](https://github.com/clitile/SplendorClient/archive/refs/tags/Splendor1.2.0.zip)
- unzip the codes
- open terminal in the directory
- run ```mvn package``` and you will get a jar file called SplendorClient-1.0-SNAPSHOT.jar. You can download a compressed java 17 to run it anywhere.