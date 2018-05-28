package com.devyy.strategy;

import java.util.List;

import com.devyy.ImportData;
import com.devyy.bean.DataBean;
import com.devyy.service.CorrectRate;
import com.devyy.service.HierarchicalAlgorithm;
import com.devyy.util.PrintUtil;

import jxl.Cell;

/** 
 * 使用层次聚类算法 
 * 
 * @author ZYY
 * 
 */ 
public class HierarchicalClustering implements ClusteringIntf {
	
	private ImportData importData =new ImportData();
	private PrintUtil printUtil  =new PrintUtil();
	private CorrectRate correctRate =new CorrectRate();
	
	/**
	 * 调用层次聚类算法
	 */
	@Override
	public void clusterAlgorithm() {
		int k = importData.getclusterNumber();
		List<Cell[]> cellList = importData.importData();
		DataBean dataVO = HierarchicalAlgorithm.initDataVO(cellList);
		while (dataVO.getClusterList().size() != k) {
			dataVO = HierarchicalAlgorithm.mergeCluster(dataVO);
		}
		printUtil.printClusterContents(dataVO);
		correctRate.getCorrectRate(dataVO);
	}
}
