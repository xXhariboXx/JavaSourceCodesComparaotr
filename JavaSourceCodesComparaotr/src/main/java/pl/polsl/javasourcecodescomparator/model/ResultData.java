package pl.polsl.javasourcecodescomparator.model;


import java.util.*;

/**
 * Class that represents result data from comparing files
 *
 * @author Dominik Rączka
 * @version 1.0
 */
public class ResultData {

    /**
     * Class to contain numerical data of compared files
     *
     * @author Dominik Rączka
     * @version 1.0
     */
    public class AccuracyData implements Comparable<AccuracyData>{

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Class private fields
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Number of lines of important code in source file
         */
        private int totalLinesNumberInOriginFile;
        /**
         * Number of lines of important code in compared file
         */
        private int totalLinesNumberInComparedFile;
        /**
         * Number of matching lines between two files
         */
        private int matchingLinesNumber;
        /**
         * Percentage of how much origin file is similar to compared file
         */
        private Double similarityPercentage;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Class private methods
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * Calculates similarityPercentage
         */
        private void calculateSimilarityPercentage(){
            similarityPercentage = (matchingLinesNumber * 1.0)/(totalLinesNumberInOriginFile * 1.0) * (100 * 1.0);
        }
        /**
         * Checks if origin source and compared source are similar size
         * @return true if are similar size (not bigger than 5 times difference)
         */
        private boolean isEquallySized(){
            return totalLinesNumberInOriginFile *5 > totalLinesNumberInComparedFile || totalLinesNumberInComparedFile *5 < totalLinesNumberInOriginFile;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Public override methods
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public String toString() {
            String result = "";

            result += "Accuracy data report: \n";
            result += "\tTotal important source lines in origin file: " + totalLinesNumberInOriginFile + "\n";
            result += "\tTotal important source lines in compared file: " + totalLinesNumberInComparedFile + "\n";
            result += "\tMatched lines number: " + matchingLinesNumber + "\n";
            result += "\tSimilarity: " + String.format("%.2f", similarityPercentage) + "%";

            return result;
        }

        @Override
        public int compareTo(AccuracyData objectToCompare){
            return -1*this.similarityPercentage.compareTo(objectToCompare.similarityPercentage);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class public fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Information about origin source
     */
    public SourceFileInfo originSource;
    /**
     * Map that holds all matched lines from every matched source file with info about the source file
     */
    public LinkedHashMap<SourceFileInfo, List<MatchedLine>> matchingLinesMap;
    /**
     * Map that hold numerical data about similarity
     */
    public LinkedHashMap<SourceFileInfo, AccuracyData> accuracyDataMap;
    /**
     * Length of longest line - for showing debug results
     */
    private int longestLineLength;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public ResultData(){
        matchingLinesMap = new LinkedHashMap<>();
        accuracyDataMap = new LinkedHashMap<>();
        this.longestLineLength = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Package-private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Finds longest line in file
     */
    void findLongestLineLength(){
        for(SourceFileInfo similarSource : matchingLinesMap.keySet()) {
            for(MatchedLine matchedLine : matchingLinesMap.get(similarSource)) {
                if (matchedLine.lineContent.length() > longestLineLength) {
                    longestLineLength = matchedLine.lineContent.length();
                }
            }
        }
    }
    /**
     * Clears garbage compared files
     */
    void clearGarbageResults(){
        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : matchingLinesMap.entrySet()){
            entry.setValue(clearGarbageResultsFromSourceFile(entry.getValue()));
        }
    }
    /**
     * Checks if object contains matching lines
     * @return true if ResultData contains any matching lines
     */
    boolean haveMatchingLines(){
        boolean bHaveMatchingLines = false;

        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : matchingLinesMap.entrySet()){
            if(entry.getValue().size() > 0){
                bHaveMatchingLines = true;
            }
        }

        return bHaveMatchingLines;
    }

    /**
     * Checks if data have info about similar sources
     * @param similarityPercentage minimum percentage to call files similar
     * @return true if result data have info about similar sources
     */
    boolean iSimilar(double similarityPercentage){
        boolean result = false;

        clearAccuracyData(similarityPercentage);
        if(accuracyDataMap.size() > 0){
            result = true;
        }

        return result;
    }
    /**
     * Calculates numerical result of comparison
     * @param originFile origin file
     * @param comparedFile file that is compared to origin file
     */
    void calculateNumericalResultData(SourceCodeFile originFile, SourceCodeFile comparedFile){
        AccuracyData accuracyData = new AccuracyData();

        accuracyData.totalLinesNumberInOriginFile = originFile.getSourceLinesList().size();
        accuracyData.totalLinesNumberInComparedFile = comparedFile.getLinesNumber();
        accuracyData.matchingLinesNumber = matchingLinesMap.get(comparedFile.getSourceFileInfo()).size();
        accuracyData.calculateSimilarityPercentage();

        accuracyDataMap.put(comparedFile.getSourceFileInfo(), accuracyData);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "\n*********************************************************\n";
        result += "*Origin source:\n" + originSource.toString() + "\n\n";
        for(SourceFileInfo similarSource : matchingLinesMap.keySet()){
            result += "\n*Similar source:\n" + similarSource.toString() + "\n";
            for(MatchedLine matchedLine : matchingLinesMap.get(similarSource)){
                matchedLine.setLongestLineLength(longestLineLength);
                result += matchedLine.toString() + "\n";
            }
            result += accuracyDataMap.get(similarSource).toString() + "\n";
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

        for(MatchedLine line : matchedLines){
            if(!line.iSGarbage()){
                nonGarbageLines.add(line);
            }
        }

        return nonGarbageLines;
    }

    /**
     * Clears accuracy data that are less similar than similarityPercentage
     * @param similarityPercentage minimum percentage to call files similar
     */
    private void clearAccuracyData(double similarityPercentage){
        Map<SourceFileInfo, AccuracyData> accuracyResultsToSave = new HashMap<>();

        for(Map.Entry<SourceFileInfo, AccuracyData> entry : accuracyDataMap.entrySet()){
            if(entry.getValue().isEquallySized() && entry.getValue().similarityPercentage >= similarityPercentage){
                accuracyResultsToSave.put(entry.getKey(), entry.getValue());
            }
        }

        accuracyDataMap.clear();
        accuracyResultsToSave = sortByValues(accuracyResultsToSave);
        for(Map.Entry<SourceFileInfo, AccuracyData> entry : accuracyResultsToSave.entrySet()){
                accuracyDataMap.put(entry.getKey(), entry.getValue());
        }
        synchronizeMaps();
    }

    private LinkedHashMap sortByValues(Map mapToSort) {
        List list = new LinkedList(mapToSort.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        LinkedHashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private void synchronizeMaps(){
        Map<SourceFileInfo, List<MatchedLine>> finalMatchedLinesResults = new LinkedHashMap<>();

        for(Map.Entry<SourceFileInfo, AccuracyData> entry : accuracyDataMap.entrySet()){
            finalMatchedLinesResults.put(entry.getKey(), matchingLinesMap.get(entry.getKey()));
        }

        matchingLinesMap.clear();
        matchingLinesMap.putAll(finalMatchedLinesResults);
    }
}
