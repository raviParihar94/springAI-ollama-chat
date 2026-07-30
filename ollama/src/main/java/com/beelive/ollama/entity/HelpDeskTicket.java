package com.beelive.ollama.entity;


import com.beelive.ollama.utils.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
@Builder
public class HelpDeskTicket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private  String issue;
    private TicketStatus status; // e.g OPEN, IN_PROGRESS, CLOSED
    private LocalDateTime createdAt;
    private LocalDateTime eta;
}
