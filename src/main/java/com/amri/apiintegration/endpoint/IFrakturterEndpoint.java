package com.amri.apiintegration.endpoint;

import com.amri.apiintegration.dto.frankfurter.FinanceResourceResultDto;
import com.amri.apiintegration.util.IResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/finance/data")
public interface IFrakturterEndpoint {

    @GetMapping("/{resourceType}")
    IResultDTO<List<FinanceResourceResultDto>> getFinanceData(@PathVariable String resourceType);
}
