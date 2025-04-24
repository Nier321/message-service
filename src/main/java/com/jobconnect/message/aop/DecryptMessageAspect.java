package com.jobconnect.message.aop;

import com.jobconnect.message.util.AESUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 通用解密切面
 * 支持通过注解精确指定解密目标参数及字段
 */
@Aspect
@Component
public class DecryptMessageAspect {
    @Around("@annotation(com.jobconnect.message.aop.DecryptMessage)")
    public Object decryptMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DecryptMessage decryptMessage = method.getAnnotation(DecryptMessage.class);
        String paramName = decryptMessage.param();
        String[] fields = decryptMessage.fields();
        String[] paramNames = signature.getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {
            if (!paramNames[i].equals(paramName)) continue;
            Object arg = args[i];
            if (arg == null) continue;
            // 1. Map类型参数
            if (arg instanceof Map) {
                Map map = (Map) arg;
                if (fields.length == 0) {
                    for (Object k : map.keySet()) {
                        Object v = map.get(k);
                        if (v instanceof String) {
                            map.put(k, AESUtil.decrypt((String) v));
                        }
                    }
                } else {
                    for (String field : fields) {
                        Object v = map.get(field);
                        if (v instanceof String) {
                            map.put(field, AESUtil.decrypt((String) v));
                        }
                    }
                }
            // 2. String参数
            } else if (arg instanceof String && fields.length == 0) {
                args[i] = AESUtil.decrypt((String) arg);
            // 3. 自定义对象参数
            } else if (!(arg instanceof String) && fields.length > 0) {
                Class<?> clazz = arg.getClass();
                for (String fieldName : fields) {
                    try {
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        Object value = field.get(arg);
                        if (value instanceof String) {
                            String decrypted = AESUtil.decrypt((String) value);
                            field.set(arg, decrypted);
                        }
                    } catch (NoSuchFieldException ignored) {}
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
