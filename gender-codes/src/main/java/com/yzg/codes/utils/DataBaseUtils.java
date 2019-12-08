package com.yzg.codes.utils;

import com.yzg.codes.entity.db.Column;
import com.yzg.codes.entity.db.DataBase;
import com.yzg.codes.entity.db.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author misterWei
 * @create 2019年12月07号:19点53分
 * @mailbox mynameisweiyan@gmail.com
 *
 * 获取数据库名称元数据工具类
 */
public class DataBaseUtils {

    public static Connection getConnection(DataBase dataBase) throws Exception {
        Class.forName(dataBase.getDriver());
        Properties props =new Properties();
     //设置连接属性,使得可获取到表的REMARK(备注)
        props.put("remarksReporting","true");
        props.put("user",
                dataBase.getUserName());
        props.put("password", dataBase.getPassWord());
        return DriverManager.getConnection(dataBase.getUrl(), props);

    }

    public static List<String> getSchemas(DataBase db) throws Exception {
        Connection connection = getConnection(db);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        List dataName = new ArrayList();
        while (catalogs.next()) {
            dataName.add(catalogs.getString(1));
        }
        connection.close();
        return dataName;
    }
    /**
     * 对 所有表元数据以及列名称进行处理封装为对象.
     */
    public static List<Table> getTables(DataBase dataBase) throws Exception {
        List<Table> tables = new ArrayList<>();
        //获取链接
        Connection connection = getConnection(dataBase);
        //得到表的元数据
        DatabaseMetaData metaData = connection.getMetaData();
        //这是mysql获取的表结构的方式,如果是别的数据库例如Oracle 就需要把用户名验证作为参数传入.
        ResultSet dataTables = metaData.getTables(null, null, null, new String[]{"TABLE"});
        while (dataTables.next()) {
            Table table = new Table();
            //获取表名称
            String tableName = dataTables.getString("TABLE_NAME");
            table.setName(tableName);
            //获取Pojo的方式表名称
            String pojoName = perfixTableName(tableName);
            pojoName = StringUtils.makeAllWordFirstLetterUpperCase(pojoName);
            table.setName2(pojoName);
            // 获取remark
            String remarks = dataTables.getString("REMARKS");
            table.setComment(remarks);
            //查询表中的所有主键--复合主键等
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            String keys = "";
            while (primaryKeys.next()) {
                String columnNameKey = primaryKeys.getString("COLUMN_NAME");
                keys +=columnNameKey+",";
            }
            table.setKey(keys);
            // 获取当前表中所有的列属性
            List<Column> columns = new ArrayList<>();
            ResultSet tableColumns = metaData.getColumns(null, null, tableName, null);
            while (tableColumns.next()) {
                Column column = new Column();
                //获取列名称
                String columnName = tableColumns.getString("COLUMN_NAME");
                column.setColumnName(columnName);
                //转化为驼峰式Java命名规范
                String pojoClomnName = StringUtils.makeAllWordFirstLetterUpperCase(columnName);
                column.setColumnName2(pojoClomnName);
                //remark
                String colunmRemark = tableColumns.getString("REMARKS");
                column.setColumnComment(colunmRemark);
                //当前列在数据库的type
                String dataType = tableColumns.getString("TYPE_NAME");
                column.setColumnDbType(dataType);
                //当前数据库列类型在Java代表的值
                column.setColumnType(PropertiesUtils.customMap.get(dataType));
                //当前列是否是主键
                 String isKey = null;
                if (StringUtils.contains(columnName,keys.split(","))) {
                    isKey = "PRE";
                }
                column.setColumnKey(isKey);
                columns.add(column);
            }
            tableColumns.close();
            table.setColumns(columns);
            tables.add(table);
        }
        dataTables.close();
        connection.close();
        return tables;
    }

    public static String perfixTableName(String tableName){
        String tableRemovePrefixes = PropertiesUtils.customMap.get("tableRemovePrefixes");
         String pojoTableName = "";
        for (String match : tableRemovePrefixes.split(",")) {
            pojoTableName  = StringUtils.removePrefix(tableName, match, true);
            if (!tableName.equals(pojoTableName)) return pojoTableName;
        }
        return pojoTableName;

    }

    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase("MYSQL".toUpperCase(),"192.168.237.128","3306","saas_ihrm");
        dataBase.setUserName("root");
        dataBase.setPassWord("12345");
       getTables(dataBase).forEach(System.out::println);
    }


}
