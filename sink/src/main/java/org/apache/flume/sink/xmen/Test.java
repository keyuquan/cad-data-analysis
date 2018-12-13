package org.apache.flume.sink.xmen;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;

public class Test {

    private static String[] hdfsHeadInfos;

    public static void main(String[] args) {
        try {
            System.out.println ( "eeeeeeeeee22222222222222".substring ( 0, 10 ));

            SimpleDateFormat Time_Format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

            FileReader reader = new FileReader ( new File ( "D:\\workspace\\cad-data-analysis\\sink\\src\\main\\java\\org\\apache\\flume\\sink\\xmen\\aa.comf" ) );
            Gson gson = new Gson ();
            ProjectConfigure.BaseConfigure conf = gson.fromJson ( reader, ProjectConfigure.BaseConfigure.class );
            System.out.println ( conf.toString () );
        } catch (Exception e) {
            e.printStackTrace ();
        }


    }
}
