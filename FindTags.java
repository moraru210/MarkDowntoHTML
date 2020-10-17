import java.util.*;

public class FindTags {

  enum Font {
    ITALICS,
    BOLD,
    NORMAL
  }

  private String str = ""; // this attribute contains the chunk of lines or line.
  private TagType type; // to handle different types of markdown commands differently.

  public FindTags(String str, TagType type) {
    this.str = str;
    this.type = type;
  }

  public void findTag() {
    if (type.equals(TagType.HEADER)) {
      handleHeaders();
    } else if (type.equals(TagType.ORDERED_LIST)) {
      // handle lists injects the list items between the <ol> tags.
      StringBuilder tempStr = new StringBuilder("<ol>\n");
      handleLists(tempStr);
      tempStr.append("</ol>");
      str = tempStr.toString();
    } else if (type.equals(TagType.UNORDERED_LIST)) {
      // same but with <ul> tags.
      StringBuilder tempStr = new StringBuilder("<ul>\n");
      handleLists(tempStr);
      tempStr.append("</ul>");
      str = tempStr.toString();
    } else {
      // handles paragraphs for now, since no empty strings are fed.
      String output = lookThroughLine(str);
      str = "<p>" + output + "</p>";
    }
  }

  private String lookThroughLine(String input) {
    //format the line to find the bold/italic text, and then find the links.
    return findLinks(formatText(input));
  }

  private String formatText(String input) {
    String output = input;
    boolean modified = false;

    //repeat process until no more pair of asterisks are found,
    //hence it cannot be formatted further.
    do {
      int length = output.length();
      int asterisks = 0; // 1 for italics, 2 or bold
      int start = 0;
      int end = 0;
      //FIFO
      Queue<Character> queue = new LinkedList<>();
      modified = false;

      for (int i = 0; i < length; i++) {

        //found the start of the asterisks
        if (asterisks == 0 && output.charAt(i) == '*') {
          if ((i + 2) < length && output.charAt(i + 1) == '*') {
            asterisks = 2;
            i = i + 2;
            start = i;
          } else if ((i + 1 < length)){
            asterisks = 1;
            i = i + 1;
            start = i;
          }
        }

        //found the ending/matching asterisks
        if (!queue.isEmpty() && (asterisks == 1 || asterisks == 2) && output.charAt(i) == '*') {
          end = asterisks == 1 ? i + 1 : i + 2;
          int stackSize = queue.size();
          char[] charArr = new char[stackSize];
          for (int j = 0; j < (stackSize); j++) {
            charArr[j] = queue.poll();
          }
          String str = String.valueOf(charArr);
          FontStyle font = new FontStyle(str, asterisks, start, end);
          font.addFonts();
          assert (queue.isEmpty());
          output =
              output.substring(0, font.getStart() - font.getAsterisks())
                  + font.getStr()
                  + output.substring(font.getEnd(), length);
          modified = true;
          break;
        }

        //add to the queue.
        if (output.charAt(i) != '*' && (asterisks == 1 || asterisks == 2)) {
          queue.add(output.charAt(i));
        }
      }
    } while (modified);

    return output;
  }

  private String findLinks(String input) {
    //split the text to be able to find the links within the line.
    String[] words = input.split("[\\[\\]]");

    for (int i = 0; i < words.length; i++) {
      if (!words[i].equals("") && words[i].charAt(0) == '(' && i > 0) {
        words[i] =  "<a href=\""
                + words[i].substring(1, words[i].length() - 1)
                + "\">"
                + words[i - 1]
                + "</a>";
        words[i - 1] = "";
      }
    }

    // this loop is to remove the redundant empty string array items.
    StringBuilder output = new StringBuilder();
    for (int k = 0; k < words.length; k++) {
      if (k == words.length - 1) {
        output.append(words[k]);
        break;
      }
      if (!words[k].equals("")) {
        output.append(words[k]);
        output.append(" ");
      }
    }

    return output.toString();
  }

  private int countHeader() {
    // counts the number of '#' until the current character is a space/not '#'.
    // since beginning character(s) has to be '#'.
    int l = 0;
    while (str.charAt(l) == '#') {
      l++;
    }
    // if there's no space between '#' and next character
    // then it's not a header anymore.
    if (str.charAt(l) != ' ') {
      l = 0;
    }
    return l;
  }

  private void handleHeaders() {
    int i = countHeader();
    String output = lookThroughLine(str.substring(i + 1));
    str = "<h" + i + ">" + output + "</h" + i + ">";
  }

  private void handleLists(StringBuilder tempStr) {
    // need an array to split the chunk into list items.
    String[] list_items = type == TagType.UNORDERED_LIST ? str.split("-") : str.split("\\d+");
    // ordered lists start with ". " while unordered start with " ".
    int subString = type == TagType.UNORDERED_LIST ? 1 : 2;
    String preProcessed;
    for (String item : list_items) {
      // don't want to concat the empty string.
      if (!item.equals("")) {
        preProcessed = lookThroughLine(item.substring(subString));
        tempStr.append("<li>").append(preProcessed).append("</li>\n");
      }
    }
  }

  public String getStr() {
    return str;
  }
}
