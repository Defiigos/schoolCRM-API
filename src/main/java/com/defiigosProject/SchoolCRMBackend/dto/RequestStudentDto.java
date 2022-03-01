package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestStudentDto {
    private Long id;
    private String name;
    private String phone;
    private RequestStudentStatusType status;
    private LocationDto location;

    public static RequestStudentDto build(RequestStudent requestStudent) {
        if (requestStudent.getLocation() == null)
            return new RequestStudentDto(
                    requestStudent.getId(),
                    requestStudent.getName(),
                    requestStudent.getPhone(),
                    requestStudent.getStatus().getStatus(),
                    null
            );
        else
            return new RequestStudentDto(
                requestStudent.getId(),
                requestStudent.getName(),
                requestStudent.getPhone(),
                requestStudent.getStatus().getStatus(),
                LocationDto.build(requestStudent.getLocation())
            );
    }
}
