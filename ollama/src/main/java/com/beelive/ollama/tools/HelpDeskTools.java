package com.beelive.ollama.tools;

import com.beelive.ollama.entity.HelpDeskTicket;
import com.beelive.ollama.model.TicketRequest;
import com.beelive.ollama.service.HelpDeskTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpDeskTools {

  private final  Logger LOGGER = LoggerFactory.getLogger(HelpDeskTools.class);

  private  final HelpDeskTicketService helpDeskTicketService;
  @Tool(name="createTicket", description =  "Create the support Ticket")
  String createTickets(@ToolParam(description = "details to create a Support Ticket") TicketRequest ticketRequest , ToolContext toolContext){
      String userName = (String )toolContext.getContext().get("userName");
      HelpDeskTicket heplDeskTicket = helpDeskTicketService.createHeplDeskTicket(ticketRequest, userName);
      return "Ticket # " + heplDeskTicket.getId() + " Created successfully for user "+ heplDeskTicket.getUserName() ;
  }

}
