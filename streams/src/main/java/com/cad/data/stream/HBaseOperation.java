package com.cad.data.stream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseOperation {

    private static Configuration conf = HBaseConfiguration.create ();

    public static HTable getTable(String tableName) throws IOException {

        conf.set ( "hbase.zookeeper.quorum", "master:2181" );
        conf.set ( "hbase.defaults.for.version.skip", "true" );

        HTable table = new HTable ( conf, tableName );

        return table;

    }

    /**
     * 插入或者更新数据
     *
     * @throws IOException
     */
    public static void putData(HTable table, String Column, String cell, String rowkey, String msg) throws IOException {
        Put put = new Put ( Bytes.toBytes ( rowkey ) );
        put.add ( Bytes.toBytes ( Column ),//
                Bytes.toBytes ( cell ),//
                Bytes.toBytes ( msg ) );
        table.put ( put );
    }


}
