//Lakshmi Naga vardhan Rao Pakalapati
package com.nagavardhan.vectorspace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PreProcessor {

	static Map<String, Integer> documentwordFrequency;
	private static File f = null;
	private String str = null;

	public PreProcessor(File f, Map<String, Integer> docFrquency) {
		PreProcessor.documentwordFrequency = docFrquency;
		PreProcessor.f = f;
	}

	public PreProcessor(String s) {
		this.str = s;
	}

	public Map<String, Double> preProces() throws IOException {
		if (str != null)
			return preProcesFromString();
		return preProcesFromFile();

	}

	private Map<String, Double> preProcesFromFile() throws IOException {

		String[] filewords = null;
		BufferedReader reader = null;
		Map<String, Integer> docWords = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = reader.readLine()) != null) {
				filewords = MyTokenizer.tokenize(line);
				for (String word : filewords) {

					if (docWords.containsKey(word))
						docWords.put(word, docWords.get(word) + 1);
					else
						docWords.put(word, 1);

				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			reader.close();
		}
		Map<String, Double> termFrequencyMap = new HashMap<>();
		int maxFrequency = 0;
		for (Entry<String, Integer> map : docWords.entrySet()) {

			if (map.getValue() > maxFrequency)
				maxFrequency = map.getValue();

		}

		for (Entry<String, Integer> map : docWords.entrySet()) {

			String word = map.getKey();
			double termFrequency = UtilityMethods.calculateTermFrequency(map.getValue(), maxFrequency);
			termFrequencyMap.put(word, termFrequency);

		}
		for (String key : termFrequencyMap.keySet()) {
			if (key != null) {
				if (documentwordFrequency.containsKey(key))
					documentwordFrequency.put(key, documentwordFrequency.get(key) + 1);
				else
					documentwordFrequency.put(key, 1);
			}
		}

		return termFrequencyMap;

	}

	private Map<String, Double> preProcesFromString() {

		String[] filewords = null;
		Map<String, Integer> docWords = new HashMap<>();
		filewords = MyTokenizer.tokenize(str);
		for (String word : filewords) {

			if (docWords.containsKey(word))
				docWords.put(word, docWords.get(word) + 1);
			else
				docWords.put(word, 1);

		}

		Map<String, Double> termFrequencyMap = new HashMap<>();
		int maxFrequency = 0;
		for (Entry<String, Integer> map : docWords.entrySet()) {

			if (map.getValue() > maxFrequency)
				maxFrequency = map.getValue();

		}

		for (Entry<String, Integer> map : docWords.entrySet()) {

			String word = map.getKey();
			double termFrequency = UtilityMethods.calculateTermFrequency(map.getValue(), maxFrequency);
			termFrequencyMap.put(word, termFrequency);

		}

		return termFrequencyMap;

	}

}
