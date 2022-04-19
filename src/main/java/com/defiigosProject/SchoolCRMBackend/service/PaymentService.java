package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentDto;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentUpdateDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadEnumException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.EntityNotFoundException;
import com.defiigosProject.SchoolCRMBackend.exception.extend.FieldRequiredException;
import com.defiigosProject.SchoolCRMBackend.model.*;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentAmountRepo;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentRepo;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentStatusRepo;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType.*;
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

    public String createPayment(Lesson lesson, Student student, PaymentAmount amount)
            throws EntityNotFoundException {

        Payment newPayment = new Payment(LocalDate.now(), LocalTime.now());

        PaymentAmount findAmount = paymentAmountRepo.findById(amount.getId())
                .orElseThrow(() -> new EntityNotFoundException("payment amount"));

        PaymentStatus unpaidStatus = paymentStatusRepo.findByStatus(PAYMENT_UNPAID)
                .orElseThrow(() -> new RuntimeException("Error, Payment status "
                        + PAYMENT_UNPAID + " is not found"));

        lesson.addPayment(newPayment);
        student.addPayment(newPayment);
        findAmount.addPayment(newPayment);
        unpaidStatus.addPayment(newPayment);

        paymentRepo.save(newPayment);
        return "Payment for student with id: " + student.getId() + " successfully created!";
    }

    public ResponseEntity<List<PaymentDto>> getPayment(Long id, Long lessonId, Long studentId, Long teacherId, Long amountId,
                                                       String date, String time, String status,  String studentName, String teacherName, String amountName,
                                                       String dateFrom, String dateTo, String timeFrom, String timeTo)
            throws BadEnumException {

        PaymentStatusType paresStatus = null;
        try {
            if (status != null)
                paresStatus = PaymentStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BadEnumException(LessonGroupStatusType.class, status);
        }

        List<Payment> paymentList = paymentRepo.findAll(
                where(withId(id))
                        .and(withLessonId(lessonId))
                        .and(withStudentId(studentId))
                        .and(withTeacherId(teacherId))
                        .and(withAmountId(amountId))
                        .and(withStudentName(studentName))
                        .and(withTeacherName(teacherName))
                        .and(withAmountName(amountName))
                        .and(withDate(date))
                        .and(withTime(time))
                        .and(withStatus(paresStatus))
                        .and(withDateFrom(dateFrom))
                        .and(withDateTo(dateTo))
                        .and(withTimeFrom(timeFrom))
                        .and(withTimeTo(timeTo)),
                Sort.by(Sort.Direction.ASC, "id")
        );
        List<PaymentDto> paymentDtoList = new ArrayList<>();
        for (Payment payment: paymentList)
            paymentDtoList.add(PaymentDto.build(payment));

        return ResponseEntity.ok(paymentDtoList);

    }

    public ResponseEntity<MessageResponse> updatePayment(Long id, PaymentUpdateDto updateDto)
            throws FieldRequiredException, EntityNotFoundException {

        Payment updatedPayment = paymentRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment with this id:" + id));

        if (updateDto.getStatus() != null && updateDto.getStatus() != updatedPayment.getStatus().getStatus()) {
            PaymentStatus oldStatus = paymentStatusRepo
                    .findByStatus(updatedPayment.getStatus().getStatus())
                    .orElseThrow(() -> new RuntimeException("Error, Old payment status is not found"));

            PaymentStatus newStatus = paymentStatusRepo
                    .findByStatus(updateDto.getStatus())
                    .orElseThrow(() -> new EntityNotFoundException("payment status"));

//            switch (updateDto.getStatus()) {
//                case PAYMENT_PAID:
//                case PAYMENT_CANCELED:
//                    if (!updatedPayment.getStatus().getStatus().equals(PAYMENT_UNPAID))
//                        throw new BadRequestException("payment must have " + PAYMENT_UNPAID + " status");
//                    break;
//                case PAYMENT_UNPAID:
//                    if (!updatedPayment.getStatus().getStatus().equals(PAYMENT_CANCELED))
//                        throw new BadRequestException("payment must have " + PAYMENT_CANCELED + " status");
//                    break;
//                case PAYMENT_REMOVED:
//                    if (!updatedPayment.getStatus().getStatus().equals(PAYMENT_PAID))
//                        throw new BadRequestException("payment must have " + PAYMENT_PAID + " status");
//                    break;
//            }

            updatedPayment.setPayDate(LocalDate.now());
            updatedPayment.setPayTime(LocalTime.now());
            oldStatus.removePayment(updatedPayment);
            newStatus.addPayment(updatedPayment);
        }

        if (updateDto.getAmountDto() != null && !updateDto.getAmountDto().getId().equals(updatedPayment.getAmount().getId())) {
            if (updateDto.getAmountDto().getId() == null ||updateDto.getAmountDto().getId().toString().isEmpty())
                throw new FieldRequiredException("payment amount id");

            PaymentAmount oldPaymentAmount = paymentAmountRepo
                    .findById(updateDto.getAmountDto().getId())
                    .orElseThrow(() -> new RuntimeException("Error, Old payment amount is not found"));

            PaymentAmount newPaymentAmount = paymentAmountRepo
                    .findById(updateDto.getAmountDto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("payment amount"));

            oldPaymentAmount.removePayment(updatedPayment);
            newPaymentAmount.addPayment(updatedPayment);
        }

        paymentRepo.save(updatedPayment);
        return ResponseEntity.ok(new MessageResponse("Lesson successfully updated"));
    }
}
