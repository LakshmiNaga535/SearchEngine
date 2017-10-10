/*
 Lakshmi Naga vardhan Rao Pakalapati
 */
package com.nagavardhan.vectorspace;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author pakal
 */
public class Retrieval500 {
    public void getRetrieval() throws IOException
    {
    FileWriter f = new FileWriter("src\\src\\com\\nagavardhan\\vectorspace\\output500");
      for(Map.Entry<Integer,ArrayList<Integer>> a:Main.Q500.entrySet())
      {
                    for(int z:a.getValue())
                       f.write(z+"\n");
                }
    f.close();
    }


}
