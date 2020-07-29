package com.pearson.teach.library;


import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelLibrary {

	
	private ExcelLibrary()
	{
		
	}
	public static int getRowCount(String sPath, String sSheet) {
		int iRowNum = 0;
		try {

			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			iRowNum = sht.getLastRowNum();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iRowNum;
	}

	public static int getColumnCount(String sPath, String sSheet) {
		int colnum = 0;
		try {

			FileInputStream fis = new FileInputStream(sPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			colnum =sht.getRow(1).getPhysicalNumberOfCells();
				 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return colnum;
	}
	
	public static String getCellData(String xlPath, String sheetName, int rowNo, int colNo) {
		DataFormatter dataFormatter = new DataFormatter();

		int iRowNum = 0;
		String data = null;
		try {

			FileInputStream fis = new FileInputStream(xlPath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sheetName);

			iRowNum = sht.getLastRowNum();
			if (rowNo <= iRowNum) {
				Cell cell = sht.getRow(rowNo).getCell(colNo);
				data = dataFormatter.formatCellValue(cell);
			} else {
				System.out.println("Please provide the valid Row Count");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;

	}

	public static String[] getRowData(String sFilepath, String sSheet, int rowno) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iCellNum = sht.getRow(rowno).getPhysicalNumberOfCells();
			sData = new String[iCellNum];
			for (int j = 0; j < iCellNum; j++) {
				Cell cell = sht.getRow(rowno).getCell(j);
				sData[j] = dataFormatter.formatCellValue(cell);

			}

		} catch (Exception e) {
		 	e.printStackTrace();
		}
		return sData;
	}

	public static String[] getColumnData(String sFilepath, String sSheet, int colno) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			FileInputStream fis = new FileInputStream(sFilepath);
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			Sheet sht = wb.getSheet(sSheet);
			int iRowNum = sht.getLastRowNum();
			sData = new String[iRowNum];

			for (int i = 1; i <= iRowNum; i++) {

				Cell cell = sht.getRow(i).getCell(colno);
				sData[i - 1] = dataFormatter.formatCellValue(cell);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}

	public static int getColumnIndex(String filepath, String sSheet, String colName) {
		String[] firstRow = getRowData(filepath, sSheet,0);
		int index = 0;
		for (int i = 0; i < firstRow.length; i++) {
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}

	public static boolean doesArrayContainsBlank(String[] data)
	{
		boolean blank=false;
		
		for(int i=0;i<data.length;i++)
		{
			if(data[i].isEmpty() ||data[i]==null)
			{
				blank=true;
			    break;
			}
		}
		
		return blank;
	}
	/*
	 *
	 * 
	 * Description:To read test data from excel sheet based on TestcaseID
	 */
	public static String[] toReadExcelData(String sFilepath, String sSheet, String sTestCaseID) {
		DataFormatter dataFormatter = new DataFormatter();
		String sData[] = null;
		try {
			//File Read
			FileInputStream fis = new FileInputStream(sFilepath);
			//Workbook Create
			Workbook wb = (Workbook) WorkbookFactory.create(fis);
			//Specify Sheet
			Sheet sht = wb.getSheet(sSheet);
			//Row Count
			int iRowNum = sht.getLastRowNum();
			//Find out the testcase based on testcaseID
			for (int i = 0; i <= iRowNum; i++) {
				//Match the Test Case ID
				if (sht.getRow(i).getCell(0).toString().equals(sTestCaseID)) {
					//Cell Count of MAtched Row
					int iCellNum = sht.getRow(i).getPhysicalNumberOfCells();
					// Initialize the String Array Length
					sData = new String[iCellNum];
					//Loop TO Read Column Data And Store it in Array
					for (int j = 0; j < iCellNum; j++) {
						Cell cell = sht.getRow(i).getCell(j);
						//Store Read Data Into Array
						sData[j] = dataFormatter.formatCellValue(cell);

					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sData;
	}
	
	public static int getColumnIndex(String filepath, String sSheet, String colName,String firstRowName) {
		//Read First Row as Excel Sheet Headings
		String[] firstRow = ExcelLibrary.toReadExcelData(filepath, sSheet, firstRowName);
		
		int index = 0;
		
		for (int i = 0; i < firstRow.length; i++) {
			
			if (firstRow[i].equalsIgnoreCase(colName)) {
				index = i;
			}
		}
		return index;
	}
	
}
