package com.example.taieb.immkd.parsing;

import android.content.Context;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by KHALED on 09/04/2015.
 */

public class parsingxls {

    private HSSFWorkbook myWorkBook;

    public HSSFWorkbook getMyWorkBook() {

        return myWorkBook;
    }


    public parsingxls() {


    }


    public List<sheet> getALLSHEET()
    {
        List<sheet> sheets = new LinkedList<sheet>();
        sheet s=null;


       for(int i=0;i< myWorkBook.getNumberOfSheets();i++)
       {
           s=new sheet();

           s.setId(i);
           s.setName(myWorkBook.getSheetName(i));

           sheets.add(s);
       }
        return sheets;
    }

   public  int findRow(int id, String cellContent) {

       HSSFCellStyle cellStyle = myWorkBook.createCellStyle();
       HSSFFont font = myWorkBook.createFont();
     //  font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
      // font.setFontHeightInPoints((short)10);
       font.setColor(IndexedColors.BLUE.getIndex());
       cellStyle.setFont(font);

       final DataFormatter df = new DataFormatter();
       HSSFSheet s = myWorkBook.getSheetAt(id);

       for (int i = 0; i < s.getPhysicalNumberOfRows(); i++) {
           Row row = s.getRow(i);

           if (row!=null)
           {
           for (int j = 0; j <15; j++) {
               Cell cl = row.getCell(j);
   //            cl.getCellStyle().setFillBackgroundColor(IndexedColors.RED.getIndex());
               if (cl != null) {
                   String valueAsString = df.formatCellValue(cl);
                   if (valueAsString.trim().equals(cellContent)) {
           //            cl.setCellStyle(cellStyle);
                       return i ;
                   }
               }
           }

           }

       }
       return  -1;
   }


    public Article getallrows(int idsheet,int idrow, String cellContent)
    {
        final DataFormatter df = new DataFormatter();
        Article article=new Article();

        String value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(1)) +
                df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(2));
        article.setCASE(value);

        value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(9));
        article.setPart_Name(value);

        value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(7));
        article.setPart_Num(value);



        boolean finish=false;
        Station stat=null;

        while(!finish){
            stat=new Station();
            value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(3));
            article.getListe_BOX().add(value);

            value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(12)) +" "+
                    df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(13));
            stat.setStat_Name(value);

            value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(14));
            stat.setQTY(value);

            article.getListe_Station().add(stat);
            idrow++;

            value=df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(7));
            System.out.println("xxxxxxxxxxx    "+value);
            System.out.println("yyyyyyyyyyy    "+df.formatCellValue(this.myWorkBook.getSheetAt(idsheet).getRow(idrow-1).getCell(7)));
            if(!isMergedCell(idsheet,this.myWorkBook.getSheetAt(idsheet).getRow(idrow).getCell(7)))
            {
                finish=true;
                break;

            }
            //System.out.println(article.toString());



        }



        return  article;



    }
    public  boolean isMergedCell(int sheetID,Cell cell)
    {
        List<CellRangeAddress> regionsList = new ArrayList<CellRangeAddress>();
        Sheet sheet=myWorkBook.getSheetAt(sheetID);
        boolean ismerged=false;
        for(int i = 0; i < sheet.getNumMergedRegions(); i++) {
            regionsList.add(sheet.getMergedRegion(i));
        }


        for(CellRangeAddress region : regionsList) {

            // If the region does contain the cell you have just read from the row
            if(region.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                // Now, you need to get the cell from the top left hand corner of this
              //  int rowNum = region.getFirstRow();
             //   int colIndex = region.getFirstColumn();
           //     cell = sheet.getRow(rowNum).getCell(colIndex);

                System.out.println("Cel is in merged region. The value stored in that region is " );
                ismerged=true;

            }

        }

        return  ismerged;


    }



    public void readExcelFile(Context context,InputStream myInput) {



        try{
            // Creating Input Stream
           // file = new File(context.getExternalFilesDir(null), filename);
           // FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            this.myWorkBook = new HSSFWorkbook(myFileSystem);

        }catch (Exception e){e.printStackTrace(); }

        return;
    }










    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    public boolean saveExcelFile(Context context, String fileName) {

        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            System.out.println("FileUtils"+ "Storage not available or read only");
            return false;
        }

        boolean success = false;


        // Create a path where we will place our List of objects on external storage
        File file = new File(context.getExternalFilesDir(null),fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            this.myWorkBook.write(os);
           // Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            //Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            //Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }
}
