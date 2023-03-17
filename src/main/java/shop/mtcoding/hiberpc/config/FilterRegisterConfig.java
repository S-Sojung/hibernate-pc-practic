package shop.mtcoding.hiberpc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.hiberpc.config.filter.MyBlackListFilter;

@Configuration
public class FilterRegisterConfig {
    // 애를 IoC에 띄우고 DI 를 해줘야한다 @RequiredArgsConstructor
    // private final MyBlackListFilter myBlackListFilter;

    @Bean // Conponent스캔하고 얘를 띄울 때, 얘를 실햏하고 IoC에 넣어준다.
    // return을 해주면 IOC에 저장을 시켜준다 내가 만든게 아닌 것은 Bean으로 해주는게 좋다
    public FilterRegistrationBean<?> blackListFilter() {
        FilterRegistrationBean<MyBlackListFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new MyBlackListFilter());
        registration.addUrlPatterns("/filter");
        registration.setOrder(1); // 필터의 순서
        return registration; // 필터 등록 .. 이 객체를 IoC에 넣기만 하면 된다!!!
    }
}
