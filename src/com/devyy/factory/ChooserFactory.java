package com.devyy.factory;

import com.devyy.strategy.ClusteringIntf;
import com.devyy.strategy.HierarchicalClustering;
import com.devyy.strategy.KMeansClustering;

public class ChooserFactory {
	/**
	 * 运行聚类算法
	 * 
	 * @param algorithmName
	 */
	public void runAlgorithm(String algorithmName) {
		long startTime = System.currentTimeMillis();
		ClusteringIntf clustering = null;
		algorithmName = algorithmName.toLowerCase();
		switch (algorithmName) {
		case "kmeans":
			clustering = new KMeansClustering();
			break;
		case "hierarchical clustering":
			clustering = new HierarchicalClustering();
			break;
		}
		clustering.clusterAlgorithm();
		long endTime = System.currentTimeMillis();
		System.out.println(algorithmName + "算法共耗时：" + (endTime - startTime) * 1.0 / 1000 + "s");
		System.out.println("------------------------------------------------");
	}
}
