/*
 Lakshmi Naga vardhan Rao Pakalapati
 */
package com.nagavardhan.vectorspace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UtilityMethods {

	/* This process is to load stop words from the file */
	public static List<String> loadStopWords(String path) throws IOException {
		List<String> stopwords = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		while ((line = reader.readLine()) != null)
			stopwords.add(line);

		return stopwords;
	}

	public static double calculateTermFrequency(int frequency, int maxFrequency) {

		return ((double) frequency) / ((double) maxFrequency);
	}

	public static double inverseDocumentFrequency(int documentFrequency, int N) {

		return Math.log(((double) N) / ((double) documentFrequency)) / Math.log(2);
	}

	public static Map sortByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static double getCosineSimScore(double[] docVector1, Double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		if(docVector1.length==docVector2.length)
		System.out.println(docVector1.length);
		int index = Math.min(docVector1.length, docVector2.length);
		for (int i = 0; i < index; i++) // docVector1 and docVector2
										// must be of same length
		{
			dotProduct += docVector1[i] * docVector2[i]; // a.b
			magnitude1 += Math.pow(docVector1[i], 2); // (a^2)
			magnitude2 += Math.pow(docVector2[i], 2); // (b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);// sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);// sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}

	/* This Method is to sort the map */
	public static Map<String, Integer> sortTheGivenMap(Map<String, Integer> unsortMap) {

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/* this method returns the top n words in the given Map */
	public static ArrayList<String> topNFromGivenMap(Map<String, Integer> sortedMap, int n) {

		ArrayList<String> stopWordsinTop = new ArrayList<>();
		List<Entry<String, Integer>> entryList = new ArrayList<>(sortedMap.entrySet());
		for (int i = 0; i < n; i++) {
			String wor = entryList.get(i).getKey();
			stopWordsinTop.add(wor);
		}
		return stopWordsinTop;
	}

	/* this method returns the stop words in the given list */

	public static List<String> getStopWordsInGivenList(List<String> words, List<String> stopwords) {
		List<String> stopwordsInList = new ArrayList<String>();
		for (String str : words)
			if (stopwords.contains(str)) stopwordsInList.add(str);
		return stopwordsInList;
	}

}
