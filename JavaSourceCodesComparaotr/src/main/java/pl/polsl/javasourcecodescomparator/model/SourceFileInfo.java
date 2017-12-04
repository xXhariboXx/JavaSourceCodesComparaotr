package pl.polsl.javasourcecodescomparator.model;

public class SourceFileInfo {

    public String FileName;

    public String AuthorName;

    public String Version;

    public String ProjectName;

    public SourceFileInfo(){
        FileName = "none";
        AuthorName = "none";
        Version = "none";
        ProjectName = "none";
    }

    @Override
    public String toString() {
        String result = "";

        result += "Project name: " + ProjectName + "\n";
        result += "File name: " + FileName + ", Author name: " + AuthorName + ", version: " + Version;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if(((SourceFileInfo) obj) != null){
            SourceFileInfo objToCompare = (SourceFileInfo) obj;
            result = objToCompare.AuthorName.equals(AuthorName) &&
                    objToCompare.FileName.equals(FileName) &&
                    objToCompare.Version.equals(Version) &&
                    objToCompare.ProjectName.equals(ProjectName);
        }

        return result;
    }
}
