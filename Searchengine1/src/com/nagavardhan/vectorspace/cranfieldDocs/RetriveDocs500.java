/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nagavardhan.vectorspace.cranfieldDocs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;

import com.nagavardhan.vectorspace.Count;
import com.nagavardhan.vectorspace.RelevanceCount;;

/**
 *
 * @author pakal
 */
public class RetriveDocs500 {
    public static TreeMap<Integer, RelevanceCount> E = new TreeMap<>();
    public void compute() throws Exception {
        FileReader f = new FileReader("C:\\Users\\pakal\\Desktop\\VectorSpace\\src\\src\\com\\nagavardhan\\vectorspace\\output500");
        BufferedReader b = new BufferedReader(f);
        int i = 0, j = 0;
        String line = b.readLine();
        RelevanceCount re = new RelevanceCount();
        while (line != null) {
            if ((i % 500) == 0) {
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
