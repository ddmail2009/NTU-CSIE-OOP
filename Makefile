


all: $(addprefix class/, POOBBS.class)
	java class/POOBBS

class/%.class: src/%.java
	javac -d class -classpath class -sourcepath src $<
