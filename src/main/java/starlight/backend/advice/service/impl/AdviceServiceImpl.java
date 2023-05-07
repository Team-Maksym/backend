package starlight.backend.advice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import starlight.backend.advice.repository.DelayDeleteRepository;
import starlight.backend.exception.user.sponsor.SponsorNotFoundException;
import starlight.backend.sponsor.SponsorRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AdviceServiceImpl {
    private DelayDeleteRepository delayDeleteRepository;
    private SponsorRepository sponsorRepository;

    @Scheduled(cron = "**0 0 * * ***") // 24 hours
    public void deleteAccounts() {
        var accounts = delayDeleteRepository.findAll();

        for (var account : accounts) {
            if (account.getDeleteDate().isAfter(Instant.now())) {
                //Если дата удаления аккаунта больше текущего момента, то пропускаем
                continue;
            }
            //Тут можно имплементировать распределение логики на удаления аккаунтов с разными ролями в системе
            var accountEntityID = account.getEntityID();
            if (sponsorRepository.findBySponsorId(accountEntityID).isEmpty()){
                //Если спонсора уже нету в системе, то удаляем из DelayDeleteRepository
                delayDeleteRepository.deleteById(accountEntityID);
                continue;
            }

            log.info("Deleting user with id: {}", accountEntityID);
            //Вытягиваем спонсора
            var sponsor = sponsorRepository.findBySponsorId(accountEntityID)
                    .orElseThrow(() -> new SponsorNotFoundException(accountEntityID));
            //Чистим связи с другими таблицами.
            //Чистим роли
            sponsor.getAuthorities().clear();
            if (!sponsor.getKudos().isEmpty()) {
                //Если есть кудосы, то очищяем владельцев каждого из них
                for (var kudos : sponsor.getKudos()) {
                    kudos.setOwner(null);
                }
            }
            //Удаляем спонсора из SponsorRepository & DelayDeleteRepository
            sponsorRepository.deleteById(accountEntityID);
            delayDeleteRepository.deleteById(accountEntityID);
        }
    }
}