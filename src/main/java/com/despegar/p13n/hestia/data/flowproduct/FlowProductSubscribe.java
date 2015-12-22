package com.despegar.p13n.hestia.data.flowproduct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.despegar.p13n.euler.commons.client.model.BrandGroup;
import com.despegar.p13n.euler.commons.client.model.EventName;
import com.despegar.p13n.euler.commons.client.model.Flow;
import com.despegar.p13n.euler.commons.client.model.Product;

/**
 * Marks a method as an event handler for a {@link Flow} 
 * and {@link Product} combination.
 * 
 * Bus will call each registered method sequentially.
 * 
 * @author sebastian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlowProductSubscribe {
    Flow[] flow() default {};

    EventName[] event() default {};

    Product[] product() default {};

    BrandGroup brand() default BrandGroup.ALL_DESPEGAR;

    boolean filterInternal() default true;

}
