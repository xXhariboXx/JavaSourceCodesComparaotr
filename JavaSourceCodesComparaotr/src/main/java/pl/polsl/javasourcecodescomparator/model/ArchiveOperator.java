package pl.polsl.javasourcecodescomparator.model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class to unzip package and list all projects inside it
 *
 * @author Dominik
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

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
            } else if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
                InputStream stream = zipFile.getInputStream(entry);
                copySource(stream);
            }
        }
    }

    /**
     * Copies source from file in .zip project to program memory
     * @param stream stream with .java file
     * @throws IOException
     */
    private void copySource(InputStream stream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        SourceCodeFile sourceFile = new SourceCodeFile();
        List<String> sourceLinesList = sourceFile.getSourceLinesList();
        String currentLine;

        while ((currentLine = bufferedReader.readLine()) != null) {
            sourceLinesList.add(currentLine);
        }

        sourceFile.setSourceLinesList(sourceLinesList);
        SourceFiles.add(sourceFile);
    }
}


