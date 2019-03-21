package org.cetc.vqm.user.pojo;

import java.io.Serializable;

/**
 * 
 * 实体类，可用于Controller中直接接受参数
 */
public class UserInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private int uid;

    private String username;

    private String password;
    
    private String rolename;
    
    private String lastlogintime;
    
    private String lastloginip;

    public UserInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rolename='" + rolename + '\'' +
                ", lastlogintime='" + lastlogintime + '\'' +
                ", lastloginip='" + lastloginip + '\'' +
                '}';
    }

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	
    
}
