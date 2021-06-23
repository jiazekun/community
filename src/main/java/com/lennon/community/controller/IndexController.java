package com.lennon.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jzk
 * @date 2021/6/23-11:42
 */
@Controller
public class IndexController {
    @GetMapping("/a")
    public String index(){
        return "index";
    }

}
