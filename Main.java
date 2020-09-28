/*
Display a text file.
To use this program, specify the name of the file that you want to see.
For example, to see a file called test.md, use the following command:

java Scanner test.md
*/

import java.io.*;
import java.util.Scanner;

public class Main {

  private final static String START_HTML = "<!DOCTYPE html>\n<html>";
  private final static String END_HTML = "</body>\n</html>";

  public static void main(String[] args) throws IOException {

    // First, confirm that a filename has been specified.
    if (args.length != 3) {
      System.out.println("Usage: Main input.md output.html title");
      return;
    }

    File fout = new File("index.html");
    FileOutputStream fos = new FileOutputStream(fout);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

    String head;
    head = createHead(args[2]);

    bw.write(START_HTML);
    bw.newLine();
    bw.write(head);
    bw.newLine();

    try (FileInputStream fin = new FileInputStream(args[0])) {

      // initialisation.
      Scanner sc = new Scanner(fin);
      FindTags line;
      // 'str' will represent the line that will be outputted.
      StringBuilder str;

      // looks if file has another line, and continues if yes.
      while (sc.hasNextLine()) {
        // takes the line that has been peeked in input file.
        str = new StringBuilder(sc.nextLine());

        // checks if string is not empty, and has the starting elements to a list.
        // checks for both ordered and unordered.
        if (str.length() > 0
            && ((str.charAt(0) == '1' && str.charAt(1) == '.') || str.charAt(0) == '-')) {

          collectChunk(str, sc);
          // create instance of 'FindTags'
          // 'FindTags' is told that is of type LIST
          // and has to edit the str so that it has the correct HTML tags.
          line = new FindTags(str.toString(), TagType.LIST);
          line.findTag();

        } else if (str.length() > 0 && str.charAt(0) == '#') {
          // if branch to check if it's a header.
          // instance of 'FindTags' created and told to find tags
          // for type HEADER.
          collectChunk(str, sc);
          line = new FindTags(str.toString(), TagType.HEADER);
          line.findTag();

        } else if (str.length() > 0) {
          // if branch checks that string is not empty.
          // recognises that it must be a paragraph,
          // so it collects lines until next empty line.

          String tempStr = "";
          while (sc.hasNextLine()) {
            tempStr = sc.nextLine();

            if (tempStr.length() > 0) {
              str.append("\n").append(tempStr);

            } else if (tempStr.length() == 0) {
              break;
            }
          }
          // instance of 'FindTags' created,
          // and the whole paragraph chunk until empty line is given as the str attribute.
          line = new FindTags(str.toString(), TagType.PARAGRAPH);
          line.findTag();

        } else {
          // because line is empty, no need to find HTML tags.
          // pretty much ignored.
          line = new FindTags("", TagType.EMPTY);
        }


        // only print the the 'line'(s) that
        // do not have an empty str attribute.
        if (!line.getStr().equals("")) {
          bw.write(line.getStr());
          bw.newLine();
        }
      }

    } catch (FileNotFoundException e) {
      System.out.println("File Not Found.");
    } catch (IOException e) {
      System.out.println("An I/O Error Occurred.");
    }

    bw.write(END_HTML);
    //bw.newLine();
    bw.close();
  }

  private static String createHead(String input) {
    String title = "<title>\n" + input + "\n</title>\n";
    return "<head>\n" + title + "</head>\n" + "<body>";
  }

  private static void collectChunk(StringBuilder str, Scanner sc) {
    // temporary string that is used as a buffer.
    // buffer is used so that it takes the whole chunk of input until
    // the next 'empty' line, or EOF.
    String tempStr;
    tempStr = sc.nextLine();
    // condition to stop when next line fetched is empty.
    while (!tempStr.equals("")) {
      str.append(tempStr);
      if (sc.hasNextLine()) {
        tempStr = sc.nextLine();
      } else {
        break;
      }
    }
  }
}
