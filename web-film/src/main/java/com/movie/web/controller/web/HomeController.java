package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.*;
import com.movie.core.service.IActorService;
import com.movie.core.service.IFilmService;
import com.movie.core.utils.GoogleUtils;
import com.movie.core.utils.RestFB;
import com.movie.web.utils.SecurityUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller(value = "HomeControllerOfWeb")
public class HomeController {
    @Autowired
    private IFilmService filmService;
    @Autowired
    private GoogleUtils googleUtils;
    @Autowired
    private IActorService actorService;

    @RequestMapping(value = "/trang-chu", method = RequestMethod.GET)
    public String homePage(Model model) {
        List<FilmDTO> filmNews = filmService.findByProperties(1, 8, "createdDate", "0");
        model.addAttribute("filmNews", filmNews);
        //phim lẻ
        List<ActorDTO> actorDTOS = actorService.findByProperties("", 1, 4, null, null);
        model.addAttribute("actors", actorDTOS);

        List<FilmDTO> filmles = filmService.findByProperties("phim-le", 1, 8, "view", "0");
        model.addAttribute("filmles", filmles);

        List<FilmDTO> filmTop = filmService.findByProperties("phim-le", 1, 8, "scores", "0");
        model.addAttribute("filmTop", filmTop);

        List<FilmDTO> filmVote = filmService.findByProperties("phim-le", 1, 8, "totalVote", "0");
        model.addAttribute("filmVote", filmVote);

        List<FilmDTO> filmCommings = filmService.findByProperties("phim-le", true, 1, 8, "createdDate", "0");
        model.addAttribute("filmCommings", filmCommings);
        ///phim bộ
        List<FilmDTO> filmBoPhoBien = filmService.findByProperties("phim-bo", 1, 8, "view", "0");
        model.addAttribute("filmBoPhoBien", filmBoPhoBien);

        List<FilmDTO> filmBoTop = filmService.findByProperties("phim-bo", 1, 8, "scores", "0");
        model.addAttribute("filmBoTop", filmBoTop);

        List<FilmDTO> filmBoVote = filmService.findByProperties("phim-bo", 1, 8, "totalVote", "0");
        model.addAttribute("filmBoVote", filmBoVote);

        List<FilmDTO> filmBoCommings = filmService.findByProperties("phim-bo", true, 1, 8, "createdDate", "0");
        model.addAttribute("filmBoCommings", filmBoCommings);

        ///phim chiếu rạp
        List<FilmDTO> filmChieuRapPhoBien = filmService.findByProperties("phim-chieu-rap", 1, 8, "view", "0");
        model.addAttribute("filmChieuRapPhoBien", filmChieuRapPhoBien);

        List<FilmDTO> filmChieuRapTop = filmService.findByProperties("phim-chieu-rap", 1, 8, "scores", "0");
        model.addAttribute("filmChieuRapTop", filmChieuRapTop);

        List<FilmDTO> filmChieuRapVote = filmService.findByProperties("phim-chieu-rap", 1, 8, "totalVote", "0");
        model.addAttribute("filmChieuRapVote", filmChieuRapVote);

        List<FilmDTO> filmChieuRapCommings = filmService.findByProperties("phim-chieu-rap", true, 1, 8, "createdDate", "0");
        model.addAttribute("filmChieuRapCommings", filmChieuRapCommings);
        return "views/web/home";
    }


    @RequestMapping(value = "/dang-xuat", method = RequestMethod.GET)
    public String logoutOfUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/video/dang-xuat", method = RequestMethod.GET)
    public String logoutOfUserInVideoPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/video/trang-chu";
    }

    @RequestMapping(value = "/dang-nhap/google", method = RequestMethod.GET)
    public ModelAndView loginPageByGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            new ModelAndView("redirect:/dang-nhap?message=google_error");
        }
        String accessToken = googleUtils.getToken(code);

        GoogleDTO googleDTO = googleUtils.getUserInfo(accessToken);
        UserDetails userDetail = googleUtils.buildUser(googleDTO);
        MyUser myUser = new MyUser(userDetail.getUsername(), userDetail.getPassword(), true, true, true, true, userDetail.getAuthorities());
        myUser.setPhoto(googleDTO.getPicture());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(myUser, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ModelAndView("redirect:/trang-chu");
    }


    @Autowired
    private RestFB restFB;

    @RequestMapping("/AccessFacebook/login-facebook")
    public String loginFacebook(HttpServletRequest request) {
        String code = request.getParameter("code");
        String accessToken = "";
        try {
            accessToken = restFB.getToken(code);
        } catch (IOException e) {
            return "redirect:/dang-nhap?facebook=error";
        }
        com.restfb.types.User user = restFB.getUserInfo(accessToken);
        UserDetails userDetail = restFB.buildUser(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/trang-chu";
    }


   /* @GetMapping("/a")
    public String index() {
        return "index";
    }*/

    @GetMapping("/")
    public String index1() {
        return "redirect:/trang-chu";
    }

    @GetMapping("/test/load")
    public String loadVideo(){
        return "redirect:https://drive.google.com/file/d/1ywttqCmBDVc-CQxzzMh2oACnaP4i9wRW/view";
    }
}
