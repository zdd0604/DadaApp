package com.dada.mylibrary.Util;

/**
 * Created by wpf on 8-5-0005.
 */
public class CEncrypeClass {
    static Character  MD[]= {
                    '0','9','a','A','r','G','u','M','R','T','A','5','8','Z','i','x','s','B','u','S',
                    'p','1','d','b','F','H','v','N','5','S','g','h','Y','1','z','9','0','t','v','Y',
                    'n','E','2','B','2','I','w','f','5','d','f','6','j','9','2','j','m','p','w','9',
                    'q','c','C','3','J','m','x','8','L','Q','8','U','X','3','k','4','9','0','x','G',
                    'o','d','E','K','4','O','C','9','P','e','V','7','W','5','l','3','K','w','g','y'};
    //---------------------------------------------------------------------------
    public static String Encrypt(String _S) {
        while(_S.length()<16) _S=_S+",";
        int c=1;
        int j=0;
        for (int i=0;i<_S.length();i++)
        {
            c=c*Math.abs((int)_S.charAt(i));
            c=c%100;
            if (c==0) c=7;
            j=j+Math.abs((int)_S.charAt(i));
            j=j%100;
        }
        String m="";
        for (int i=0;i<_S.length();i++)
            m=m+MD[Math.abs((int)(_S.charAt(i))+c+j+i*i) %100].toString();
        return m;
    }
    public static String Encrypt16(String _S) {
        return Encrypt(_S).substring(0, 16);
    }

}
