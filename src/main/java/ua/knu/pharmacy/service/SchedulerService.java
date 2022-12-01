package ua.knu.pharmacy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.knu.pharmacy.entity.*;
import ua.knu.pharmacy.exception.NotFoundException;
import ua.knu.pharmacy.repository.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final DailyReportMedicineRepository dailyReportMedicineRepository;
    private final MedicineStockRepository medicineStockRepository;
    private  final LastUpdateRepository lastUpdateRepository;
    private final MedicineRepository medicineRepository;
    static final Logger LOGGER =
            Logger.getLogger(SchedulerService.class.getName());

    @Scheduled(cron = "0 0 0  * * ?")
    public void dailyUpdate(){
        update(LocalDate.now());
        LOGGER.info("data updated on " + LocalDate.now().toString());
    }

    @PostConstruct
    public void init(){
        update(LocalDate.now());
        LOGGER.info("data updated on " + LocalDate.now().toString());
    }
    @Transactional
    public void update(LocalDate date){

        LocalDate current = lastUpdateRepository.findAll().stream().map(LastUpdate::getDate).toList().get(0);

        while (current.isBefore(date)){

            LocalDate finalCurrent = current;
            List<MedicineStock> stock = medicineStockRepository.findAll().stream()
                    .filter(r-> Objects.equals(r.getDate(), finalCurrent))
                    .toList();
            for(MedicineStock s : stock){
                if(!Objects.equals(s.getSupply().getExpirationDate(), finalCurrent.plusDays(1))){
                    medicineStockRepository.save(MedicineStock.builder()
                                    .date(finalCurrent.plusDays(1))
                                    .supply(s.getSupply())
                                    .count(s.getCount())
                                    .medicine(s.getMedicine())
                            .build());
                } else {
                    dailyReportMedicineRepository.save(DailyReportMedicine.builder()
                                    .isSale(false)
                                    .date(finalCurrent.plusDays(1))
                                    .medicine(s.getMedicine())
                                    .count(s.getCount())
                                    .supply(s.getSupply().getSupply())
                                    .amount(BigDecimal.valueOf(s.getCount())
                                            .multiply(s.getSupply().getPrice()))
                            .build());
                }
            }
            current = current.plusDays(1);
        }

        LastUpdate last  = lastUpdateRepository.findById(1L)
                .orElseThrow( () -> new NotFoundException("update is loss"));
        last.setDate(date);
        lastUpdateRepository.save(last);

    }
}
