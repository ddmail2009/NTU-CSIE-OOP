mkdir class
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/AI/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Item/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Effect/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Loader/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Object/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Overlay/*.java
javac -encoding utf8 -cp class -d class -sourcepath src src/LWC/pkgfinal/Skill/*.java
Xcopy src\img class\img /E/Y
Xcopy src\music class\music /E/Y
Xcopy src\config class\config /E/Y
@echo Main-Class: LWC.pkgfinal.LWC_Main > manifest.mf
jar cvfm OOP_FINAL.jar manifest.mf -C class .
