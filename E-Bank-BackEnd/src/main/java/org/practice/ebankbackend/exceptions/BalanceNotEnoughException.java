package org.practice.ebankbackend.exceptions;

public class BalanceNotEnoughException extends Exception {

   public BalanceNotEnoughException(String message) {
      super(message);
   }
}
