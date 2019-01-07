package com.cad.data.threads;

import com.alibaba.fastjson.JSONObject;
import com.cad.data.database.mysql.utils.JDBCTools;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Test {


    public static void main(String[] args) {

        long a = 1l;
        long s = System.currentTimeMillis ();
        for (long i = 0l; i <  90000000000l; i++) {
            a = a + i;
        }
        System.out.println ( (System.currentTimeMillis () - s) );
        System.out.println ( a );
    }
}
