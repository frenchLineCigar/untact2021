package com.tena.untact2021.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //런타임까지 유지
@Target(ElementType.PARAMETER) //파라미터만에만 적용
public @interface CurrentMember {

}
