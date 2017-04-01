package com.splant.smartgarden.daggerModel.Scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by aifengbin on 2017/3/13.
 * 此接口用来定义注解器的生命周期和activity相同
 */

@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}
