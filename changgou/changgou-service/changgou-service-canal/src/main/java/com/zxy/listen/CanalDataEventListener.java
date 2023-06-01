package com.zxy.listen;


import com.alibaba.otter.canal.protocol.CanalEntry;
import com.example.changgouserviceitemapi.feign.pageFeign;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// Canal监听器--》监听数据库增删改查操作
@CanalEventListener
public class CanalDataEventListener {
    @Autowired
    pageFeign pageFeign;
    //增加的数据监听
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        System.out.println("insert systemdate");
        rowData.getAfterColumnsList().forEach((c)->{
            System.out.println(c.getName()+"::"+c.getValue());
        });
    };

    //修改的数据监听
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        System.out.println("update systemdate");
        rowData.getAfterColumnsList().forEach((c)->{
            System.out.println(c.getName()+"::"+c.getValue());
        });
    };

    //删除的数据监听
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        System.out.println("delete systemdate");
        rowData.getAfterColumnsList().forEach((c)->{
            System.out.println(c.getName()+"::"+c.getValue());
        });
    };


    // 数据变动--》cancel同步
    @ListenPoint(destination = "example",schema = "changgou",table = "tb_spu",eventType = {CanalEntry.EventType.INSERT,CanalEntry.EventType.DELETE,CanalEntry.EventType.UPDATE})
    public void onEventSpuUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData ){
        // todo 明天再写
        String spuId="";
        if(eventType== CanalEntry.EventType.DELETE){//删除
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for(CanalEntry.Column beforeColumn:beforeColumnsList){
                if(beforeColumn.getName().equals("id")){
                    spuId= beforeColumn.getValue();
                    break;
                }
            }
            // todo 删除
        }else{
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for(CanalEntry.Column afterColumn:afterColumnsList){
                if(afterColumn.getName().equals("id")){
                    spuId= afterColumn.getValue();
                    break;
                }
            }
            pageFeign.createHtml(Long.valueOf(spuId));
        }

    }

    /***
     * 自定义数据修改监听
     * @param eventType
     * @param rowData
     */
    /*   @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content_category", "tb_content"}, eventType = CanalEntry.EventType.UPDATE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("DeleteListenPoint");
        rowData.getAfterColumnsList().forEach((c) -> System.out.println("By--Annotation: " + c.getName() + " ::   " + c.getValue()));
     }*/
}
