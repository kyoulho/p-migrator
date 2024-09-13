package io.playce.migrator.domain.common.service;

import io.playce.common.subscription.dto.Subscription;
import io.playce.common.subscription.manager.SubscriptionLoader;
import io.playce.migrator.domain.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionLoader subscriptionLoader;
    private final ApplicationService applicationService;

    public Subscription getSubscription() {
        Subscription subscription = subscriptionLoader.getSubscription();
        Integer usedCount = applicationService.getAllApplicationCount();
        subscription.setUsedCount(usedCount);
        return subscription;
    }

    public boolean checkSubscription() {
        Subscription subscription = subscriptionLoader.getSubscription();
        Integer usedCount = applicationService.getAllApplicationCount();
        return subscription.getCount() > usedCount;
    }
}
