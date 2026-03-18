package com.example.demo._core.advice;

import com.example.demo._core.handler.ex.Exception400;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Aspect
@Component
public class MyValidationAdvice {

    @Before("execution(* com.example.demo..*Controller.*(..))")
    public void validationAdvice(JoinPoint jp) {
        Object[] args = jp.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) {
                    throw new Exception400(
                            bindingResult.getFieldErrors().get(0).getDefaultMessage() + ":" + bindingResult.getFieldErrors().get(0).getField()
                    );
                }
            }
        }
    }
}
