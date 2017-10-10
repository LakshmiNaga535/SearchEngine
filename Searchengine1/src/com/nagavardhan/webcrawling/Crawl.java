package com.nagavardhan.webcrawling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/*This class gets all webpages from a root webpage in bfs fashion*/
public class Crawl {

	public static List<String> getAllURLs(String url) {

		Document doc = null;
		Elements links = null;
		List<String> urls = new ArrayList<>();
		try {
			doc = Jsoup.connect(url).get();
			links = doc.select("a[href]");

			Iterator<Element> link = links.iterator();
			while (link.hasNext()) {
				String docurl = link.next().attr("abs:href");
				if (docurl.contains("unt.edu")) urls.add(docurl);
			}
		} catch (IOException e) {
			System.out.println("Timeout for " + url);
		}

		return urls;
	}

	public static Set<String> bfs(String url) {

		Queue<String> queue = new LinkedList<>();
		Set<String> processedUrls = new HashSet<String>();
		List<String> a = getAllURLs(url);
		queue.addAll(a);
		while (!queue.isEmpty() && processedUrls.size() <= 3000) {
			String now_url = queue.poll();
			if (!processedUrls.contains(now_url)) {

				System.out.println(now_url);
				processedUrls.add(now_url);
				queue.addAll(getAllURLs(now_url));
			}
		}
		System.out.println(processedUrls.size());

		return processedUrls;
	}

	public static void processURL(String url) {

	}

	public static void main(String[] args) {

		String html = "<html><head><title>First parse</title></head>"
				+ "<body><p>Parsed HTML into a doc.</p></body></html>";
		Document doc = Jsoup.parse(html);
		System.out.println(doc.text());

		// neglect if it contains #

	}

}
