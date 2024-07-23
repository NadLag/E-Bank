package org.practice.ebankbackend.dtos;

import lombok.Data;
import org.practice.ebankbackend.enums.OperationType;

import java.util.Date;


@Data
public class AccountOperationDTO {

   private Long id;
   private Date operationDate;
   private String description;
   private double amount;
   private OperationType operationType;

}