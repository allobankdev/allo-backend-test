package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.Response;

public interface CalegServiceInterface {
    public Response listAllCaleg(String dapilFilter, String partaiFilter, String sortBy);
}
