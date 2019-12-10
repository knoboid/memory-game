package mjb.memorygame.controllers;

/**
 * RestError
 */
public class RestError {

    private String error;

    public RestError() {
        this.error = "error";
    } 

    public RestError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}