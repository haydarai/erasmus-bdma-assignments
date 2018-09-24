import java.util.ArrayList;
import java.util.List;

class ArrayUtil {
    /**
     * Get index of certain string in array
     * @param strings array of string
     * @param item the string itself
     * @return index of the srtring
     */
    static int getIndexOf(String[] strings, String item) {
        for (int i = 0; i < strings.length; i++) {
            if (item.equals(strings[i])) return i;
        }
        return -1;
    }

    /**
     * Remove a column attribute that already used for splitting
     * @param array original array
     * @param colRemove index of column you want to remove
     * @return array with specific column already removed
     */
    static String[][] removeCol(String [][] array, int colRemove)
    {
        int row = array.length;
        int col = array[0].length;

        String[][] newArray = new String[row][col-1]; //new Array will have one column less

        for(int i = 0; i < row; i++)
        {
            for(int j = 0,currColumn=0; j < col; j++)
            {
                if(j != colRemove)
                {
                    newArray[i][currColumn++] = array[i][j];
                }
            }
        }

        return newArray;
    }

    /**
     * Get a space with certain column attribute and value but the column is removed
     * @param source full array
     * @param columnIndex the guideline attribute of what you want to keep
     * @param columnValue the value of the column that you want to keep (e.g: I want to keep all data that humidity == high)
     * @return array of attributes that have specific column value (e.g: all data that humidity == high), but having the humidity column removed
     */
    static String[][] getSpaceWithSpecificColumnAndValue(String[][] source, int columnIndex, String columnValue) {
        List<String[]> toKeep = new ArrayList<>();
        toKeep.add(source[0]);
        for (int i = 1; i < source.length; i++) {
            if (source[i][columnIndex].equals(columnValue)) {
                toKeep.add(source[i]);
            }
        }

        String[][] result = toKeep.toArray(new String[0][0]);
        return ArrayUtil.removeCol(result, columnIndex);
    }
}
