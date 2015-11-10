JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		Base64.java \
		ClassificationNode.java \
		Database.java \
		DatabaseClassifier.java \
		getWordsLynx.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class