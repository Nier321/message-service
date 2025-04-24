package com.jobconnect.message.aop;

import java.lang.annotation.*;

/**
 * 用于标记Controller方法，其返回值中的指定字段会被自动AES加密
 * 可通过fields参数指定需要加密的字段名，留空则加密所有String类型字段
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptResponse {
    /**
     * 需要加密的字段名，留空则加密所有String类型字段
     */
    String[] fields() default {};
}
