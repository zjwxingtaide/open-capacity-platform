package com.open.capacity.client.oauth2.controller;

import com.open.capacity.client.oauth2.token.store.RedisTemplateTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/hello")
    public String hello() {
        redisTemplate.opsForValue().set("hello", "owen");
        return "hello";
    }

    @RequestMapping(value = {"/users"}, produces = "application/json") // 获取用户信息。/auth/user
    public Map<String, Object> user(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        logger.debug("认证详细信息:" + user.getUserAuthentication().getPrincipal().toString());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }

    @RequestMapping(value = {"/user"}, produces = "application/json") // 获取用户信息。/auth/user
    public Principal user(Principal user) {
        return user;
    }


    @GetMapping("/del/{accessToken}/{refreshToken}")
    public String hello2(@PathVariable String accessToken, @PathVariable String refreshToken) {
        RedisTemplateTokenStore redisTemplateStore = new RedisTemplateTokenStore();
        redisTemplateStore.setRedisTemplate(redisTemplate);
        redisTemplateStore.removeAccessToken(accessToken);
        redisTemplateStore.removeRefreshToken(refreshToken);
        return "delR";
    }
}
