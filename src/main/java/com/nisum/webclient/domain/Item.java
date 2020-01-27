package com.nisum.webclient.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Item {
        private String Id;
        private String description;
        private Double price;
    }


