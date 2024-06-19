package org.plantict.models;

import lombok.Data;

@Data
public class AttractionRequest {

    private String  name;
    private String  description;
    private Integer ticketDuration;
    private Integer maxTicket;
}
