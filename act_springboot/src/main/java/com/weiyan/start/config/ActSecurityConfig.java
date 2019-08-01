package com.weiyan.start.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mister_wei
 * @version 1.1.1
 * @title SaaS-IHRM
 * @package com.weiyan.start.config
 * @date 2019/7/31 17:28
 */
@Configuration
public class ActSecurityConfig {
    private Logger logger = LoggerFactory.getLogger(ActSecurityConfig.class);

    @Bean
    public UserDetailsService myUserDetailsService() {

        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        String[][] usersGroupsAndRoles = {
                {"salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"ryandawsonuk", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"erdemedeiros", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"system", "password", "ROLE_ACTIVITI_USER"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };

        for (String[] user : usersGroupsAndRoles) {
                                                                                       //从下 标为2 开始 取到这个数组最后的数据
            List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length)); //copyOfRange 是将一个数组 转换为之取的数据 转为list集合操作
            logger.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]"); //这句代码的意思是 把这个list遍历转成 SimpleGrantedAuthority对象 通过collect(Collectors.toList()) 再转成list集合
            inMemoryUserDetailsManager.createUser(new User(user[0], passwordEncoder().encode(user[1]),authoritiesStrings.stream().map(s -> new SimpleGrantedAuthority(s)).collect(Collectors.toList())));

        }


        return inMemoryUserDetailsManager;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
   public static void main(String[] args) {
        String[][] usersGroupsAndRoles = {
                {"salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"ryandawsonuk", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"erdemedeiros", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
                {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
                {"system", "password", "ROLE_ACTIVITI_USER"},
                {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
        };
       for (String[] usersGroupsAndRole : usersGroupsAndRoles) {
           List<String> strings = Arrays.asList(usersGroupsAndRole);
           List<HashMap> collect = strings.stream().map(s -> new HashMap()).collect(Collectors.toList());
           System.out.println(collect);
       }

    }*/
}
