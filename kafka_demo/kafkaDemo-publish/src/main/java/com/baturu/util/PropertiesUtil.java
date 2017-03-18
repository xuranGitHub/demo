package com.baturu.util;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by fire on 15/7/26.
 */
public class PropertiesUtil {

    private final static Logger logger = Logger.getLogger(PropertiesUtil.class);


    public static Properties getProperties2(String propertiesName) {
        Properties properties = new Properties();
        try {
            properties.load(ClassLoader.getSystemResourceAsStream(propertiesName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 读取properties文件
     *
     * @author fire
     * @time 2015年07月28日
     * @param fileName 文件名称
     * @return
     */
    public static Properties getProperties(String fileName) {
        String projectPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        projectPath = projectPath.replaceAll("%20", " ");
        Properties properties = new Properties();
        InputStream in;
        try {
            InputStream is = new PropertiesUtil().getClass().getResourceAsStream("/" + fileName);
            in = new BufferedInputStream(is);
            properties.load(in);
        } catch (Exception e) {
            logger.error("读取配置文件出错:" + e.getMessage());
        }
        return properties;
    }

    /**
     * 读取properties文件
     *
     * @author fire
     * @time 2015年07月28日
     * @param fileName 文件名称
     * @return
     */
    public static Map<String, String> getPropertiesMap(String fileName) {
        Map<String, String> retVal = new HashMap<>();
        Properties properties = getProperties(fileName);
        try {
            Set<Object> set = properties.keySet();
            for (Object o : set) {
                String tempKey = o.toString();
                String value = properties.getProperty(tempKey);
                retVal.put(tempKey, value);
            }
        } catch (Exception e) {
            logger.error("读取配置文件出错:" + e.getMessage());
        }
        return retVal;
    }

}
