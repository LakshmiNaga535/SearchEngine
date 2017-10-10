package com.nagavardhan.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistData {

	private static SQLLite sqllite = SQLLite.getInstance();

	public static void createRequiredTables() {

		String idfSql = "CREATE TABLE IF NOT EXISTS IDF" + "(WORD CHAR(250) NOT NULL," + "FREQUENCY REAL NOT NULL)";
		String tdidfSql = "CREATE TABLE IF NOT EXISTS TFIDF" + "(WORD CHAR(250) NOT NULL,"
				+ "FREQUENCIS TEXT NOT NULL)";
		sqllite.executeQuery(idfSql);
		sqllite.executeQuery(tdidfSql);
	}

	public static void insertIntoIDF(String word, double frequency) {

		String sql = "INSERT INTO IDF (WORD,FREQUENCY) " + "VALUES ('" + word + "','" + frequency + "');";
		sqllite.executeQuery(sql);

	}

	public static void insertIntoTFIDF(String word, Double[] frequency) {

		String frequencies = Arrays.toString(frequency);
		String sql = "INSERT INTO TFIDF (WORD,FREQUENCIS) " + "VALUES ('" + word + "','" + frequencies + "');";
		sqllite.executeQuery(sql);

	}

	public static Map<String, Double> getIDF() {
		Map<String, Double> idf = new HashMap<>();
		ResultSet rs = sqllite.executeQueryResultSet("SELECT * FROM IDF");
		try {
			while (rs.next()) {
				String word = rs.getString("WORD");
				double frequency = rs.getDouble("FREQUENCY");
				idf.put(word, frequency);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idf;
	}

	public static List<String> getAllWords() {
		List<String> words = new ArrayList<>();
		ResultSet rs = sqllite.executeQueryResultSet("SELECT WORD FROM IDF");
		try {
			while (rs.next()) {
				String word = rs.getString("WORD");
				words.add(word);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return words;
	}

	public static Map<String, Double[]> getTFIDF() {
		Map<String, Double[]> tfidf = new HashMap<>();
		ResultSet rs = sqllite.executeQueryResultSet("SELECT * FROM TFIDF");
		try {
			while (rs.next()) {
				String word = rs.getString("WORD");
				String text = rs.getString("FREQUENCIS");
				String[] frequencies = text.substring(1, text.length() - 1).split(",");
				Double[] frequency = new Double[frequencies.length];
				int i = 0;
				for (String b : frequencies)
					frequency[i++] = Double.parseDouble(b);

				tfidf.put(word, frequency);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tfidf;
	}

	public static void dropAllTables() {
		String sql = "DROP TABLE IF EXISTS test.IDF;";
		String tfidf = "DROP TABLE IF EXISTS test.TFIDF;";
		try {
			sqllite.executeQuery(sql);
			sqllite.executeQuery(tfidf);
		} catch (Exception e) {

		}

	}

	public static void main(String[] args) {

		System.out.println(getAllWords());

	}

}
