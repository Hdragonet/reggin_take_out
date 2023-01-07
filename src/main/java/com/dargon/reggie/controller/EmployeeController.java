package com.dargon.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.Employee;
import com.dargon.reggie.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /*
     *
     * 员工登录
     *
     * */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        String password = employee.getPassword();

        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> lcw = new LambdaQueryWrapper<>();

        lcw.eq(Employee::getUsername, employee.getUsername());


        Employee one = employeeService.getOne(lcw);

        if (one == null) {
            return R.error("登录失败");
        }

        if (!one.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        if (one.getStatus() != 1) {
            return R.error("登录失败");
        }

        request.getSession().setAttribute("employee", one.getId());

        return R.success(one);
    }


    /*
     *
     * 员工退出
     *
     * */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {

        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }


    /*
     * 新增员工
     *
     * */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        //初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //当前系统时间
//        employee.setCreateTime(LocalDateTime.now());
//
//        employee.setUpdateTime(LocalDateTime.now());
//
//        Long id = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(id);
//
//        employee.setUpdateUser(id);

        employeeService.save(employee);

        return R.success("新增员工成功");

    }


    /*
     * 员工信息分页查询
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        log.info("page={},pagsize={},name={}", page, pageSize, name);

        Page<Employee> page1 = new Page<>(page, pageSize);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper();

        wrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        wrapper.orderByDesc(Employee::getUpdateTime);


        employeeService.page(page1, wrapper);

        return R.success(page1);
    }


    /*
     *
     * 员工状态更新
     *
     * */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {

//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(id);

        boolean b = employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    /*
    *
    * id查询员工信息
    *
    * */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable long id){

        Employee employee = employeeService.getById(id);

        if (employee==null){

            return R.error("没有查询到员工信息");
        }

        return R.success(employee);
    }


}
