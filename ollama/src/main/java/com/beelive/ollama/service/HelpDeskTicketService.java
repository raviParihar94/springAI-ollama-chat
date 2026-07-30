package com.beelive.ollama.service;

import com.beelive.ollama.entity.HelpDeskTicket;
import com.beelive.ollama.model.TicketRequest;
import com.beelive.ollama.repository.HelpDeskTicketRepository;
import com.beelive.ollama.utils.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {


    private final HelpDeskTicketRepository helpDeskRepository;

    public HelpDeskTicket createHeplDeskTicket(TicketRequest ticketRequest, String userName){
        HelpDeskTicket ticket =  HelpDeskTicket.builder()
                .userName(userName)
                .status(TicketStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .issue(ticketRequest.issue())
                .eta(LocalDateTime.now().plusHours(48))
                .build();
       return helpDeskRepository.save(ticket);

    }


    public List<HelpDeskTicket> getTicketsByUserName(String userName ){
        return helpDeskRepository.findByUserName(userName);
    }
}
