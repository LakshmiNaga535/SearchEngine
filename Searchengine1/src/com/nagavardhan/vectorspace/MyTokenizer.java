//Lakshmi Naga vardhan Rao Pakalapati
package com.nagavardhan.vectorspace;

import java.util.Vector;

/* This is My Own Version of String Tokenizer that removes Punctuations and tokenizes on white space*/
public class MyTokenizer {

	public static String[] tokenize(String doc) {

		if (doc == null)
			return null;
		doc=doc.replaceAll("<.*?>","");
		String[] terms = doc.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		Porter stemmer = new Porter();
		Vector<String> tokens = new Vector<String>();
		for (String term : terms) {
			if (!term.isEmpty())
				term = stemmer.stripAffixes(term);
			if (!term.isEmpty())
				tokens.add(term);
		}
		return tokens.toArray(new String[0]);

	}


}
