package com.devyy.strategy;

import java.util.ArrayList;
import java.util.List;

import com.devyy.ImportData;
import com.devyy.bean.DataBean;
import com.devyy.bean.PointBean;
import com.devyy.service.CorrectRate;
import com.devyy.service.KMeans;
import com.devyy.util.PrintUtil;

import jxl.Cell;

/** 
 * 使用K-Means算法 
 * 
 * @author ZYY
 * 
 */  
public class KMeansClustering implements ClusteringIntf {
	private KMeans kMeans = new KMeans();
	private ImportData importData =new ImportData();
	private PrintUtil printUtil  =new PrintUtil();
	private CorrectRate correctRate =new CorrectRate();	
	
	// 调用K-Means算法	 
	@Override
	public void clusterAlgorithm() {
		List<Cell[]> cellList = importData.importData();
		int clusterNumber = importData.getclusterNumber();
		DataBean dataBean = kMeans.initDataBean(cellList, clusterNumber);
		List<PointBean> pointList = dataBean.getPointList();
		int count = clusterNumber;
		List<Double[]> centerValueList = new ArrayList<Double[]>();
		for (int i = 0; i < clusterNumber; i++) {
			Double[] center = dataBean.getClusterList().get(i).getClusterCenter();
			centerValueList.add(center);
		}
		while (count != 0) {
			count = 0;
			for (int i = 0; i < pointList.size(); i++) {
				dataBean = kMeans.distributeIntoCluster(dataBean, dataBean.getPointList().get(i));
			}
			dataBean = kMeans.countClusterCenter(dataBean);
			List<Double[]> newCenterValueList = new ArrayList<Double[]>();
			for (int i = 0; i < clusterNumber; i++) {
				Double[] center = dataBean.getClusterList().get(i).getClusterCenter();
				newCenterValueList.add(center);
			}
			for (int i = 0; i < clusterNumber; i++) {
				Double[] oldCenter = centerValueList.get(i);
				Double[] newCenter = newCenterValueList.get(i);
				for (int j = 0; j < oldCenter.length; j++) {
					// 控制误差的精确度范围在0.01%
					if (Math.abs(oldCenter[j] - newCenter[j]) >= 0.0001) {
						count++;
						break;
					}
				}
			}
			for (int i = 0; i < clusterNumber; i++) {
				centerValueList.remove(0);
			}
			centerValueList.addAll(newCenterValueList);
		}
		printUtil.printClusterContents(dataBean);
		correctRate.getCorrectRate(dataBean);
	}
}
