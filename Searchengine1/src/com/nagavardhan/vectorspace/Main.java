//Lakshmi Naga vardhan Rao Pakalapati
package com.nagavardhan.vectorspace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.nagavardhan.vectorspace.cranfieldDocs.RetriveDocs500;

public class Main {
	static int A = 1, B = 1, C = 1, D = 1;
	public static String folder_path = "src\\src\\com\\nagavardhan\\vectorspace\\cranfieldDocs";
	public static Map<String, Integer> documentFrequencyOfWords = new ConcurrentHashMap<>();
	public static Map<String, Map<String, Double>> documentTermFrequencies = new HashMap<>();
	public static Map<String, Double> idf = new HashMap<>();
	public static int count_tot_doc = 0;
	public static TreeMap<Integer, ArrayList<Integer>> Q10 = new TreeMap<>();
	public static TreeMap<Integer, ArrayList<Integer>> Q50 = new TreeMap<>();
	public static TreeMap<Integer, ArrayList<Integer>> Q100 = new TreeMap<>();
	public static TreeMap<Integer, ArrayList<Integer>> Q500 = new TreeMap<>();
	public static Map<String, Double[]> tfidf = new HashMap<>();

	public static void main(String[] args) throws IOException, Exception {

		File direcroty = new File(folder_path);
		File[] files = direcroty.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				PreProcessor preProcessor = new PreProcessor(f, documentFrequencyOfWords);
				documentTermFrequencies.put(f.getName(), preProcessor.preProces());
				count_tot_doc++;
			}
		}

		// calculating idf
		idf = calculateIDF(documentFrequencyOfWords, count_tot_doc);

		// calculating tf-idf

		for (Entry<String, Map<String, Double>> termFrequency : documentTermFrequencies.entrySet()) {

			Double[] tfidfOfDoc = new Double[idf.size()];
			Map<String, Double> termFre = termFrequency.getValue();
			int i = 0;
			for (Entry<String, Double> map : idf.entrySet()) {

				String term = map.getKey();
				double idf = map.getValue();
				if (termFre.containsKey(term))

					tfidfOfDoc[i] = termFre.get(term) * idf;

				else
					tfidfOfDoc[i] = (double) 0;

				i++;
			}
			tfidf.put(termFrequency.getKey(), tfidfOfDoc);
		}

		// Processing the query

		System.out.println("Query Processing");
		BufferedReader reader = new BufferedReader(
				new FileReader("src\\src\\com\\nagavardhan\\vectorspace\\queries.txt"));
		String query;
		while ((query = reader.readLine()) != null) {

			PreProcessor preProcessor = new PreProcessor(query);
			Map<String, Double> queryTermFrequency = preProcessor.preProces();
			// calculating tf-idf of query
			Map<String, Double> cosineSimilarity = queryTfidfAndCosineSimilarity(queryTermFrequency);
			Map<String, Double> SortcosineSimilarity = sortByComparator(cosineSimilarity);
			printTop10(SortcosineSimilarity);
			printTop50(SortcosineSimilarity);
			printTop100(SortcosineSimilarity);
			printTop500(SortcosineSimilarity);
		}
		Retrieval10 r0 = new Retrieval10();
		r0.getRetrieval();
		Retrieval50 r1 = new Retrieval50();
		r1.getRetrieval();
		Retrieval100 r2 = new Retrieval100();
		r2.getRetrieval();
		Retrieval500 r3 = new Retrieval500();
		r3.getRetrieval();
		RetriveDocs10 rd10 = new RetriveDocs10();
		rd10.compute();
		RetriveDocs50 rd50 = new RetriveDocs50();
		rd50.compute();
		RetriveDocs100 rd100 = new RetriveDocs100();
		rd100.compute();
		RetriveDocs500 rd500 = new RetriveDocs500();
		rd500.compute();
		Relevance t = new Relevance();
		t.getRelevance();
		System.out.println("Precision and Recall for Top 10 Docs\n");
		System.out.println("Query-id" + "\t    Retrieve-Relavant" + "\t\t Precision" + "\t Recall");
		Precision_Recall10 PR10 = new Precision_Recall10();
		PR10.getPR();
		System.out.println("Precision and Recall for Top 50 Docs\n");
		System.out.println("Query-id" + "\t    Retrieve-Relavant" + "\t\t Precision" + "\t Recall");
		Precision_Recall50 PR50 = new Precision_Recall50();
		PR50.getPR();
		System.out.println("Precision and Recall for Top 100 Docs\n");
		System.out.println("Query-id" + "\t    Retrieve-Relavant" + "\t\t Precision" + "\t Recall");
		Precision_Recall100 PR100 = new Precision_Recall100();
		PR100.getPR();
		System.out.println("Precision and Recall for Top 500 Docs\n");
		System.out.println("Query-id" + "\t    Retrieve-Relavant" + "\t\t Precision" + "\t Recall");
		Precision_Recall500 PR500 = new Precision_Recall500();
		PR500.getPR();

	}

	public static Map<String, Double> calculateIDF(Map<String, Integer> documentFrequencyOfWords, int count_tot_doc) {

		Map<String, Double> idf = new HashMap<String, Double>();
		for (Entry<String, Integer> map : documentFrequencyOfWords.entrySet()) {

			String term = map.getKey();
			int value = map.getValue();
			idf.put(term, UtilityMethods.inverseDocumentFrequency(value, count_tot_doc));

		}
		return idf;
	}

	public static Map<String, Double> queryTfidfAndCosineSimilarity(Map<String, Double> termfrequency)
			throws IOException {
		double[] tf = new double[idf.size()];
		int i = 0;
		for (Entry<String, Double> ma : idf.entrySet()) {
			String term = ma.getKey();
			double idf = ma.getValue();
			if (termfrequency.containsKey(term)) {
				tf[i] = termfrequency.get(term) * idf;

			} else {
				tf[i] = (double) 0;
			}

			i++;
		}

		Map<String, Double> cosineSimilarity = new HashMap<>();
		for (Entry<String, Double[]> a : tfidf.entrySet()) {
			cosineSimilarity.put(a.getKey(), UtilityMethods.getCosineSimScore(tf, a.getValue()));
		}
		return UtilityMethods.sortByValue(cosineSimilarity);

	}

	public static void printTop10(Map<String, Double> k) {
		int j = 0;
		String x, s;
		ArrayList<Integer> a = new ArrayList<>();
		for (Map.Entry<String, Double> map : k.entrySet())

			// System.out.println(map.getKey().replaceAll("[a-z ]", ""));
			if (j < 10) {
				x = map.getKey().replaceAll("[a-z ]", "");
				s = x.replaceAll("^0+", "");
				a.add(Integer.parseInt(s));
				j++;
			} else {
				break;
			}

		Q10.put(A, a);
		A++;
	}

	public static void printTop50(Map<String, Double> k) {
		int j = 0;
		String x, s;
		ArrayList<Integer> a = new ArrayList<>();
		for (Map.Entry<String, Double> map : k.entrySet())

			// System.out.println(map.getKey().replaceAll("[a-z ]", ""));
			if (j < 50) {
				x = map.getKey().replaceAll("[a-z ]", "");
				s = x.replaceAll("^0+", "");
				a.add(Integer.parseInt(s));
				j++;
			} else {
				break;
			}

		Q50.put(B, a);
		B++;
	}

	public static void printTop100(Map<String, Double> k) {
		int j = 0;
		String x, s;
		ArrayList<Integer> a = new ArrayList<>();
		for (Map.Entry<String, Double> map : k.entrySet())

			// System.out.println(map.getKey().replaceAll("[a-z ]", ""));
			if (j < 100) {
				x = map.getKey().replaceAll("[a-z ]", "");
				s = x.replaceAll("^0+", "");
				a.add(Integer.parseInt(s));
				j++;
			} else {
				break;
			}

		Q100.put(C, a);
		C++;
	}

	public static void printTop500(Map<String, Double> k) {
		int j = 0;
		String x, s;
		ArrayList<Integer> a = new ArrayList<>();
		for (Map.Entry<String, Double> map : k.entrySet())

			// System.out.println(map.getKey().replaceAll("[a-z ]", ""));
			if (j < 500) {
				x = map.getKey().replaceAll("[a-z ]", "");
				s = x.replaceAll("^0+", "");
				a.add(Integer.parseInt(s));
				j++;
			} else {
				break;
			}

		Q500.put(D, a);
		D++;
	}

	public static Map<String, Double> sortByComparator(Map<String, Double> unsortMap) {
		// Convert Map to List
		List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		// Convert sorted map back to a Map
		Map<String, Double> sortedMap = new LinkedHashMap<>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
