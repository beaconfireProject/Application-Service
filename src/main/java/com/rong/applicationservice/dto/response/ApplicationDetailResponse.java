package com.rong.applicationservice.dto.response;

import com.rong.applicationservice.domain.ApplicationWorkFlow;
import com.rong.applicationservice.domain.DigitalDocument;
import com.rong.applicationservice.domain.Employee;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDetailResponse {
    private ApplicationWorkFlow applicationWorkFlow;
    private Object employee;
    private List<DigitalDocument> digitalDocuments;
}
