package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source line and its information
 *
 * @author Dominik Rączka
 * @version 0.9
 */
public class SourceLine {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class public fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Content of source line
     */
    public String SourceLineContent;
    /**
     * Content of processed lines - only important data
     */
    public String ProcessedLineContent;
    /**
     * Index of source line
     */
    public int SourceLineIndex;
    /**
     * True if source line was matched by comparator
     */
    public  boolean WasSourceLineMatched;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean processSourceLine(){
        if(SourceLineContent.contains("{")) {
            SourceLineContent = SourceLineContent.replaceAll("\\{", "");
        }
        if(SourceLineContent.contains("}")) {
            SourceLineContent = SourceLineContent.replaceAll("}", "");
        }
        if(SourceLineContent.contains("\t")) {
            SourceLineContent = SourceLineContent.replaceAll("\t", "");
        }

        while(SourceLineContent.startsWith(" ")){
            SourceLineContent = SourceLineContent.substring(1);
        }

        ProcessedLineContent = SourceLineContent;
        ProcessedLineContent.toLowerCase();

        if(ProcessedLineContent.contains("break")) {
            ProcessedLineContent = ProcessedLineContent.replaceAll("break;", "");
        }
        if(ProcessedLineContent.contains("return")) {
            ProcessedLineContent = ProcessedLineContent.replaceAll("return;", "");
        }
        if(ProcessedLineContent.contains("@author")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("@author", "");
        }
        if(ProcessedLineContent.contains("@test")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("@test", "");
        }
        if(ProcessedLineContent.contains("@override")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("@Override", "");
        }
        if(ProcessedLineContent.contains("try")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("try", "");
        }
        if(ProcessedLineContent.contains("else")){
            ProcessedLineContent = ProcessedLineContent.replaceAll("else", "");
        }

        return  validateSourceLine();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean validateSourceLine(){
        boolean result = true;

        if(ProcessedLineContent.length() == 0){
            result = false;
        } else if(ProcessedLineContent.startsWith("//")){
            result = false;
        } else if(ProcessedLineContent.startsWith("/**")){
            result = false;
        } else if(ProcessedLineContent.startsWith("/*")){
            result = false;
        } else if(ProcessedLineContent.startsWith("*/")){
            result = false;
        } else if (ProcessedLineContent.startsWith("*")){
            result = false;
        } else if(ProcessedLineContent.contains("import")) {
            result = false;
        } else if(ProcessedLineContent.contains("package")) {
            result = false;
        } else if(ProcessedLineContent.length() == 1 && ProcessedLineContent.equals(";")){
            result = false;
        }

        return  result;
    }
}
