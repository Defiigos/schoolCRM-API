package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentDto;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentUpdateRequest;
import com.defiigosProject.SchoolCRMBackend.exception.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentAmountRepo;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentStatusRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.PaymentSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final PaymentStatusRepo paymentStatusRepo;
    private final PaymentAmountRepo paymentAmountRepo;

    public PaymentService(PaymentRepo paymentRepo,
                          PaymentStatusRepo paymentStatusRepo,
                          PaymentAmountRepo paymentAmountRepo) {
        this.paymentRepo = paymentRepo;
        this.paymentStatusRepo = paymentStatusRepo;
        this.paymentAmountRepo = paymentAmountRepo;
    }

    public String createPayment(Lesson lesson, Student student, PaymentAmount amount) throws EntityNotFoundException {

        Payment newPayment = new Payment(LocalDate.now(), LocalTime.now());

        PaymentAmount findAmount = paymentAmountRepo.findById(amount.getId())
                .orElseThrow(() -> new EntityNotFoundException("payment amount"));

        PaymentStatus unpaidStatus = paymentStatusRepo.findByStatus(PaymentStatusType.PAYMENT_UNPAID)
                .orElseThrow(() -> new RuntimeException("Error, Payment status "
                        + PaymentStatusType.PAYMENT_UNPAID + " is not found"));

        lesson.addPayment(newPayment);
        student.addPayment(newPayment);
        findAmount.addPayment(newPayment);
        unpaidStatus.addPayment(newPayment);

        paymentRepo.save(newPayment);
        return "Payment for studentId: " + student.getId() + " successfully created!";
    }

    public ResponseEntity<List<PaymentDto>> getPayment(Long id, Long lessonId, Long studentId, Long amountId,
                                                       String date, String time, PaymentStatusType status) {

        List<Payment> paymentList = paymentRepo.findAll(
                where(withId(id))
                        .and(withLessonId(lessonId))
                        .and(withStudentId(studentId))
                        .and(withAmountId(amountId))
                        .and(withDate(date))
                        .and(withTime(time))
                        .and(withStatus(status))
        );
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (Payment payment: paymentList)
            paymentDtoList.add(PaymentDto.build(payment));

        return ResponseEntity.ok(paymentDtoList);

    }

    public ResponseEntity<MessageResponse> updatePayment(Long id, PaymentUpdateRequest request)
            throws FieldRequiredException, EntityNotFoundException {

        Payment updatedPayment = paymentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment with this id:" + id));

        if (request.getPayDate() != null){
            updatedPayment.setPayDate(request.getPayDate());
        }

        if (request.getPayTime() != null){
            updatedPayment.setPayTime(request.getPayTime());
        }

        if (request.getStatus() != null) {
            PaymentStatus oldStatus = paymentStatusRepo
                    .findByStatus(updatedPayment.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old payment status is not found"));

            PaymentStatus newStatus = paymentStatusRepo
                    .findByStatus(request.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("payment status"));

            oldStatus.removePayment(updatedPayment);
            newStatus.addPayment(updatedPayment);
        }

        if (request.getAmountDto() != null) {
            if (request.getAmountDto().getId() == null ||request.getAmountDto().getId().toString().isEmpty())
                throw new FieldRequiredException("payment amount id");

            PaymentAmount oldPaymentAmount = paymentAmountRepo
                    .findById(request.getAmountDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old payment amount is not found"));

            PaymentAmount newPaymentAmount = paymentAmountRepo
                    .findById(request.getAmountDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("payment amount"));

            oldPaymentAmount.removePayment(updatedPayment);
            newPaymentAmount.addPayment(updatedPayment);
        }

        paymentRepo.save(updatedPayment);
        return ResponseEntity.ok(new MessageResponse("Lesson successfully updated"));
    }
}
