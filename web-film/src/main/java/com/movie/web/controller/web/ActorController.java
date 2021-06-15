package com.movie.web.controller.web;

import com.movie.core.constant.CoreConstant;
import com.movie.core.constant.WebConstant;
import com.movie.core.dto.*;
import com.movie.core.service.IActorService;
import com.movie.core.service.IFilmService;
import com.movie.web.command.ActorCommand;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller(value = "ActorController")
public class ActorController {

    @Autowired
    private IFilmService filmService;
    @Autowired
    private IActorService actorService;

    @RequestMapping(value = "/danh-sach-dien-vien", method = RequestMethod.GET)
    public String celebrity(Model model, @RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "limit", required = false) Integer limit,
                            @RequestParam(value = "sort", required = false) String sort,
                            @RequestParam(value = "sortDsc", required = false) String sortDsc,
                            @RequestParam(value = "search", required = false) String search) {
        ActorCommand command = new ActorCommand();
        command = setValueForCommand(command, search, page, sort, sortDsc, limit);
        List<ActorDTO> actorDTOS = actorService.findByProperties(command.getSearch(), command.getPage(), command.getLimit(), command.getSortExpression(), command.getSortDirection());
        command.setListResult(actorDTOS);
        command.setTotalItems(actorService.getTotalItem(command.getSearch()));
        command.setTotalPage((int) Math.ceil((double) command.getTotalItems() / command.getLimit()));
        model.addAttribute(WebConstant.LIST_ITEM, command);
        return "views/web/celebrity";
    }

    private ActorCommand setValueForCommand(ActorCommand command, String search, Integer page, String sort, String sortDsc, Integer limit) {
        if (page != null) {
            command.setPage(page);
        }
        if (sort != null) {
            command.setSortExpression(sort);
        }
        if (sortDsc != null) {
            command.setSortDirection(sortDsc);
        }
        if (search != null) {
            command.setSearch(search);
        }
        if (limit != null) {
            command.setLimit(limit);
        } else {
            command.setLimit(12);
        }
        return  command;
    }


    @RequestMapping(value = "/thong-tin-dien-vien/{actorCode}-{actorId}", method = RequestMethod.GET)
    public String showActorDetail(@PathVariable("actorId") Long actorId, Model model) {
        ActorDTO actorDTO=actorService.findOneById(actorId);
                actorDTO.setFilms(filmService.findByProperties(actorId));
        model.addAttribute("actor", actorDTO);
        return "views/web/celebrityInfo";
    }
}
