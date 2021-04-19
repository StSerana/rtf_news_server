package com.example.servingwebcontent;

import lombok.Builder;
import lombok.Data;
import org.apache.coyote.Response;

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

    enum Status {
        SUCCESS,
        ERROR
    }
}
