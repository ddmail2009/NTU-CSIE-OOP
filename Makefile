FILE=POOArticle POOBoard POODirectory  POOBBS
CLASSFILE=$(addsuffix .class, $(FILE))
SOURCEFILE=$(addsuffix .java, $(FILE))

.PHONY: all

all: $(addprefix class/, $(CLASSFILE))
	
run: $(addprefix class/, $(CLASSFILE))
	java -cp class POOBBS

class/%.class: src/%.java
	javac -d class -classpath class -sourcepath src $<
