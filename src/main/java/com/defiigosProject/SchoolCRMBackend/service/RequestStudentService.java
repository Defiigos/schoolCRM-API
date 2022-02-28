package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.RequestStudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.Specification.RequestStudentSpecification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class RequestStudentService {

    private final RequestStudentStatusRepo requestStudentStatusRepo;
    private final RequestStudentRepo requestStudentRepo;
    private final LocationRepo locationRepo;

    public RequestStudentService(RequestStudentStatusRepo requestStudentStatusRepo, RequestStudentRepo requestStudentRepo, LocationRepo locationRepo) {
        this.requestStudentStatusRepo = requestStudentStatusRepo;
        this.requestStudentRepo = requestStudentRepo;
        this.locationRepo = locationRepo;
    }

    public ResponseEntity<MessageResponse> createRequestStudent(
            RequestStudentDto requestStudentDto) throws BadRequestException {

        if (requestStudentDto.getName() == null || requestStudentDto.getName().isEmpty()){
            throw new BadRequestException("Error: Name (\"name\") required");
        }

        if (requestStudentDto.getPhone() == null || requestStudentDto.getPhone().isEmpty()){
            throw new BadRequestException("Error: Phone (\"phone\") required");
        }


        RequestStudent newRequestStudent = new RequestStudent(
                requestStudentDto.getName(),
                requestStudentDto.getPhone()
        );
        RequestStudentStatus newStatus = requestStudentStatusRepo.findByStatus(RequestStudentStatusType.REQUEST_NEW)
                .orElseThrow(() -> new RuntimeException("Error, Request student status (REQUEST_NEW) is not found"));
        newStatus.addRequestStudent(newRequestStudent);

        if (requestStudentDto.getLocation() != null){
            if (requestStudentDto.getLocation().getId() == null)
                throw new BadRequestException("Error: Location id (\"id\") required");

            Optional<Location> optionalFindLocation = locationRepo.findById(requestStudentDto.getLocation().getId());
            if (optionalFindLocation.isEmpty())
                throw new BadRequestException("Error: Location(\"location\") is not found");
            Location findLocation = optionalFindLocation.get();

            findLocation.addRequestStudent(newRequestStudent);
        }

        requestStudentRepo.save(newRequestStudent);
        return ResponseEntity.ok(new MessageResponse("RequestStudent successfully created"));
    }

    public ResponseEntity<List<RequestStudentDto>> getRequestStudent(
            Long id, String name, String phone, String status, Long locationId){

        List<RequestStudent> requestStudentList = requestStudentRepo.findAll(
                where(RequestStudentSpecification.withId(id))
                        .and(RequestStudentSpecification.withName(name))
                        .and(RequestStudentSpecification.withPhone(phone))
                        .and(RequestStudentSpecification.withStatus(status))
                        .and(RequestStudentSpecification.withLocationId(locationId))
        );

        List<RequestStudentDto> requestStudentDtoList = new ArrayList<>();
        for (RequestStudent requestStudent:
             requestStudentList) {
            if (requestStudent.getLocation()!= null)
                requestStudentDtoList.add(
                        new RequestStudentDto(
                                requestStudent.getId(),
                                requestStudent.getName(),
                                requestStudent.getPhone(),
                                requestStudent.getStatus().getStatus(),
                                new LocationDto(
                                        requestStudent.getLocation().getId(),
                                        requestStudent.getLocation().getAddress(),
                                        requestStudent.getLocation().getName(),
                                        requestStudent.getLocation().getStatus().getStatus()
                                )
                        )
                );
            else
                requestStudentDtoList.add(
                        new RequestStudentDto(
                                requestStudent.getId(),
                                requestStudent.getName(),
                                requestStudent.getPhone(),
                                requestStudent.getStatus().getStatus(),
                                null
                        )
                );
        }

        return ResponseEntity.ok(requestStudentDtoList);
    }

    public ResponseEntity<MessageResponse> updateRequestStudent(
            Long id, RequestStudentDto requestStudentDto) throws BadRequestException {

        Optional<RequestStudent> optionalRequestStudent = requestStudentRepo.findById(id);
        if (optionalRequestStudent.isEmpty())
            throw new BadRequestException("Error: Request student with id:" + id + " is not found");
        RequestStudent findRequestStudent = optionalRequestStudent.get();


        if (requestStudentDto.getName() != null ) {
            if (requestStudentDto.getName().isEmpty()) {
                throw new BadRequestException("Error: Name('name') must not be empty");
            }
            findRequestStudent.setName(requestStudentDto.getName());
        }

        if (requestStudentDto.getPhone() != null) {
            if (requestStudentDto.getPhone().isEmpty()){
                throw new BadRequestException("Error: Phone('phone') must not be empty");
            }
            findRequestStudent.setPhone(requestStudentDto.getPhone());
        }

        if (requestStudentDto.getStatus() != null) {
            RequestStudentStatus oldStatus = requestStudentStatusRepo
                    .findByStatus(findRequestStudent.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old request student status is not found"));
            oldStatus.removeRequestStudent(findRequestStudent);

            RequestStudentStatus newStatus = requestStudentStatusRepo
                    .findByStatus(requestStudentDto.getStatus())
                    .orElseThrow(() -> new BadRequestException("Error, New request student status('status') is not found"));
            newStatus.addRequestStudent(findRequestStudent);
        }

        if (requestStudentDto.getLocation() != null){
          if (requestStudentDto.getLocation().getId() == null)
                throw new BadRequestException("Error: Location id (\"id\") required");

            Optional<Location> optionalFindLocation = locationRepo.findById(requestStudentDto.getLocation().getId());
            if (optionalFindLocation.isEmpty())
                throw new BadRequestException("Error: Location(\"location\") is not found");

            optionalFindLocation.get().addRequestStudent(findRequestStudent);
        }

        requestStudentRepo.save(findRequestStudent);
        return ResponseEntity.ok(new MessageResponse("Request student successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteRequestStudent(Long id) throws BadRequestException{
        Optional<RequestStudent> optionalRequestStudent = requestStudentRepo.findById(id);
        if (optionalRequestStudent.isEmpty())
            throw new BadRequestException("Error: Request student with id:" + id + " is not found");

        requestStudentRepo.deleteById(id);

        return ResponseEntity.ok(new MessageResponse("Request student successfully deleted"));
    }
}
