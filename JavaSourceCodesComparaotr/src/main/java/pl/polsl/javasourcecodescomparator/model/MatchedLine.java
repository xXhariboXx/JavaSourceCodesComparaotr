package pl.polsl.javasourcecodescomparator.model;

public class MatchedLine {

    public Integer OriginLineNumber;

    public Integer ComparedLineNumber;

    public String LineContent;

    public MatchedLine(){
        this.OriginLineNumber = -1;
        this.ComparedLineNumber = -1;
        this.LineContent = "";
    }

    public MatchedLine(int originLineNumber, int comparedLineNumber, String lineContent){
        this.OriginLineNumber = originLineNumber;
        this.ComparedLineNumber = comparedLineNumber;
        this.LineContent = lineContent;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "Origin source content: " + LineContent + "; Line number in origin file: " + OriginLineNumber.toString() + ", line number in compared file: " + ComparedLineNumber.toString();

        return result;
    }
}
