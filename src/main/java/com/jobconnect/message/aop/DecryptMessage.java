package com.jobconnect.message.aop;

import java.lang.annotation.*;

/**
 * 自定义注解，用于标记需要自动解密的参数
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DecryptMessage {
    /**
     * 要解密的参数名（方法参数名，必须与方法签名一致）
     */
    String param();
    /**
     * 要解密的字段名（可选，适用于对象/Map），为空则解密整个参数
     */
    String[] fields() default {};
}
