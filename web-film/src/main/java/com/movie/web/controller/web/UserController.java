package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.*;
import com.movie.core.service.IEvaluateService;
import com.movie.core.service.IFilmService;
import com.movie.core.service.IUserService;
import com.movie.core.service.IVideoService;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.ActorCommand;
import com.movie.web.command.FilmCommand;
import com.movie.web.command.UserCommand;
import com.movie.web.utils.SecurityUtils;
import com.restfb.types.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller(value = "UserControllerOfWen")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IFilmService filmService;
    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @Autowired
    private IEvaluateService evaluateService;

    @RequestMapping(value = "/trang-ca-nhan/anh-dai-dien", method = RequestMethod.GET)
    public String userAvatar(Model model, @RequestParam(value = "message", required = false) String message) throws Exception {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        return "views/web/userAvatar";
    }

    @Autowired
    private IVideoService videoService;

    @RequestMapping(value = "/trang-ca-nhan/dang-video", method = RequestMethod.GET)
    public String userUploadVideo(Model model, @RequestParam(value = "message", required = false) String message,
                                  @RequestParam(value = "videoId", required = false) Long videoId) throws Exception {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        VideoDTO videoDTO = new VideoDTO();
        if (videoId != null) {
            videoDTO = videoService.findOneById(videoId, SecurityUtils.getUserPrincipal().getId());
        }
        model.addAttribute("video", videoDTO);
        return "views/web/userUploadVideo";
    }

    @RequestMapping(value = "/trang-ca-nhan/chinh-sua", method = RequestMethod.GET)
    public String userInfo(Model model, @RequestParam(value = "message", required = false) String message) throws Exception {
        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        model.addAttribute("user", userDTO);
        return "views/web/UserProfile";
    }

    @RequestMapping(value = "/trang-ca-nhan/phim-yeu-thich", method = RequestMethod.GET)
    public String favoriteFilm(Model model,
                               @RequestParam(value = "message", required = false) String message,
                               @RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "sortDsc", required = false) String sortDsc
    ) throws Exception {
        FilmCommand command = new FilmCommand();
        command = setValueForCommand(command, page, sort, sortDsc, limit);
        List<FilmDTO> filmDTOS = filmService.findByProperties(SecurityUtils.getUserPrincipal().getId(), 1, command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());

        command.setListResult(filmDTOS);
        command.setTotalItems(filmService.getTotalFilmFavorite(SecurityUtils.getUserPrincipal().getId(), 1));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));


        UserDTO userDTO = userService.findOneById(SecurityUtils.getUserPrincipal().getId());
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        model.addAttribute(WebConstant.LIST_ITEM, command);

        model.addAttribute("user", userDTO);
        return "views/web/favoriteFilm";
    }

    @RequestMapping(value = "/trang-ca-nhan/chinh-sua-thong-tin", method = RequestMethod.POST)
    public String saveProfile(UserDTO userDTO) {
        userDTO.setId(SecurityUtils.getUserPrincipal().getId());
        userDTO.setUserName(SecurityUtils.getUserPrincipal().getUsername());
        try {
            userService.save(userDTO);
            return "redirect:/trang-ca-nhan/chinh-sua?message=redirect_update";
        } catch (Exception e) {
            return "redirect:/trang-ca-nhan/chinh-sua?message=redirect_error";
        }
    }

    @RequestMapping(value = "/trang-ca-nhan/follower", method = RequestMethod.GET)
    public String follower(Model model) throws Exception {
        List<UserDTO> userDTOS = userService.findFollower(SecurityUtils.getUserPrincipal().getId());
        model.addAttribute("followers", userDTOS);
        return "views/video/follower";
    }

    @RequestMapping(value = "/trang-ca-nhan/MyFollow", method = RequestMethod.GET)
    public String MyFollow(Model model) throws Exception {
        List<UserDTO> userDTOS = userService.findMyFollow(SecurityUtils.getUserPrincipal().getId());
        model.addAttribute("Myfollow", userDTOS);
        return "views/video/MyFollow";
    }

    @RequestMapping(value = "/trang-ca-nhan/phim-theo-doi", method = RequestMethod.GET)
    public String followFilm(Model model,
                             @RequestParam(value = "message", required = false) String message,
                             @RequestParam(value = "page", required = false) Integer page,
                             @RequestParam(value = "limit", required = false) Integer limit,
                             @RequestParam(value = "sort", required = false) String sort,
                             @RequestParam(value = "sortDsc", required = false) String sortDsc
    ) throws Exception {
        FilmCommand command = new FilmCommand();
        command = setValueForCommand(command, page, sort, sortDsc, limit);
        List<FilmDTO> filmDTOS = filmService.findByUserId(SecurityUtils.getUserPrincipal().getId(), 1, command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());

        command.setListResult(filmDTOS);
        command.setTotalItems(filmService.getTotalFilmFavorite(SecurityUtils.getUserPrincipal().getId(), 1));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        model.addAttribute(WebConstant.LIST_ITEM, command);

        return "views/web/followFilm";
    }


    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.user.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.user.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.user.message.update.success"));
        return mapValue;
    }


    @RequestMapping(value = "/trang-ca-nhan/doi-mat-khau", method = RequestMethod.POST)
    public String changePassword(UserDTO userDTO) throws Exception {
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            userDTO.setId(SecurityUtils.getUserPrincipal().getId());
            userService.updatePassword(userDTO);
            return "redirect:/trang-ca-nhan/chinh-sua?message=redirect_update";
        }
        return "redirect:/trang-ca-nhan/chinh-sua?message=redirect_error";
    }

    @RequestMapping(value = "/dang-ki", method = RequestMethod.POST)
    public String registerUser(UserDTO userDTO) {

        try {
            userService.save(userDTO);
            return "redirect:/ajax-register-success";
        } catch (Exception e) {
            return "redirect:/ajax-register-failure";
        }
    }

    private FilmCommand setValueForCommand(FilmCommand command, Integer page, String sort, String sortDsc, Integer limit) {
        if (page != null) {
            command.setPage(page);
        }
        if (sort != null) {
            command.setSortExpression(sort);
        }
        if (sortDsc != null) {
            command.setSortDirection(sortDsc);
        }

        if (limit != null) {
            command.setLimit(limit);
        }
        return command;
    }
}
