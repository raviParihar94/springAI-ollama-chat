package com.beelive.ollama.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimesTool {

    private final Logger LOGGER = LoggerFactory.getLogger(TimesTool.class);


    @Tool(name = "currentLocalTime" , description =  "Get the current time in Users timezone")
    String getCurrentLocalTime(){
        LOGGER.info("Returning the current time in the user's timezone");
        return LocalTime.now().toString();
    }


    @Tool(name= "getCurrentTime", description = "get the current time in specified time zone. ")
    String getCurrentTime(@ToolParam(description = " Value representing the time zone") String timeZone ){
        LOGGER.info("Returning the current time in the timeZone {}", timeZone);
      return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}
