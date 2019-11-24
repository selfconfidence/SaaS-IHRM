package com.ihrm.employee.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.DownloadUtils;
import com.ihrm.domain.*;
import com.ihrm.domain.response.EmployeeReportResult;
import com.ihrm.employee.feign.UserServiceFeign;
import com.ihrm.employee.hander.SaxExcelHander;
import com.ihrm.employee.service.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/employees")
public class EmployeeController extends BaseController {
    @Autowired
    private UserCompanyPersonalService userCompanyPersonalService;
    @Autowired
    private UserCompanyJobsService userCompanyJobsService;
    @Autowired
    private ResignationService resignationService;
    @Autowired
    private TransferPositionService transferPositionService;
    @Autowired
    private PositiveService positiveService;
    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private UserServiceFeign userServiceFeign;


    /**
     * 员工个人信息保存
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        UserCompanyPersonal sourceInfo = BeanMapUtils.mapToBean(map, UserCompanyPersonal.class);
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyPersonal();
        }
        sourceInfo.setUserId(uid);
        sourceInfo.setCompanyId(super.companyId);
        userCompanyPersonalService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工个人信息读取
     */
    @RequestMapping(value = "/{id}/personalInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyPersonal info = userCompanyPersonalService.findById(uid);
        if(info == null) {
            info = new UserCompanyPersonal();
            info.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 员工岗位信息保存
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.PUT)
    public Result saveJobsInfo(@PathVariable(name = "id") String uid, @RequestBody UserCompanyJobs sourceInfo) throws Exception {
        //更新员工岗位信息
        if (sourceInfo == null) {
            sourceInfo = new UserCompanyJobs();
            sourceInfo.setUserId(uid);
            sourceInfo.setCompanyId(super.companyId);
        }
        userCompanyJobsService.save(sourceInfo);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 员工岗位信息读取
     */
    @RequestMapping(value = "/{id}/jobs", method = RequestMethod.GET)
    public Result findJobsInfo(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs info = userCompanyJobsService.findById(uid);
        if(info == null) {
            info = new UserCompanyJobs();
            info.setUserId(uid);
            info.setCompanyId(companyId);
        }
        return new Result(ResultCode.SUCCESS,info);
    }

    /**
     * 离职表单保存
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.PUT)
    public Result saveLeave(@PathVariable(name = "id") String uid, @RequestBody EmployeeResignation resignation) throws Exception {
        resignation.setUserId(uid);
        resignationService.save(resignation);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 离职表单读取
     */
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.GET)
    public Result findLeave(@PathVariable(name = "id") String uid) throws Exception {
        EmployeeResignation resignation = resignationService.findById(uid);
        if(resignation == null) {
            resignation = new EmployeeResignation();
            resignation.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,resignation);
    }

    /**
     * 导入员工
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importDatas(@RequestParam(name = "file") MultipartFile attachment) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单保存
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.PUT)
    public Result saveTransferPosition(@PathVariable(name = "id") String uid, @RequestBody EmployeeTransferPosition transferPosition) throws Exception {
        transferPosition.setUserId(uid);
        transferPositionService.save(transferPosition);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 调岗表单读取
     */
    @RequestMapping(value = "/{id}/transferPosition", method = RequestMethod.GET)
    public Result findTransferPosition(@PathVariable(name = "id") String uid) throws Exception {
        UserCompanyJobs jobsInfo = userCompanyJobsService.findById(uid);
        if(jobsInfo == null) {
            jobsInfo = new UserCompanyJobs();
            jobsInfo.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,jobsInfo);
    }

    /**
     * 转正表单保存
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.PUT)
    public Result savePositive(@PathVariable(name = "id") String uid, @RequestBody EmployeePositive positive) throws Exception {
        positiveService.save(positive);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 转正表单读取
     */
    @RequestMapping(value = "/{id}/positive", method = RequestMethod.GET)
    public Result findPositive(@PathVariable(name = "id") String uid) throws Exception {
        EmployeePositive positive = positiveService.findById(uid);
        if(positive == null) {
            positive = new EmployeePositive();
            positive.setUserId(uid);
        }
        return new Result(ResultCode.SUCCESS,positive);
    }

    /**
     * 历史归档详情列表
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.GET)
    public Result archives(@PathVariable(name = "month") String month, @RequestParam(name = "type") Integer type) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 归档更新
     */
    @RequestMapping(value = "/archives/{month}", method = RequestMethod.PUT)
    public Result saveArchives(@PathVariable(name = "month") String month) throws Exception {
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 历史归档列表
     */
    @RequestMapping(value = "/archives", method = RequestMethod.GET)
    public Result findArchives(@RequestParam(name = "pagesize") Integer pagesize, @RequestParam(name = "page") Integer page, @RequestParam(name = "year") String year) throws Exception {
        Map map = new HashMap();
        map.put("year",year);
        map.put("companyId",companyId);
        Page<EmployeeArchive> searchPage = archiveService.findSearch(map, page, pagesize);
        PageResult<EmployeeArchive> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
    }

    /**
     * 百万级别数据量Excel 导出
     */
    @SuppressWarnings("all")
    @RequestMapping("exportEmployee/{month}")
    public void exportEmployee(@PathVariable("month") String month) throws Exception {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        RequestContextHolder.setRequestAttributes(sra, true);
        // 联合查询
        List<EmployeeReportResult> emps = userServiceFeign.findByEmployeeResult(month,companyId);
        // 百万级别导出数据类对象,200 为阈值,每次读取到阈值,读取到临时文件中
        // 关键点
        SXSSFWorkbook workbook = new SXSSFWorkbook(200);

        Sheet million = workbook.createSheet("百万级别数据导出");
        Row row = million.createRow(0);
        //创建Excel
        //第一行标题
        String[] tiles = "编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
        int indexCell = 0;
        for (String tile : tiles) {
            Cell cell = row.createCell(indexCell++);
            cell.setCellValue(tile);
        }
        AtomicInteger indexRow = new AtomicInteger(2);
        for (int i = 0 ; i< 10000;i++) {
            emps.forEach((em) -> {
                Row rows = million.createRow(indexRow.getAndIncrement());
                //通过反射简化
                int indexCells = 0;
                Cell cell = rows.createCell(0);
                cell.setCellValue(em.getUserId());
                cell = rows.createCell(1);
                cell.setCellValue(em.getUsername());
                cell = rows.createCell(2);
                cell.setCellValue(em.getMobile());
                cell = rows.createCell(3);
                cell.setCellValue(em.getTheHighestDegreeOfEducation());
                cell = rows.createCell(4);
                cell.setCellValue(em.getNationalArea());
                cell = rows.createCell(5);
                cell.setCellValue(em.getPassportNo());
                cell = rows.createCell(6);
                cell.setCellValue(em.getNativePlace());
                cell = rows.createCell(7);
                cell.setCellValue(em.getBirthday());
                cell = rows.createCell(8);
                cell.setCellValue(em.getZodiac());
                cell = rows.createCell(9);
                cell.setCellValue(em.getTimeOfEntry());
                cell = rows.createCell(10);
                cell.setCellValue(em.getTypeOfTurnover());
                cell = rows.createCell(11);
                cell.setCellValue(em.getReasonsForLeaving());

                cell = rows.createCell(12);
                cell.setCellValue(em.getResignationTime());

            });
        }
        ByteArrayOutputStream byi = new ByteArrayOutputStream();
        workbook.write(byi);

        new DownloadUtils().download(byi,response,month+".xlsx");
    }

    /**
     * 百万数据导入解决方法
     */
    @Autowired
    private SaxExcelHander saxExcelHander;
    @RequestMapping("imports")
    public void imports(@RequestParam("file") MultipartFile file) throws Exception {
        //1.根据Excel获取OPCPackage对象
        OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
        //2.创建XSSFReader对象
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        //3.获取SharedStringsTable对象
        SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();
        //4.获取StylesTable对象
        StylesTable stylesTable = xssfReader.getStylesTable();
        //5.创建Sax的XmlReader对象
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        //6.设置处理器
        xmlReader.setContentHandler(new XSSFSheetXMLHandler(stylesTable,sharedStringsTable,saxExcelHander,false));
        //7 随行读取
        XSSFReader.SheetIterator sheets =  (XSSFReader.SheetIterator)xssfReader.getSheetsData();
       // 代表每一个Sheet页
        while (sheets.hasNext()){
           xmlReader.parse(new InputSource(sheets.next()));
       }
       opcPackage.close();
    }

}
