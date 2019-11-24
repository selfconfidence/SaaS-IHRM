package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.ExcelExportUtil;
import com.ihrm.common.utils.ExcelImportUtil;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.User;
import com.ihrm.domain.response.EmployeeReportResult;
import com.ihrm.domain.response.UserResult;
import com.ihrm.system.feign.DepartmentFeign;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * @author misterWei
 * @create 2019年09月14号:20点06分
 * @mailbox mynameisweiyan@gmail.com
 */
@RestController
@RequestMapping("/sys")
@CrossOrigin
public class UserController extends BaseController {

    @Autowired
    private DepartmentFeign departmentFeign;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PermissionService permissionService;

    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRoles", method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String, Object> map) {
        //1.获取被分配的用户id
        String userId = (String) map.get("id");
        //2.获取到角色的id列表
        List<String> roleIds = (List<String>) map.get("roleIds");
        //3.调用service完成角色分配
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        User user = userService.findById(id);
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS, userResult);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //固定好公司值传递
        userService.save(user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, name = "API_USER_DELETE")
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        userService.update(id, user);
        return new Result(ResultCode.SUCCESS);
    }

    @RequestMapping(value = "/user/{page}/{size}", method = RequestMethod.GET)
    public Result findByPageList(@PathVariable Integer page, @PathVariable Integer size, @RequestParam Map parmeMap) {
        //添加企业ID
        parmeMap.put("companyId", companyId);
        Page userList = userService.findAll(page, size, parmeMap);
        return new Result(ResultCode.SUCCESS, new PageResult<User>(userList.getTotalElements(), userList.getContent()));
    }

    /**
     * 用户登陆
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User userP) {
        /**
         * JWT更换 使用Shiro 自定义Session方式实现Realm 域实现
         */
        /**
         * Md5Hash 参数说明 1 .加密的数据 2 .盐值  3.加密次数
         */
        String password = new Md5Hash(userP.getPassword(), userP.getMobile(), 3).toString();
        try {
            UsernamePasswordToken upToken = new UsernamePasswordToken(userP.getMobile(), password);
            Subject shiroSubject = SecurityUtils.getSubject();
            shiroSubject.login(upToken);
            String token = (String) shiroSubject.getSession().getId();
            return new Result(ResultCode.SUCCESS, token);
        } catch (Exception e) {
            return new Result(ResultCode.USERAUTHERROR);
        }

  /*      User user = userService.findByPhone(userP.getMobile());
        if (user != null && user.getPassword().equals(userP.getPassword())) {
            //使用JWT生成密钥形式
            Map<String, Object> parMap = BeanMapUtils.beanToMap(user);
            *//**
         * 需要API访问后端控制,需要加入token中
         *//*
            StringBuilder apis = new StringBuilder();
            for (Role role : user.getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    if (permission.getType() == PermissionConstants.PY_API) {
                        apis.append(permission.getCode()).append(",");
                    }
                }
            }
             parMap.put("apis",apis.toString());
            String jwtToken = jwtUtils.createJWT(user.getId(), user.getUsername(), parMap);
            return new Result(ResultCode.SUCCESS, jwtToken);
        }*/
    }

    /**
     * 根据当前token 加载出用户的所有信息
     */

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Result profit(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        Object userInitResult = subject.getPrincipals().getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, userInitResult);
        /*String id = (String) claims.get("id");
        User user = userService.findById(id);
        String level = user.getLevel();
        Result result = null;
        List<Permission> permissions = null;
        //笃定是否是特定管理员
        Map parMap = new HashMap();
        if ("saasAdmin".equals(level)) {
            //查询所有的权限控制
            permissions = permissionService.findAll(parMap);
            result = new Result(ResultCode.SUCCESS, new UserInitResult(user, permissions));
        } else if ("coAdmin".equals(level)) {
            //查询企业特定的权限控制
            parMap.put("enVisible", 1);
            permissions = permissionService.findAll(parMap);
            result = new Result(ResultCode.SUCCESS, new UserInitResult(user, permissions));
        } else {
            result = new Result(ResultCode.SUCCESS, new UserInitResult(user));
        }*/

    }

   /* public static void main(String[] args) {
        System.out.println(new Md5Hash("123456", "13800000003", 3).toString());
    }*/

    /**
     * Feign 的测试调用
     */
    @RequestMapping(value = "/feignDepById/{id}", method = RequestMethod.GET)
    public Result feignDepById(@PathVariable("id") String id) {
        return departmentFeign.findById(id);
    }

    /**
     * 根据报表进行导入用户
     */

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Result importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        //String filename = file.getOriginalFilename();
        List<User> users = new ExcelImportUtil(User.class).readExcel(file.getInputStream(), 1, 1);
        /*//判断当前文件是那种格式的
        Workbook workbook = null;
        if (filename.endsWith("xls")) {
            //2003
            workbook = new HSSFWorkbook(file.getInputStream());
        } else {
            // 2007及以上
            workbook = new XSSFWorkbook(file.getInputStream());
        }
        Sheet sheet = workbook.getSheetAt(0);
        List<User> users = new ArrayList<>();
        // 根据需求定义 要读取的格式 i,j均为灵活读取,
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Object[] objs = new Object[row.getLastCellNum()];
            for (int j = 1; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                Object cellValue = PoiStyleFlagUtils.getCellValue(cell);
                objs[j] = cellValue;
            }
            User user = new User(objs);
            users.add(user);
        }*/
        userService.saves(users,companyId,companyName);

        return new Result(ResultCode.SUCCESS);
    }
   /* *//**
     * 导出员工Excel 月统计维度
     *
     * month 2019-03  这种格式
     *//*

    @RequestMapping("exportEmployee/{month}")
    public void exportEmployee(@PathVariable("month") String month, HttpServletResponse response) throws IOException {
        // 联合查询
        List<EmployeeReportResult> emps = userService.findByEmployeeResult(month,companyId);
        //创建Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("employee");
        Row row = sheet.createRow(0);
        //第一行标题
        String[] tiles = "编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
       int indexCell = 0;
        for (String tile : tiles) {
            Cell cell = row.createCell(indexCell++);
            cell.setCellValue(tile);
        }
        AtomicInteger indexRow = new AtomicInteger(1);
        emps.forEach((em) ->{
         Row rows = sheet.createRow(indexRow.getAndIncrement());
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
        ByteArrayOutputStream byi = new ByteArrayOutputStream();
        workbook.write(byi);

        new DownloadUtils().download(byi,response,month+".xlsx");

    }*/


    /**
     * 导出员工Excel 按照规定的style 进行
     *
     * month 2019-03  这种格式
     */
    @SuppressWarnings("all")
    @RequestMapping("exportEmployee/{month}")
    public void exportEmployee(@PathVariable("month") String month) throws Exception {

       // 加载样式模板
        Resource resource = new ClassPathResource("excel-template/hr-template.xlsx");
        // 联合查询
        List<EmployeeReportResult> emps = userService.findByEmployeeResult(month,companyId);

        new ExcelExportUtil(EmployeeReportResult.class,2,2).export(response,new FileInputStream(resource.getFile()),emps,month+".xlsx");
       /* // 加载第一行Style
        Workbook workbook = new XSSFWorkbook(new FileInputStream(resource.getFile()));

        Sheet sheetAt = workbook.getSheetAt(0);
        CellStyle titleStyle = sheetAt.getRow(0).getRowStyle();
        Row sheetAtRow = sheetAt.getRow(0);
        Cell cell1 = sheetAtRow.createCell(0);
        cell1.setCellStyle(titleStyle);
        cell1.setCellValue(month+"月份人事报表");
        sheetAtRow = sheetAt.getRow(2);
        CellStyle[] cellStyles = new CellStyle[sheetAtRow.getLastCellNum()];
        for(int i = 0; i< sheetAtRow.getLastCellNum() ; i++){
             cellStyles[i] = sheetAtRow.getCell(i).getCellStyle();
        }

        // 联合查询
        List<EmployeeReportResult> emps = userService.findByEmployeeResult(month,companyId);
        //创建Excel
        //第一行标题
       *//* String[] tiles = "编号,姓名,手机,最高学历,国家地区,护照号,籍贯,生日,属相,入职时间,离职类型,离职原因,离职时间".split(",");
        int indexCell = 0;
        for (String tile : tiles) {
            Cell cell = row.createCell(indexCell++);
            cell.setCellValue(tile);
        }*//*
        AtomicInteger indexRow = new AtomicInteger(2);
        emps.forEach((em) ->{
            Row rows = sheetAt.createRow(indexRow.getAndIncrement());
            //通过反射简化
            int indexCells = 0;
            Cell cell = rows.createCell(0);
            cell.setCellValue(em.getUserId());
            cell.setCellStyle(cellStyles[0]);
            cell = rows.createCell(1);
            cell.setCellValue(em.getUsername());
            cell.setCellStyle(cellStyles[1]);
            cell = rows.createCell(2);
            cell.setCellValue(em.getMobile());
            cell.setCellStyle(cellStyles[2]);
            cell = rows.createCell(3);
            cell.setCellValue(em.getTheHighestDegreeOfEducation());
            cell.setCellStyle(cellStyles[3]);
            cell = rows.createCell(4);
            cell.setCellValue(em.getNationalArea());
            cell.setCellStyle(cellStyles[4]);
            cell = rows.createCell(5);
            cell.setCellValue(em.getPassportNo());
            cell.setCellStyle(cellStyles[5]);
            cell = rows.createCell(6);
            cell.setCellValue(em.getNativePlace());
            cell.setCellStyle(cellStyles[6]);
            cell = rows.createCell(7);
            cell.setCellValue(em.getBirthday());
            cell.setCellStyle(cellStyles[7]);
            cell = rows.createCell(8);
            cell.setCellValue(em.getZodiac());
            cell.setCellStyle(cellStyles[8]);
            cell = rows.createCell(9);
            cell.setCellValue(em.getTimeOfEntry());
            cell.setCellStyle(cellStyles[9]);

            cell = rows.createCell(10);
            cell.setCellValue(em.getTypeOfTurnover());
            cell.setCellStyle(cellStyles[10]);

            cell = rows.createCell(11);
            cell.setCellValue(em.getReasonsForLeaving());
            cell.setCellStyle(cellStyles[11]);

            cell = rows.createCell(12);
            cell.setCellValue(em.getResignationTime());
            cell.setCellStyle(cellStyles[12]);

        });
        ByteArrayOutputStream byi = new ByteArrayOutputStream();
        workbook.write(byi);

        new DownloadUtils().download(byi,response,month+".xlsx");*/
    }
    /**
     * 远程Feign 调用提供的方法
     */
    @RequestMapping(value = "findByEmployeeResult/{month}",method = RequestMethod.GET)
    public List<EmployeeReportResult> findByEmployeeResult(@PathVariable("month") String month,@RequestParam("companyId")String companyId){
        return userService.findByEmployeeResult(month, companyId);
    }
}
