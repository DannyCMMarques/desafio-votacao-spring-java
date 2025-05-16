package com.crud.demo.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Around("execution(*  com.crud.demo..*(..))")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("Entrando em {}() com args = {}", 
             joinPoint.getSignature().getName(), joinPoint.getArgs());
    try {
      Object result = joinPoint.proceed();
      log.info("Saindo de {}() com resultado = {}", 
               joinPoint.getSignature().getName(), result);
      return result;
    } catch (Throwable t) {
      log.error("Erro em {}(): {}", 
                joinPoint.getSignature().getName(), t.getMessage());
      throw t;
    }

  }
}