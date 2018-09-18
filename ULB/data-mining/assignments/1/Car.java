import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Car
{
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("car.csv"));

        while (scanner.hasNextLine()) {
            Scanner dataScanner = new Scanner(scanner.nextLine());
            dataScanner.useDelimiter(",");
            int index = 0;

            while (dataScanner.hasNext()) {
                String data = dataScanner.next();
                switch(index) {
                    case 0: {
                        System.out.println("buying: " + data);
                        break;
                    }
                    case 1: {
                        System.out.println("maint: " + data);
                        break;
                    }
                    case 2: {
                        System.out.println("doors: " + data);
                        break;
                    }
                    case 3: {
                        System.out.println("persons: " + data);
                        break;
                    }
                    case 4: {
                        System.out.println("lug_boot: " + data);
                        break;
                    }
                    case 5: {
                        System.out.println("safety: " + data);
                        break;
                    }
                    case 6: {
                        System.out.println("acceptability: " + data);
                        break;
                    }
                }
                index++;
            }
            System.out.println();
            dataScanner.close();
        }

        scanner.close();
    }
}