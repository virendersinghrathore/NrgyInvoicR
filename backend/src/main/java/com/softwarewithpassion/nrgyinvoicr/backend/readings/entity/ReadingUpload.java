package com.softwarewithpassion.nrgyinvoicr.backend.readings.entity;

import com.softwarewithpassion.nrgyinvoicr.backend.AuditableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@NamedEntityGraph(
        name = "readingUploadWithReadingWithMeterWithClient",
        attributeNodes = @NamedAttributeNode(value = "reading", subgraph = "meter"),
        subgraphs = {
                @NamedSubgraph(name = "meter", attributeNodes = @NamedAttributeNode(value = "meter", subgraph = "client")),
                @NamedSubgraph(name = "client", attributeNodes = @NamedAttributeNode("client"))
        })
public class ReadingUpload extends AuditableEntity {
    private static final int ERROR_MESSAGE_LENGTH = 2048;

    @Id
    @SequenceGenerator(name = "reading_upload_id_seq", sequenceName = "reading_upload_id_seq", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = SEQUENCE, generator = "reading_upload_id_seq")
    private Long id;

    @NotNull
    @Column(length = 255)
    private String fileName;

    @NotNull
    @Column
    private ZonedDateTime date;

    @Column(length = ERROR_MESSAGE_LENGTH)
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column
    private ReadingUploadStatus status;

    @ManyToOne
    @JoinColumn(name = "reading_id", nullable = false)
    private Reading reading;

    public ReadingUpload() {
    }

    public ReadingUpload(@NotNull String fileName, Reading reading) {
        this(fileName, reading, null);
    }

    public ReadingUpload(@NotNull String fileName, String errorMessage) {
        this(fileName, null, errorMessage);
    }

    private ReadingUpload(@NotNull String fileName, Reading reading, String errorMessage) {
        this.date = ZonedDateTime.now();
        this.fileName = fileName;
        this.reading = reading;
        this.errorMessage = errorMessage != null && errorMessage.length() > ERROR_MESSAGE_LENGTH ? errorMessage.substring(0, ERROR_MESSAGE_LENGTH) : errorMessage;
        this.status = errorMessage == null || errorMessage.isBlank() ? ReadingUploadStatus.OK : ReadingUploadStatus.ERROR;
    }

    public String getFileName() {
        return fileName;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ReadingUploadStatus getStatus() {
        return status;
    }

    public Reading getReading() {
        return reading;
    }
}
