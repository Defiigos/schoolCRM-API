package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.response.LocationResponse;
import com.defiigosProject.SchoolCRMBackend.dto.response.RequestStudentResponse;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.Specification.RequestStudentSpecification;
import com.defiigosProject.SchoolCRMBackend.dto.request.CreateRequestStudentRequest;
import com.defiigosProject.SchoolCRMBackend.dto.request.UpdateRequestStudentRequest;
import com.defiigosProject.SchoolCRMBackend.dto.response.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
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

    public ResponseEntity<MessageResponse> createRequestStudent(CreateRequestStudentRequest createRequestStudentRequest){

        if (createRequestStudentRequest.getName() == null || createRequestStudentRequest.getName().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name (\"name\") required"));
        }

        if (createRequestStudentRequest.getPhone() == null || createRequestStudentRequest.getPhone().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Phone (\"phone\") required"));
        }


        RequestStudent newRequestStudent = new RequestStudent(
                createRequestStudentRequest.getName(),
                createRequestStudentRequest.getPhone()
        );

        if (createRequestStudentRequest.getLocation() != null){
            if (createRequestStudentRequest.getLocation().getId() == null)
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Location id (\"id\") required"));

            Optional<Location> optionalRequestStudentLocation = locationRepo.findOne(Example.of(createRequestStudentRequest.getLocation()));

            if (optionalRequestStudentLocation.isEmpty())
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Location(\"location\") not found"));
            Location requestStudentLocation = optionalRequestStudentLocation.get();

            requestStudentLocation.addRequestStudent(newRequestStudent);
            createRequestStudentRequest.getLocation().addRequestStudent(newRequestStudent);
        }

        RequestStudentStatus newStatus = requestStudentStatusRepo.findByStatus(RequestStudentStatusType.REQUEST_NEW)
                .orElseThrow(() -> new RuntimeException("Error, Request student status (REQUEST_NEW) is not found"));
        newStatus.addRequestStudent(newRequestStudent);

        requestStudentRepo.save(newRequestStudent);
        return ResponseEntity.ok(new MessageResponse("RequestStudent successfully created"));
    }

    public ResponseEntity<List<RequestStudentResponse>> getRequestStudent(Long id, String name, String phone, String status, Long locationId){

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
                                        requestStudent.getLocation().getName()
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

    public ResponseEntity<MessageResponse> updateRequestStudent(Long id, UpdateRequestStudentRequest updateRequestStudentRequest){

        Optional<RequestStudent> optionalRequestStudent = requestStudentRepo.findById(id);
        if (optionalRequestStudent.isEmpty())
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Request student not found"));

        RequestStudent findRequestStudent = optionalRequestStudent.get();

        if (updateRequestStudentRequest.getStatus() != null)
        {
            RequestStudentStatus oldStatus = requestStudentStatusRepo
                    .findByStatus(findRequestStudent.getRequestStudentStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Request student status is not found"));
            oldStatus.removeRequestStudent(findRequestStudent);

            RequestStudentStatus newStatus = requestStudentStatusRepo
                    .findByStatus(updateRequestStudentRequest.getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Request student status is not found"));
            newStatus.addRequestStudent(findRequestStudent);

            findRequestStudent.setRequestStudentStatus(newStatus);
        }


        if (updateRequestStudentRequest.getName() != null ) {
            if (updateRequestStudentRequest.getName().isEmpty()) {
                return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Name required"));
            }
            findRequestStudent.setName(updateRequestStudentRequest.getName());
        }

        if (updateRequestStudentRequest.getPhone() != null){
            if (updateRequestStudentRequest.getPhone().isEmpty()){
                return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Phone required"));
            }
            findRequestStudent.setPhone(updateRequestStudentRequest.getPhone());
        }

        if (updateRequestStudentRequest.getLocation() != null){
            if (updateRequestStudentRequest.getLocation().getAddress() != null || updateRequestStudentRequest.getLocation().getAddress().isEmpty()){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Location address required"));
            }
            findRequestStudent.setLocation(updateRequestStudentRequest.getLocation());
        }

        requestStudentRepo.save(findRequestStudent);
        return ResponseEntity.ok(new MessageResponse("Request student successfully updated"));
    }
}
