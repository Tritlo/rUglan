javadoc -docletpath TeXDoclet.jar -doclet org.stfm.texdoclet.TeXDoclet -sourcepath rUglan/src/main/java -output docs/is.mpg.ruglan.tex -title "rUglan documentation" -author "Jon Arnar Tomasson, Matthias Pall Gissurarson and Sigurdur Fannar Vilhelmsson" -package is.mpg.ruglan -tree -noindex
javadoc -docletpath TeXDoclet.jar -doclet org.stfm.texdoclet.TeXDoclet -tree -sourcepath rUglan/src/instrumentTest/java -output docs/is.mpg.ruglan.test.tex -title "rUglan documentation" -author "Jon Arnar Tomasson, Matthias Pall Gissurarson and Sigurdur Fannar Vilhelmsson" -package is.mpg.ruglan.test -noindex
