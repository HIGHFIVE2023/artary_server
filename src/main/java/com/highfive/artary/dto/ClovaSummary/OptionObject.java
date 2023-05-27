package com.highfive.artary.dto.ClovaSummary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionObject {

    private String language;
    private String model;
    private int tone;
    private int summaryCount;
}
