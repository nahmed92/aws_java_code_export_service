package com.etilize.burraq.eas.specification;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for product-specifications-service
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Component
@FeignClient("product-specifications-service")
public interface ProductSpecificationsServiceClient {

    /**
     * Method to get product specifications
     *
     * @param id the product id
     * @param localeId optional localeId
     * @return specifications
     */
    @GetMapping("/specifications/{id}")
    Resource<Specification> findByIdAndLocaleId(@PathVariable("id") String id,
            @RequestParam(value = "localeId", required = true) String localeId);
}
