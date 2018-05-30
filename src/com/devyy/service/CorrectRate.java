package com.devyy.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.devyy.ImportData;
import com.devyy.bean.ClusterBean;
import com.devyy.bean.DataBean;
import com.devyy.bean.PointBean;

import jxl.Cell;

public class CorrectRate {
	
	private ImportData importData =new ImportData();
	
	/**
	 * 获得两个Excel表之间的正确的映射
	 * @return 返回键值对map
	 */
	public Map<Double[], String> getMap() {
		List<Cell[]> resultCellList = importData.importResultData();
		List<Cell[]> testCellList = importData.importData();
		Map<Double[], String> map = new HashMap<Double[], String>();
		for (int j = 0; j < testCellList.size(); j++) {
			Cell[] testCell = testCellList.get(j);
			Cell[] resultCell = resultCellList.get(j);
			String name = resultCell[0].getContents();
			Double[] cellValue = new Double[testCell.length];
			for (int i = 0; i < testCell.length; i++) {
				cellValue[i] = Double.valueOf(testCell[i].getContents());
			}
			map.put(cellValue, name);
		}
		return map;
	}

	/**
	 * 获得正确率
	 * @param dataBean
	 */
	public void getCorrectRate(DataBean dataBean) {
		int maxLoca = 0;// 最多项在数组中出现的位置
		int maxSize = 0;// 最多项出现的次数
		int sum = 0;// 正确项的总和
		Map<Double[], String> map = getMap();
		// 每个簇所获得的簇名
		String[] clusterNameReal = new String[dataBean.getClusterList().size()];
		Set<String> set = new HashSet<String>();
		for (Iterator<Entry<Double[], String>> iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Double[], String> entry = iter.next();
			String value = entry.getValue();
			set.add(value);
		}
		// 封装簇名
		String[] clusterNameArray = set.toArray(new String[dataBean.getClusterList().size()]);
		int[] countArray = new int[clusterNameArray.length];
		// 每个簇正确项个数的数组
		int[] correctArray = new int[clusterNameArray.length];
		for (int j = 0; j < countArray.length; j++) {
			correctArray[j] = 0;
		}
		List<ClusterBean> clusterList = dataBean.getClusterList();
		// 遍历每个簇，根据簇中元素所属于正确结果的最多值定一个初始的簇类
		for (int i = 0; i < clusterList.size(); i++) {
			// 计数器初始化
			for (int j = 0; j < countArray.length; j++) {
				countArray[j] = 0;
			}
			// 最多项出现的次数初始化
			maxSize = 0;
			// 簇中元素的List
			List<PointBean> pointList = clusterList.get(i).getPointList();
			// 遍历簇内元素，得到该簇的真实名字
			for (int j = 0; j < pointList.size(); j++) {
				String valueStr = "";
				Double[] testDoubleArray = dataBean.getClusterList().get(i).getPointList().get(j).getPoint();
				Set<Double[]> valueSet = map.keySet();
				int temp = 0;
				for (Iterator<Double[]> iter = valueSet.iterator(); iter.hasNext();) {
					int countSame = 0;
					Double[] valueArray = iter.next();
					for (int m = 0; m < valueArray.length; m++) {
						if (valueArray[m].doubleValue() == testDoubleArray[m].doubleValue()) {
							countSame++;
						}
					}
					if (countSame == valueArray.length) {
						valueStr = map.get(valueArray);
						dataBean.getPointList().get(temp).setPointName(valueStr);
						dataBean.getClusterList().get(i).getPointList().get(j).setPointName(valueStr);
						break;
					}
					temp++;
				}
				for (int m = 0; m < clusterNameArray.length; m++) {
					if (clusterNameArray[m].equals(valueStr)) {
						countArray[m]++;
					}
				}
			}
			for (int z = 0; z < countArray.length; z++) {
				if (countArray[z] >= maxSize) {
					maxSize = countArray[z];
					maxLoca = z;
				}
			}
			clusterNameReal[i] = clusterNameArray[maxLoca];
			correctArray[i] = maxSize;
		}
		
		System.out.println();
		System.out.println("-----------result-----------");
		for (int i = 0; i < correctArray.length; i++) {
			sum += correctArray[i];
			System.out.println("簇" + clusterNameReal[i] + "共有" + dataBean.getClusterList().get(i).getPointList().size()
					+ "项，其中正确项有" + correctArray[i] + "项；");
		}
		System.out.println("项的总数为：" + dataBean.getPointList().size() + "项");
		double result = sum * 1.0 / dataBean.getPointList().size() * 100;
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("正确率为：" + df.format(result) + "%");
	}
}
