package co.id.allobank.finance.service;

import co.id.allobank.finance.exception.ServiceException;
import co.id.allobank.finance.utils.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinanceDataService {

    private final InMemoryFinanceStore store;

    public Object getData(String type){
        if(!store.exists(type)){
            throw new ServiceException(ErrorCode.INVALID_RESOURCE_TYPE);
        }

        return store.get(type);
    }
}
