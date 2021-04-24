package com.movie.web.controller.admin;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.EpisodeDTO;
import com.movie.core.dto.FilmDTO;
import com.movie.core.service.IEpisodeService;
import com.movie.core.service.IFilmService;
import com.movie.core.utils.FormUtil;
import com.movie.core.utils.RequestUtil;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.EpisodeCommand;
import com.movie.web.command.FilmCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller(value = "EpisodeControllerOfAdmin")
public class EpisodeController {

    @Autowired
    private IFilmService filmService;
    @Autowired
    private IEpisodeService episodeService;

    ResourceBundle bundle = ResourceBundle.getBundle("i18n/message_vn");

    @RequestMapping(value = {"/admin/film/{filmCode}-{filmId}"}, method = RequestMethod.GET)
    public String showEpisodeEdit(@PathVariable("filmCode") String filmCode, @PathVariable("filmId") Long filmId, @RequestParam(value = "message", required = false) String message, Model model) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        boolean check = buildCheck(filmId);
        if (check == true) {
            FilmDTO filmDTO = buildFilmToShow(filmId);
            model.addAttribute("film", filmDTO);
            EpisodeDTO episodeDTO = new EpisodeDTO();
            model.addAttribute(WebConstant.FORM_ITEM, episodeDTO);
//            buildFilmToShow(filmId);
            if (!filmCode.equals(filmDTO.getCode())) {
                return "redirect:/admin/film/" + filmDTO.getCode() + "-" + filmId;
            }
        } else {
            return "redirect:/admin/film/list";
        }
        return "views/admin/episode/edit";
    }


    private FilmDTO buildFilmToShow(Long filmId) {
        FilmDTO filmDTO = filmService.findOneById(filmId);
        List<EpisodeDTO> episodeDTOS;
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            episodeDTOS = episodeService.findAllByFilmId(filmDTO.getId());
        } else {
            episodeDTOS = episodeService.findAllByFilmId(filmDTO.getId(), CoreConstant.ACTIVE_STATUS);
        }
        filmDTO.setEpisodes(episodeDTOS);
        return filmDTO;
    }


    @RequestMapping(value = {"/admin/film/{filmCode}-{filmId}/tap-{episodeCode}-{id}"}, method = RequestMethod.GET)
    public String showEpisodeEdit(@PathVariable("filmCode") String filmCode, @PathVariable("filmId") Long filmId,
                                  @PathVariable("episodeCode") Integer episodeCode, Model model,
                                  @PathVariable("id") Long id, @RequestParam(value = "message", required = false) String message) {
        boolean check = buildCheck(filmId);
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        if (check == true) {
            FilmDTO filmDTO = buildFilmToShow(filmId);
            model.addAttribute("film", filmDTO);
            EpisodeDTO episodeDTO;
            if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
                episodeDTO = episodeService.findOneById(id);
            } else {
                episodeDTO = episodeService.findOneById(id, CoreConstant.ACTIVE_STATUS);
            }
            if (episodeDTO != null) {
                model.addAttribute(WebConstant.FORM_ITEM, episodeDTO);
                if (!filmCode.equals(filmDTO.getCode()) || !episodeCode.equals(episodeDTO.getEpisodeCode())) {
                    return "redirect:/admin/film/" + filmDTO.getCode() + "-" + filmId + "/tap-" + episodeDTO.getEpisodeCode() + "-" + id;
                }
            } else {
                return "redirect:/admin/film/list";
            }
        } else {
            return "redirect:/admin/film/list";
        }
        return "views/admin/episode/edit";
    }

    @RequestMapping(value = {"/ajax/admin/film/list", "/admin/film/episode/list"}, method = RequestMethod.GET)
    public String showEpisodeList(Model model, @RequestParam(value = "message", required = false) String message, HttpServletRequest request) {
        if (message != null) {
            WebCommonUtil.addRedirectMessage(model, getMapMessage(), message);
        }
        EpisodeCommand command = FormUtil.populate(EpisodeCommand.class, request);
        RequestUtil.initSearchBeanManual(command);
        boolean check = buildCheck(command.getFilmId());
        List<EpisodeDTO> episodeDTOS = new ArrayList<>();
        if (check == true) {
            boolean status = false;
            if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
                status = false;
            } else if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_POSTER)) {
                status = true;
            }
            episodeDTOS = episodeService.findAllByProperties(command.getFilmId(), command.getSearch(), status, command.getPage(),
                    command.getLimit(), command.getSortExpression(), command.getSortDirection());
            command.setListResult(episodeDTOS);
            if (command.getFilmId() != null) {
                FilmDTO filmDTO = filmService.findOneById(command.getFilmId());
                command.setFilmDTO(filmDTO);
            }
            command.setTotalItems(episodeService.countAllByProperties(command.getFilmId(), command.getSearch(), status));
            command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
            model.addAttribute(WebConstant.LIST_ITEM, command);
        }
        return "views/admin/episode/list";
    }

    private boolean buildCheck(Long id) {
        boolean check = false;
        if (SecurityUtils.getEmployeeAuthorities().contains(WebConstant.ROLE_ADMIN)) {
            check = true;
        } else {
            check = filmService.checkPosterFilm(id, SecurityUtils.getPrincipal().getUsername());
        }
        return check;
    }

    private Map<String, String> getMapMessage() {
        Map<String, String> mapValue = new HashMap<String, String>();
        mapValue.put(WebConstant.REDIRECT_ERROR, bundle.getString("label.message.error"));
        mapValue.put(WebConstant.REDIRECT_INSERT, bundle.getString("label.episode.message.add.success"));
        mapValue.put(WebConstant.REDIRECT_DELETE, bundle.getString("label.episode.message.delete.success"));
        mapValue.put(WebConstant.REDIRECT_UPDATE, bundle.getString("label.episode.message.update.success"));
        return mapValue;
    }
}
