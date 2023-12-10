package com.paws.services.appointments;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.entities.common.enums.AppointmentLocation;
import com.paws.entities.common.enums.AppointmentStatus;
import com.paws.entities.common.enums.BillStatus;
import com.paws.exceptions.*;
import com.paws.jobs.CancelLateAppointmentJob;
import com.paws.payloads.common.PagedResult;
import com.paws.payloads.request.MakeAppointmentItemRequest;
import com.paws.payloads.response.*;
import com.paws.repositories.*;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@Transactional(rollbackFor = {Throwable.class , Exception.class})
public class AppointmentServiceImpl implements AppointmentService{
    private final AppointmentRepository appointmentRepository;
    private final AppointmentItemRepository appointmentItemRepository;
    private final DetailedAppointmentItemRepository detailedAppointmentItemRepository;
    private final PetWeightRangeRepository petWeightRangeRepository;
    private final SpaServiceDetailRepository spaServiceDetailRepository;
    private final SpaServiceRepository spaServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final PetTypeRepository petTypeRepository;
    private final BillRepository billRepository;
    private final Scheduler scheduler;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentItemRepository appointmentItemRepository, DetailedAppointmentItemRepository detailedAppointmentItemRepository, PetWeightRangeRepository petWeightRangeRepository, SpaServiceDetailRepository spaServiceDetailRepository, SpaServiceRepository spaServiceRepository, EmployeeRepository employeeRepository, CustomerRepository customerRepository, PetTypeRepository petTypeRepository, BillRepository billRepository, Scheduler scheduler) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentItemRepository = appointmentItemRepository;
        this.detailedAppointmentItemRepository = detailedAppointmentItemRepository;
        this.petWeightRangeRepository = petWeightRangeRepository;
        this.spaServiceDetailRepository = spaServiceDetailRepository;
        this.spaServiceRepository = spaServiceRepository;
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
        this.petTypeRepository = petTypeRepository;
        this.billRepository = billRepository;
        this.scheduler = scheduler;
    }

    @Override
    public void approve(long appointmentId) throws AppointmentNotFoundException, InvalidAppointmentStatusException, NoEmployeeAssignedException, CannotScheduleAppointmentException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.PENDING, appointment.getStatus());
        }

        for(AppointmentItem item: appointment.getAppointmentItems()) {
            if(item.getEmployee() == null) {
                throw new NoEmployeeAssignedException();
            }
        }

        JobDetail job = JobBuilder.newJob(CancelLateAppointmentJob.class)
                .usingJobData(CancelLateAppointmentJob.APPOINTMENT_ID, appointmentId).build();

        int cancelAfter = 1;
        Date timeToStart = Date.from(appointment.getAppointmentTime()
                .plusMinutes(cancelAfter).atZone(ZoneId.systemDefault()).toInstant());

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .startAt(timeToStart)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException ex) {
            throw new CannotScheduleAppointmentException();
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void assignEmployee(long appointmentId, long appointmentItemId, long employeeId) throws AppointmentNotFoundException, AppointmentItemNotFoundException, EmployeeNotFoundException, InvalidAppointmentStatusException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        if(appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.PENDING, appointment.getStatus());
        }

        AppointmentItem item = appointment.getAppointmentItems().stream().filter(
                x -> x.getId().equals(appointmentItemId)
        ).findFirst().orElse(null);
        if(item == null) {
            throw new AppointmentItemNotFoundException();
        }

        if(employeeId == -1) {
            item.setEmployee(null);
        } else {
            Employee emp = employeeRepository.findById(employeeId);
            if(emp == null) {
                throw new EmployeeNotFoundException();
            }

            item.setEmployee(emp);
        }

        appointmentRepository.save(appointment);
    }

    @Override
    public void initDetailed(long appointmentItemId) throws AppointmentItemNotFoundException, InvalidAppointmentStatusException {
        AppointmentItem item = appointmentItemRepository.findById(appointmentItemId);
        if(item == null) {
            throw new AppointmentItemNotFoundException();
        }

        Appointment appointment = item.getAppointment();
        if(appointment.getStatus() != AppointmentStatus.SCHEDULED
                && appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new InvalidAppointmentStatusException(AppointmentStatus.SCHEDULED, appointment.getStatus());
        }

        if(appointment.getStatus() == AppointmentStatus.SCHEDULED) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
            appointmentRepository.save(appointment);
        }

        if(detailedAppointmentItemRepository.findById(item.getId()).isPresent()) {
            return;
        }

        DetailedAppointmentItem detailedItem = new DetailedAppointmentItem();
        detailedItem.setAppointmentItem(item);
        detailedItem.setStatus(AppointmentItemStatus.MEASURING_WEIGHT);

        detailedAppointmentItemRepository.save(detailedItem);
    }

    @Override
    public PagedResult<AppointmentDto> getAll(Pageable pageable) {
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);

        PagedResult<AppointmentDto> result = new PagedResult<>();
        result.setPageIndex(appointments.getNumber());
        result.setPageSize(appointments.getSize());
        result.setTotalPages(appointments.getTotalPages());

        List<AppointmentDto> appointmentDtos = appointments.stream().map(
                x -> {
                    AppointmentDto appointmentDto = new AppointmentDto();
                    appointmentDto.setAppointmentTime(x.getAppointmentTime());
                    appointmentDto.setId(x.getId());
                    appointmentDto.setLocation(x.getLocation());
                    appointmentDto.setCreatedAt(x.getCreatedAt());
                    appointmentDto.setUpdatedAt(x.getUpdatedAt());
                    appointmentDto.setStatus(x.getStatus());
                    appointmentDto.setCustomerFullName(x.getCustomer().getFullName());

                    return appointmentDto;
                }).toList();

        result.setData(appointmentDtos);

        return result;
    }

    @Override
    public AppointmentDto getDetails(long appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        AppointmentDto appointmentDto = new AppointmentDto();
        Customer customer = appointment.getCustomer();

        appointmentDto.setStatus(appointment.getStatus());
        appointmentDto.setNote(appointment.getNote());
        appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
        appointmentDto.setId(appointment.getId());
        appointmentDto.setCreatedAt(appointment.getCreatedAt());
        appointmentDto.setUpdatedAt(appointment.getUpdatedAt());
        appointmentDto.setLocation(appointment.getLocation());
        appointmentDto.setCustomerFullName(customer.getFullName());

        appointmentDto.setAppointmentItems(new ArrayList<>());

        List<AppointmentItem> appointmentItems = appointment.getAppointmentItems();
        for(AppointmentItem item : appointmentItems) {
            AppointmentItemDto itemDto = new AppointmentItemDto();
            itemDto.setId(item.getId());
            itemDto.setPetType(item.getPetType().getName());
            itemDto.setPetName(item.getPetName());

            Employee emp = item.getEmployee();
            if(emp != null) {
                EmployeeDto empDto = new EmployeeDto();
                empDto.setId(emp.getId());
                empDto.setUsername(emp.getUsername());
                empDto.setFullName(emp.getFullName());

                itemDto.setEmployee(empDto);
            }

            Set<SpaService> spaServices = item.getSpaServices();
            List<SpaSvcDto> spaSvcDtos = spaServices.stream().map(
                    x -> {
                        SpaSvcDto dto = new SpaSvcDto();
                        dto.setId(x.getId());
                        dto.setName(x.getName());
                        dto.setDescription(x.getDescription());

                        return dto;
                    }).sorted(Comparator.comparing(SpaSvcDto::getId)).toList();

            itemDto.setSpaServices(spaSvcDtos);

            DetailedAppointmentItem detailedItem = detailedAppointmentItemRepository.findById(item.getId()).orElse(null);
            if(detailedItem != null) {
                itemDto.setPetWeight(detailedItem.getPetWeight());
                itemDto.setStatus(detailedItem.getStatus());
                itemDto.setActualStartingTime(detailedItem.getActualStartingTime());
                itemDto.setCurrentServiceEndingTime(detailedItem.getCurrentServiceEndingTime());
                itemDto.setPetWeight(detailedItem.getPetWeight());
                itemDto.setDoneServiceIndex(detailedItem.getDoneServiceIndex());
                itemDto.setEndingTime(detailedItem.getEndingTime());
            }

            appointmentDto.getAppointmentItems().add(itemDto);
        }

        return appointmentDto;
    }

    @Override
    public void measurePetWeight(long appointmentItemId, double weight) {
        DetailedAppointmentItem detailedAppointmentItem = detailedAppointmentItemRepository.findById(appointmentItemId);
        AppointmentItem appointmentItem = appointmentItemRepository.findById(appointmentItemId);

        detailedAppointmentItem.setPetWeight(weight);
        detailedAppointmentItem.setActualStartingTime(LocalDateTime.now());
        detailedAppointmentItem.setStatus(AppointmentItemStatus.IN_PROGRESS);

        LocalDateTime now = LocalDateTime.now();
        long sum = 0;
        for(SpaService service: appointmentItem.getSpaServices()) {
            LocalDateTime endTime = calculateEndTimeForService(weight, service.getId());
            long seconds = Duration.between(now, endTime).toSeconds();
            sum += seconds;
        }

        LocalDateTime appointmentEndTime = now.plusSeconds(sum);

        detailedAppointmentItem.setEndingTime(appointmentEndTime);
        detailedAppointmentItemRepository.save(detailedAppointmentItem);
    }

    @Override
    public LocalDateTime calculateEndTimeForService(double weight, long serviceId) {
        double floorWeight = Math.floor(weight * 10) / (double) 10;
        PetWeightRange range = petWeightRangeRepository
                .getPetWeightRangeByWeight(BigDecimal.valueOf(floorWeight));

        LocalDateTime now = LocalDateTime.now();

        SpaService service = spaServiceRepository.getSpaServiceById(serviceId);
        float mins = service.getDefaultEstimatedCompletionMinutes();

        SpaServiceDetail serviceDetail = spaServiceDetailRepository.findByServiceIdAndWeightRange(serviceId, range.getId().getMinWeight(), range.getId().getMaxWeight());
        if(serviceDetail != null)
            mins = serviceDetail.getEstimatedCompletionMinutes();// estimated time plus 15 complement minutes

        return now.plusMinutes((int)mins);
    }

    @Override
    @Transactional
    public void makeAppointment(String username,
                                AppointmentLocation location,
                                LocalDateTime time,
                                String note,
                                List<MakeAppointmentItemRequest> makeAppointmentItemRequests) throws CustomerNotFoundException, InvalidAppointmentTimeException, PetTypeNotFoundException, SpaServiceNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if(customer == null) {
            throw new CustomerNotFoundException();
        }

        // Validate appointment time is valid
        LocalDateTime lowBoundary = LocalDateTime.now();
        LocalDateTime highBoundary = lowBoundary.plusDays(14); // 2 weeks

        if(time.isBefore(lowBoundary) || time.isAfter(highBoundary)) {
            throw new InvalidAppointmentTimeException();
        }

        Appointment appointment = new Appointment();

        for(MakeAppointmentItemRequest request : makeAppointmentItemRequests) {
            PetType petType = petTypeRepository.findById((long)request.getPetTypeId());
            if(petType == null) {
                throw new PetTypeNotFoundException();
            }

            AppointmentItem appointmentItem = new AppointmentItem();
            appointmentItem.setPetName(request.getPetName());
            appointmentItem.setPetType(petType);

            for(Long serviceId : request.getServiceIds()) {
                SpaService spaService = spaServiceRepository.getSpaServiceById(serviceId);
                if(spaService == null) {
                    throw new SpaServiceNotFoundException();
                }

                appointmentItem.getSpaServices().add(spaService);
            }

            appointment.addItem(appointmentItem);
        }

        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNote(note);
        appointment.setAppointmentTime(time);
        appointment.setLocation(location);

        customer.addAppointment(appointment);

        customerRepository.save(customer);
    }

    @Override
    public void setDoneItem(long appointmentItemId) throws AppointmentItemNotFoundException {
        DetailedAppointmentItem item = detailedAppointmentItemRepository.findById(appointmentItemId);
        if(item == null) {
            throw new AppointmentItemNotFoundException();
        }

        item.setStatus(AppointmentItemStatus.DONE);

        detailedAppointmentItemRepository.save(item);
    }

    @Override
    @Transactional
    public void generateBill(long appointmentId, String username) throws AppointmentNotFoundException, EmployeeNotFoundException {
        Appointment appointment = appointmentRepository.findById(appointmentId);
        if(appointment == null) {
            throw new AppointmentNotFoundException();
        }

        Employee employee = employeeRepository.getEmployeeByUsername(username);
        if(employee == null) {
            throw new EmployeeNotFoundException();
        }

        Bill bill = new Bill();
        bill.setEmployee(employee);
        bill.setAppointment(appointment);
        bill.setStatus(BillStatus.UN_PAID);

        BigDecimal sum = BigDecimal.valueOf(0);

        List<AppointmentItem> items = appointment.getAppointmentItems();
        for(AppointmentItem item : items) {
            DetailedAppointmentItem detailedItem = detailedAppointmentItemRepository.findById((long)item.getId());
            Set<SpaService> spaServices = item.getSpaServices();
            for(SpaService service: spaServices) {
                BillItem billItem = new BillItem();
                BigDecimal price = calculatePriceByWeight(service, detailedItem.getPetWeight());

                sum = sum.add(price);

                billItem.setPrice(price);
                billItem.setPetWeight(detailedItem.getPetWeight());
                billItem.setPetType(item.getPetType());
                billItem.setSpaService(service);
                billItem.setPetName(item.getPetName());

                bill.addItem(billItem);
            }
        }

        bill.setTotalAmount(sum);

        billRepository.save(bill);
    }

    @Override
    public BillDto getBill(long appointmentId) throws BillNotFoundException {
        Bill bill = billRepository.findById(appointmentId)
                .orElseThrow(BillNotFoundException::new);

        BillDto billDto = new BillDto();
        billDto.setTotalAmount(bill.getTotalAmount());
        billDto.setStatus(bill.getStatus());
        billDto.setId(bill.getId());
        billDto.setCreatedAt(bill.getCreatedAt());
        billDto.setUpdatedAt(bill.getUpdatedAt());

        Employee emp = bill.getEmployee();
        billDto.setEmployee(new EmployeeDto());
        billDto.getEmployee().setUsername(emp.getUsername());
        billDto.getEmployee().setFullName(emp.getFullName());
        billDto.getEmployee().setId(emp.getId());

        Customer customer = bill.getAppointment().getCustomer();
        billDto.setCustomer(new CustomerDto());
        billDto.getCustomer().setUsername(customer.getUsername());
        billDto.getCustomer().setFullName(customer.getFullName());
        billDto.getCustomer().setAddress(customer.getAddress());

        List<BillItemDto> items = bill.getBillItems().stream().map(x -> {
            BillItemDto dto = new BillItemDto();
            dto.setId(x.getId());
            dto.setPrice(x.getPrice());
            dto.setPetWeight(x.getPetWeight());
            dto.setPetName(x.getPetName());

            PetType petType = x.getPetType();
            dto.setPetType(new PetTypeDto());
            dto.getPetType().setId(petType.getId());
            dto.getPetType().setName(petType.getName());

            SpaService svc = x.getSpaService();
            dto.setSpaService(new SpaSvcDto());
            dto.getSpaService().setId(svc.getId());
            dto.getSpaService().setName(svc.getName());

            return dto;
        }).toList();

        billDto.setBillItems(items);

        return billDto;
    }

    @Override
    @Transactional
    public void setPaidBill(long appointmentId) throws BillNotFoundException, AppointmentItemNotFoundException {
        Bill bill = billRepository.findById(appointmentId).orElseThrow(BillNotFoundException::new);
        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);

        Appointment appointment = bill.getAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);

        List<AppointmentItem> items = appointment.getAppointmentItems();
        for(AppointmentItem item : items) {
            DetailedAppointmentItem detailedAppointmentItem = detailedAppointmentItemRepository.findById(item.getId())
                    .orElseThrow(AppointmentItemNotFoundException::new);

            if(detailedAppointmentItem.getStatus() != AppointmentItemStatus.DONE) {
                detailedAppointmentItem.setStatus(AppointmentItemStatus.DONE);
                detailedAppointmentItemRepository.save(detailedAppointmentItem);
            }
        }

        appointmentRepository.save(appointment);
    }

    private BigDecimal calculatePriceByWeight(SpaService service, double weight) {
        BigDecimal price = service.getDefaultPrice();

        double floorWeight = Math.floor(weight * 10) / (double) 10;
        PetWeightRange range = petWeightRangeRepository
                .getPetWeightRangeByWeight(BigDecimal.valueOf(floorWeight));
        if(range == null) {
            return price;
        }

        SpaServiceDetail svcDetail = spaServiceDetailRepository.findByServiceIdAndWeightRange(service.getId(),
                range.getId().getMinWeight(), range.getId().getMaxWeight());

        if(svcDetail != null) {
            price = svcDetail.getPrice();
        }

        return price;
    }
}
