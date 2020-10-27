package engine.dto;

public class ResponseDTO {
    private static final String FEEDBACK_SUCCESS = "Congratulations, you're right!";
    private static final String FEEDBACK_FAIL = "Wrong answer! Please, try again.";

    private boolean success;
    private String feedback;

    public ResponseDTO() {
    }

    public ResponseDTO(boolean success) {
        this.success = success;

        if (success) {
            this.feedback = FEEDBACK_SUCCESS;
        } else {
            this.feedback = FEEDBACK_FAIL;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
