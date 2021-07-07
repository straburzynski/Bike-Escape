package com.bikeescape.api.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.codec.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAttachmentData {
    private String attachment;

    private String attachmentName;

    private String attachmentType;

    public EmailAttachmentData(String name, byte[] content, String type) {
        setAttachmentName(name);
        setAttachment(new String(Base64.encode(content)));
        setAttachmentType(type);
    }

    public byte[] getAttachment() {
        return Base64.decode(attachment.getBytes());
    }
}
