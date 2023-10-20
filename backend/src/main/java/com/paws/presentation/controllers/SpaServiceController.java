package com.paws.presentation.controllers;

import com.paws.application.common.errors.SpaServiceNameAlreadyExistsException;
import com.paws.application.common.errors.SpaServiceNotFoundException;
import com.paws.application.services.spasvcs.SpaSvcService;
import com.paws.application.services.spasvcs.common.SpaSvcDto;
import com.paws.presentation.controllers.contracts.spaservices.CreateSpaServiceRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/services")
public class SpaServiceController {
    private final SpaSvcService spaSvcService;

    public SpaServiceController(SpaSvcService spaSvcService) {
        this.spaSvcService = spaSvcService;
    }

    @GetMapping("")
    public String index(Model model) {
        List<SpaSvcDto> spaServices = spaSvcService.getAllServices();
        model.addAttribute("spaServices", spaServices);

        return "service/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        CreateSpaServiceRequest request = new CreateSpaServiceRequest();
        model.addAttribute("createRequest", request);

        return "service/create";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("createRequest") CreateSpaServiceRequest request, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "service/create";
        }

        try {
            SpaSvcDto result = spaSvcService.addService(
                    request.getName(),
                    request.getDescription(),
                    request.getDefaultPrice(),
                    request.getDefaultEstimatedCompletionMinutes());
        } catch (SpaServiceNameAlreadyExistsException ex) {
            model.addAttribute("error", "Tên dịch vụ bị trùng.");
            model.addAttribute("createRequest", request);

            return "service/create";
        }

        return "redirect:/services";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("serviceId") long serviceId, RedirectAttributes redirectAttributes) {
        try {
            spaSvcService.deleteService(serviceId);
        } catch(SpaServiceNotFoundException ex) {

        }

        return "redirect:/services";
    }
}
