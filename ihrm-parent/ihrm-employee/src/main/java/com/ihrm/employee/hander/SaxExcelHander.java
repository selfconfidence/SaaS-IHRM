package com.ihrm.employee.hander;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.springframework.stereotype.Component;

/**
 * @author misterWei
 * @create 2019年11月24号:22点22分
 * @mailbox mynameisweiyan@gmail.com
 * 百万数据导入连接测试
 */
@Component
public class SaxExcelHander implements XSSFSheetXMLHandler.SheetContentsHandler {

    private PoiEntity   poiEntity;

    //开始行
    @Override
    public void startRow(int i) {
        if (i !=0) {
             poiEntity = new  PoiEntity();
        }
    }

    //结束的行
    @Override
    public void endRow(int i) {
        //保存数据库
        System.out.println(poiEntity);

    }

    //每一个表格的数据  cellReference 是 A1 B2 的这种格式
    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment xssfComment) {
        if (poiEntity != null) {
            switch (cellReference.substring(0, 1)) {
                case "A":
                    poiEntity.setId(formattedValue);
                    break;
                case "B":
                    poiEntity.setBreast(formattedValue);
                    break;
                case "C":
                    poiEntity.setAdipocytes(formattedValue);
                    break;
                case "D":
                    poiEntity.setNegative(formattedValue);
                    break;
                case "E":
                    poiEntity.setStaining(formattedValue);
                    break;
                case "F":
                    poiEntity.setSupportive(formattedValue);
                    break;
                default:
                    break;
            }
        }
    }
}
