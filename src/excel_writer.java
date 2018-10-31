import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class excel_writer {
    public static void write(Map<PC, Integer> systems, String homedir) throws FileNotFoundException {
        int row = 0, column = 0;
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter shipment name: ");
        String shipment_name = reader.nextLine(); reader.close();
        if(shipment_name.equals(""))
            shipment_name = Long.toString(System.currentTimeMillis());

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(shipment_name);
        XSSFRow currentRow = sheet.createRow(row);
        XSSFCell currentCell = currentRow.createCell(column);

        for (PC key : systems.keySet()) {
            String [] data;
            data = key.get_data();

            for (int i = 0; i < data.length; ++i){
                currentCell.setCellValue(data[i]);
                currentCell = currentRow.createCell(++column);
            }
            currentCell.setCellValue(systems.get(key));
            column = 0;
            currentRow = sheet.createRow(++row);
            currentCell = currentRow.createCell(column);
        }


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
