package com.ruoyi.common.core.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.ruoyi.common.core.jackson.deserializer.LocalDateExtDeserializer;
import com.ruoyi.common.core.jackson.deserializer.LocalDateTimeExtDeserializer;
import com.ruoyi.common.core.jackson.serializer.LocalDateExtSerializer;
import com.ruoyi.common.core.jackson.serializer.LocalDateTimeExtSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * WebMvcConfig
 *
 * @author huajiao
 * @date 2020-02-16 12:00
 */
@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Value("${spring.jackson.serialization.write-dates-as-timestamps:false}")
    private Boolean writeDatesAsTimestamps;

    /**
     * 让SpringMvc支持解析Jdk8Time
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        registrar.registerFormatters(registry);
    }

    /**
     * 增加了Jackson对Jdk8Time的支持
     *
     * @param converters RequestBody内容转换器列表, 这里只需要对jackson提供的转换器进行拦截
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (MappingJackson2HttpMessageConverter.class.isAssignableFrom(converter.getClass())) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                SimpleModule dateModule = new SimpleModule();
                //jackson序列化
                dateModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
                dateModule.addSerializer(LocalDate.class, new LocalDateExtSerializer(writeDatesAsTimestamps));
                dateModule.addSerializer(LocalDateTime.class, new LocalDateTimeExtSerializer(writeDatesAsTimestamps));
                //jackson反序列化, 默认的格式为 yyyy-MM-ddTHH:mm:ss, 故需要配置
                dateModule.addDeserializer(LocalDate.class, new LocalDateExtDeserializer());
                dateModule.addDeserializer(LocalDateTime.class, new LocalDateTimeExtDeserializer());
                objectMapper.registerModule(dateModule);
            }
        }
    }

    /**
     * 设置允许跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

}
