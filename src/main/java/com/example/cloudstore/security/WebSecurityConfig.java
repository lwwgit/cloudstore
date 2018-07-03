package com.example.cloudstore.security;


import com.example.cloudstore.authentication.MyAuthenticationFailureHandler;
import com.example.cloudstore.authentication.MyAuthenticationSuccessHandler;
import com.example.cloudstore.filter.CorsControllerFilter;
import com.example.cloudstore.filter.JWTAuthenticationFilter;
import com.example.cloudstore.filter.SmsCodeFilter;
import com.example.cloudstore.filter.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsUtils;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private CorsControllerFilter corsControllerFilter;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        //数据库不存在的时候需要下面这句代码
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setMyAuthenticationFailureHandler(myAuthenticationFailureHandler);
        SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
        smsCodeFilter.setMyAuthenticationFailureHandler(myAuthenticationFailureHandler);

        http.addFilterBefore(corsControllerFilter, SecurityContextPersistenceFilter.class);

        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));

        http.addFilterBefore(validateCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 如果有允许匿名的url，填在下面
                .antMatchers("/code/**","/forget/password","/user/check","/register/**","/swagger-ui.html","/login.html").permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin()//表单登录 .httpBasic是另一种登录方式
                    .loginPage("/loginPage")
                    .loginProcessingUrl("/authentication/login")//form表单提交的路径，默认是'/login'
//                    .successForwardUrl("/login/success")
//                    .failureUrl("/login/error")
                    .successHandler(myAuthenticationSuccessHandler)//返回的是json串
                    .failureHandler(myAuthenticationFailureHandler)
                    .permitAll()
                    .and()
                .logout().logoutUrl("/logout")
                .permitAll()
                .and()
                .rememberMe()
                    .rememberMeParameter("remember_me")
                    .tokenRepository(persistentTokenRepository())
                    .tokenValiditySeconds(3600)
                    .userDetailsService(userDetailsService);


// 关闭CSRF（Cross-site request forgery）跨站请求伪造
        http.csrf().disable();
        http.apply(smsCodeAuthenticationSecurityConfig);


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**","/img/**");
    }
}

