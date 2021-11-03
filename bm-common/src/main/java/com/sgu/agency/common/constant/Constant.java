package com.sgu.agency.common.constant;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final double REF_BONUS = 20000D;
    public static final String softId = "518924ea-7834-44be-9dce-52d7959354d1";
    public static final List<String> DEFAULT_DEBT_ACCOUNT = Arrays.asList("131", "331");
    public static final List<String> REVENUE_DEBIT_ACCOUNT = Arrays.asList("131");
    public static final List<String> REVENUE_CREDIT_ACCOUNT = Arrays.asList("5111");
    /**
     * Các tài khoản không hiện dư nợ đầu kỳ và không load công nợ lên form phiếu
     */
    public static final List<String> IGNORE_DEBT_ACCOUNT = Arrays.asList("6421");
    public static final List<String> DEFAULT_COST_ACCOUNT = Arrays.asList("6421");
    public static final String GLOBAL_ADMIN = "Global Admin";
    public static final String countryCode = "VN";
}
