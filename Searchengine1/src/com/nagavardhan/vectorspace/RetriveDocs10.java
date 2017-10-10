/*
 * To change this license header, choose License Headers in Project Properties.
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
public class RetriveDocs10 {
    public static TreeMap<Integer, RelevanceCount> E = new TreeMap<>();
    public void compute() throws Exception {
        FileReader f = new FileReader("src\\src\\com\\nagavardhan\\vectorspace\\output10");
        BufferedReader b = new BufferedReader(f);
        int i = 0, j = 0;
        String line = b.readLine();
        RelevanceCount re = new RelevanceCount();
        while (line != null) {
            if ((i % 10) == 0) {
                re = new RelevanceCount();
                E.put(j, re);
                j++;
            }
            re.a.add(new Count(Integer.parseInt(line)));
            i++;
            line = b.readLine();
        }
        b.close();
    }
}
