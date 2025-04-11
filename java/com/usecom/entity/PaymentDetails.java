package com.usecom.entity;

import com.usecom.domain.PAYMENT_STATUS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

	private String paymentId;
	private String rezopayPaymentLinkId;
	private String rezopayPaymentLinkReferenceId;
	private String rezopayPaymentLinkStatus;
	private String rezopayPaymentIdZWSP;
	private PAYMENT_STATUS status = PAYMENT_STATUS.PENDING;	
}
