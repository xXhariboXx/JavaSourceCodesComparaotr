package pl.polsl.javasourcecodescomparator.model;

import java.util.ArrayList;
import java.util.List;

public class SourceCodeFile {

    private String Author;

    private String ProjectName;

    private String Version;

    private List<String> SourceLinesList;

    private int LinesNumber;

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public List<String> getSourceLinesList() {
        return SourceLinesList;
    }

    public void setSourceLinesList(List<String> sourceLinesList) {
        SourceLinesList = sourceLinesList;
    }

    public int getLinesNumber() {
        return LinesNumber;
    }

    public void setLinesNumber(int linesNumber) {
        LinesNumber = linesNumber;
    }

    SourceCodeFile(){
        SourceLinesList = new ArrayList<>();
    }

    @Override
    public String toString() {
        String result = "";
        for(String line : SourceLinesList){
            result += line + "\n";
        }

        return result;
    }
}
