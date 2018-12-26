package com.cad.data.stream.Operation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseOperation {

    private static Configuration conf = HBaseConfiguration.create ();
    private static HTable table_alarm_event = null;

    public static HTable getSlarmEventTable() throws IOException {

        conf.set ( "hbase.zookeeper.quorum", "master:2181" );
        conf.set ( "hbase.defaults.for.version.skip", "true" );

        if ( table_alarm_event == null ) {
            table_alarm_event = new HTable ( conf, "alarm_event" );
            table_alarm_event.setAutoFlushTo ( true );
        }
        return table_alarm_event;

    }

    /**
     * 插入或者更新数据
     *
     * @throws IOException
     */
    public static void putData(HTable table, String Column, String cell, String rowKey, String msg) throws IOException {
        Put put = new Put ( Bytes.toBytes ( rowKey ) );
        put.add ( Bytes.toBytes ( Column ), Bytes.toBytes ( cell ), Bytes.toBytes ( msg ) );
        table.put ( put );
        table.flushCommits ();

    }
}
