package com.movie.web.interceptor;

import com.movie.core.constant.WebConstant;
import com.movie.core.dto.CategoryDTO;
import com.movie.core.dto.CountryDTO;
import com.movie.core.dto.FilmTypeDTO;
import com.movie.core.service.ICategoryService;
import com.movie.core.service.ICountryService;
import com.movie.core.service.IFilmTypeService;
import com.movie.web.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Component
public class WebHandelInterceptor implements HandlerInterceptor {
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICountryService countryService;

    @Autowired
    private IFilmTypeService filmTypeService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        List<CategoryDTO> categoryDTOS = categoryService.findAll();
        List<CountryDTO> countryDTOS = countryService.findAll();
        List<FilmTypeDTO> filmTypeDTOS = filmTypeService.findAll();
        List<Integer> years = new ArrayList<Integer>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            years.add((year));
            year--;
        }
        request.setAttribute(WebConstant.CATEGORY_LIST, categoryDTOS);
        request.setAttribute(WebConstant.COUNTRY_LIST, countryDTOS);
        request.setAttribute(WebConstant.FILM_TYPE_LIST, filmTypeDTOS);
        request.setAttribute(WebConstant.YEARS, years);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
