package com.dargon.reggie.common;


import com.dargon.reggie.exception.CustomException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class})
@ResponseBody
public class ProjectExceptionAdvice {

       @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
       public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){



           if (ex.getMessage().contains("Duplicate entry")){
               String[] s = ex.getMessage().split(" ");
               String msg = s[2]+"已存在";

               return R.error(msg);

           }

           return R.error("未知错误");
       }

       @ExceptionHandler(CustomException.class)
       public R<String> operationException(CustomException customException){


           return R.error(customException.getMessage());
       }


}
