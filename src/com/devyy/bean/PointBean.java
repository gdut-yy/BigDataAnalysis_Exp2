package com.devyy.bean;


/** 
 * 数据点实体，封装了具体的Cell中每行的数据点的具体的double值，以及数据点所在的簇和该簇的簇名 
 * 
 * @author ZYY
 * 
 */  
public class PointBean {
	private Double[] point;// 数据点
	private String clusterid;// 数据点所在的簇
	private String pointName;// 给数据点所对应的簇名

	public Double[] getPoint() {
		return point;
	}

	public void setPoint(Double[] point) {
		this.point = point;
	}

	public String getClusterid() {
		return clusterid;
	}

	public void setClusterid(String clusterid) {
		this.clusterid = clusterid;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}
}
