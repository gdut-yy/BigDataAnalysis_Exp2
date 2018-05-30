package com.devyy.bean;

import java.util.List;

/** 
 * 簇实体，封装了簇心和该簇中的簇内元素集 
 * 
 * @author ZYY
 * 
 */  
public class ClusterBean {
	private String clusterid;
	private Double[] clusterCenter;// 簇心
	private List<PointBean> pointList;// 簇内元素

	public String getClusterid() {
		return clusterid;
	}

	public void setClusterid(String clusterid) {
		this.clusterid = clusterid;
	}

	public Double[] getClusterCenter() {
		return clusterCenter;
	}

	public void setClusterCenter(Double[] clusterCenter) {
		this.clusterCenter = clusterCenter;
	}

	public List<PointBean> getPointList() {
		return pointList;
	}

	public void setPointList(List<PointBean> pointList) {
		this.pointList = pointList;
	}
}
