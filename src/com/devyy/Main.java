package com.devyy;

import com.devyy.strategy.ClusteringIntf;
import com.devyy.strategy.KMeansClustering;

public class Main {
	public static void main(String[] args) {
		ClusteringIntf clustering = new KMeansClustering();
		clustering.clusterAlgorithm();
	}
}
