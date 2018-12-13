/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flume.sink.xmen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 数据中心定制化HDFS SINK
 * BIVersion|AppId|Topic|triggerTime|platform|ZoneID
 *
 * @author ElonLo
 */
public class CadHDFSSink extends HDFSBaseSink {

    private Map<String, String> map;
    private Gson gson = new Gson ();

    public static SimpleDateFormat Time_Format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss" );

    public CadHDFSSink() {
    }

    @Override
    protected boolean fillingData(String line) {
        map = null;
        Map<String, String> map_local = gson.fromJson ( line, new TypeToken<Map<String, String>> () {
        }.getType () );

        if ( map_local.get ( "LogType" ) != null && map_local.get ( "AreaType" ) != null && (map_local.get ( "LogTime" ) != null && map_local.get ( "LogTime" ).length () >= 14) ) {
            map = map_local;
            return true;
        } else {
            LOG.error ( "The line data format error. line = " + line );
        }
        return false;
    }

    @Override
    protected String getWriterPath(int id) {

        StringBuilder builder = new StringBuilder ();

        builder.append ( this.filePath );
        builder.append ( DIRECTORY_DELIMITER );
        builder.append ( map.get ( "LogType" ) );
        builder.append ( DIRECTORY_DELIMITER );
        builder.append ( map.get ( "LogTime" ).substring ( 0, 10 ) );
        builder.append ( DIRECTORY_DELIMITER );
        builder.append ( map.get ( "AreaType" ) );
        builder.append ( DIRECTORY_DELIMITER );
        builder.append ( map.get ( "LogTime" ).substring ( 11, 13 ) );
        builder.append ( defaultInUseSuffix );

        return builder.toString ();
    }

    @Override
    protected String getWriterKey(String writeTime) {
        StringBuilder builder = new StringBuilder ();
        builder.append ( map.get ( "LogType" ) );
        builder.append ( "_" );
        builder.append ( map.get ( "LogTime" ).substring ( 0, 10 ) );
        builder.append ( "_" );
        builder.append ( this.region );
        builder.append ( "_" );
        builder.append ( map.get ( "AreaType" ) );
        builder.append ( "_" );
        builder.append ( writeTime );
        return builder.toString ();
    }

    @Override
    protected long getWriterTime() {
        try {
            return Long.valueOf ( Time_Format.parse ( map.get ( "LogTime" ) ).getTime () );
        } catch (ParseException e) {
            return System.currentTimeMillis ();
        }
    }


}
