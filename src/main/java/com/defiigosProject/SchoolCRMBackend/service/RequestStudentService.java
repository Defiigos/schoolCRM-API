package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.RequestStudentDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldNotNullException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.LocationRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.RequestStudentStatusRepo;
import com.defiigosProject.SchoolCRMBackend.repo.Specification.RequestStudentSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType.REQUEST_NEW;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class RequestStudentService {

    private final String uri;
    private final RequestStudentStatusRepo requestStudentStatusRepo;
    private final RequestStudentRepo requestStudentRepo;
    private final LocationRepo locationRepo;

    public RequestStudentService(@Value("${URI}") String uri, RequestStudentStatusRepo requestStudentStatusRepo,
                                 RequestStudentRepo requestStudentRepo, LocationRepo locationRepo) {
        this.uri = uri;
        this.requestStudentStatusRepo = requestStudentStatusRepo;
        this.requestStudentRepo = requestStudentRepo;
        this.locationRepo = locationRepo;
    }

    public ResponseEntity<MessageResponse> createRequestStudent(
            RequestStudentDto requestStudentDto)
            throws FieldRequiredException, EntityNotFoundException {

        if (requestStudentDto.getName() == null || requestStudentDto.getName().isEmpty()){
            throw new FieldRequiredException("name");
        }

        if (requestStudentDto.getPhone() == null || requestStudentDto.getPhone().isEmpty()){
            throw new FieldRequiredException("phone");
        }


        RequestStudent newRequestStudent = new RequestStudent(
                requestStudentDto.getName(),
                requestStudentDto.getPhone()
        );
        RequestStudentStatus newStatus = requestStudentStatusRepo.findByStatus(REQUEST_NEW)
                .orElseThrow(() -> new RuntimeException("Error, Request student status "
                        + REQUEST_NEW + " is not found"));
        newStatus.addRequestStudent(newRequestStudent);

        if (requestStudentDto.getLocation() != null){
            if (requestStudentDto.getLocation().getId() == null)
                throw new FieldRequiredException("location id");

            Optional<Location> optionalFindLocation = locationRepo.findById(requestStudentDto.getLocation().getId());
            if (optionalFindLocation.isEmpty())
                throw new EntityNotFoundException("location");
            Location findLocation = optionalFindLocation.get();

            findLocation.addRequestStudent(newRequestStudent);
        }

        requestStudentRepo.save(newRequestStudent);
        return ResponseEntity.created(URI.create(uri + "/api/requestStudents"))
                .body(new MessageResponse("Request student successfully created"));
    }

    public ResponseEntity<List<RequestStudentDto>> getRequestStudent(
            Long id, String name, String phone, String status, String locationName, Long locationId)
            throws BadEnumException {

        RequestStudentStatusType parseStatus = null;
        try {
            if (status != null)
                parseStatus = RequestStudentStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(RequestStudentStatusType.class, status);
        }

        List<RequestStudent> requestStudentList = requestStudentRepo.findAll(
                where(RequestStudentSpecification.withId(id))
                        .and(RequestStudentSpecification.withName(name))
                        .and(RequestStudentSpecification.withPhone(phone))
                        .and(RequestStudentSpecification.withStatus(parseStatus))
                        .and(RequestStudentSpecification.withLocationId(locationId))
                        .and(RequestStudentSpecification.withLocationName(locationName)),
                Sort.by(Sort.Direction.DESC, "id")
        );

        List<RequestStudentDto> requestStudentDtoList = new ArrayList<>();
        for (RequestStudent requestStudent: requestStudentList) {
                requestStudentDtoList.add(RequestStudentDto.build(requestStudent));
        }
        return ResponseEntity.ok(requestStudentDtoList);
    }

    public ResponseEntity<MessageResponse> updateRequestStudent(
            Long id, RequestStudentDto requestStudentDto)
            throws EntityNotFoundException, FieldRequiredException, FieldNotNullException {

        RequestStudent findRequestStudent = requestStudentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request student with id:" + id));

        if (requestStudentDto.getName() != null ) {
            if (requestStudentDto.getName().isEmpty()) {
                throw new FieldNotNullException("name");
            }
            findRequestStudent.setName(requestStudentDto.getName());
        }

        if (requestStudentDto.getPhone() != null) {
            if (requestStudentDto.getPhone().isEmpty()){
                throw new FieldNotNullException("phone");
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
                    .orElseThrow(() -> new EntityNotFoundException("request student status"));
            newStatus.addRequestStudent(findRequestStudent);
        }

        if (requestStudentDto.getLocation() != null){
          if (requestStudentDto.getLocation().getId() == null)
                throw new FieldRequiredException("location");

            Optional<Location> optionalFindLocation = locationRepo.findById(requestStudentDto.getLocation().getId());
            if (optionalFindLocation.isEmpty())
                throw new EntityNotFoundException("location");

            optionalFindLocation.get().addRequestStudent(findRequestStudent);
        }

        requestStudentRepo.save(findRequestStudent);
        return ResponseEntity.ok(new MessageResponse("Request student successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteRequestStudent(Long id)
            throws EntityNotFoundException {

        RequestStudent requestStudent = requestStudentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request student with id:"));

        requestStudentRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Request student successfully deleted"));
    }
}
