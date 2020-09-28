public class FindTags {

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
      //handles paragraphs for now, since no empty strings are fed.
      str = "<p>" + str + "</p>";
    }
  }

  private int countHeader() {
    //counts the number of '#' until the current character is a space/not '#'.
    //since beginning character(s) has to be '#'.
    int l = 0;
    while (str.charAt(l) == '#') {
      l++;
    }
    //if there's no space between '#' and next character
    //then it's not a header anymore.
    if (str.charAt(l) != ' '){
      l = 0;
    }
    return l;
  }

  private void handleHeaders() {
    int i = countHeader();
    str = "<h" + i + ">" + str.substring(i + 1) + "</h" + i + ">";
  }

  private void handleLists() {
    StringBuilder tempStr = new StringBuilder("<ol>\n");
    int num = 2;
    int index = 0;
    while (true) {
      int tempIndex = index;
      index = str.indexOf(num + ". ");
      if (index == -1) {
        tempStr
            .append("<li>")
            .append(str.substring(tempIndex + 3))
            .append("</li>\n");
        break;
      }
      tempStr.append("<li>").append(str.substring(tempIndex + 3, index)).append("</li>\n");
      num++;
    }

    tempStr.append("</ol>");
    str = tempStr.toString();
  }

  public String getStr() {
    return str;
  }
}
