package com.paws.controllers;

import com.paws.exceptions.InvalidWeightRangeException;
import com.paws.exceptions.SpaServiceNameAlreadyExistsException;
import com.paws.exceptions.SpaServiceNotFoundException;
import com.paws.models.spaservices.EditSpaServiceRequest;
import com.paws.models.spaservices.ServiceDetailByWeightRange;
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
import java.time.Instant;
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

        return "services/index";
    }

    @GetMapping("/new")
    public String create(Model model) {
        CreateSpaServiceRequest request = new CreateSpaServiceRequest();
        model.addAttribute("createRequest", request);

        return "services/create";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("createRequest") CreateSpaServiceRequest request, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "services/create";
        }

        try {
            spaSvcService.addService(
                    request.getName(),
                    request.getDescription(),
                    request.getDefaultPrice(),
                    request.getDefaultEstimatedCompletionMinutes());
        } catch (SpaServiceNameAlreadyExistsException ex) {
            model.addAttribute("error", "Tên dịch vụ bị trùng.");
            model.addAttribute("createRequest", request);

            return "services/create";
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

        return "services/details";
    }

    @GetMapping("{serviceId}/edit")
    public String edit(@PathVariable("serviceId") long serviceId, Model model) throws SpaServiceNotFoundException {
        SpaSvcDto service = spaSvcService.getServiceById(serviceId);
        if(service == null) {
            return "redirect:/services";
        }

        EditSpaServiceRequest request = getEditSpaServiceRequest(service);
        model.addAttribute("editRequest", request);

        return "services/edit";
    }

    @PostMapping("{serviceId}/edit/general")
    public String editGeneral(@PathVariable("serviceId") long serviceId,
                              @Valid @ModelAttribute("editRequest") EditSpaServiceRequest request,
                              BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes) throws SpaServiceNotFoundException {
        if(bindingResult.hasErrors()) {
            SpaSvcDto service = spaSvcService.getServiceById(serviceId);
            request.setDetails(getListOfDetails(service));
            request.setId(service.getId());

            return "services/edit";
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

    private EditSpaServiceRequest getEditSpaServiceRequest(SpaSvcDto service) throws SpaServiceNotFoundException {
        EditSpaServiceRequest request = new EditSpaServiceRequest();
        request.setId(service.getId());
        request.setName(service.getName());
        request.setDescription(service.getDescription());
        request.setDefaultPrice(service.getDefaultPrice());
        request.setDefaultEstimatedCompletionMinutes(service.getDefaultEstimatedCompletionMinutes());

        request.setDetails(getListOfDetails(service));

        return request;
    }

    private List<ServiceDetailDto> getListOfDetails(SpaSvcDto service) throws SpaServiceNotFoundException {
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

        return addableListOfDetails;
    }
}
