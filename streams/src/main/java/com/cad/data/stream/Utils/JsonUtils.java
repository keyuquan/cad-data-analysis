package com.cad.data.stream.Utils;

import com.cad.data.stream.Bean.AlarmEventBean;
import com.google.gson.Gson;

public class JsonUtils {
    public static Gson gson = null;

    public static AlarmEventBean fromAlarmEventBeanJson(String json) {
        if ( gson == null ) {
            gson = new Gson ();
        }
        return gson.fromJson ( json, AlarmEventBean.class );
    }
}
