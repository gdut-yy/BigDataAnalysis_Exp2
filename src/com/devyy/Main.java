package com.devyy;

import com.devyy.factory.ChooserFactory;

public class Main {
	public static void main(String[] args) {
		new ChooserFactory().runAlgorithm("KMeans");
//		new ChooserFactory().runAlgorithm("Hierarchical Clustering");
	}
}
