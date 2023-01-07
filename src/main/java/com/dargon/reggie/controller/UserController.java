package com.dargon.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dargon.reggie.common.R;
import com.dargon.reggie.domain.User;
import com.dargon.reggie.service.UserService;
import com.dargon.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/sendCode")
    public R<String> sendCode(@RequestBody User user){


        if (StringUtils.isNotEmpty(user.getPhone())){



            ValueOperations valueOperations = redisTemplate.opsForValue();

            if (valueOperations.get(user.getPhone())==null){//判断短信验证码过期了才能重新发送
                String code = ValidateCodeUtils.generateValidateCode(4).toString();
                valueOperations.set(user.getPhone(),code,5, TimeUnit.MINUTES); //设置登录的验证码，存到redis中，过期时间5分钟

                log.info(code);

                return R.success(null,"发送验证码成功");
            }else{
                return R.error("发送失败，短信还在有效期");
            }

        }



        return R.error("短信验证码发送失败");

    }

    /*
     *
     * 用户登录未完善
     * */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();

        if (phone == null) {
            return R.error("手机号不能为空");
        }
        if (phone != null) {
            phone = phone.trim();
        }
        String code = map.get("code").toString();

        //从redis中获取 验证码
        Object rediscode = redisTemplate.opsForValue().get(phone);

        //判断验证码是否相同
        if (rediscode!=null&&rediscode.equals(code)){

         redisTemplate.delete(phone);

         LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();

        wrapper.eq(User::getPhone, phone);
        wrapper.eq(User::getStatus, 1);

        User one = userService.getOne(wrapper);

        //查询数据库中是否存在user
        if (one == null) {
            one = new User();
            one.setPhone(phone);
            one.setStatus(1);
            userService.save(one);
        }

        session.setAttribute("user",one.getId());
            return R.success(one);
        }

        return R.error("登录失败");
    }


    @PostMapping("/loginout")
    public R<String> loginOut(HttpSession session) {

        session.removeAttribute("user");

        return R.success("成功");
    }

}
