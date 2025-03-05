package org.example.coin.domain.nasdaq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NasdaqResDto {
    @JsonProperty("output1")
    private Output1Dto output1;

    @JsonProperty("output2")
    private List<Output2Dto> output2;

    @JsonProperty("rt_cd")
    private String rtCd;

    @JsonProperty("msg_cd")
    private String msgCd;

    @JsonProperty("msg1")
    private String msg1;
}
