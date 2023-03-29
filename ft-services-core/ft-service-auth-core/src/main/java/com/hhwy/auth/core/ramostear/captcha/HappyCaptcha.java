package com.hhwy.auth.core.ramostear.captcha;

import com.hhwy.auth.core.util.SpringContextUtils;
import com.hhwy.common.redis.service.RedisService;
import com.ramostear.captcha.common.Fonts;
import com.ramostear.captcha.core.AnimCaptcha;
import com.ramostear.captcha.core.Captcha;
import com.ramostear.captcha.support.CaptchaStyle;
import com.ramostear.captcha.support.CaptchaType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HappyCaptcha {

    private RedisService redisService;

    public static final String SESSION_KEY = "happy-captcha";
    private CaptchaType type;
    private CaptchaStyle style;
    private Font font;
    private int width;
    private int height;
    private int length;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public static Builder require(HttpServletRequest request, HttpServletResponse response) {
        return new Builder(request, response);
    }

    public boolean finish() {
        try {
            Long aLong = 60L;
            if (this.style.equals(CaptchaStyle.IMG)) {
                Captcha captcha = new Captcha();
                captcha.setType(this.type);
                captcha.setWidth(this.width);
                captcha.setHeight(this.height);
                captcha.setLength(this.length);
                captcha.setFont(this.font);
                this.setHeader(this.response);
                String replace = UUID.randomUUID().toString().replace("-", "");
                response.setHeader("redisKey",replace);
                this.redisService = SpringContextUtils.getApplicationContext().getBean(RedisService.class);
                //存到redis内
                System.out.println("存储redis获取的用户信息:"+ replace);
                redisService.setCacheObject(replace,captcha.getCaptchaCode(),aLong,TimeUnit.SECONDS);
                System.out.println("token数据存储完毕>>>>>>>>>>>>>>");
                this.request.getSession().setAttribute("happy-captcha", captcha.getCaptchaCode());
                captcha.render(this.response.getOutputStream());
                return true;
            } else if (this.style.equals(CaptchaStyle.ANIM)) {
                AnimCaptcha captcha = new AnimCaptcha();
                captcha.setType(this.type);
                captcha.setWidth(this.width);
                captcha.setHeight(this.height);
                captcha.setLength(this.length);
                captcha.setFont(this.font);
                this.setHeader(this.response);
                String replace = UUID.randomUUID().toString().replace("-", "");
                response.setHeader("redisKey",replace);
                this.redisService = SpringContextUtils.getApplicationContext().getBean(RedisService.class);
                //存到redis内
                System.out.println("存储redis获取的用户信息:"+ replace);
                redisService.setCacheObject(replace,captcha.getCaptchaCode(),aLong,TimeUnit.SECONDS);
                System.out.println("token数据存储完毕>>>>>>>>>>>>>>");
                this.request.getSession().setAttribute("happy-captcha", captcha.getCaptchaCode());
                captcha.render(this.response.getOutputStream());
                return true;
            } else {
                return false;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean verification(HttpServletRequest request, String code, boolean ignoreCase) {
        if (code != null && !code.trim().equals("")) {
            String captcha = (String)request.getSession().getAttribute("happy-captcha");
            return ignoreCase ? code.equalsIgnoreCase(captcha) : code.equals(captcha);
        } else {
            return false;
        }
    }

    public static void remove(HttpServletRequest request) {
        request.getSession().removeAttribute("happy-captcha");
    }

    public void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
    }

    private HappyCaptcha(Builder builder) {
        this.type = builder.type;
        this.style = builder.style;
        this.font = builder.font;
        this.width = builder.width;
        this.height = builder.height;
        this.length = builder.length;
        this.request = builder.request;
        this.response = builder.response;
    }

    public static class Builder {
        private CaptchaType type;
        private CaptchaStyle style;
        private Font font;
        private int width;
        private int height;
        private int length;
        private final HttpServletRequest request;
        private final HttpServletResponse response;

        public Builder(HttpServletRequest request, HttpServletResponse response) {
            this.type = CaptchaType.DEFAULT;
            this.style = CaptchaStyle.IMG;
            this.font = Fonts.getInstance().defaultFont();
            this.width = 160;
            this.height = 50;
            this.length = 5;
            this.request = request;
            this.response = response;
        }

        public HappyCaptcha build() {
            return new HappyCaptcha(this);
        }

        public Builder type(CaptchaType type) {
            this.type = type;
            return this;
        }

        public Builder style(CaptchaStyle style) {
            this.style = style;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder font(Font font) {
            this.font = font;
            return this;
        }
    }
}
