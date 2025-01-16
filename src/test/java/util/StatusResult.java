package util;

/**
 * The class is responsible to hold-
 * 	1. Success/Failure status
 * 	2. Errors
 **/

public class StatusResult {

	private boolean isSuccess;
	private String error;

	/**
	 * Parameterized constructor to set the success/failure status
	 * @param status
	 */
	public StatusResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public StatusResult(boolean isSuccess, String error) {
		super();
		this.isSuccess = isSuccess;
		this.error = error;
	}

	/**
	 * @return True/False status of each CQ Wrapper action
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @return error of CQ Wrapper actions
	 */
	public String getError() {
		return error;
	}

	/**
	 * Sets the True/False status
	 * @param statusValue True/Failure
	 */
	public void setStatus(boolean statusValue) {
		isSuccess = statusValue;
	}

	/**
	 * sets the error message
	 * @param errorMessage
	 */
	public void setError(String errorMessage) {
		error = errorMessage;
	}
}