package pl.polsl.javasourcecodescomparator.model;

import org.apache.commons.io.FileUtils;
import pl.polsl.javasourcecodescomparator.exceptions.WrongFileExtensionException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import net.lingala.zip4j.exception.ZipException;

/**
 * Class to process package and list all projects inside it
 *
 * @author Dominik Rączka
 * @version 1.0
 */
public class ArchiveOperator {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * All java source files from directory
     */
    private List<SourceCodeFile> sourceCodeFiles;
    /**
     * List of error messages from opening .zip files;
     */
    private List<Exception> errorExceptionsList;
    /**
     * List of projects name in directory
     */
    private List<String> projectsNamesList;
    /**
     * Total number of projects in directory
     */
    private int totalProjectsNumber;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<SourceCodeFile> getSourceCodeFiles() {
        return sourceCodeFiles;
    }
    public void setSourceCodeFiles(List<SourceCodeFile> sourceCodeFiles) {
        this.sourceCodeFiles = sourceCodeFiles;
    }
    public List<Exception> getErrorExceptionsList() {
        return errorExceptionsList;
    }
    public void setErrorExceptionsList(List<Exception> errorExceptionsList) {
        this.errorExceptionsList = errorExceptionsList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public ArchiveOperator(){
        sourceCodeFiles = new ArrayList<>();
        errorExceptionsList = new ArrayList<>();
        projectsNamesList = new ArrayList<>();
        totalProjectsNumber = 0;
    }
    /**
     * Reads folder with projects
     *
     * @param archivePath path to folder with projects
     */
    public boolean readArchive(String archivePath) {
        errorExceptionsList.clear();
        sourceCodeFiles.clear();
        projectsNamesList.clear();
        totalProjectsNumber = 0;
        boolean result = false;

        if(archivePath.contains(".zip")) {
            File folder = new File(unZipDirectory((archivePath)));
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                totalProjectsNumber++;
                File inFolder = new File(file.getPath());
                File[] inListOfFiles = inFolder.listFiles();
                for (File inFile : inListOfFiles) {
                    try {
                        if (inFile.getName().contains(".zip")) {
                            exploreZip(inFile.getPath());
                        } else {
                            errorExceptionsList.add(new WrongFileExtensionException(inFile));
                        }
                    } catch (Exception e) {
                        errorExceptionsList.add(new WrongFileExtensionException(e.getMessage()));
                    }
                }
            }

            result = true;
        }

        return result;
    }
    /**
     * Gets report of errors during reading the directory
     * @return String representation of errors from reading the directory
     */
    public String getErrorMessagesReport(){
        String result = "";

        result += "Archive file reading errors report.\n";
        if(errorExceptionsList.size() > 0) {
            for (Exception exception : errorExceptionsList) {
                result += "Error: " + exception.getMessage() + "\n";
            }
        } else {
            result += "No errors!";
        }

        return result;
    }
    /**
     * Converts project names from directory to String representation
     * @return String representation of ProjectsNames
     */
    public String getProjectsNamesString(){
        String result = "";

        result += "Projects in directory: \n";
        for (String projectName : projectsNamesList){
            result += "\t- " + projectName + "\n";
        }
        result += "Total projects number: " + totalProjectsNumber + "\n";
        result += "Validated projects number: " + projectsNamesList.size() + "\n";
        result += "Error projects number: " + errorExceptionsList.size() + "\n";

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Unzips ZIP archive to directory with the same name as archive name
     * @param pathToDirectory path to ZIP archive
     * @return path of unzipped directory
     */
    private String unZipDirectory(String pathToDirectory){
        String result = "";

        try {
            net.lingala.zip4j.core.ZipFile zipFile = new net.lingala.zip4j.core.ZipFile(pathToDirectory);

            String pathToUnzip = pathToDirectory.replaceAll(".zip", "");

            try {
                if (Files.isDirectory(Paths.get(pathToUnzip))) {
                    FileUtils.deleteDirectory(new File(pathToUnzip));
                }
            } catch(Exception e){

            }

            zipFile.extractAll(pathToUnzip);

            result = pathToUnzip;
        } catch (ZipException e) {
            e.printStackTrace();
        }

        return result;
    }
    /**
     * Explores zip projects to extract source codes
     * @param zipFilePath path to zip file with project
     * @throws Exception
     */
    private void exploreZip(String zipFilePath) throws Exception {
        Charset CP866 = Charset.forName("CP866");
        java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(zipFilePath, CP866);

        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        int folderNumber = 0;
        String projectName = "";

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                if(folderNumber == 1) {
                    projectName = extractProjectName(entry.getName());
                    projectsNamesList.add(projectName);
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
    private void copySource(InputStream stream, String projectName, String sourceFileName) throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        SourceCodeFile sourceFile = new SourceCodeFile();
        String currentLine;

        SourceFileInfo sourceFileInfo = new SourceFileInfo();
        sourceFileInfo.projectName = projectName;
        sourceFileInfo.fileName = sourceFileName;

        sourceFile.setSourceFileInfo(sourceFileInfo);

        int lineIndex = 1;
        while ((currentLine = bufferedReader.readLine()) != null) {
            sourceFile.addSourceLine(new SourceLine(currentLine, lineIndex));
            lineIndex++;
        }

        sourceCodeFiles.add(sourceFile);
    }

    /**
     * Extracts project name from project directory path
     * @param directoryPath path of project directory
     * @return project name
     */
    private String extractProjectName(String directoryPath){
        String projectName = "none";

        if(directoryPath.length() > 0) {
            projectName = directoryPath.substring(0, directoryPath.length() - 1);
        }

        if(projectName.contains("/")) {
            projectName = projectName.substring(0, projectName.indexOf("/"));
        }

        return  projectName;
    }

    /**
     * Extracts source file name from source file path
     * @param filePath path to source file
     * @return source file name
     */
    private String extractSourceFileName(String filePath){
        String sourceFilename = "none";

        if(filePath.contains("/")) {
            sourceFilename = filePath.substring(filePath.lastIndexOf("/") + 1);
        }

        return sourceFilename;
    }
}


