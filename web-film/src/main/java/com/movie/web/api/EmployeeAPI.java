package com.movie.web.api;

import com.movie.core.dto.EmployeeDTO;
import com.movie.core.service.IEmployeeService;
import com.movie.core.utils.UploadUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController(value = "EmployeeAPI")
public class EmployeeAPI {

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping("/api/admin/employee")
    public EmployeeDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) throws Exception {
        return employeeService.save(employeeDTO);
    }

    @PutMapping("/api/admin/employee/status")
    public boolean updateEmployeeStatus(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployeeStatus(employeeDTO);
        return true;
    }


    @PutMapping("/api/admin/employee/password")
    public Long updateEmployeePassword(@RequestBody EmployeeDTO employeeDTO) throws Exception {
        employeeService.updatePassword(employeeDTO.getId(), employeeDTO.getPassword());
        return employeeDTO.getId();
    }

    @PutMapping("/api/admin/employee")
    public Long updateEmployee(@RequestBody EmployeeDTO employeeDTO) throws Exception {
        employeeDTO = employeeService.save(employeeDTO);
        return employeeDTO.getId();
    }



    @DeleteMapping("/api/admin/employee")
    public void deleteEmployee(@RequestBody Long[] ids) {
        employeeService.deleteById(ids);
    }
}
