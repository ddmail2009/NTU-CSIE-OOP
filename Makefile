class_path = ntu/csie/oop13spring/
package_name = ntu.csie.oop13spring

arena = Arena
pet = Pet_Isaac Pet_Foe
other = Coordinate Skills
allfile = $(other) $(arena) $(pet) 
allclass = $(addsuffix .class, $(allfile))


all: $(addprefix $(class_path), $(allclass))
	java -jar hw4.jar $(addprefix $(package_name)., $(arena)) $(addprefix $(package_name)., $(pet))

$(class_path)%.class: src/$(class_path)%.java 
	javac -cp hw4.jar -sourcepath src -d . $<
