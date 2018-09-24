import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FileUtil {
    /**
     * Read CSV file with attributes on the first row and the following rows as rows of data
     * @param pathName
     * @return 2D array of String
     */
    static String[][] readCSV(String pathName) {
        try {
            Scanner scanner = new Scanner(new File(pathName));
            List<List<String>> table = new ArrayList<>();
            int index = 1;

            while (scanner.hasNextLine()) {
                Scanner dataScanner = new Scanner(scanner.nextLine());
                dataScanner.useDelimiter(",");
                List<String> row = new ArrayList<>();
                if (table.size() == 0) {
                    row.add("index");
                } else {
                    row.add(String.valueOf(index));
                    index++;
                }

                while (dataScanner.hasNext()) {
                    String data = dataScanner.next();
                    row.add(data);
                }
                table.add(row);
                dataScanner.close();
            }

            scanner.close();

            String[][] array = new String[table.size()][];
            for (int i = 0; i < table.size(); i++) {
                array[i] = table.get(i).toArray(new String[0]);
            }
            return array;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new String[0][];
    }
}
