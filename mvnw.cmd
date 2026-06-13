@echo off
set JAVA_HOME=C:\Users\Jemy\.jdks\azul-17.0.10
"%JAVA_HOME%\bin\java.exe" -cp .mvn\wrapper\maven-wrapper.jar org.apache.maven.wrapper.MavenWrapperMain %*
