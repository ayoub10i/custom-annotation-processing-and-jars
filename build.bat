javac ToStringProcessor.java
pause
jar cvf lib.jar ToStringProcessor.class ToString.class
del *.class
pause
javac -cp lib.jar -processor ToStringProcessor Ayoub.java
pause
jar uf lib.jar Ayoub.class AyoubGenerated.class
del *.class
jar tf lib.jar
pause
javac -cp lib.jar Main.java
pause
java -cp ;lib.jar Main
pause
echo Main-class: Main > manifest.txt
jar umf manifest.txt lib.jar
del manifest.txt
jar uf lib.jar Main.class
del Main.class
java -jar lib.jar
pause
