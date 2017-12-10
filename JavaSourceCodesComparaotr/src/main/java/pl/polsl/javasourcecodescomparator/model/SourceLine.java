package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source line and its information
 *
 * @author Dominik Rączka
 * @version 0.9
 */
public class SourceLine {
    /**
     * Content of source line
     */
    public String SourceLineContent;
    public String ProcessedLineContent;
    /**
     * Indes of source line
     */
    public int SourceLineIndex;
    /**
     * True if source line was matched by comparator
     */
    public  boolean WasSourceLineMatched;

    /**
     * Creates new SourceLine. Sets WasLineMatched to false by default
     * @param sourceLineContent content of source line to create
     * @param sourceLineIndex index of source line to create
     */
    public  SourceLine(String sourceLineContent, int sourceLineIndex){
        this.SourceLineContent = sourceLineContent;
        this.SourceLineIndex = sourceLineIndex;
        this.WasSourceLineMatched = false;
    }

    public boolean processSourceLine(){
        while(SourceLineContent.startsWith(" ")){
            SourceLineContent = SourceLineContent.substring(1);
        }

        if(SourceLineContent.contains("{")) {
            SourceLineContent = SourceLineContent.replaceAll("\\{", "");
        }
        if(SourceLineContent.contains("}")) {
            SourceLineContent = SourceLineContent.replaceAll("}", "");
        }

        ProcessedLineContent = SourceLineContent;

        if(ProcessedLineContent.contains("break")) {
            ProcessedLineContent = ProcessedLineContent.replaceAll("break;", "");
        }
        if(ProcessedLineContent.contains("@author")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("@author", "");
        }
        if(ProcessedLineContent.contains("try")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("try", "");
        }
        if(ProcessedLineContent.contains("else")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("else", "");
        }

        return  validateSourceLine();
    }

    private boolean validateSourceLine(){
        boolean result = true;

        if(ProcessedLineContent.length() == 0){
            result = false;
        } else if(ProcessedLineContent.startsWith("//")){
            result = false;
        } else if(ProcessedLineContent.startsWith("/**")){
            result = false;
        } else if(ProcessedLineContent.startsWith("*/")){
            result = false;
        } else if (ProcessedLineContent.startsWith("*")){
            result = false;
        } else if(ProcessedLineContent.contains("import")) {
            result = false;
        } else if(ProcessedLineContent.length() == 1 && ProcessedLineContent.equals(";")){
            result = false;
        }

        return  result;
    }
}
