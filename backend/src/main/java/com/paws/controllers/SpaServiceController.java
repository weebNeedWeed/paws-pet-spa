package com.paws.controllers;

import com.paws.exceptions.InvalidWeightRangeException;
import com.paws.exceptions.SpaServiceNameAlreadyExistsException;
import com.paws.exceptions.SpaServiceNotFoundException;
import com.paws.models.spaservices.ServiceDetailByWeightRange;
import com.paws.models.spaservices.UpdateGeneralInformationRequest;
import com.paws.services.spasvcs.SpaSvcService;
import com.paws.services.spasvcs.payloads.ServiceDetailDto;
import com.paws.services.spasvcs.payloads.SpaSvcDto;
import com.paws.models.spaservices.CreateSpaServiceRequest;
import com.paws.services.weightranges.WeightRangeService;
import com.paws.services.weightranges.payloads.WeightRangeDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/services")
public class SpaServiceController {
    private final SpaSvcService spaSvcService;
    private final WeightRangeService weightRangeService;

    public SpaServiceController(SpaSvcService spaSvcService, WeightRangeService weightRangeService) {
        this.spaSvcService = spaSvcService;
        this.weightRangeService = weightRangeService;
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
    public String delete(@ModelAttribute("serviceId") long serviceId) {
        try {
            spaSvcService.deleteService(serviceId);
        } catch(SpaServiceNotFoundException ex) {
        }

        return "redirect:/services";
    }

    @GetMapping("{serviceId}/details")
    public String details(@PathVariable("serviceId") long serviceId, Model model) throws SpaServiceNotFoundException {
        SpaSvcDto service = spaSvcService.getServiceById(serviceId);
        if(service == null) {
            return "redirect:/services";
        }

        List<ServiceDetailDto> serviceDetails = spaSvcService.getAllDetails(serviceId);

        model.addAttribute("spaService", service);
        model.addAttribute("serviceDetails", serviceDetails);

        return "service/details";
    }

    @GetMapping("{serviceId}/edit")
    public String edit(@PathVariable("serviceId") long serviceId, Model model) throws SpaServiceNotFoundException {
        SpaSvcDto service = spaSvcService.getServiceById(serviceId);
        if(service == null) {
            return "redirect:/services";
        }

        UpdateGeneralInformationRequest generalRequest = new UpdateGeneralInformationRequest();
        generalRequest.setName(service.getName());
        generalRequest.setDescription(service.getDescription());
        generalRequest.setDefaultPrice(service.getDefaultPrice());
        generalRequest.setDefaultEstimatedCompletionMinutes(service.getDefaultEstimatedCompletionMinutes());

        model.addAttribute("generalRequest", generalRequest);
        model.addAttribute("spaService", service);

        List<ServiceDetailDto> serviceDetails = spaSvcService.getAllDetails(service.getId());
        List<WeightRangeDto> weightRanges = weightRangeService.getAllWeightRanges();

        List<ServiceDetailDto> addableListOfDetails = new ArrayList<>(serviceDetails);

        for(WeightRangeDto range : weightRanges) {
            boolean containsRange = serviceDetails.stream().anyMatch(x -> x.getMinWeight().equals(range.getMinWeight()) &&
                    x.getMaxWeight().equals(range.getMaxWeight()));
            if(containsRange) {
                continue;
            }

            ServiceDetailDto dto = new ServiceDetailDto();
            dto.setMinWeight(range.getMinWeight());
            dto.setMaxWeight(range.getMaxWeight());
            addableListOfDetails.add(dto);
        }

        model.addAttribute("serviceDetails", addableListOfDetails);

        return "service/edit";
    }

    @PostMapping("{serviceId}/edit/general")
    public String editGeneral(@PathVariable("serviceId") long serviceId,
                              @Valid @ModelAttribute("generalRequest") UpdateGeneralInformationRequest request,
                              BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            return "service/edit";
        }

        try {
            spaSvcService.updateService(serviceId,
                    request.getName(),
                    request.getDescription(),
                    request.getDefaultPrice(),
                    request.getDefaultEstimatedCompletionMinutes());
        } catch (SpaServiceNotFoundException ex) {
            return "redirect:/services";
        } catch (SpaServiceNameAlreadyExistsException ex) {
            redirectAttributes.addFlashAttribute("error", "Tên dịch vụ đã tồn tại.");
        }

        return "redirect:/services/" + serviceId + "/details";
    }

    @PostMapping("{serviceId}/edit/detail")
    public String editDetail(@PathVariable("serviceId") long serviceId,
                             @Valid ServiceDetailByWeightRange detail, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/services/" + serviceId + "/edit";
        }

        try {
            spaSvcService.updateDetailByWeightRange(serviceId,
                    detail.getMinWeight(),
                    detail.getMaxWeight(),
                    detail.getPrice(),
                    detail.getEstimatedCompletionMinutes());
        } catch (SpaServiceNotFoundException | InvalidWeightRangeException ex) {
            return "redirect:/services";
        }

        return "redirect:/services/" + serviceId + "/edit";
    }

    @PostMapping("{serviceId}/delete/detail")
    public String deleteDetail(@PathVariable("serviceId") long serviceId, BigDecimal minWeight, BigDecimal maxWeight) {
        try {
            spaSvcService.deleteDetail(serviceId, minWeight, maxWeight);
        } catch (SpaServiceNotFoundException ex) {
            return "redirect:/services";
        }

        return "redirect:/services/" + serviceId + "/edit";
    }
}
