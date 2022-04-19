package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.payment.PaymentAmountDto;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentAmountRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.PaymentAmountSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class PaymentAmountService {

    private final String uri;
    private final PaymentAmountRepo paymentAmountRepo;

    public PaymentAmountService(@Value("${URI}") String uri, PaymentAmountRepo paymentAmountRepo) {
        this.uri = uri;
        this.paymentAmountRepo = paymentAmountRepo;
    }

    public ResponseEntity<MessageResponse> createPaymentAmount(PaymentAmountDto paymentAmountDto)
            throws FieldRequiredException, EntityAlreadyExistException {

        if (paymentAmountDto.getSum() == null || paymentAmountDto.getSum().toString().isEmpty()){
            throw new FieldRequiredException("sum");
        }

        if (paymentAmountRepo.existsBySum(paymentAmountDto.getSum())){
            throw new EntityAlreadyExistException("sum");
        }

        PaymentAmount newPaymentAmount = new PaymentAmount(paymentAmountDto.getSum(), paymentAmountDto.getName());
        paymentAmountRepo.save(newPaymentAmount);
        return ResponseEntity.created(URI.create(uri + "/api/payments/amounts"))
                .body(new MessageResponse("Payment Amount successfully created"));
    }

    public ResponseEntity<List<PaymentAmountDto>> getPaymentAmount(Long id, String sum, String name)
            throws BadRequestException {

        Float parseSum = null;
        try {
            if (sum != null)
                parseSum = Float.parseFloat(sum);
        }
        catch (DateTimeParseException e) {
            throw new BadRequestException("sum format incorrect");
        }

        List<PaymentAmount> paymentAmountList = paymentAmountRepo.findAll(
                where(withId(id))
                        .and(withSum(parseSum))
                        .and(withName(name))
        );

        List<PaymentAmountDto> paymentAmountDtoList = new ArrayList<>();
        for (PaymentAmount paymentAmount: paymentAmountList){
            paymentAmountDtoList.add(PaymentAmountDto.build(paymentAmount));
        }
        return ResponseEntity.ok(paymentAmountDtoList);
    }

    public ResponseEntity<MessageResponse> updatePaymentAmount(Long id, PaymentAmountDto paymentAmountDto)
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {

        PaymentAmount updatedPaymentAmount = paymentAmountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment amount with this id:" + id));

        if (paymentAmountDto.getSum() != null &&
                (Float.compare(paymentAmountDto.getSum(), updatedPaymentAmount.getSum()) != 0)) {

            if (!updatedPaymentAmount.getPayments().isEmpty())
                throw new EntityUsedException("payment amount", "payment");

            if (paymentAmountDto.getSum().toString().isEmpty())
                throw new FieldNotNullException("sum");

            if (!updatedPaymentAmount.getSum().equals(paymentAmountDto.getSum())) {
                if (paymentAmountRepo.existsBySum(paymentAmountDto.getSum())) {
                    throw new EntityAlreadyExistException("sum");
                }
                updatedPaymentAmount.setSum(paymentAmountDto.getSum());
            }
        }

        if (paymentAmountDto.getName() != null){
            updatedPaymentAmount.setName(paymentAmountDto.getName());
        }

        paymentAmountRepo.save(updatedPaymentAmount);
        return ResponseEntity.ok(new MessageResponse("Payment amount successfully updated"));
    }

    public ResponseEntity<MessageResponse> deletePaymentAmount(Long id)
            throws EntityNotFoundException, EntityUsedException {

        PaymentAmount deletedPaymentAmount = paymentAmountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment amount with this id:" + id));

        if (!deletedPaymentAmount.getPayments().isEmpty())
            throw new EntityUsedException("payment amount", "payment");

        paymentAmountRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Payment amount successfully deleted"));
    }
}
