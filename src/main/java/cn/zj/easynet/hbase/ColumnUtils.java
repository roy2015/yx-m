package cn.zj.easynet.hbase;

import org.apache.hadoop.hbase.util.Bytes;

public class ColumnUtils {

        public static byte[] getFamily(String column){
                return getBytes(column, 0);
        }

        public static byte[] getQualifier(String column){
                return getBytes(column, 1);
        }

        private static byte[] getBytes(String column , int offset){
                String[] split = column.split(":");
                return Bytes.toBytes(offset > split.length -1 ? split[0] :split[offset]);
        }
}