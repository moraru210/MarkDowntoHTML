# MarkDowntoHTML

> This is a project I've made to convert any given MarkDown file to an HTML file and basically
create a static based web page. The idea came during the C final project in my first year of
MEng Computing at Imperial College of London, where we built a static site generator, but
due to the complications of using C, we resulted to using library 'CMARK' to do all the 
MarkDown translation. During the summer I realised that I wanted to create my own instead
of using a library, and hence why I did it in Java.

So far, my program is able to handle:
- *Italics* and **bold** text
- Headers
- Ordered lists
- Unordered lists
- Paragraphs
- Links

However, i would like to further implement:
- Lists inside a list (nested lists)
- Images

To run the program, you must run **java Main MarkDownFile.md WebFile.html WebPageTitle**