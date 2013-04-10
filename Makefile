FILE=POOArticle POOBoard POODirectory  POOBBS
CLASSFILE=$(addsuffix .class, $(FILE))
SOURCEFILE=$(addsuffix .java, $(FILE))

.PHONY: all run doc

all: $(addprefix class/, $(CLASSFILE))
	
run: $(addprefix class/, $(CLASSFILE))
	java -cp class POOBBS

doc: $(addprefix src/, $(SOURCEFILE))
	javadoc -d doc -sourcepath src -classpath class src/*.java

class/%.class: src/%.java
	javac -d class -sourcepath src $<
