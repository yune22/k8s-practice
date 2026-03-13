package com.beyond.ordersystem.ordering.feignclients;

import com.beyond.ordersystem.ordering.dtos.OrderCreateDto;
import com.beyond.ordersystem.ordering.dtos.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

// name부분은 eureka에 등록된 application name을 의미
// url부분은 k8s의 서비스명
@FeignClient(name = "product-service", url="${product.service.url:}")
public interface ProductFeignClient {

    @GetMapping("/product/detail/{id}")
    ProductDto getProductById(@PathVariable("id") Long id);

    @GetMapping("/product/updatestock")
    void updateStockQuantity(@RequestBody OrderCreateDto dto);
}
