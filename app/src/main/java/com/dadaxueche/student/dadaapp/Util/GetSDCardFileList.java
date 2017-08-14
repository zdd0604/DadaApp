package com.dadaxueche.student.dadaapp.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wpf on 8-31-0031.
 */
public class GetSDCardFileList {
    public static List<String> getFileList(String _filePath) {
        List<String> arrayList_List = new ArrayList<>();
        File file = new File(_filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File fl : files) {
                    if (!fl.isDirectory()) {
                        arrayList_List.add(fl.getName());
                    }
                }
            }
        }
        return arrayList_List;
    }
}
