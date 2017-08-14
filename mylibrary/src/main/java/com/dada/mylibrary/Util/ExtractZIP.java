package com.dada.mylibrary.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by wpf on 9-18-0018.
 */
public class ExtractZIP {

    private String questionExtractName = "";

    public File getZipFile(String filePath,String fileName) {
        return new File(filePath + fileName);
    }

    public boolean Extract(String filePath,File zipFile) {
        try {
            ZipFile zFile = new ZipFile(zipFile);
            Enumeration zList = zFile.entries();
            while (zList.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry)zList.nextElement();
                if(zipEntry.isDirectory()) {
                    Check.CheckDir(filePath + zipEntry.getName());
                } else {
                    InputStream is = zFile.getInputStream(zipEntry);
                    questionExtractName = zipEntry.getName();
                    WriteFile.Write(is,filePath, questionExtractName);
                }
            }
            zFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getQuestionExtractName() {
        return questionExtractName;
    }
}
