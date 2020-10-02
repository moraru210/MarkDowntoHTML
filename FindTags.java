import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class FindTags {

  enum Font {
    ITALICS,
    BOLD,
    NORMAL
  }

  private String str = "";
  private TagType type;

  public FindTags(String str, TagType type) {
    this.str = str;
    this.type = type;
  }

  public void findTag() {
    if (type.equals(TagType.HEADER)) {
      handleHeaders();
    } else if (type.equals(TagType.LIST)) {
      handleLists();
    } else {
      // handles paragraphs for now, since no empty strings are fed.
      String output = lookThroughLine(str);
      str = "<p>" + output + "</p>";
    }
  }

  private String lookThroughLine(String input) {
    String[] words = input.split("\\[|]|\\(|\\)|\\s");
    Queue<String> emphasisQ = new PriorityQueue<>();
    Font type = Font.NORMAL;
    int pos = 0;

    for (int i = 0; i < words.length; i++) {

      if (words[i].charAt(0) == '*' && emphasisQ.isEmpty()) {
        if (words[i].charAt(1) == '*') {
          type = Font.BOLD;
          emphasisQ.add("<strong>" + words[i].substring(2));
          pos = i;
        } else {
          type = Font.ITALICS;
          emphasisQ.add("<em>" + words[i].substring(1));
          pos = i;
        }
      }

      if (!emphasisQ.isEmpty() && (words[i].charAt(words[i].length() - 1) == '*') && i > pos) {
        if (words[i].charAt(words[i].length() - 2) == '*' && type == Font.BOLD) {
          emphasisQ.add(words[i].substring(0, words[i].length() - 2) + "</strong>");
          for (String s : emphasisQ) {
            words[pos] = s;
            pos++;
          }
          emphasisQ.clear();
        } else if (type == Font.ITALICS) {
          emphasisQ.add(words[i].substring(0, words[i].length() - 1) + "</em>");
          for (String s : emphasisQ) {
            words[pos] = s;
            pos++;
          }
          emphasisQ.clear();
        } else {
          emphasisQ.add(words[i]);
        }
      } else if (pos == i
          && !emphasisQ.isEmpty()
          && words[i].charAt(words[i].length() - 2) == '*') {
        words[i] = "<strong>" + words[i].substring(2, words[i].length() - 2) + "</strong>";
      } else if (pos == i
          && !emphasisQ.isEmpty()
          && words[i].charAt(words[i].length() - 1) == '*'
          && words[i].charAt(words[i].length() - 2) != '*') {
        words[i] = "<em>" + words[i].substring(1, words[i].length() - 1) + "</em>";
      }
    }
    return String.join(" ", words);
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

  private void handleLists() {
    StringBuilder tempStr = new StringBuilder("<ol>\n");
    int num = 2;
    int index = 0;
    while (true) {
      int tempIndex = index;
      index = str.indexOf(num + ". ");
      if (index == -1) {
        String output = lookThroughLine(str.substring(tempIndex + 3));
        tempStr.append("<li>").append(output).append("</li>\n");
        break;
      }
      String output = lookThroughLine(str.substring(tempIndex + 3, index));
      tempStr.append("<li>").append(output).append("</li>\n");
      num++;
    }

    tempStr.append("</ol>");
    str = tempStr.toString();
  }

  public String getStr() {
    return str;
  }
}
