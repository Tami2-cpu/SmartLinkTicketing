package com.zhilian.core.config;

import cn.hutool.core.util.StrUtil;
import com.zhilian.core.utils.JwtUtil;
import com.zhilian.core.utils.UserContext;
import com.zhilian.user.entity.User;
import com.zhilian.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        logger.info("Received token: {}", token != null ? token.substring(0, 20) + "..." : "null");

        try {
            if (StrUtil.isNotBlank(token)) {
                String tokenWithoutBearer = token.replace("Bearer ", "");
                if (StrUtil.isNotBlank(tokenWithoutBearer)) {
                    // 验证token是否过期
                    if (!jwtUtil.isTokenExpired(tokenWithoutBearer)) {
                        String username = jwtUtil.getUsernameFromToken(tokenWithoutBearer);
                        logger.info("Extracted username from token: {}", username);

                        if (StrUtil.isNotBlank(username) && userDetailsService != null) {
                            try {
                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                logger.info("Loaded user details for: {}", username);

                                // 验证token是否有效
                                if (jwtUtil.validateToken(tokenWithoutBearer, username)) {
                                    logger.info("Token validation successful for: {}", username);
                                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());
                                    SecurityContextHolder.getContext().setAuthentication(authentication);
                                    logger.info("Set authentication in SecurityContext for: {}", username);

                                    // 从数据库获取完整的用户信息并存储到ThreadLocal
                                    User user = userService.getByUsername(username);
                                    if (user != null) {
                                        UserContext.setUser(user);
                                        logger.info("Set user in UserContext: {}", user.getUsername());
                                    } else {
                                        logger.error("User not found in database: {}", username);
                                    }
                                } else {
                                    logger.error("Token validation failed for: {}", username);
                                }
                            } catch (UsernameNotFoundException e) {
                                logger.error("User not found: {}", username);
                            }
                        } else {
                            logger.error("Invalid username or userDetailsService is null");
                        }
                    } else {
                        logger.error("JWT token已过期");
                    }
                } else {
                    logger.info("Empty token after removing Bearer prefix");
                }
            } else {
                logger.info("No token provided");
            }
        } catch (Exception e) {
            logger.error("JWT解析失败: {}", e.getMessage());
            e.printStackTrace();
        }

        chain.doFilter(request, response);

        // 清理ThreadLocal，防止内存泄漏
        logger.info("Clearing UserContext");
        UserContext.clear();
    }
}