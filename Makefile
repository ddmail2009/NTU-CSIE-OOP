.PHONY: compile run

all:
	make compile 
	make run

compile:
	javac -d class src/*.java
	jar cvf hw5.jar -C class .
	rm class || del class

run:
	echo "<applet code='CarSimulationJApplet.class' archive='hw5.jar' width='800' height='600'></applet>" > test.html
	appletviewer test.html