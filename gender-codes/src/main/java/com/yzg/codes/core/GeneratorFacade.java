package com.yzg.codes.core;

import com.yzg.codes.entity.Settings;
import com.yzg.codes.entity.db.DataBase;
import com.yzg.codes.entity.db.Table;
import com.yzg.codes.utils.DataBaseUtils;
import com.yzg.codes.utils.PropertiesUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在UI页面获取基本参数链接, 提供数据供应 最后调用Generator 生成 实际模板代码
 */
public class GeneratorFacade {
    private String templetPath;
    private String outpath;
    private Settings settings;
    private DataBase db;
    private Generator generator;

    public GeneratorFacade(String templetPath, String outpath, Settings settings, DataBase db) throws IOException {
        this.templetPath = templetPath;
        this.outpath = outpath;
        this.settings = settings;
        this.db = db;
        generator = new Generator(templetPath,outpath);
    }

    /**
     * 提供模板所需要的数据支撑
     *
     * @throws Exception
     */
    public void generatorByData() throws Exception {
        List<Table> tables = DataBaseUtils.getTables(db);
        List<Map> listMaps = new ArrayList<>();
        tables.forEach((table -> {
            Map map = new HashMap(100);
            map.put("table", table);
            //基础的代码路径,以及定义包名等
            map.putAll(settings.getSettingMap());
            //自定义的元数据
            map.putAll(PropertiesUtils.customMap);
            map.put("ClassName", table.getName2());
            listMaps.add(map);
        }));
        //开始生成代码操作
        listMaps.forEach((k) -> {
            try {
                generator.processFileGenerate(k);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
