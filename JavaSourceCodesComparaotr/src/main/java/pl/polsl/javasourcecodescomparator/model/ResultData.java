package pl.polsl.javasourcecodescomparator.model;


import java.util.*;

/**
 * Class that represents result data from comparing files
 *
 * @author Dominik Rączka
 * @version 0.9
 */
public class ResultData {

    /**
     * Class to contain numerical data of compared files
     *
     * @author Dominik Rączka
     * @version 0.9
     */
    public class AccuracyData implements Comparable<AccuracyData>{
        /**
         * Number of lines of important code in source file
         */
        private int TotalLinesNumberInOriginFile;
        /**
         * Number of lines of important code in compared file
         */
        private int TotalLinesNumberInComparedFile;
        /**
         * Number of matching lines between two files
         */
        private int MatchingLinesNumber;
        /**
         * Percentage of how much origin file is similar to compared file
         */
        private Double SimilarityPercentage;

        /**
         * Calculates SimilarityPercentage
         */
        private void calculateSimilarityPercentage(){
            SimilarityPercentage = (MatchingLinesNumber * 1.0)/(TotalLinesNumberInOriginFile * 1.0) * (100 * 1.0);
        }
        /**
         * Checks if origin source and compared source are similar size
         * @return true if are similar size (not bigger than 5 times difference)
         */
        private boolean isEquallySized(){
            return TotalLinesNumberInOriginFile*5 > TotalLinesNumberInComparedFile || TotalLinesNumberInComparedFile*5 < TotalLinesNumberInOriginFile;
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

        @Override
        public int compareTo(AccuracyData objectToCompare){
            return -1*this.SimilarityPercentage.compareTo(objectToCompare.SimilarityPercentage);
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
     * Map that holds all matched lines from every matched source file with info about the source file
     */
    public LinkedHashMap<SourceFileInfo, List<MatchedLine>> MatchingLinesMap;
    /**
     * Map that hold numerical data about similarity
     */
    public LinkedHashMap<SourceFileInfo, AccuracyData> AccuracyDataMap;
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
        MatchingLinesMap = new LinkedHashMap<>();
        AccuracyDataMap = new LinkedHashMap<>();
        this.LongestLineLength = 0;
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
    void clearGarbageResults(){
        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : MatchingLinesMap.entrySet()){
            entry.setValue(clearGarbageResultsFromSourceFile(entry.getValue()));
        }
    }
    /**
     * Checks if object contains matching lines
     * @return true if ResultData contains any matching lines
     */
    boolean haveMatchingLines(){
        boolean bHaveMatchingLines = false;

        for(Map.Entry<SourceFileInfo, List<MatchedLine>> entry : MatchingLinesMap.entrySet()){
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
        if(AccuracyDataMap.size() > 0){
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

        accuracyData.TotalLinesNumberInOriginFile = originFile.getSourceLinesList().size();
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

        result += "\n*********************************************************\n";
        result += "*Origin source:\n" + OriginSource.toString() + "\n\n";
        for(SourceFileInfo similarSource : MatchingLinesMap.keySet()){
            result += "\n*Similar source:\n" + similarSource.toString() + "\n";
            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)){
                matchedLine.setLongestLineLength(LongestLineLength);
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

        for(Map.Entry<SourceFileInfo, AccuracyData> entry : AccuracyDataMap.entrySet()){
            if(entry.getValue().isEquallySized() && entry.getValue().SimilarityPercentage >= similarityPercentage){
                accuracyResultsToSave.put(entry.getKey(), entry.getValue());
            }
        }

        AccuracyDataMap.clear();
        accuracyResultsToSave = sortByValues(accuracyResultsToSave);
        for(Map.Entry<SourceFileInfo, AccuracyData> entry : accuracyResultsToSave.entrySet()){
                AccuracyDataMap.put(entry.getKey(), entry.getValue());
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

        for(Map.Entry<SourceFileInfo, AccuracyData> entry : AccuracyDataMap.entrySet()){
            finalMatchedLinesResults.put(entry.getKey(), MatchingLinesMap.get(entry.getKey()));
        }

        MatchingLinesMap.clear();
        MatchingLinesMap.putAll(finalMatchedLinesResults);
    }
}
