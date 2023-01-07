package com.dargon.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.dargon.reggie.common.BaseConText;
import com.dargon.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 检查用户是否登录
 *
 * */
@Slf4j
@WebFilter(filterName = "LoginCheck", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取访问路径
        String requestURI = request.getRequestURI();
        //定义不处理的请求路径
        String urls[] = new String[]{"/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/user/sendCode"
        };

        String users[] = new String[]{
                "/backend/**",
                "/employee/**"
        };

        Boolean check = check(urls, requestURI);


        if (check) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //判断后台登录用户
        Object employee = request.getSession().getAttribute("employee");
        if (employee!=null){


                log.info("用户已登录，id是{}：", request.getSession().getAttribute("employee"));
                BaseConText.set((Long) request.getSession().getAttribute("employee"));
                filterChain.doFilter(request, response);
                return;

        }
          //用户权限
          Boolean check1 = check(users, requestURI);
          //前台user用户
        if (request.getSession().getAttribute("user") != null) {
            log.info("user已登录，id是{}：", request.getSession().getAttribute("user"));

            BaseConText.set((Long) request.getSession().getAttribute("user"));

            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    public Boolean check(String urls[], String uri) {
        for (String s : urls) {

            boolean match = PATH_MATCHER.match(s, uri);

            if (match) {
                return true;
            }

        }
        return false;

    }
}
