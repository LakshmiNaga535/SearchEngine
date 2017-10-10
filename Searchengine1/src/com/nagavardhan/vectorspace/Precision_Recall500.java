/*
 Lakshmi Naga vardhan Rao Pakalapati
 */
package com.nagavardhan.vectorspace;

import java.util.Map;

import com.nagavardhan.vectorspace.cranfieldDocs.RetriveDocs500;

/**
 *
 * @author pakal
 */
public class Precision_Recall500 {
    public void getPR() throws Exception{
        double recall=0, precision=0;
        for (Map.Entry<Integer, RelevanceCount> map : Relevance.r.entrySet()) {
            int g = map.getKey();
            Map<Integer, RelevanceCount> m = RetriveDocs500.E;
            RelevanceCount q = m.get(g);
            int relret = 0;
            for (Count c1 : map.getValue().a) {
                for (Count c2 : q.a) {
                    if (c1.Docid == c2.Docid) {
                        relret += 1;
                    }
                }
            }

            System.out.println((map.getKey() + 1) + " \t\t" + "\t\t" + relret + " \t\t\t" + (double) relret / q.a.size() + " \t   " + (double) relret / map.getValue().a.size());
            precision=precision+(double) relret / q.a.size();
            recall=recall+(double) relret / map.getValue().a.size();
        }
               System.out.println("Average Precision: "+(double)precision/10);
        System.out.println("Average recall: "+(double)recall/10);

    }
}
