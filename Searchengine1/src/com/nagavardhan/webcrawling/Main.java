//Lakshmi Naga vardhan Rao Pakalapati
package com.nagavardhan.webcrawling;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Jsoup;

import com.nagavardhan.db.PersistData;
import com.nagavardhan.vectorspace.UtilityMethods;

public class Main {

	public static Map<String, Integer> documentFrequencyOfWords = new ConcurrentHashMap<>();
	public static Map<String, Map<String, Double>> documentTermFrequencies = new LinkedHashMap<>();
	public static Map<String, Double> idf = new HashMap<>();
	public static int count_tot_doc = 0;
	public static Map<String, Double[]> tfidf = new HashMap<>();
	public static boolean initialized = false;

	public static void crawl() {

		Set<String> links = Crawl.bfs("http://www.unt.edu");
		PersistData.dropAllTables();
		PersistData.createRequiredTables();
		for (String link : links) {

			try {
				System.out.println("Processing " + link);
				PreProcessor preProcessor = new PreProcessor(Jsoup.parse(new URL(link), 5000),
						documentFrequencyOfWords);
				documentTermFrequencies.put(link, preProcessor.preProces());
				count_tot_doc++;
			} catch (Exception e) {
				System.out.println("Unable to parse " + link);
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
			PersistData.insertIntoTFIDF(termFrequency.getKey(), tfidfOfDoc);

		}

	}

	public static void initializeFromDB() {
		if (!initialized) {
			idf = PersistData.getIDF();
			tfidf = PersistData.getTFIDF();
			initialized = true;
			System.out.println("initialized from DB");
		}
	}

	public static List<String> hadleQuery(String query) {

		initializeFromDB();
		PreProcessor preProcessor = new PreProcessor(query);
		Map<String, Double> queryTermFrequency = null;
		Map<String, Double> cosineSimilarity = null;
		try {
			queryTermFrequency = preProcessor.preProces();
			cosineSimilarity = queryTfidfAndCosineSimilarity(queryTermFrequency);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Double> SortcosineSimilarity = sortByComparator(cosineSimilarity);
		List<String> links = new ArrayList<>();
		for (Entry<String, Double> entry : SortcosineSimilarity.entrySet()) {

			if (!entry.getValue().isNaN()) links.add(entry.getKey());
		}

		return links;
	}

	public static void main(String[] args) throws IOException, Exception {

		Scanner in = new Scanner(System.in);
		System.out.println("Enter yes if you want crawl webpages?? else it will load from db");
		String ans = in.nextLine();
		if (ans.equalsIgnoreCase("yes"))
			crawl();
		else {
			// getting data from db
			idf = PersistData.getIDF();
			tfidf = PersistData.getTFIDF();
		}
		// Processing the query
		System.out.println("Query Processing");
		System.out.println("Enter the Query ");
		String query = in.nextLine();
		while (query != null && query.length() > 0) {

			PreProcessor preProcessor = new PreProcessor(query);
			Map<String, Double> queryTermFrequency = preProcessor.preProces();
			// calculating tf-idf of query
			Map<String, Double> cosineSimilarity = queryTfidfAndCosineSimilarity(queryTermFrequency);
			Map<String, Double> SortcosineSimilarity = sortByComparator(cosineSimilarity);
			printTop10(SortcosineSimilarity);
			System.out.println("Enter Next query");
			query = in.nextLine();
		}

	}

	public static Map<String, Double> calculateIDF(Map<String, Integer> documentFrequencyOfWords, int count_tot_doc) {

		Map<String, Double> idf = new HashMap<String, Double>();
		for (Entry<String, Integer> map : documentFrequencyOfWords.entrySet()) {

			String term = map.getKey();
			int value = map.getValue();
			double fre = UtilityMethods.inverseDocumentFrequency(value, count_tot_doc);
			idf.put(term, fre);
			PersistData.insertIntoIDF(term, fre);

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
		for (Map.Entry<String, Double> map : k.entrySet()) {

			System.out.println(map.getKey());

		}
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
