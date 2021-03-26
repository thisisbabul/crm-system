package com.sohanf.crmsystem.multitenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TenantInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Tenant Interceptor - PreHandle");

        try {
            /*String url = request.getRequestURL().toString();
            boolean hasHttp = url.contains("http://".toString());
            int firstIndexOfSubdomain = hasHttp ? 7 : 8;
            int lastIndexOfSubdomain = url.indexOf('.');
            if (lastIndexOfSubdomain != -1) {
                String subdomain = url.substring(firstIndexOfSubdomain, lastIndexOfSubdomain);
                Optional<User> user = userRepository.findBySubdomain(subdomain);
                if (user.get().getSubdomain().equals(subdomain)) {
                    return HandlerInterceptor.super.preHandle(request, response, handler);
                }
                return false;
            }*/
            System.out.println("In preHandle we are Intercepting the Request");
            System.out.println("____________________________________________");
            String requestURI = request.getRequestURI();
            String tenantId = request.getHeader("tenantId");
            System.out.println("RequestURI::" + requestURI +" || Search for tenantId  :: " + tenantId);
            System.out.println("____________________________________________");
            if (tenantId == null) {
                TenantContext.setCurrentTenant("public");
                /*response.getWriter().write("tenantId not present in the Request Header");
                response.setStatus(400);
                return false;*/
            }
            TenantContext.setCurrentTenant(tenantId);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        catch (Throwable ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        TenantContext.clear();
    }
}
