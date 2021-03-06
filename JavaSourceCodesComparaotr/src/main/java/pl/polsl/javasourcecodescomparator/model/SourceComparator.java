package pl.polsl.javasourcecodescomparator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that performs files comparison
 * @author Dominik
 * @version 1.0
 */


public class SourceComparator {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * All java source files to compare
     */
    private List<SourceCodeFile> sourceFilesToCompareList;
    /**
     * List of result data from comparison
     */
    private List<ResultData> resultDataList;
    /**
     * Minimum
     */
    private double minimumSimilarityPercentage;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<SourceCodeFile> getSourceFilesToCompareList() {
        return sourceFilesToCompareList;
    }
    public void setSourceFilesToCompareList(List<SourceCodeFile> sourceFilesToCompareList) {
        this.sourceFilesToCompareList = sourceFilesToCompareList;
    }
    public double getMinimumSimilarityPercentage() {
        return minimumSimilarityPercentage;
    }
    public void setMinimumSimilarityPercentage(double minimumSimilarityPercentage) {
        this.minimumSimilarityPercentage = minimumSimilarityPercentage;
    }
    public void setMinimumSimilarityPercentage(String minimumSimilarityPercentage) {
        try{
            this.minimumSimilarityPercentage = Double.parseDouble(minimumSimilarityPercentage);
        } catch(Exception e){
            this.minimumSimilarityPercentage = 10.0;
        }

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public SourceComparator(){
        sourceFilesToCompareList = new ArrayList<>();
        resultDataList = new ArrayList<>();
        minimumSimilarityPercentage = 10.0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Compare all files with all source lines
     */
    public void compareAllFiles(boolean compareInProject, boolean compareTheSameAuthor){
        for(SourceCodeFile sourceFile : sourceFilesToCompareList){
            compareFileWithOthers(sourceFile, compareInProject, compareTheSameAuthor);
        }
        clearGarbage();
    }
    /**
     * Clears data in comparator
     */
    public void clearComparatorData(){
        sourceFilesToCompareList.clear();
        resultDataList.clear();
    }
    /**
     * Converts all result data from SourceComparator to String
     * @return SourceComparator converted to String
     */
    public String getTotalResultString() {
        String result = "";

        for(ResultData resultData : resultDataList){
            resultData.findLongestLineLength();
            result += resultData.toString() + "\n";
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Compares file with other files
     * @param originSourceCodeFile file to compare with others
     */
    private void compareFileWithOthers(SourceCodeFile originSourceCodeFile, boolean compareInProject, boolean compareTheSameAuthor){
        ResultData resultData = new ResultData();
        resultData.originSource = originSourceCodeFile.getSourceFileInfo();

        for(SourceCodeFile sourceFile : sourceFilesToCompareList){
            if((!areSourcesFromTheSameProject(originSourceCodeFile, sourceFile) || compareInProject) &&
                    !areSourcesTheSameVersion(originSourceCodeFile, sourceFile) &&
                    (!originSourceCodeFile.getSourceFileInfo().authorName.equals(sourceFile.getSourceFileInfo().authorName) || compareTheSameAuthor)){

                List<MatchedLine> matchingLines = compareCodeFiles(originSourceCodeFile, sourceFile);
                if(matchingLines.size() > 0) {
                    resultData.matchingLinesMap.put(sourceFile.getSourceFileInfo(), matchingLines);
                    resultData.calculateNumericalResultData(originSourceCodeFile, sourceFile);
                }
            }
        }

        resultDataList.add(resultData);
    }

    private boolean areSourcesTheSame(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return originSourceCodeFile.getSourceFileInfo().equals(sourceCodeFileToCompare.getSourceFileInfo());
    }

    private  boolean areSourcesFromTheSameProject(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        boolean result = originSourceCodeFile.getSourceFileInfo().projectName.equals(sourceCodeFileToCompare.getSourceFileInfo().projectName);
        return result;
    }

    private  boolean areSourcesTheSameVersion(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return originSourceCodeFile.getSourceFileInfo().authorName.equals(sourceCodeFileToCompare.getSourceFileInfo().authorName) &&
                originSourceCodeFile.getSourceFileInfo().version.equals(sourceCodeFileToCompare.getSourceFileInfo().version);
    }
    /**
     * Compare two code files
     * @param originSource source code file labeled as origin
     * @param sourceToCompare source file to compare to origin
     * @return list of matched line in both files
     */
    private List<MatchedLine> compareCodeFiles(SourceCodeFile originSource, SourceCodeFile sourceToCompare){
        List<MatchedLine> resultList = new ArrayList<>();

        for(SourceLine originSourceLine : originSource.getSourceLinesList()){
            for(SourceLine sourceLineToCompare : sourceToCompare.getSourceLinesList()){
                if (!sourceLineToCompare.wasSourceLineMatched) {
                    if (originSourceLine.sourceLineContent.equals(sourceLineToCompare.sourceLineContent)) {
                        sourceLineToCompare.wasSourceLineMatched = true;
                        resultList.add(new MatchedLine(originSourceLine.sourceLineIndex, sourceLineToCompare.sourceLineIndex, originSourceLine.sourceLineContent));
                        break;
                    }
                }
            }
        }

        return  resultList;
    }
    /**
     * Clears garbage results. Lower similarity percentage than minimum similarity percentage
     */
    private void clearGarbage(){
        ArrayList<ResultData> listToSave = new ArrayList<>();

        for(ResultData resultData : resultDataList){
            resultData.clearGarbageResults();
            if(resultData.haveMatchingLines() && resultData.iSimilar(minimumSimilarityPercentage)){
                listToSave.add(resultData);
            }
        }

        resultDataList.clear();
        resultDataList.addAll(listToSave);
    }
}
