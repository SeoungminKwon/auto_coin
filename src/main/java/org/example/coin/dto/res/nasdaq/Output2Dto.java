package org.example.coin.dto.res.nasdaq;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Output2Dto {
    @JsonProperty("stck_bsop_date")
    private String businessDate; // 영업 일자

    @JsonProperty("ovrs_nmix_prpr")
    private String currentPrice; // 현재가

    @JsonProperty("ovrs_nmix_oprc")
    private String openPrice; // 시가

    @JsonProperty("ovrs_nmix_hgpr")
    private String highPrice; // 최고가

    @JsonProperty("ovrs_nmix_lwpr")
    private String lowPrice; // 최저가

    @JsonProperty("acml_vol")
    private String accumulatedVolume; // 누적 거래량

    @JsonProperty("mod_yn")
    private String modifiedStatus; // 변경 여부
}
