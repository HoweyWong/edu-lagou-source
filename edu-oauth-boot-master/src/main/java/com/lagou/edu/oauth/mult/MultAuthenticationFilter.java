package com.lagou.edu.oauth.mult;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.oauth.exception.AuthErrorType;
import com.lagou.edu.oauth.mult.authenticator.MultAuthenticator;
import com.lagou.edu.oauth.mult.authenticator.sms.exception.SmsValidateException;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsCodeValidateResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class MultAuthenticationFilter extends GenericFilterBean implements ApplicationContextAware {

    private static final String AUTH_TYPE_PARAM_NAME = "auth_type";

    private static final String OAUTH_TOKEN_URL = "/oauth/token";

    private Collection<MultAuthenticator> authenticators;

    private ApplicationContext applicationContext;

    private RequestMatcher requestMatcher;

    public MultAuthenticationFilter() {
        this.requestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "GET"),
                new AntPathRequestMatcher(OAUTH_TOKEN_URL, "POST")
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String grantType = request.getParameter("grant_type");
        if (requestMatcher.matches(request) && "password".equals(grantType)) {

            //设置集成登录信息
            MultAuthentication multAuthentication = new MultAuthentication();
            multAuthentication.setAuthType(request.getParameter(AUTH_TYPE_PARAM_NAME));
            multAuthentication.setAuthParameters(request.getParameterMap());
            // 放入当前线程中
            MultAuthenticationContext.set(multAuthentication);
            try {
                //预处理
                this.prepare(multAuthentication, response);

                filterChain.doFilter(request, response);

                //后置处理
                this.complete(multAuthentication);
            } catch (Exception e) {
                PrintWriter writer = response.getWriter();
                if (e instanceof SmsValidateException) {
                    SmsValidateException exception = (SmsValidateException) e;
                    SmsCodeValidateResult result = exception.getResult();
                    response.setContentType("application/json;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    Result fail = Result.fail(result.getAuthErrorType(), AuthErrorType.ERROR_VERIFY_CODE);
                    writer.write(JSON.toJSONString(fail));
                    writer.flush();
                    writer.close();
                }
                return;
            } finally {
                MultAuthenticationContext.clear();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 进行预处理
     *
     * @param multAuthentication
     * @param response
     */
    private void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

        //延迟加载认证器
        if (this.authenticators == null) {
            synchronized (this) {
                Map<String, MultAuthenticator> integrationAuthenticatorMap = applicationContext.getBeansOfType(MultAuthenticator.class);
                if (integrationAuthenticatorMap != null) {
                    this.authenticators = integrationAuthenticatorMap.values();
                }
            }
        }

        if (this.authenticators == null) {
            this.authenticators = new ArrayList<>();
        }

        for (MultAuthenticator authenticator : authenticators) {
            if (authenticator.support(multAuthentication)) {
                authenticator.prepare(multAuthentication, response);
            }
        }
    }

    /**
     * 后置处理
     *
     * @param multAuthentication
     */
    private void complete(MultAuthentication multAuthentication) {
        for (MultAuthenticator authenticator : authenticators) {
            if (authenticator.support(multAuthentication)) {
                authenticator.complete(multAuthentication);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
