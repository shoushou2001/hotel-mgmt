@echo off
set JAVA_HOME=C:\Users\Jemy\.jdks\azul-17.0.10
set MVN_HOME=C:\Users\Jemy\.m2\wrapper\dists\apache-maven-3.9.5-bin\32db9c34\apache-maven-3.9.5
set PATH=%JAVA_HOME%\bin;%MVN_HOME%\bin;%PATH%
mvn %*
