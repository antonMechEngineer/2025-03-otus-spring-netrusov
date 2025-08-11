package ru.otus.hw.mapper;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Advertisement;

@Component
public class AdvertisementMapper {

    public String toSms(Advertisement advertisement) {
        return "SMS: " + advertisement.advertisementMsg();
    }

    public String toPush(Advertisement advertisement) {
        return "PUSH: " + advertisement.advertisementMsg();
    }
}
