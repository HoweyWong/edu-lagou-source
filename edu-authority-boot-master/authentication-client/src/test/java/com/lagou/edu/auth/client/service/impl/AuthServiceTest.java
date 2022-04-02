package com.lagou.edu.auth.client.service.impl;

import com.lagou.edu.auth.client.provider.AuthProvider;
import com.lagou.edu.auth.client.service.IAuthService;
import com.lagou.edu.common.entity.vo.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.mockito.Mockito.when;

public class AuthServiceTest {

    @InjectMocks
    IAuthService authService;

    @Mock
    AuthProvider authProvider;

    private static final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwib3JnYW5pemF0aW9uIjoiYWRtaW4iLCJleHAiOjEzNTcyMDIxNjM3LCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiI4MzcyMzI0Ny00ZDA5LTQ0YjYtYTNlOS01OGUzYzZiMGUzYjIiLCJjbGllbnRfaWQiOiJ0ZXN0X2NsaWVudCJ9.IkOtKapS5PJLKU1NfiqVSCgsQngE7qGnIx1NziJMvVA";
    private static final String BEARER = "Bearer ";

    @Before
    public void before() throws NoSuchFieldException, IllegalAccessException {
        authService = new AuthService();
        setInstancePrivateField(authService, "signingKey", "123456");
        setInstancePrivateField(authService, "ignoreUrls", "/oauth,/open");
        MockitoAnnotations.initMocks(this);
    }

    private void setInstancePrivateField(Object instance, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field signingKeyField = instance.getClass().getDeclaredField(fieldName);
        signingKeyField.setAccessible(true);
        signingKeyField.set(instance, value);
    }

    @Test
    public void testInvalidJwtAccessToken_假如授权服务通过给定密钥生成了token_当输入该token组成的authentication_那么返回false表示token有效() {
        Assert.assertFalse(authService.invalidJwtAccessToken(BEARER + VALID_TOKEN));
    }

    @Test
    public void testInvalidJwtAccessToken_假如_当输入随机字串_那么返回true表示token无效() {
        String authentication = BEARER + "im random string";
        Assert.assertTrue(authService.invalidJwtAccessToken(authentication));
    }

    @Test
    public void testInvalidJwtAccessToken_假如有人获取用户token_当输入篡改过playload中信息_那么返回true表示token无效() {
        String authentication = BEARER + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.111eyJGc2VyX25hbWUiOiJ6aG91dGFvbyIsInNjb3BlIjpbInJlYWQiXSwib3JnYW5pemF0aW9uIjoiemhvdXRhb28iLCJleHAiOjE1Mjc0NTM5NDQsImF1dGhvcml0aWVzIjpbIkFETUlOIiwiSVQiXSwianRpIjoiZTZiNzM5ZmUtYWEzZC00Y2RmLWIxZjUtNzZkMmVlMjU0ODU1IiwiY2xpZW50X2lkIjoidGVzdF9jbGllbnQifQ.l6PQrs98zT40H6Ad4NHE7NSXyeWnMn-ZhURw3zO-EfE";
        Assert.assertTrue(authService.invalidJwtAccessToken(authentication));
    }

    @Test
    public void testInvalidJwtAccessToken_假如有人获取用户token_当输入token去掉了signature_那么返回true表示token无效() {
        String authentication = BEARER + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ6aG91dGFvbyIsInNjb3BlIjpbInJlYWQiXSwib3JnYW5pemF0aW9uIjoiemhvdXRhb28iLCJleHAiOjE1Mjc0NTM5NDQsImF1dGhvcml0aWVzIjpbIkFETUlOIiwiSVQiXSwianRpIjoiZTZiNzM5ZmUtYWEzZC00Y2RmLWIxZjUtNzZkMmVlMjU0ODU1IiwiY2xpZW50X2lkIjoidGVzdF9jbGllbnQifQ";
        Assert.assertTrue(authService.invalidJwtAccessToken(authentication));
    }

    @Test
    public void testIgnoreAuthentication_假如配置的忽略前缀为oauth和open_当用户请求以oauth开头的url_那么返回返回true() {
        Assert.assertTrue(authService.ignoreAuthentication("/oauth/token?test=123"));
    }

    @Test
    public void testIgnoreAuthentication_假如配置的忽略前缀为oauth和open_当用户请求以open开头的url_那么返回返回true() {
        Assert.assertTrue(authService.ignoreAuthentication("/open/"));
    }

    @Test
    public void testIgnoreAuthentication_假如配置的忽略前缀为oauth和open_当用户请求以test开头的url_那么返回返回true() {
        Assert.assertFalse(authService.ignoreAuthentication("/test"));
    }

    @Test
    public void testIgnoreAuthentication_假如配置的忽略前缀为oauth和open_当用户请求以open结尾的url_那么返回返回true() {
        Assert.assertFalse(authService.ignoreAuthentication("/test/open"));
    }

}