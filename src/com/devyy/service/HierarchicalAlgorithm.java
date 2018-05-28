package com.devyy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.devyy.bean.ClusterBean;
import com.devyy.bean.DataBean;
import com.devyy.bean.PointBean;

import jxl.Cell;

/** 
 * 层次聚类算法 
 * 
 * @author ZYY
 * 
 */  
public class HierarchicalAlgorithm {
	// 定义最大欧式距离为5000
	public static final double MAXLENGTH = 5000.0;

	/**
	 * 初始化层次聚类算法的DataVO实体
	 * 
	 * @param cellList
	 *            封装了Excel中一行行数据的List
	 * @return
	 */
	public static DataBean initDataVO(List<Cell[]> cellList) {
		int i, j;
		DataBean dataVO = new DataBean();
		List<ClusterBean> clusterList = new ArrayList<ClusterBean>();
		List<PointBean> pointList = new ArrayList<PointBean>();
		Cell[] cell = new Cell[cellList.get(0).length];
		for (i = 0; i < cellList.size(); i++) {
			cell = cellList.get(i);
			Double[] point = new Double[cellList.get(0).length];
			for (j = 0; j < cell.length; j++) {
				point[j] = Double.valueOf(cell[j].getContents());
			}
			List<PointBean> clusterPointList = new ArrayList<PointBean>();
			ClusterBean cluster = new ClusterBean();
			PointBean pointVO = new PointBean();
			pointVO.setPoint(point);
			pointVO.setPointName(null);
			String clusterId = UUID.randomUUID().toString();
			pointVO.setClusterid(clusterId);
			clusterPointList.add(pointVO);
			cluster.setClusterCenter(point);
			cluster.setClusterid(clusterId);
			cluster.setPointList(clusterPointList);
			clusterList.add(cluster);
			pointList.add(pointVO);
		}
		dataVO.setClusterList(clusterList);
		dataVO.setPointList(pointList);
		return dataVO;
	}

	/**
	 * 簇合并
	 * 
	 * @param dataBean
	 *            DataVO实体
	 * @return 修改后的DataVO实体
	 */
	public static DataBean mergeCluster(DataBean dataBean) {
		double max = MAXLENGTH;
		// 定义一个临时数组
		Double[] tempArray = new Double[dataBean.getClusterList().get(0).getClusterCenter().length];
		// 定义要合并的两个簇的下标
		int clusterLoca1 = 0, clusterLoca2 = 0;
		int j, m, count, n, p;
		double sum;
		// 遍历每个簇
		for (int i = 0; i < dataBean.getClusterList().size(); i++) {
			// 得到第一个簇的中心点
			Double[] clusterCenter1 = dataBean.getClusterList().get(i).getClusterCenter();
			for (int k = i + 1; k < dataBean.getClusterList().size(); k++) {
				sum = 0.0;
				// 得到第二个簇的中心点
				Double[] clusterCenter2 = dataBean.getClusterList().get(k).getClusterCenter();
				// 将平方值保存在一个temp数组，求未开根号的欧式距离
				for (j = 0; j < tempArray.length; j++) {
					tempArray[j] = Math.pow(clusterCenter1[j] - clusterCenter2[j], 2);
					sum += tempArray[j].doubleValue();
				}
				if (sum < max) {
					max = sum;
					clusterLoca1 = i;// 第一个簇的位置
					clusterLoca2 = k;// 第二个簇的位置
				}
			}
		}
		// 合并两个簇
		String clusterid = UUID.randomUUID().toString();
		ClusterBean cluster1 = dataBean.getClusterList().get(clusterLoca1);
		// 遍历第一个簇的全集，更新其所在dataVO中的数据点的簇id
		for (m = 0; m < cluster1.getPointList().size(); m++) {
			count = 0;
			Double[] pointValueArray = cluster1.getPointList().get(m).getPoint();
			List<PointBean> everypoint = dataBean.getPointList();
			for (n = 0; n < everypoint.size(); n++) {
				Double[] everypointValue = everypoint.get(n).getPoint();
				for (p = 0; p < everypointValue.length; p++) {
					if (pointValueArray[p].doubleValue() == everypointValue[p].doubleValue()) {
						count++;
					}
				}
				if (count == everypointValue.length) {
					PointBean newpoint1 = everypoint.get(n);
					newpoint1.setClusterid(clusterid);
					dataBean.getPointList().set(n, newpoint1);
					break;
				}
			}
		}
		// 更新簇中的数据的簇id
		for (m = 0; m < cluster1.getPointList().size(); m++) {
			PointBean point = cluster1.getPointList().get(m);
			point.setClusterid(clusterid);
			cluster1.getPointList().set(m, point);
		}
		ClusterBean cluster2 = dataBean.getClusterList().get(clusterLoca2);
		// 遍历第二个簇的全集，更新其所在dataVO中的簇id
		for (m = 0; m < cluster2.getPointList().size(); m++) {
			count = 0;
			Double[] pointValueArray = cluster2.getPointList().get(m).getPoint();
			List<PointBean> everypoint = dataBean.getPointList();
			for (n = 0; n < everypoint.size(); n++) {
				Double[] everypointValue = everypoint.get(n).getPoint();
				for (p = 0; p < everypointValue.length; p++) {
					if (pointValueArray[p].doubleValue() == everypointValue[p].doubleValue()) {
						count++;
					}
				}
				if (count == everypointValue.length) {
					PointBean newpoint2 = everypoint.get(n);
					newpoint2.setClusterid(clusterid);
					dataBean.getPointList().set(n, newpoint2);
					break;
				}
			}
		}
		// 更新簇中的数据的簇id
		for (m = 0; m < cluster2.getPointList().size(); m++) {
			PointBean point = cluster2.getPointList().get(m);
			point.setClusterid(clusterid);
			cluster2.getPointList().set(m, point);
		}
		ClusterBean newCluster = new ClusterBean();
		List<PointBean> newPointList = new ArrayList<PointBean>();
		newPointList.addAll(cluster1.getPointList());
		newPointList.addAll(cluster2.getPointList());
		Double[] clusterCenter1 = cluster1.getClusterCenter();
		Double[] clusterCenter2 = cluster2.getClusterCenter();
		Double[] newCenter = new Double[clusterCenter1.length];
		for (int i = 0; i < clusterCenter1.length; i++) {
			newCenter[i] = (clusterCenter1[i] * cluster1.getPointList().size()
					+ clusterCenter2[i] * cluster2.getPointList().size())
					/ (cluster1.getPointList().size() + cluster2.getPointList().size());
		}
		newCluster.setClusterCenter(newCenter);
		newCluster.setClusterid(clusterid);
		newCluster.setPointList(newPointList);
		dataBean.getClusterList().set(clusterLoca1, newCluster);
		dataBean.getClusterList().remove(clusterLoca2);
		return dataBean;
	}
}
