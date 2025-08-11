package ru.otus.hw.services;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Advertisement;

import ru.otus.hw.domain.PathType;
import ru.otus.hw.domain.PaymentReceipt;

import static ru.otus.hw.domain.OrganizationType.MEDICAL;
import static ru.otus.hw.domain.PathType.PUSH;
import static ru.otus.hw.domain.PathType.SMS;


@Service
@Slf4j
public class AdvertisementServiceImpl implements AdvertisementService {

	private static final PathType[] PATH_TYPES = PathType.values();

	@Override
	public Advertisement calculate(PaymentReceipt paymentReceipt) {
		log.info("Calculate advertisement  for client {}", paymentReceipt.clientId());
		delay();
		log.info("Advertisement calculated done for client {}", paymentReceipt.clientId());
		PathType pathType = paymentReceipt.organizationType() == MEDICAL ? SMS : PUSH;
		return new Advertisement(
				paymentReceipt.clientId(),
				"Some " + paymentReceipt.organizationType() +  " advertisement!",
				pathType);
	}

	private static void delay() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
