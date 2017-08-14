package com.dada.mylibrary.Util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by wpf on 9-23-0023.
 */
public class ReadFile {

    public static String Read(String filePath,String fileName) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader bre = new BufferedReader(new FileReader(filePath + fileName));
        String str;
        while ((str = bre.readLine()) != null) {
            result.append(str);
        }
        return result.toString();
    }
}
