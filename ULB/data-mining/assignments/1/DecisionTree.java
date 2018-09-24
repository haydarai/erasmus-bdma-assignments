import java.util.HashMap;
import java.util.Map;

class DecisionTree {
    /**
     * Running ID3 algorithm
     * @param source 2d array of string with first row as attributes and second until the final row as rows of data
     * @param index the number of the row (1st, 2nd, etc), used only for drawing the tree, doesn't have anything to do with the algorithm
     * @param target the index of target attribute, (e.g: in play.csv it's the "play", in car.csv it's "acceptability")
     * @param level current level of the tree, used for making drawing the tree on console easier, doesn't have anything to do with the algorithm
     * @return
     */
    static Node ID3(String[][] source, int index, int target, int level) {
        // If no rows of data left, means that there is no node in this branch
        if (source.length <= 1) {
            return new Node("Failure", level);
        }

        Map<String, Integer> map = new HashMap<>();

        String targetLabel = source[0][target];  // used to remember the target attribute name, while be used for searching later, doesn't have anything to do with the algorithm

        // Check how many occurence of possible classes and attributes
        for (int i = 1; i < source.length; i++) {
            Integer targetCount = map.get(source[0][target] + "_" + source[i][target]);
            if (targetCount == null) {
                targetCount = 1;
            } else {
                targetCount++;
            }
            map.put(source[0][target] + "_" + source[i][target], targetCount);
        }

        // If all input records have same value (e.g: if humidty == normal always equal to play == true
        // then return Node of "humidity == normal" with a children "true" with its index)
        if (map.keySet().size() == 1) {
            String value = source[1][target] + ": [";
            for (int i = 1; i < source.length; i++) {
                value += source[i][index];
                if (i != source.length - 1) {
                    value += ";";
                } else {
                    value += "]";
                }
            }
            return new Node(value, level);
        }

        // Saving information about highest gain attribute
        String highestGainName = null;
        int highestGainIndex = 1;
        double highestGain = 0;

        // The process of looking for the attribute that have highest gain
        for (int i = 0; i < source[0].length; i++) {
            if (i != target && i != index) {
                double gain = calculateGain(source, i, target);
                if (gain > highestGain) {
                    highestGainName = source[0][i];
                    highestGainIndex = i;
                    highestGain = gain;
                }
            }
        }

        // There is only 1 class left to be used as splitting criterion (why it's 3? because the other 2 are: index and
        // target attribute). Hence this class also can't classify it precisely, return this attribute with the most
        // occurrence
        if (source[0].length == 3 && highestGain == 0) {
            String highestOccurenceKey = "";
            int highestNumberOfOccurence = 0;
            for (String key : map.keySet()) {
                int occurrence = map.get(key);
                if (occurrence > highestNumberOfOccurence) {
                    highestNumberOfOccurence = occurrence;
                    highestOccurenceKey = key;
                }
            }
            String[] keys = highestOccurenceKey.split("_"); // split by _ so we now have ["play", "no"]
            String highestOccurrence = keys[keys.length - 1]; // get "no" as this is what we need for printing the tree
            String value = highestOccurrence + ": [";
            for (int i = 1; i < source.length; i++) {
                value += source[i][index];
                if (i != source.length - 1) {
                    value += ";";
                } else {
                    value += "]";
                }
            }
            return new Node(value, level);
        }

        Map<String, Integer> columnMap = new HashMap<>();
        for (int i = 1; i < source.length; i++) {
            Integer columnCount = map.get(source[i][highestGainIndex]);
            if (columnCount == null) {
                columnCount = 1;
            } else {
                columnCount++;
            }
            columnMap.put(source[i][highestGainIndex], columnCount);
        }

        // Use the attribute with the highest gain as the current node
        Node node = new Node(highestGainName, level);
        for (String key : columnMap.keySet()) {
            Node keyNode = new Node(key, level + 1);
            // Split attribute with highest gain and its value as splitting criterion
            // (e.g: "humidty" is the node, its children are "high" and "normal")
            String[][] splittedSource = ArrayUtil.getSpaceWithSpecificColumnAndValue(source, highestGainIndex, key);

            // Get the new target index as it might be shifted due to deletion based on current attribute
            target = ArrayUtil.getIndexOf(splittedSource[0], targetLabel);
            // Add the children to its attribute node ("humidity->high")
            keyNode.addChild(ID3(splittedSource, index, target, level + 2));

            // Add the new highest gain node (e.g: "humidity") to the tree
            node.addChild(keyNode);
        }

        return node; // Return the root node
    }

    private static double entropySplit(Map<String, Integer> map, Map<String, Integer> columnMap,
                                       Map<String, Integer> targetMap, String columnName) {
        int divider = map.get("total");
        double entropySplit = 0;
        for (String key : columnMap.keySet()) {
            String[] splittedKey = key.split("_");
            String columnValue = splittedKey[1];
            double entropy = entropy(map, columnName, columnValue, targetMap);
            entropySplit += MathUtil.divide(map.get(key), divider) * entropy;
        }
//        System.out.println("Entropy-Split(" + columnName + "): " + entropySplit);
        return entropySplit;
    }

    /**
     * Calculate the entropy for an input attribute
     * @param map
     * @param columnName
     * @param columnValue
     * @param targetMap
     * @return
     */
    private static double entropy(Map<String, Integer> map, String columnName, String columnValue,
                                  Map<String, Integer> targetMap) {
        String dividerName = columnName + "_" + columnValue;
        if (!map.containsKey(dividerName)) {
            return 0;
        }
        int divider = map.get(dividerName);
        double sum = 0;

        for (String key : targetMap.keySet()) {
            String numeratorName = dividerName + "_" + key;
            if (map.containsKey(numeratorName)) {
                int numerator = map.get(numeratorName);
                double division = MathUtil.divide(numerator, divider);
                sum += -1 * division * MathUtil.log2(division);
            }
        }

        return sum;
    }

    /**
     * Calculate the entropy of the target attribute
     * @param map collection of target attribute and number of occurence (e.g: play, 100)
     * @return
     */
    private static double entropy(Map<String, Integer> map) {
        double sum = 0;
        int divider = 0;

        // Summing up the number of all data to get the divider
        for (int value : map.values()) {
            divider += value;
        }

        // Calculating for each value (e.g: humidity == high and humidity == normal)
        for (String key : map.keySet()) {
            int numerator = map.get(key);
            double division = MathUtil.divide(numerator, divider);
            sum += -1 * division * MathUtil.log2(division);
        }

//        System.out.println("E(S): " + sum);
        return sum;
    }

    /**
     * Calculate gain of an attribute
     * @param entropy entropy of the whole space
     * @param entropySplit entropy split of the attribute
     * @param columnName name of the attribute, only used for debugging
     * @return information gain of the attribute
     */
    private static double gain(double entropy, double entropySplit, String columnName) {
        double gain = entropy - entropySplit;
//        System.out.println("Gain(" + columnName + "): " + gain);
        return gain;
    }

    /**
     * Method to prepare all the things you need for calculating the gain
     * @param source the table of the data
     * @param column index of input attribute
     * @param target index of target attribute
     * @return gain for a specific attribute
     */
    private static double calculateGain(String[][] source, int column, int target) {
        Map<String, Integer> map = new HashMap<>(); // map for ...saving stuff that either belong to both column and target or something that doesn't belong to both
        Map<String, Integer> columnMap = new HashMap<>(); // map for saving count occurrence about a column attribute
        Map<String, Integer> targetMap = new HashMap<>(); // map for saving count occurrence about a target attribute
        map.put("total", source.length - 1);

        // Basically all the long and complex code below is to save how many time a certain column
        // and target value occurred (e.g: humidity == high with play == yes)
        for (int i = 1; i < source.length; i++) {
            Integer columnCount = map.get(source[0][column] + "_" + source[i][column]);
            if (columnCount == null) {
                columnCount = 1;
            } else {
                columnCount++;
            }
            map.put(source[0][column] + "_" + source[i][column], columnCount);
            columnMap.put(source[0][column] + "_" + source[i][column], columnCount);

            Integer targetCount = map.get(source[0][target] + "_" + source[i][target]);
            if (targetCount == null) {
                targetCount = 1;
            } else {
                targetCount++;
            }
            map.put(source[0][target] + "_" + source[i][target], targetCount);
            targetMap.put(source[0][target] + "_" + source[i][target], targetCount);

            Integer columnTargetCount = map.get(source[0][column] + "_" + source[i][column] + "_" + source[0][target] + "_" + source[i][target]);
            if (columnTargetCount == null) {
                columnTargetCount = 1;
            } else {
                columnTargetCount++;
            }
            map.put(source[0][column] + "_" + source[i][column] + "_" + source[0][target] + "_" + source[i][target], columnTargetCount);
        }

        double entropy = entropy(targetMap); // calculate the entropy for the whole space (target attribute)
        double entropySplit = entropySplit(map, columnMap, targetMap, source[0][column]); // calculate the entropy-split for the specific column attribute
        double gain = gain(entropy, entropySplit, source[0][column]); // calculate the gain for a specific column attribute

        return gain;
    }
}
