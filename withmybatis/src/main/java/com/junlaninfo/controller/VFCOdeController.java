package com.junlaninfo.controller;

import com.junlaninfo.config.VerificationCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by 辉 on 2020/8/11.
 * 生成验证码
 */
@RestController
public class VFCOdeController {
    @GetMapping("/code")
   public  void   code(HttpSession  session, HttpServletResponse  response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        session.setAttribute("verify_code", text);
        VerificationCode.output(image,response.getOutputStream());
    }
}
