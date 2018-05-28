package com.devyy.bean;

import java.util.List;

/** 
 * DataVO实体类，封装了簇的list以及数据点集的list 
 * 
 * @author ZYY
 * 
 */ 
public class DataBean {
	private List<ClusterBean> clusterList;// 簇
	private List<PointBean> pointList;// 数据点集

	public List<ClusterBean> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<ClusterBean> clusterList) {
		this.clusterList = clusterList;
	}

	public List<PointBean> getPointList() {
		return pointList;
	}

	public void setPointList(List<PointBean> pointList) {
		this.pointList = pointList;
	}
}
