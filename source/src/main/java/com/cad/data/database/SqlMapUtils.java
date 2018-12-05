package com.cad.data.database;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlMapUtils {


    // 湖北  报警表
    public static final String HB_ALARMEVENT_LOG_TB = "carlink.tbalarmevent";

    // 组装 sqlMap对象，把不同的sql，以key-value形式，存到不同业务类型的 map中
    private static Map<String, String> sql_lm_map = new HashMap<String, String> ();
    private static Map<String, String> sql_jg_map = new HashMap<String, String> ();

    static {
        try {
            //创建解析器
            SAXReader saxReader = new SAXReader ();
            InputStream xmlInputStream = SqlMapUtils.class.getClassLoader ().getResourceAsStream ( "dataTransferSql.xml" );
            Document document = saxReader.read ( xmlInputStream );
            Element root = document.getRootElement ();
            List<Element> elementList = root.elements ();
            // 解析每一个sql的dom结构，装进各个不同业务系统的map中
            for (Element thisElement : elementList) {
                //获取属性值
                String thisSqlId = thisElement.attributeValue ( "id" );
                String systemType = thisElement.attributeValue ( "AreaType" );
                String thisSqlString = thisElement.getText ();
                if ( "hb".equals ( systemType ) ) {
                    sql_lm_map.put ( thisSqlId, thisSqlString );
                }
                if ( "hn".equals ( systemType ) ) {
                    sql_jg_map.put ( thisSqlId, thisSqlString );
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static Map<String, String> getLmSqlMap() {
        return sql_lm_map;
    }

    public static Map<String, String> getJgSqlMap() {
        return sql_jg_map;
    }


//	public static void main(String[] args) {
//		System.out.println("~~~###~~~ " + sql_lm_map.get("lm_user_business_logs"));
//	}


}
