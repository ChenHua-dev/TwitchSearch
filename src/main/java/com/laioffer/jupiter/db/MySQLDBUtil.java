package com.laioffer.jupiter.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MySQLDBUtil {
    private static final String INSTANCE = "twitch-instance.c402psclexhd.us-west-1.rds.amazonaws.com"; // under Connectivity & security -> Endpoint
    private static final String PORT_NUM = "3306";
    private static final String DB_NAME = "jupiter";

    public static String getMySQLAddress() throws IOException {
        // extract user_name and password from config.properties
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = MySQLDBUtil.class.getClassLoader().getResourceAsStream(propFileName);
        prop.load(inputStream);

        String username = prop.getProperty("user");
        String password = prop.getProperty("password");


        return String.format(
                "jdbc:mysql://%s:%s/%s?user=%s&password=%s" +
                        "&autoReconnect=true" +
                        "&serverTimezone=UTC" +
                        "&createDatabaseIfNotExist=true",
                INSTANCE, PORT_NUM, DB_NAME, username, password);
    }

}
