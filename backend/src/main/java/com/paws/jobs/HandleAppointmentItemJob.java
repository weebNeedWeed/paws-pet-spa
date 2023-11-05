package com.paws.jobs;

import com.paws.entities.*;
import com.paws.entities.common.enums.AppointmentItemStatus;
import com.paws.repositories.*;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HandleAppointmentItemJob implements Job {
    public static final String APPOINTMENT_ITEM_ID = "appointmentItemId";

    private final AppointmentItemRepository appointmentItemRepository;
    private final SpaServiceDetailRepository spaServiceDetailRepository;
    private final PetWeightRangeRepository petWeightRangeRepository;
    private final DetailedAppointmentItemRepository detailedAppointmentItemRepository;

    @Autowired
    public HandleAppointmentItemJob(AppointmentItemRepository appointmentItemRepository, SpaServiceDetailRepository spaServiceDetailRepository, PetWeightRangeRepository petWeightRangeRepository, DetailedAppointmentItemRepository detailedAppointmentItemRepository) {
        this.appointmentItemRepository = appointmentItemRepository;
        this.spaServiceDetailRepository = spaServiceDetailRepository;
        this.petWeightRangeRepository = petWeightRangeRepository;
        this.detailedAppointmentItemRepository = detailedAppointmentItemRepository;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        long itemId = dataMap.getLong(APPOINTMENT_ITEM_ID);

        AppointmentItem appointmentItem = appointmentItemRepository.findById(itemId);

        DetailedAppointmentItem detailedItem = detailedAppointmentItemRepository.findById(itemId);

        switch (detailedItem.getStatus()) {
            case MEASURING_WEIGHT -> {
                if(detailedItem.getPetWeight() != null) {
                    Double weight = detailedItem.getPetWeight();
                    double floorWeight = Math.floor(weight * 10) / (double) 10;

                    PetWeightRange range = petWeightRangeRepository
                            .getPetWeightRangeByWeight(BigDecimal.valueOf(floorWeight));

                    float time = 0;
                    for(SpaService service: appointmentItem.getSpaServices()) {
                        float mins = service.getDefaultEstimatedCompletionMinutes();
                        SpaServiceDetail detail = spaServiceDetailRepository
                                .findByServiceIdAndWeightRange(service.getId(), range.getId().getMinWeight(), range.getId().getMaxWeight());
                        if(detail != null) {
                            mins = detail.getEstimatedCompletionMinutes();
                        }

                        time += mins;
                    }

                    LocalDateTime estimated = detailedItem.getActualStartingTime().plusMinutes((int)time);
                    detailedItem.setEstimatedEndingTime(estimated);
                    detailedItem.setStatus(AppointmentItemStatus.IN_PROGRESS);

                    detailedAppointmentItemRepository.save(detailedItem);
                }
            }
        }

    }
}
