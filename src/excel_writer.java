import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;
import java.util.Scanner;

public class excel_writer {
    public static void write(Map map){
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter shipment name: ");
        String shipment_name = reader.nextLine(); reader.close();

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(shipment_name);




    }
}
