package com.bikeescape.api.model.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {

    private String subject;

    private String message;

    private String[] recipients;

    private EmailAttachmentData[] attachments;

}
