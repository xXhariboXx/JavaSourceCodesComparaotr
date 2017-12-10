package pl.polsl.javasourcecodescomparator.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents result data from comparing files
 *
 * @author Dominik Rączka
 * @version 0.5
 */
public class ResultData {

    /**
     * Class to contain numerical data of compared files
     *
     * @author Dominik Rączka
     * @version 0.9
     */
    public class AccuracyData{
        private int TotalLinesNumberInOriginFile;
        private int TotalLinesNumberInComparedFile;
        private int MatchingLinesNumber;
        private double SimilarityPercentage;

        private void calculateSimilarityPercentage(){
            SimilarityPercentage = (MatchingLinesNumber * 1.0)/(TotalLinesNumberInOriginFile * 1.0) * (100 * 1.0);
        }

        @Override
        public String toString() {
            String result = "";

            result += "Accuracy data report: \n";
            result += "\tTotal important source lines in origin file: " + TotalLinesNumberInOriginFile + "\n";
            result += "\tTotal important source lines in compared file: " + TotalLinesNumberInComparedFile + "\n";
            result += "\tMatched lines number: " + MatchingLinesNumber + "\n";
            result += "\tSimilarity: " + String.format("%.2f", SimilarityPercentage) + "%";

            return result;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class public fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Information about origin source
     */
    public SourceFileInfo OriginSource;
    /**
     * List of similar sources
     */
    @Deprecated
    public List<String> SimilarSourcesList;
    /**
     * Map that holds all matched lines from every matched source file with info about the source file
     */
    public Map<SourceFileInfo, List<MatchedLine>> MatchingLinesMap;
    /**
     *
     */
    public Map<SourceFileInfo, List<MatchedLine>> LongestCommonPartsMap;

    public Map<SourceFileInfo, AccuracyData> AccuracyDataMap;
    /**
     * Length of longest line - for showing debug results
     */
    private int LongestLineLength;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public ResultData(){
        SimilarSourcesList = new ArrayList<>();
        MatchingLinesMap = new HashMap<>();
        LongestCommonPartsMap = new HashMap<>();
        AccuracyDataMap = new HashMap<>();
        this.LongestLineLength = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Finds longest common parts
     */
    @Deprecated
    public void findLongestCommonParts(){
        for(SourceFileInfo similarSource : MatchingLinesMap.keySet()){
            MatchedLine previousMatchedLine = new MatchedLine();
            List<MatchedLine> longestMatchedLines = new ArrayList<>();

            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)) {
                if(previousMatchedLine.OriginLineNumber == -1){
                    previousMatchedLine = matchedLine;
                } else{
                    if(previousMatchedLine.OriginLineNumber == (matchedLine.OriginLineNumber - 1)){
                        longestMatchedLines.add(previousMatchedLine);
                    } else if(longestMatchedLines.size() > 0 && longestMatchedLines.get(longestMatchedLines.size() - 1).equals(previousMatchedLine)){
                        longestMatchedLines.add(previousMatchedLine);
                    }
                    previousMatchedLine = matchedLine;
                }

                if(matchedLine.LineContent.length() > LongestLineLength){
                    LongestLineLength = matchedLine.LineContent.length();
                }
            }
            
            LongestCommonPartsMap.put(similarSource, longestMatchedLines);
        }
    }
    /**
     * Converts LongestCommonPartsMap to String
     * @return String representation of LongestCommonPartsMap
     */
    public String longestCommonPartsToString(){
        String result = "";

        result += "Origin source:\n" + OriginSource.toString() + "\n";
        for(SourceFileInfo similarSource : LongestCommonPartsMap.keySet()){
            result += "Similar source:\n" + similarSource.toString() + "\n";
            for(MatchedLine matchedLine : LongestCommonPartsMap.get(similarSource)){
                matchedLine.LongestLineLength = LongestLineLength;
                result += matchedLine.toString() + "\n";
            }
        }

        return result;
    }
    /**
     * Finds longest line in file
     */
    public void findLongestLineLength(){
        for(SourceFileInfo similarSource : MatchingLinesMap.keySet()) {
            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)) {
                if (matchedLine.LineContent.length() > LongestLineLength) {
                    LongestLineLength = matchedLine.LineContent.length();
                }
            }
        }
    }
    /**
     * Clears garbage compared files
     */
    public void clearGarbageResults(){
        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : MatchingLinesMap.entrySet()){
            entry.setValue(clearGarbageResultsFromSourceFile(entry.getValue()));
        }
    }

    public boolean haveMatchingLines(){
        boolean bHaveMatchingLines = false;

        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : MatchingLinesMap.entrySet()){
            if(entry.getValue().size() > 0){
                bHaveMatchingLines = true;
            }
        }

        return bHaveMatchingLines;
    }

    public void calculateNumericalResultData(SourceCodeFile originFile, SourceCodeFile comparedFile){
        AccuracyData accuracyData = new AccuracyData();

        accuracyData.TotalLinesNumberInOriginFile = originFile.getLinesNumber();
        accuracyData.TotalLinesNumberInComparedFile = comparedFile.getLinesNumber();
        accuracyData.MatchingLinesNumber = MatchingLinesMap.get(comparedFile.getSourceFileInfo()).size();
        accuracyData.calculateSimilarityPercentage();

        AccuracyDataMap.put(comparedFile.getSourceFileInfo(), accuracyData);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "Origin source:\n" + OriginSource.toString() + "\n";
        for(SourceFileInfo similarSource : MatchingLinesMap.keySet()){
            result += "Similar source:\n" + similarSource.toString() + "\n";
            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)){
                matchedLine.LongestLineLength = LongestLineLength;
                result += matchedLine.toString() + "\n";
            }
            result += AccuracyDataMap.get(similarSource).toString() + "\n";
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Clears garbage lines from list of source lines
     * @param matchedLines list of matched lines to check
     * @return list of matched lines without garbage lines
     */
    private List<MatchedLine> clearGarbageResultsFromSourceFile(List<MatchedLine> matchedLines){
        List<MatchedLine> nonGarbageLines = new ArrayList<>();
//        MatchedLine previousMatchedLine = new MatchedLine();

        for(MatchedLine line : matchedLines){
//            if(previousMatchedLine.OriginLineNumber == -1) {
//                previousMatchedLine = line;
//            }

            //if((previousMatchedLine.OriginLineNumber == (line.OriginLineNumber - 1)) || !line.iSGarbage()) {
            if(!line.iSGarbage()){
                nonGarbageLines.add(line);
            }

            //previousMatchedLine = line;
        }

        return nonGarbageLines;
    }
}
