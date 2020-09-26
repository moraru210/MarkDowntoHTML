/*
Display a text file.
To use this program, specify the name of the file that you want to see.
For example, to see a file called test.txt, use the following command:

java Scanner test.txt
*/

import java.io.*;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    int i;

    // First, confirm that a filename has been specified.
    if (args.length != 1) {
      System.out.println("Usage: ShowFile filename");
      return;
    }

    try (FileInputStream fin = new FileInputStream(args[0])) {
      Scanner sc = new Scanner(fin);
      FindTags line;
      StringBuilder str;
      // returns true if there's another line to be read.
      while (sc.hasNextLine()) {
        // return line that was skipped.
        str = new StringBuilder(sc.nextLine());
        if (str.length() > 0
            && ((str.charAt(0) == '1' && str.charAt(1) == '.') || str.charAt(0) == '-')) {
          String tempStr;
          tempStr = sc.nextLine();
          while (!tempStr.equals("")) {
            str.append(tempStr);
            if (sc.hasNextLine()) {
              tempStr = sc.nextLine();
            } else {
              break;
            }
          }

          line = new FindTags(str.toString(), TagType.LIST);
          line.findTag();

        } else if (str.length() > 0 && str.charAt(0) == '#') {
          line = new FindTags(str.toString(), TagType.HEADER);
          line.findTag();

        } else if (str.length() > 0){
          String tempStr = "";
          while (sc.hasNextLine()) {
            tempStr = sc.nextLine();
            if (tempStr.length() > 0) {
              str.append("\n").append(tempStr);
            } else if (tempStr.length() == 0) {
              break;
            }
          }
          line = new FindTags(str.toString(), TagType.PARAGRAPH);
          line.findTag();
        } else {
          line = new FindTags("", TagType.EMPTY);
        }
        if (!line.getStr().equals("")){
          System.out.println(line.getStr());
        }
      }

    } catch (FileNotFoundException e) {
      System.out.println("File Not Found.");
    } catch (IOException e) {
      System.out.println("An I/O Error Occurred.");
    }
  }
}
