package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.models.enums.ResourceTypeEnum;

public interface FinanceServiceInterface {
    Object getFinanceData(ResourceTypeEnum resourceType);
}
