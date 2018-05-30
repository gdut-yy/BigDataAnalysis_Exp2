package com.devyy;

import java.util.List;

import com.devyy.util.ExcelUtil;

import jxl.Cell;
import jxl.Workbook;

public class ImportData {
	private ExcelUtil excelUtil = new ExcelUtil();	
	
	/**
	 * 导入测试数据
	 * @return 返回封装了测试数据的list
	 */
	public List<Cell[]> importData() {
		String absolutePath = "fisheriris_meas.xls";
		int sheetLoca = 0;
		int initRowLoca = 1;
		Workbook workbook = excelUtil.readExcel(absolutePath);
		List<Cell[]> list = excelUtil.sheetEncapsulation(workbook, sheetLoca, initRowLoca);
		return list;
	}

	/**
	 * 得到簇的数目
	 * @return 返回簇的数目
	 */
	public int getclusterNumber() {
		int clusterNumber = 3;
		return clusterNumber;
	}

	/**
	 * 导入正确的分类结果数据
	 * @return 返回封装该结果数据的list
	 */
	public List<Cell[]> importResultData() {
		String absolutePath = "fisheriris_species.xls";
		int sheetLoca = 0;
		int initRowLoca = 1;
		Workbook workbook = excelUtil.readExcel(absolutePath);
		List<Cell[]> list = excelUtil.sheetEncapsulation(workbook, sheetLoca, initRowLoca);
		return list;
	}
}
