build:
	javac Beamdrone.java
	javac Fortificatii.java
	javac Curatare.java
run-p1:
	java -Xmx128m -Xss128m Curatare
run-p2:
	java -Xmx128m -Xss128m Fortificatii
run-p3:
	java -Xmx128m -Xss128m Beamdrone
clean:
	rm *.class