package br.com.solucian.book.api.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiErrorResponse {

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private List<String> details;
        private String path;
        private String system;

        public ApiErrorResponse(LocalDateTime timestamp, int status, String error,
                                String message, List<String> details, String path, String system) {
                this.timestamp = timestamp;
                this.status = status;
                this.error = error;
                this.message = message;
                this.details = details;
                this.path = path;
                this.system = system;
        }
}