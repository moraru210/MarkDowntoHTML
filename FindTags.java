public class FindTags {

  private String str = "";
  private TagType type;

  public FindTags(String str, TagType type) {
    this.str = str;
    this.type = type;
  }

  public void findTag() {
    if (type.equals(TagType.HEADER)) {
      int i = countHeader();
      str = "<h" + i + ">" + str.substring(i) + "</h" + i + ">";
    } else if (type.equals(TagType.LIST)) {
      handleLists();
    } else {
      str = "<p>" + str + "</p>";
    }
  }

  private int countHeader() {
    int l = 0;
    for (int i = 0; i < str.length(); i++) {
      if (str.charAt(i) != '#') {
        break;
      }
      l++;
    }
    return l;
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
            .append(str.substring(tempIndex + 3, str.length() - 1))
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
