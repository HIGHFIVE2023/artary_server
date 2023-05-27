package com.highfive.artary.dto.ClovaSummary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClovaSummaryRequestDto {

    private DocumentObject document;
    private OptionObject option;
}
