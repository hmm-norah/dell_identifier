import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class excel_writer {
    public static void write(Map map, String homedir) throws FileNotFoundException {
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter shipment name: ");
        String shipment_name = reader.nextLine(); reader.close();
        if(shipment_name.equals(""))
            shipment_name = Long.toString(System.currentTimeMillis());

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(shipment_name);
        XSSFRow currentRow = sheet.createRow(0);
        XSSFCell currentCell = currentRow.createCell(0);
        currentCell.setCellValue("Hello");
        currentCell = currentRow.createCell(1);
        currentCell.setCellValue("Testing");

        FileOutputStream fos = new FileOutputStream(new File(homedir + shipment_name + ".xlsx"));
        try {
            wb.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done");





    }
}
