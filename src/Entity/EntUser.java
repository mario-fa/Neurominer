package Entity;

import java.util.Date;

public class EntUser 
{
	private int user_id;
	private String user_nm;
	private String username_ds;
	private String password_ds;
	private int profile_id;
	private float status_fl;
	private Date registration_dt;
	
	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the user_nm
	 */
	public String getUser_nm() {
		return user_nm;
	}
	/**
	 * @param user_nm the user_nm to set
	 */
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	/**
	 * @return the username_ds
	 */
	public String getUsername_ds() {
		return username_ds;
	}
	/**
	 * @param username_ds the username_ds to set
	 */
	public void setUsername_ds(String username_ds) {
		this.username_ds = username_ds;
	}
	/**
	 * @return the password_ds
	 */
	public String getPassword_ds() {
		return password_ds;
	}
	/**
	 * @param password_ds the password_ds to set
	 */
	public void setPassword_ds(String password_ds) {
		this.password_ds = password_ds;
	}
	/**
	 * @return the profile_id
	 */
	public int getProfile_id() {
		return profile_id;
	}
	/**
	 * @param profile_id the profile_id to set
	 */
	public void setProfile_id(int profile_id) {
		this.profile_id = profile_id;
	}
	/**
	 * @return the status_fl
	 */
	public float getStatus_fl() {
		return status_fl;
	}
	/**
	 * @param status_fl the status_fl to set
	 */
	public void setStatus_fl(float status_fl) {
		this.status_fl = status_fl;
	}
	/**
	 * @return the registration_dt
	 */
	public Date getRegistration_dt() {
		return registration_dt;
	}
	/**
	 * @param registration_dt the registration_dt to set
	 */
	public void setRegistration_dt(Date registration_dt) {
		this.registration_dt = registration_dt;
	}
}