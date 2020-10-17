public class FontStyle {
    private String str;
    private int asterisks;
    private int start;
    private int end;

    public FontStyle(String str, int asterisks, int start, int end) {
        this.str = str;
        this.asterisks = asterisks;
        this.start = start;
        this.end = end;
    }

    public void addFonts() {
        if (asterisks == 2) {
            str = "<strong>" + str + "</strong>";
        } else {
            str = "<em>" + str + "</em>";
        }
    }

    public String getStr() {
        return str;
    }

    public int getAsterisks() {
        return asterisks;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
