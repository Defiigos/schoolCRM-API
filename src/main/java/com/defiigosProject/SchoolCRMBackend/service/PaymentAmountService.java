package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.dto.PaymentAmountDto;
import com.defiigosProject.SchoolCRMBackend.exception.*;
import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import com.defiigosProject.SchoolCRMBackend.repo.PaymentAmountRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.PaymentAmountSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class PaymentAmountService {

    private final PaymentAmountRepo paymentAmountRepo;

    public PaymentAmountService(PaymentAmountRepo paymentAmountRepo) {
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
        return ResponseEntity.ok(new MessageResponse("Payment Amount successfully created"));
    }

    public ResponseEntity<List<PaymentAmountDto>> getPaymentAmount(Long id, Float sum, String name){

        List<PaymentAmount> paymentAmountList = paymentAmountRepo.findAll(
                where(withId(id))
                        .and(withSum(sum))
                        .and(withName(name))
        );

        List<PaymentAmountDto> paymentAmountDtoList = new ArrayList<>();
        for (PaymentAmount paymentAmount: paymentAmountList){
            paymentAmountDtoList.add(PaymentAmountDto.build(paymentAmount));
        }
        return ResponseEntity.ok(paymentAmountDtoList);
    }

    public ResponseEntity<MessageResponse> updatePaymentAmount(Long id, PaymentAmountDto paymentAmountDto)
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException {

        PaymentAmount updatedPaymentAmount = paymentAmountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment amount with this id:" + id));


        if (paymentAmountDto.getSum() != null){
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

    public ResponseEntity<MessageResponse> deletePaymentAmount(Long id) throws EntityNotFoundException, EntityUsedException {

        PaymentAmount deletedPaymentAmount = paymentAmountRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("payment amount with this id:" + id));

        if (!deletedPaymentAmount.getPayments().isEmpty())
            throw new EntityUsedException("payment amount", "payment");

        paymentAmountRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Payment amount successfully deleted"));
    }
}
