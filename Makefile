.PHONY: compile run applet

all:
	make compile
	@echo Type "make applet" to use AppletViewer to AppletView
	@echo Type "make run" to use JFrame to view


compile:
	javac -d class src/*.java
	@echo Main-Class: CarSimulationJFrame > manifest.mf
	jar cvfm hw5.jar manifest.mf -C class . 

applet: compile
	echo "<applet code='CarSimulationJApplet.class' archive='hw5.jar' width='800' height='600'></applet>" > index.html
	appletviewer index.html

run: compile
	java -jar hw5.jar