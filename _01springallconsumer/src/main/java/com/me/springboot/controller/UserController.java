package com.me.springboot.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.me.springboot.model.User;
import com.me.springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Liguangzhe
 * @Date created in 18.21 2020/3/23
 */
@Controller
public class UserController {

    @Reference
    private UserService userService;


    @RequestMapping("/index")
    public String index(Model model,
                        @RequestParam(value = "curPage", required = false) Integer curPage) {
        int pageSize = 3;
        if (curPage == null) {
            curPage = 1;
        }
        int totalRows = userService.getUserByTotal();


        int totalPages = totalRows / pageSize;
        int left = totalRows % pageSize;
        if (left > 0) {
            totalPages = totalPages + 1;
        }

        if (curPage < 1) {
            curPage = 1;
        }

        if (curPage > totalPages) {
            curPage = totalPages;
        }

        int startRow = (curPage - 1) * pageSize;
        Map<String, Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("startRow", startRow);
        paramMap.put("pageSize", pageSize);
        List<User> userList = userService.getUserByPage(paramMap);

        model.addAttribute("userList", userList);
        model.addAttribute("curPage", curPage);
        model.addAttribute("totalPages", totalPages);

        return "index";
    }

    /**
     * 添加用户
     * */
    @RequestMapping("/user/toAddUser")
    public String toAddUser() {
        return "addUser";
    }

    @RequestMapping("/user/addUser")
    public String addUser(User user) {
        Integer id = user.getId();

        if (id == null) {
            userService.addUser(user);
        }
        else {
            userService.updateUser(user);
        }

        return "redirect:/index";
    }

    @RequestMapping("/user/toUpdateUser")
    public String toUpdate(Model model, @RequestParam("id") Integer id) {
        User user = userService.getUserById(id);

        model.addAttribute("user", user);

        return "addUser";
    }

    @RequestMapping("/user/delete")
    public String delete(@RequestParam("id") Integer id) {

        userService.deleteUser(id);

        return "redirect:/index";
    }
}
