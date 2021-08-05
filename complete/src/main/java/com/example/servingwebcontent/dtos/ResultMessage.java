package com.example.servingwebcontent.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultMessage {
    @Builder.Default
    private Status status = Status.ERROR;
    @Builder.Default
    private String message = "";

    public static ResultMessage getSuccess(){
        return ResultMessage.builder()
                .message("add successful")
                .status(Status.SUCCESS).build();
    }

    public static ResultMessage getError(){
        return ResultMessage.builder()
                .message("some problems")
                .status(Status.ERROR).build();
    }


    enum Status {
        SUCCESS,
        ERROR
    }
}
