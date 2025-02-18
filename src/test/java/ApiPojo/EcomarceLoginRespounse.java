package apiPojo;

public class EcomarceLoginRespounse {
    
    private String token;  // Correct field for token
    private String userId;  // Fixed the typo from 'uerId' to 'userId'
    private String message;

    // Getters and Setters
    public String getToken() {
        return token;        
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {  // Fixed getter
        return userId;        
    }

    public void setUserId(String userId) {  // Fixed setter
        this.userId = userId;
    }

    public String getMessage() {
        return message;        
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
