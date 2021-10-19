for d in ui collider  controller  gameobjects playground  ui rendering ; do rm src/${d}/*.class; done

javac -cp src src/${1}.java
java -cp src ${1}

