package com.devyy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.devyy.bean.ClusterBean;
import com.devyy.bean.DataBean;
import com.devyy.bean.PointBean;

import jxl.Cell;

/** 
 * K-Means算法 
 * 
 * @author ZYY
 * 
 */  
public class KMeans {
	// 定义最大欧式距离为5000
	public static final double MAXLENGTH = 5000.0;

	/**
	 * 计算新的簇中心
	 * @param dataBean——DataBean实体类
	 * @return 更新后的DataBean实体
	 */
	public DataBean countClusterCenter(DataBean dataBean) {
		List<ClusterBean> clusterList = dataBean.getClusterList();
		List<ClusterBean> newClusterList = new ArrayList<ClusterBean>();
		int i, j, p;
		for (i = 0; i < clusterList.size(); i++) {
			ClusterBean cluster = clusterList.get(i);
			List<PointBean> pointList = cluster.getPointList();
			Double[] countArray = new Double[clusterList.get(0).getPointList().get(0).getPoint().length];
			for (j = 0; j < countArray.length; j++) {
				countArray[j] = 0.0;
			}
			for (j = 0; j < pointList.size(); j++) {
				PointBean point = pointList.get(j);
				Double[] pointValue = point.getPoint();
				for (p = 0; p < pointValue.length; p++) {
					countArray[p] = pointValue[p] + countArray[p];
				}
			}
			for (j = 0; j < countArray.length; j++) {
				countArray[j] /= pointList.size();
			}
			cluster.setClusterCenter(countArray);
			newClusterList.add(cluster);
		}
		dataBean.setClusterList(newClusterList);
		return dataBean;
	}

	/**
	 * 将对象指派到与其距离最近的簇
	 * @param dataBean——DataBean实体
	 * @param pointBean——数据点
	 * @return 修改后的dataBean实体
	 */
	public DataBean distributeIntoCluster(DataBean dataBean, PointBean pointBean) {
		double sum = 0.0, max = MAXLENGTH;
		// loca存放在原先簇中的位置，locaRecord存放是在哪个簇
		int locaRecord = 0, loca = 0;
		int i, j, count, n, m;
		List<ClusterBean> clusterList = dataBean.getClusterList();
		List<PointBean> pointList = dataBean.getPointList();
		List<PointBean> clusterPointList = null;
		Double[] distanceArray = new Double[clusterList.size()];
		// 获取数据点内容
		Double[] pointValueArray = pointBean.getPoint();
		Double[] tempArray = new Double[pointValueArray.length];
		// 遍历每一个簇
		for (i = 0; i < clusterList.size(); i++) {
			sum = 0.0;
			// 得到该簇的中心点
			Double[] clusterCenter = clusterList.get(i).getClusterCenter();
			// 将平方值保存在一个temp数组
			for (j = 0; j < pointValueArray.length; j++) {
				tempArray[j] = Math.pow(clusterCenter[j] - pointValueArray[j], 2);
			}
			// 求欧式距离
			for (j = 0; j < tempArray.length; j++) {
				sum += tempArray[j];
			}
			// 将结果保存在距离数组中
			distanceArray[i] = Math.sqrt(sum);
		}
		// 遍历距离数组，找到要插入的簇
		for (i = 0; i < distanceArray.length; i++) {
			if (distanceArray[i] < max) {
				max = distanceArray[i];
				locaRecord = i;
			}
		}
		// 获得该簇
		ClusterBean cluster = clusterList.get(locaRecord);
		// 找到簇中的该元素
		for (i = 0; i < pointList.size(); i++) {
			if (pointList.get(i).equals(pointBean)) {
				loca = i;
				break;
			}
		}
		// 在同一个簇，不做任何处理
		if (cluster.getClusterid().equals(pointBean.getClusterid())) {
			return dataBean;
		}
		// 这个数据不在任何一个簇，加进来
		else if (pointBean.getClusterid() == null) {
			clusterPointList = cluster.getPointList();
		}
		// 在不同的簇中
		else {
			clusterPointList = cluster.getPointList();
			// 遍历每个簇，找到该元素
			for (i = 0; i < clusterList.size(); i++) {
				boolean flag = false;
				// 遍历每个簇中元素
				for (m = 0; m < clusterList.get(i).getPointList().size(); m++) {
					PointBean everypoint = clusterList.get(i).getPointList().get(m);
					Double[] everypointValue = everypoint.getPoint();
					count = 0;
					for (n = 0; n < everypointValue.length; n++) {
						if (pointValueArray[n].doubleValue() == everypointValue[n].doubleValue()) {
							count++;
						}
					}
					if (count == everypointValue.length) {
						clusterList.get(i).getPointList().remove(m);
						flag = true;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		}
		// 设置数据点的所在簇位置
		pointBean.setClusterid(cluster.getClusterid());
		// 更新dataBean中的数据点信息
		pointList.set(loca, pointBean);
		// 将数据点加入到簇的数据点集中
		clusterPointList.add(pointBean);
		// 将数据点集加入到簇中
		cluster.setPointList(clusterPointList);
		// 更新dataBean中的簇信息
		clusterList.set(locaRecord, cluster);
		// 将簇信息放入dataBean中
		dataBean.setClusterList(clusterList);
		// 将数据点集信息放入到dataBean中
		dataBean.setPointList(pointList);
		return dataBean;
	}

	/**
	 * 初始化DataBean
	 * @param cellList——封装了Excel表中一行行数据的list
	 * @param k——k-means算法中的k
	 * @return 修改后的DataBean实体
	 */
	public DataBean initDataBean(List<Cell[]> cellList, int k) {
		int i, j;
		DataBean dataBean = new DataBean();
		List<PointBean> pointList = new ArrayList<PointBean>();
		List<ClusterBean> clusterList = new ArrayList<ClusterBean>();
		List<ClusterBean> newClusterList = new ArrayList<ClusterBean>();
		Cell[] cell = new Cell[cellList.get(0).length];
		// 将所有元素加入到DataBean中管理以及加入PointBean中
		for (i = 0; i < cellList.size(); i++) {
			cell = cellList.get(i);
			Double[] point = new Double[cellList.get(0).length];
			for (j = 0; j < cell.length; j++) {
				point[j] = Double.valueOf(cell[j].getContents());
			}
			PointBean pointBean = new PointBean();
			pointBean.setPoint(point);
			pointBean.setPointName(null);
			if (i < k) {
				String clusterid = UUID.randomUUID().toString();
				pointBean.setClusterid(clusterid);
				ClusterBean cluster = new ClusterBean();
				cluster.setClusterid(clusterid);
				clusterList.add(cluster);
			} else {
				pointBean.setClusterid(null);
			}
			pointList.add(pointBean);
		}
		dataBean.setPointList(pointList);
		// 将前k个点作为k个簇
		for (i = 0; i < k; i++) {
			cell = cellList.get(i);
			Double[] point = new Double[cellList.get(0).length];
			for (j = 0; j < cell.length; j++) {
				point[j] = Double.valueOf(cell[j].getContents());
			}
			ClusterBean cluster = clusterList.get(i);
			cluster.setClusterCenter(point);
			List<PointBean> clusterPointList = new ArrayList<PointBean>();
			PointBean pointVO = new PointBean();
			pointVO.setPoint(point);
			clusterPointList.add(pointVO);
			cluster.setPointList(clusterPointList);
			newClusterList.add(cluster);
		}
		dataBean.setClusterList(newClusterList);
		return dataBean;
	}
}
