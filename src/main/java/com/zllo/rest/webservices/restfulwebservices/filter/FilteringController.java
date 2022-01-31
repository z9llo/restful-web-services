package com.zllo.rest.webservices.restfulwebservices.filter;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController {

    @GetMapping(path = "/filtering")
    public MappingJacksonValue retrieveSomeBean() {
        SomeBean bean = new SomeBean("Value 1", "Value 2", "Value 3");
        return filterBean(bean, "field1", "field2");
    }

    @GetMapping(path = "/filtering-list")
    public MappingJacksonValue retrieveSomeBeanList() {
        List<SomeBean> list = Arrays.asList(
            new SomeBean("Value 1", "Value 2", "Value 3"),
            new SomeBean("Value 12", "Value 22", "Value 32")
        );

        return filterBean(list, "field3");
    }

    private MappingJacksonValue filterBean(Object o, String... propertys) {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(propertys);
        FilterProvider filters = new SimpleFilterProvider().addFilter(SomeBean.FILTER_SOME_BEAN, filter);
        MappingJacksonValue mapping = new MappingJacksonValue(o);
        mapping.setFilters(filters);

        return mapping;
    }

}
