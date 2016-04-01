
#MaduraDocs#
	

MaduraDocs generates a PDF file from an XML source file. It is a [maven](https://maven.apache.org/) plugin, so it can be added to a maven project and the PDF will be generated as part of the build (the package phase). The advantages of generating documents this way are:

 * You can store the source document in your source repository and control its versioning in the same way you do it for source code, including comparing versions etc. This is not really possible for formats like Word and OpenOffice.
 * Because they are just XML documents in a source control system they can be edited by multiple people at the same time, which often happens when a document needs to be reviewed by several people, and the changes merged efficiently.
 * Changes to the document can be tracked by source control, including commit comments, and these an be automatically built into the document history which is tedious to manage otherwise.
 * Injecting things like the build number or version number into the document, changing company name etc, is easy to do because they are injected as parameters.

It looks rather like [DocBook](http://docbook.org/), but it is different enough. You would not use both this and DocBook though, you would pick just one of them.