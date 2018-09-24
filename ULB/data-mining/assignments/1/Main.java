public class Main {
    public static void main(String[] args) {
        String filePath = "car.csv"; // default filepath

        // If first argument (filePath) was filled in by user
        if (args.length >= 1) {
            filePath = args[0];
        }
        String[][] source = FileUtil.readCSV(filePath);

        int index = 0; // index is used for numbering the row of each entry
        int target = source[0].length - 1; // default target index is the last one

        // If second argument (target index) was filled in by user
        if (args.length >= 2) {
            target = Integer.valueOf(args[1]) + 1;
            if (target > source[0].length - 1) {
                throw new Error("Output class index should not surpass the number of the attribute. The first attribute index is 0 and the last attribute index is " + String.valueOf(source[0].length - 2));
            }
        }

        Node root = DecisionTree.ID3(source, index, target, 0);
        System.out.println(root.toString()); // print the whole tree, see Node.java to see how this is done
    }
}
