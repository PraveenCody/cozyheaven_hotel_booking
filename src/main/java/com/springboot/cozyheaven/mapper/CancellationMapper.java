package com.springboot.cozyheaven.mapper;

import com.springboot.cozyheaven.dto.request.CancellationDto;
import com.springboot.cozyheaven.model.Cancellation;

public class CancellationMapper {

    public static Cancellation getMappedforCancellation(CancellationDto cancellationDto){

        Cancellation cancellation = new Cancellation();
        cancellation.setReason(cancellationDto.reason());
        return cancellation;

    }

}
