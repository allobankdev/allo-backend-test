package com.example.tech.splitbiller.model.request;

import com.example.tech.splitbiller.util.SplitTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExpenseSplitRequest(
        @NotNull(message = "Please provide type split")
        SplitTypeEnum type,

        @NotEmpty
        List<SplitParticipantRequest> shares
) {}
