package com.hhwy.canal.core.example;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import java.net.InetSocketAddress;
import java.util.List;

public class SimpleCanalClientExample {

    public static void main(String args[]) {
        // 创建链接，connector也是canal数据操作客户端
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.1.66", 11111), "example", "", "");
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            // 链接对应的canal server
            connector.connect();
            // 客户端订阅，重复订阅时会更新对应的filter信息，这里订阅所有库的所有表
            connector.subscribe(".*\\..*");
            // 回滚到未进行 ack 的地方，下次fetch的时候，可以从最后一个没有 ack 的地方开始拿
            connector.rollback();
            int totalEmptyCount = 100;
            // 循环遍历100次
            while (emptyCount < totalEmptyCount) {
                // 尝试拿batchSize条记录，有多少取多少，不会阻塞等待
                Message message = connector.getWithoutAck(batchSize);
                // 消息id
                long batchId = message.getId();
                // 实际获取记录数
                int size = message.getEntries().size();
                // 如果没有获取到消息
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    // 如果消息不为空，重置遍历。从0开始重新遍历
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                // 进行 batch id 的确认。
                connector.ack(batchId); // 提交确认
                // 回滚到未进行 ack 的地方，指定回滚具体的batchId；如果不指定batchId，回滚到未进行ack的地方
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } finally {
            // 释放链接
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            // 如果是事务操作，直接忽略。EntryType常见取值：事务头BEGIN/事务尾END/数据ROWDATA
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChange = null;
            try {
                // 获取byte数据，并反序列化
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChange.getEventType();

            System.out.println("====================================begin========================================");
            System.out.println(String.format("基本信息 binlog[%s:%s] , 表[%s.%s] , 操作: %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));


            // 如果是ddl或者是查询操作，直接打印sql
            System.out.println(rowChange.getSql() + ";");

            // 如果是删除、更新、新增操作解析出数据
            for (RowData rowData : rowChange.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    // 删除操作，只有删除前的数据
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    // 新增数据，只有新增后的数据
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    // 更新数据：获取更新前后内容
                    System.out.println("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
            System.out.println("------------------------------------end------------------------------------------");
        }
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
