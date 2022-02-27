package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.request.RequestStudentRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.LocationResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.RequestStudentResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.Specification.RequestStudentSpecification;
import org.springframework.data.domain.Example;
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
            RequestStudentRequest requestStudentRequest) throws BadRequestException {

        if (requestStudentRequest.getName() == null || requestStudentRequest.getName().isEmpty()){
            throw new BadRequestException("Error: Name (\"name\") required");
        }

        if (requestStudentRequest.getPhone() == null || requestStudentRequest.getPhone().isEmpty()){
            throw new BadRequestException("Error: Phone (\"phone\") required");
        }


        RequestStudent newRequestStudent = new RequestStudent(
                requestStudentRequest.getName(),
                requestStudentRequest.getPhone()
        );
        RequestStudentStatus newStatus = requestStudentStatusRepo.findByStatus(RequestStudentStatusType.REQUEST_NEW)
                .orElseThrow(() -> new RuntimeException("Error, Request student status (REQUEST_NEW) is not found"));
        newStatus.addRequestStudent(newRequestStudent);

        if (requestStudentRequest.getLocation() != null){
            if (requestStudentRequest.getLocation().getId() == null)
                throw new BadRequestException("Error: Location id (\"id\") required");

            Optional<Location> optionalFindLocation = locationRepo.findOne(
                    Example.of(requestStudentRequest.getLocation()));
            if (optionalFindLocation.isEmpty())
                throw new BadRequestException("Error: Location(\"location\") is not found");
            Location findLocation = optionalFindLocation.get();

            findLocation.addRequestStudent(newRequestStudent);
        }

        requestStudentRepo.save(newRequestStudent);
        return ResponseEntity.ok(new MessageResponse("RequestStudent successfully created"));
    }

    public ResponseEntity<List<RequestStudentResponse>> getRequestStudent(
            Long id, String name, String phone, String status, Long locationId){

        List<RequestStudent> requestStudentList = requestStudentRepo.findAll(
                where(RequestStudentSpecification.withId(id))
                        .and(RequestStudentSpecification.withName(name))
                        .and(RequestStudentSpecification.withPhone(phone))
                        .and(RequestStudentSpecification.withStatus(status))
                        .and(RequestStudentSpecification.withLocationId(locationId))
        );

        List<RequestStudentResponse> requestStudentResponseList = new ArrayList<>();
        for (RequestStudent requestStudent:
             requestStudentList) {
            if (requestStudent.getLocation()!= null)
                requestStudentResponseList.add(
                        new RequestStudentResponse(
                                requestStudent.getId(),
                                requestStudent.getName(),
                                requestStudent.getPhone(),
                                requestStudent.getRequestStudentStatus().getStatus(),
                                new LocationResponse(
                                        requestStudent.getLocation().getId(),
                                        requestStudent.getLocation().getAddress(),
                                        requestStudent.getLocation().getName(),
                                        requestStudent.getLocation().getLocationStatus().getStatus()
                                )
                        )
                );
            else
                requestStudentResponseList.add(
                        new RequestStudentResponse(
                                requestStudent.getId(),
                                requestStudent.getName(),
                                requestStudent.getPhone(),
                                requestStudent.getRequestStudentStatus().getStatus(),
                                null
                        )
                );
        }

        return ResponseEntity.ok(requestStudentResponseList);
    }

    public ResponseEntity<MessageResponse> updateRequestStudent(
            Long id, RequestStudentRequest requestStudentRequest) throws BadRequestException {

        Optional<RequestStudent> optionalRequestStudent = requestStudentRepo.findById(id);
        if (optionalRequestStudent.isEmpty())
            throw new BadRequestException("Error: Request student with id:" + id + " is not found");
        RequestStudent findRequestStudent = optionalRequestStudent.get();


        if (requestStudentRequest.getName() != null ) {
            if (requestStudentRequest.getName().isEmpty()) {
                throw new BadRequestException("Error: Name('name') must not be empty");
            }
            findRequestStudent.setName(requestStudentRequest.getName());
        }

        if (requestStudentRequest.getPhone() != null) {
            if (requestStudentRequest.getPhone().isEmpty()){
                throw new BadRequestException("Error: Phone('phone') must not be empty");
            }
            findRequestStudent.setPhone(requestStudentRequest.getPhone());
        }

        if (requestStudentRequest.getStatus() != null) {
            RequestStudentStatus oldStatus = requestStudentStatusRepo
                    .findByStatus(findRequestStudent.getRequestStudentStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old request student status is not found"));
            oldStatus.removeRequestStudent(findRequestStudent);

            RequestStudentStatus newStatus = requestStudentStatusRepo
                    .findByStatus(requestStudentRequest.getStatus())
                    .orElseThrow(() -> new BadRequestException("Error, New request student status('status') is not found"));
            newStatus.addRequestStudent(findRequestStudent);
        }

        if (requestStudentRequest.getLocation() != null){
          if (requestStudentRequest.getLocation().getId() == null)
                throw new BadRequestException("Error: Location id (\"id\") required");

            Optional<Location> optionalFindLocation = locationRepo.findOne(
                    Example.of(requestStudentRequest.getLocation()));
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
