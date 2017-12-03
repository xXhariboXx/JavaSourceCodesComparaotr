package pl.polsl.javasourcecodescomparator.model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class to process package and list all projects inside it
 *
 * @author Dominik Rączka
 * @version 0.1
 */
public class ArchiveOperator {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * All java source files from project
     */
    private List<SourceCodeFile> SourceFiles;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<SourceCodeFile> getSourceFiles() {
        return SourceFiles;
    }
    public void setSourceFiles(List<SourceCodeFile> sourceFiles) {
        SourceFiles = sourceFiles;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public ArchiveOperator(){
        SourceFiles = new ArrayList<>();
    }

    /**
     * Reads folder with projects
     *
     * @param archivePath path to folder with projects
     * @throws Exception
     */
    public void readArchive(String archivePath) throws Exception {
        File folder = new File(archivePath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.getName().contains(".zip")) {
                exploreZip(file.getPath());
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Explores zip projects to extract source codes
     * @param zipFilePath path to zip file with project
     * @throws Exception
     */
    private void exploreZip(String zipFilePath) throws Exception {
        Charset CP866 = Charset.forName("CP866");
        ZipFile zipFile = new ZipFile(zipFilePath, CP866);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        int folderNumber = 0;
        String projectName = "";

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                if(folderNumber == 1) {
                    projectName = extractProjectName(entry.getName());
                }
                folderNumber++;
            } else if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
                InputStream stream = zipFile.getInputStream(entry);
                copySource(stream, projectName, extractSourceFileName(entry.getName()));
            }
        }
    }

    /**
     * Copies source from file in .zip project to program memory
     * @param stream stream with .java file
     * @throws IOException
     */
    private void copySource(InputStream stream, String projectName, String sourceFileName) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        SourceCodeFile sourceFile = new SourceCodeFile();
        String currentLine;

        int lineIndex = 1;
        while ((currentLine = bufferedReader.readLine()) != null) {
            sourceFile.addSourceLine(new SourceLine(currentLine, lineIndex));
            lineIndex++;
        }

        sourceFile.setProjectName(projectName);
        sourceFile.setSourceFileName(sourceFileName);
        sourceFile.extractPureSource();

        SourceFiles.add(sourceFile);
    }

    /**
     * Extracts project name from project directory path
     * @param directoryPath path of project directory
     * @return project name
     */
    private String extractProjectName(String directoryPath){
        String projectName = "";

        projectName = directoryPath.substring(0, directoryPath.length() - 1);
        projectName = projectName.substring(projectName.lastIndexOf("/") + 1);

        return  projectName;
    }

    /**
     * Extracts source file name from source file path
     * @param filePath path to source file
     * @return source file name
     */
    private String extractSourceFileName(String filePath){
        String sourceFilename = "";

        sourceFilename = filePath.substring(filePath.lastIndexOf("/") + 1);

        return sourceFilename;
    }
}


