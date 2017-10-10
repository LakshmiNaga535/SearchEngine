/*
 Lakshmi Naga vardhan Rao Pakalapati
 */
package com.nagavardhan.vectorspace;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

/**
 *
 * @author pakal
 */
public class Relevance {
    public static TreeMap<Integer, RelevanceCount> r = new TreeMap<>();
    public void getRelevance() throws Exception {
        FileReader f = new FileReader("src\\src\\com\\nagavardhan\\vectorspace\\relevance.txt");
        BufferedReader b = new BufferedReader(f);
        String line = b.readLine();
        int i = 0;
        RelevanceCount re = new RelevanceCount();
        while (line != null) {
            String a[] = line.split(" ");
            if (!(Integer.parseInt(a[0]) == i)) {
                re = new RelevanceCount();
                r.put(i, re);
                i++;
            }
            re.a.add(new Count(Integer.parseInt(a[1])));
            line = b.readLine();
        }
        b.close();
    }
    
}
