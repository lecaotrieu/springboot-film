package com.movie.web.api;

import com.movie.core.dto.EvaluateDTO;
import com.movie.core.service.IEvaluateService;
import com.movie.core.service.IFilmService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value = "EvaluateAPI")
public class EvaluateAPI {
    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private IFilmService filmService;

    @PostMapping("/api/evaluate")
    public Long saveEvaluate(@RequestBody EvaluateDTO evaluateDTO) {
        return evaluateService.save(evaluateDTO);
    }

    @PutMapping("/api/evaluate")
    public Long updateEvaluate(@RequestBody EvaluateDTO evaluateDTO) {
        return evaluateService.save(evaluateDTO);
    }

    @PutMapping("/api/evaluate/follow/{filmId}")
    public Integer updateEvaluateFollow(@RequestBody Integer follow, @PathVariable Long filmId) throws Exception {
        Long userId = SecurityUtils.getUserPrincipal().getId();
        if (userId == null) {
            throw new Exception();
        }
        return evaluateService.updateFollow(filmId, follow, userId);
    }

    @PutMapping("/api/evaluate/like/{filmId}")
    public Integer updateEvaluateLike(@RequestBody Integer like, @PathVariable Long filmId) throws Exception {
        Long userId = SecurityUtils.getUserPrincipal().getId();
        if (userId == null) {
            throw new Exception();
        }
        return evaluateService.updateLike(filmId, like, userId);
    }

    @PutMapping("/api/evaluate/scores/{filmId}")
    public String updateEvaluateScores(@RequestBody Integer scores, @PathVariable Long filmId) throws Exception {
        Long userId = SecurityUtils.getUserPrincipal().getId();
        if (userId == null) {
            throw new Exception();
        }
        Integer rs = evaluateService.updateScores(filmId, scores, userId);
        Double filmScores = filmService.updateScores(filmId);
        return "{\"scores\":" + rs + ", \"filmScores\":" + filmScores + "}";
    }
}
