package gl.automation.Dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReportModel {
    private String voucherDtlId;
    private String voucherNumber;
    private String branch;
    private String glCode;
    private String drcrFlag;
    private String amount;
    private String narration;
    private String referenceId;
    private String loanId;
    private Date valueDate;
    private Date voucherDate;
    private Date voucherCreationDate;
    private String productCode;
    private String entityId;
    private String schemeCode;
    private String custId;
    private String custName;
    private String productType;
    private String productName;
    private String sanctionedLoanAmount;
    private String casApplicationNumber;
    private String chequeNumber;
    private String loanBranchStateCode;
    private String customerAddressStateCode;
    private String pan;

}
