package com.dadaxueche.student.dadaapp.Util;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2015-10-12
 * Time: 08:42
 */
public class ResultEvent {
    public static boolean downflag;
    public static int itemindex;

    public ResultEvent(int itemindex, boolean downflag) {
        this.downflag = downflag;
        this.itemindex = itemindex;
    }
}