package com.jobconnect.message.aop;

import com.jobconnect.message.util.AESUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 通用加密响应切面
 * 可加密Map、String、List<String>等的所有String类型字段
 * 支持通过注解参数指定字段名，否则加密所有String类型字段
 */
@Aspect
@Component
public class EncryptResponseAspect {
    /**
     * 拦截所有带有@EncryptResponse注解的方法，对响应体进行通用加密
     */
    @Around("@annotation(com.jobconnect.message.aop.EncryptResponse)")
    public Object encryptResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        // 获取注解参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EncryptResponse encryptResponse = method.getAnnotation(EncryptResponse.class);
        String[] fields = encryptResponse.fields();

        if (result instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
            Object body = responseEntity.getBody();
            if (body instanceof Map) {
                Map map = (Map) body;
                if (fields.length == 0) {
                    // 加密所有String类型字段
                    for (Object k : map.keySet()) {
                        Object v = map.get(k);
                        if (v instanceof String) {
                            map.put(k, AESUtil.encrypt((String) v));
                        }
                    }
                } else {
                    // 只加密指定字段
                    for (String field : fields) {
                        Object v = map.get(field);
                        if (v instanceof String) {
                            map.put(field, AESUtil.encrypt((String) v));
                        }
                    }
                }
            } else if (body instanceof String) {
                // 直接加密字符串
                return ResponseEntity.status(responseEntity.getStatusCode())
                        .headers(responseEntity.getHeaders())
                        .body(AESUtil.encrypt((String) body));
            } else if (body instanceof List) {
                // 加密List<String>
                List list = (List) body;
                for (int i = 0; i < list.size(); i++) {
                    Object v = list.get(i);
                    if (v instanceof String) {
                        list.set(i, AESUtil.encrypt((String) v));
                    }
                }
            }
            // 其他类型（如自定义对象）可扩展
        }
        return result;
    }
}
