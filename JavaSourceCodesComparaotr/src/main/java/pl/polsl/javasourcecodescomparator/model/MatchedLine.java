package pl.polsl.javasourcecodescomparator.model;

public class MatchedLine {

    public Integer OriginLineNumber;

    public Integer ComparedLineNumber;

    public String LineContent;

    public int LongestLineLength;


    public MatchedLine(){
        this.OriginLineNumber = -1;
        this.ComparedLineNumber = -1;
        this.LineContent = "";
    }

    public MatchedLine(int originLineNumber, int comparedLineNumber, String lineContent){
        this.OriginLineNumber = originLineNumber;
        this.ComparedLineNumber = comparedLineNumber;
        this.LineContent = lineContent;
        this.LongestLineLength = 0;
    }

    public boolean iSGarbage(){
        boolean bIsGarbage = false;

        while (LineContent.startsWith(" ")){
            LineContent = LineContent.substring(1);
        }

        if(LineContent.length() <= 1){
            bIsGarbage = true;
        }

        return  bIsGarbage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += /*"Origin source content: " + */LineContent;
        while((LineContent.length() + result.length()) < LongestLineLength){
            result += " ";
        }
        result += "; Line number in origin file: " + OriginLineNumber.toString() + ", line number in compared file: " + ComparedLineNumber.toString();

        return result;
    }
}
