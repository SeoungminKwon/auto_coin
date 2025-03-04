package org.example.coin.dto.res.nasdaq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Output1Dto {
    @JsonProperty("ovrs_nmix_prdy_vrss")
    private String previousDayDifference; // 전일 대비

    @JsonProperty("prdy_vrss_sign")
    private String previousDaySign; // 전일 대비 부호

    @JsonProperty("prdy_ctrt")
    private String previousDayRate; // 전일 대비율

    @JsonProperty("ovrs_nmix_prdy_clpr")
    private String previousDayClosePrice; // 전일 종가

    @JsonProperty("acml_vol")
    private String accumulatedVolume; // 누적 거래량

    @JsonProperty("hts_kor_isnm")
    private String stockNameKor; // 한글 종목명

    @JsonProperty("ovrs_nmix_prpr")
    private String currentPrice; // 현재가

    @JsonProperty("stck_shrn_iscd")
    private String stockShortCode; // 단축 종목 코드

    @JsonProperty("ovrs_prod_oprc")
    private String openPrice; // 시가

    @JsonProperty("ovrs_prod_hgpr")
    private String highPrice; // 최고가

    @JsonProperty("ovrs_prod_lwpr")
    private String lowPrice; // 최저가
}
