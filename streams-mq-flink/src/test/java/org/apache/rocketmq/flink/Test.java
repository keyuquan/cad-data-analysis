package org.apache.rocketmq.flink;


import org.json.JSONObject;

public class Test {

    public static void main(String[] args) {

        String jsonString = "{\"alarmEventId\":9420962,\"provinceId\":100000000,\"provinceName\":\"湖北省\",\"cityId\":100000022,\"cityName\":\"十堰市\",\"districtId\":100000023,\"districtName\":\"茅箭区\",\"companyId\":108,\"companyName\":\"十堰市鑫顺物流有限公司\",\"vehicleCode\":\"HBC9D6092\",\"plateNum\":\"鄂C9D609\",\"plateColorCode\":\"2\",\"vehicleOperateTypeId\":100000003,\"vehicleOperateTypeCode\":\"30\",\"vehicleOperateTypeName\":\"危险货物运输\",\"alarmTypeCode\":\"32\",\"alarmLevel\":1,\"eventId\":\"7a2b4be3-7775-4f5f-84df-ecd4dba72dfd\",\"eventStartTime\":\"2018-08-21 01:09:10\",\"eventStartLongitude\":115.7040390,\"eventStartLatitude\":36.1981050,\"eventEndTime\":\"2018-08-21 01:09:16\",\"eventEndLongitude\":115.7029070,\"eventEndLatitude\":36.1974310,\"eventPersistSeconds\":6,\"eventStatus\":2,\"maxSpeed\":77,\"minSpeed\":75,\"avgSpeed\":76,\"eventDesc\":\"事件开始速度：76\",\"violationDistance\":0.0,\"status\":1,\"createTime\":\"2018-08-21 01:09:11\",\"createUserId\":\"0\",\"lastModifyTime\":\"2018-08-21 01:09:17\",\"lastModifyUserId\":\"0\",\"valid\":1,\"eventOriginalTime\":\"2018-08-21 01:09:10\",\"eventOriginalLongitude\":115.7040390,\"eventOriginalLatitude\":36.1981050,\"eventStartDate\":\"2018-08-20 13:00:00\",\"similarityRatio\":0.00,\"limitSpeed\":80,\"operatorId\":30,\"operatorName\":\"-\",\"LogType\":\"alarm_event\",\"AreaType\":\"hb\",\"LogTime\":\"2018-08-21 01:09:10\"}\n";

        JSONObject jsonObject = new JSONObject ( jsonString );

        jsonObject.get ( "alarmEventId" );
        System.out.println ( jsonObject.get ( "alarmEventId" ) );
    }
}
