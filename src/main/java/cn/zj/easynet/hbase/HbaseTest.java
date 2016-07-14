package cn.zj.easynet.hbase;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

public class HbaseTest {
	private static final Logger logger = Logger.getLogger(HbaseTest.class);
	static Configuration configuration = HBaseConfiguration.create();
     
    @SuppressWarnings("resource")
	public static void createTable(String tableName) { 
        logger.warn("start create table ......"); 
        try { 
            HBaseAdmin hBaseAdmin = new HBaseAdmin(configuration); 
            if (hBaseAdmin.tableExists(tableName)) {// 如果存在要创建的表，那么先删除，再创建 
                hBaseAdmin.disableTable(tableName); 
                hBaseAdmin.deleteTable(tableName); 
                logger.warn(tableName + " is exist,detele...."); 
            } 
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName); 
            tableDescriptor.addFamily(new HColumnDescriptor("uinfo")); 
            hBaseAdmin.createTable(tableDescriptor); 
        } catch (MasterNotRunningException e) { 
            e.printStackTrace(); 
        } catch (ZooKeeperConnectionException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        logger.warn("end create table ......"); 
    } 
 
     
    @SuppressWarnings("resource")
	public static void insertData(String tableName) { 
        logger.warn("start insert data ......"); 
        HTablePool pool = new HTablePool(configuration, 1000); 
        HTable table = (HTable) pool.getTable(tableName); 
        Put put = new Put("112233bbbcccc".getBytes());// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值 
        put.add("column1".getBytes(), null, "aaa".getBytes());// 本行数据的第一列 
        put.add("column2".getBytes(), null, "bbb".getBytes());// 本行数据的第三列 
        put.add("column3".getBytes(), null, "ccc".getBytes());// 本行数据的第三列 
        try { 
            table.put(put); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        logger.warn("end insert data ......"); 
    }
    
    @SuppressWarnings("resource")
	public static void insertData(String tableName, String rowKey, String familyName, String qualifierName, String value) throws UnsupportedEncodingException { 
        logger.warn("start insert data ......"); 
        HTablePool pool = new HTablePool(configuration, 1000); 
        HTable table = (HTable) pool.getTable(tableName); 
        Put put = new Put(rowKey.getBytes("utf-8"));// 一个PUT代表一行数据，再NEW一个PUT表示第二行数据,每行一个唯一的ROWKEY，此处rowkey为put构造方法中传入的值 
        put.add(familyName.getBytes("utf-8"), qualifierName.getBytes("utf-8"), value.getBytes("utf-8"));// 
        try { 
            table.put(put); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        logger.warn("end insert data ......"); 
    } 
 
     
    @SuppressWarnings("resource")
	public static void dropTable(String tableName) { 
        try { 
            HBaseAdmin admin = new HBaseAdmin(configuration); 
            admin.disableTable(tableName); 
            admin.deleteTable(tableName); 
        } catch (MasterNotRunningException e) { 
            e.printStackTrace(); 
        } catch (ZooKeeperConnectionException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
 
    } 
     
     @SuppressWarnings("resource")
	public static void deleteRow(String tablename, String rowkey)  { 
        try { 
            HTable table = new HTable(configuration, tablename); 
            List list = new ArrayList(); 
            Delete d1 = new Delete(rowkey.getBytes()); 
            list.add(d1); 
             
            table.delete(list); 
            logger.warn("删除行成功!"); 
             
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
         
 
    } 
 
      
     public static void deleteByCondition(String tablename, String rowkey)  { 
            //目前还没有发现有效的API能够实现根据非rowkey的条件删除这个功能能，还有清空表全部数据的API操作 
 
    } 
 
 
     
    public static void QueryAll(String tableName) { 
        HTablePool pool = new HTablePool(configuration, 1000); 
        HTable table = (HTable) pool.getTable(tableName); 
        try { 
            ResultScanner rs = table.getScanner(new Scan()); 
            for (Result r : rs) { 
                logger.warn("获得到rowkey:" + new String(r.getRow())); 
                for (KeyValue keyValue : r.raw()) { 
                    logger.warn("列：" + new String(keyValue.getFamily()) 
                            + "====值:" + new String(keyValue.getValue())); 
                } 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    public static void QueryByRowKey(String tableName , String rowKey) { 
    	 
        try { 
            HTablePool pool = new HTablePool(configuration, 1000); 
            HTable table = (HTable) pool.getTable(tableName);
            Result r = table.get(new Get(rowKey.getBytes("utf-8")));
             logger.warn("获得到rowkey:" + new String(r.getRow())); 
             for (KeyValue keyValue : r.raw()) { 
            	 logger.warn("列族：" + new String(keyValue.getFamily())
            	 +"\t列：" + new String(keyValue.getQualifier())
            	 + "\t值: " + new String(keyValue.getValue())); 
             } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
    }
    
    public static void QueryByCondition1(String tableName) { 
 
        HTablePool pool = new HTablePool(configuration, 1000); 
        HTable table = (HTable) pool.getTable(tableName); 
        try { 
            Get scan = new Get("abcdef".getBytes());// 根据rowkey查询 
            Result r = table.get(scan); 
            logger.warn("获得到rowkey:" + new String(r.getRow())); 
            for (KeyValue keyValue : r.raw()) { 
                logger.warn("列：" + new String(keyValue.getFamily()) 
                        + "====值:" + new String(keyValue.getValue())); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
     
    public static void QueryByCondition2(String tableName) { 
 
        try { 
            HTablePool pool = new HTablePool(configuration, 1000); 
            HTable table = (HTable) pool.getTable(tableName); 
            Filter filter = new SingleColumnValueFilter(Bytes 
                    .toBytes("column1"), null, CompareOp.EQUAL, Bytes 
                    .toBytes("aaa")); // 当列column1的值为aaa时进行查询 
            Scan s = new Scan(); 
            s.setFilter(filter); 
            ResultScanner rs = table.getScanner(s); 
            for (Result r : rs) { 
                logger.warn("获得到rowkey:" + new String(r.getRow())); 
                for (KeyValue keyValue : r.raw()) { 
                    logger.warn("列族：" + new String(keyValue.getFamily())
                            +"\t列：" + new String(keyValue.getQualifier())
                            + "\t值: " + new String(keyValue.getValue())); 
                } 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
    } 
 
     
    public static void QueryByCondition3(String tableName) { 
 
        try { 
            HTablePool pool = new HTablePool(configuration, 1000); 
            HTable table = (HTable) pool.getTable(tableName); 
 
            List<Filter> filters = new ArrayList<Filter>(); 
 
            Filter filter1 = new SingleColumnValueFilter(Bytes 
                    .toBytes("column1"), null, CompareOp.EQUAL, Bytes 
                    .toBytes("aaa")); 
            filters.add(filter1); 
 
            Filter filter2 = new SingleColumnValueFilter(Bytes 
                    .toBytes("column2"), null, CompareOp.EQUAL, Bytes 
                    .toBytes("bbb")); 
            filters.add(filter2); 
 
            Filter filter3 = new SingleColumnValueFilter(Bytes 
                    .toBytes("column3"), null, CompareOp.EQUAL, Bytes 
                    .toBytes("ccc")); 
            filters.add(filter3); 
 
            FilterList filterList1 = new FilterList(filters); 
 
            Scan scan = new Scan(); 
            scan.setFilter(filterList1); 
            ResultScanner rs = table.getScanner(scan); 
            for (Result r : rs) { 
                logger.warn("获得到rowkey:" + new String(r.getRow())); 
                for (KeyValue keyValue : r.raw()) { 
                    logger.warn("列族：" + new String(keyValue.getFamily())
                            +"\t列：" + new String(keyValue.getQualifier())
                            + "\t值:" + new String(keyValue.getValue())); 
                } 
            } 
            rs.close(); 
 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
 
    }
    
    /**
	 * @param args
	 */
	public static void main(String[] args) {
		String tablename = "user";
		QueryByCondition2(tablename); 
    } 
 
}
