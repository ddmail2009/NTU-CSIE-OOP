.PHONY: compile run

all:
	make compile 
	make run

compile:
	javac -d class src/*.java
	jar cvf hw5.jar -C class .

run:
	echo "<applet code='CarSimulationJApplet.class' archive='hw5.jar' width='800' height='600'></applet>" > index.html
	appletviewer test.html