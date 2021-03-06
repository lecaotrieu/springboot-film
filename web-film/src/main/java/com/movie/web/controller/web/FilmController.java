package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.*;
import com.movie.core.service.*;
import com.movie.core.utils.WebCommonUtil;
import com.movie.web.command.FilmCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller(value = "FilmControllerOfWeb")
public class FilmController {
    @Autowired
    private IFilmService filmService;
    @Autowired
    private IEpisodeService episodeService;
    @Autowired
    private IActorService actorService;

    @RequestMapping(value = "/film/{filmCode}-{filmId}/{episode}", method = RequestMethod.GET)
    public String showFilmAndEpisode(Model model, @PathVariable("filmId") Long filmId, @PathVariable("filmCode") String filmCode, @PathVariable("episode") Integer epCode) {
        FilmDTO filmDTO = filmService.findOne(filmId, filmCode, CoreConstant.ACTIVE_STATUS);
        filmDTO.setEpisodes(episodeService.findAllByFilmId(filmId));
        EpisodeDTO episodeDTO = episodeService.findOneByFilmAndCode(filmCode, epCode);
        EvaluateDTO evaluateDTO = new EvaluateDTO();
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            evaluateDTO = evaluateService.findOneByUserAndFilm(filmId, SecurityUtils.getUserPrincipal().getId());
        }
        model.addAttribute("evaluate", evaluateDTO);
        List<FilmDTO> relatedFilms = filmService.findByProperties("", filmDTO.getFilmType().getCode(), "", "", "", 1, 8, "modifiedDate", CoreConstant.SORT_ASC);
        model.addAttribute("relatedFilms", relatedFilms);
        model.addAttribute("episode", episodeDTO);
        List<CommentDTO> commentDTOS = commentService.findByProperties(filmId, null, "", "", 1, 5, "createdDate", "0");
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentDTO commentDTO : commentDTOS) {
                CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentDTO.getId());
                if (commentLikeDTO != null) {
                    commentDTO.setLike(commentLikeDTO.getStatus());
                }
            }
        }
        filmDTO.setComments(commentDTOS);
        filmDTO.setTotalComment(commentService.totalComment(filmId));
        model.addAttribute("film", filmDTO);
        return "views/web/moviedetail";
    }


    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = {"/phim", "/phim/tim-kiem", "/phim/nam-{year}", "/phim/quoc-gia/{country}", "/phim/quoc-gia/{country}/{year}", "/phim/the-loai/{category}",
            "/phim/the-loai/{category}/{country}", "/phim/the-loai/{category}/{country}/{year}", "/phim/the-loai/{category}/nam-{year}",
            "/phim/{filmType}", "/phim/{filmType}/nam-{year}", "/phim/{filmType}/nuoc-{country}", "/phim/{filmType}/nuoc-{country}/{year}", "/phim/{filmType}/{category}",
            "/phim/{filmType}/{category}/nam-{year}", "/phim/{filmType}/{category}/{country}", "/phim/{filmType}/{category}/{country}/{year}"}, method = RequestMethod.GET)
    public String showListFilm(@PathVariable(value = "year", required = false) String year,
                               @PathVariable(value = "country", required = false) String country,
                               @PathVariable(value = "category", required = false) String category,
                               @PathVariable(value = "filmType", required = false) String filmType,
                               @RequestParam(value = "sort", required = false) String sortExpression,
                               @RequestParam(value = "d", required = false) String sortDsc,
                               @RequestParam(value = "v", required = false) Integer v,
                               @RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "limit", required = false) Integer limit,
                               @RequestParam(value = "page", required = false) Integer page, Model model) {
        FilmCommand command = new FilmCommand();
        command = setValueForCommand(command, search, page, sortExpression, filmType, category, country, year);
        if (limit != null) command.setLimit(limit);
        else command.setLimit(12);
        List<FilmDTO> filmDTOS = filmService.findByProperties(command.getSearch(), command.getFilmType(), command.getCategory(), command.getCountry(), command.getYear(), command.getPage(), command.getLimit(), sortExpression, sortDsc);
        command.setListResult(filmDTOS);
        command.setTotalItems(filmService.getTotalItem(command.getSearch(), command.getFilmType(), command.getCategory(), command.getCountry(), command.getYear()));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        if (v != null && v == 2) return "views/web/moviegrid";
        return "views/web/movielist";
    }

    private FilmCommand setValueForCommand(FilmCommand command, String search, Integer page, String sortExpression, String filmType, String category, String country, String year) {
        if (page != null) {
            command.setPage(page);
        }
        if (sortExpression != null) {
            command.setSortExpression(sortExpression);
        }
        if (filmType != null) {
            command.setFilmType(filmType);
        }
        if (category != null) {
            command.setCategory(category);
        }
        if (country != null) {
            command.setCountry(country);
        }
        if (year != null) {
            command.setYear(year);
        }
        if (search != null) {
            command.setSearch(search);
        }
        return command;
    }


    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentLikeService commentLikeService;

    @RequestMapping(value = "/film/{filmCode}-{filmId}", method = RequestMethod.GET)
    public String showFilmDetail(@PathVariable("filmId") Long filmId, @PathVariable("filmCode") String filmCode, Model model) {
        FilmDTO filmDTO = filmService.findOne(filmId, filmCode, CoreConstant.ACTIVE_STATUS);
        EvaluateDTO evaluateDTO = new EvaluateDTO();
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            evaluateDTO = evaluateService.findOneByUserAndFilm(filmId, SecurityUtils.getUserPrincipal().getId());
        }
        model.addAttribute("evaluate", evaluateDTO);
        List<CommentDTO> commentDTOS = commentService.findByProperties(filmId, null, "", "", 1, 5, "createdDate", "0");
        if (SecurityUtils.getUserAuthorities().contains(WebConstant.ROLE_USER)) {
            for (CommentDTO commentDTO : commentDTOS) {
                CommentLikeDTO commentLikeDTO = commentLikeService.findByUserAndComment(SecurityUtils.getUserPrincipal().getId(), commentDTO.getId());
                if (commentLikeDTO != null) {
                    commentDTO.setLike(commentLikeDTO.getStatus());
                }
            }
        }
        filmDTO.setComments(commentDTOS);
        filmDTO.setTotalComment(commentService.totalComment(filmId));
        model.addAttribute("film", filmDTO);
        return "views/web/MovieInformation";
    }


}
