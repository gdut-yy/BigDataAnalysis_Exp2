package com.devyy.util;

import java.util.Iterator;
import java.util.List;

import com.devyy.bean.ClusterBean;
import com.devyy.bean.DataBean;
import com.devyy.bean.PointBean;

public class PrintUtil {
	/**
	 * 打印每个簇中的具体内容
	 * 
	 * @param dataBean——DataBean实体
	 */
	public void printClusterContents(DataBean dataBean) {
		List<ClusterBean> clusterList = dataBean.getClusterList();
		// 遍历每个簇
		for (int i = 0; i < clusterList.size(); i++) {
			System.out.println("第" + (i + 1) + "个簇共有" + clusterList.get(i).getPointList().size() + "项，内容如下：");
			ClusterBean cluster = clusterList.get(i);// 得到该簇
			List<PointBean> pointList = cluster.getPointList();// 簇内元素的list
			// 遍历簇内元素
			for (Iterator<PointBean> iter = pointList.iterator(); iter.hasNext();) {
				PointBean pointVO = iter.next();
				Double[] valueArray = pointVO.getPoint();
				for (int j = 0; j < valueArray.length - 1; j++) {
					System.out.print(valueArray[j] + ", ");
				}
				System.out.println(valueArray[valueArray.length - 1]);
			}
		}
	}
}
