package org.cetc.vqm.log.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 实体类，可用于Controller中直接接受参数
 */
public class LogInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private int logid;

    private String username;
    
    private Date time;
    
    private String operation;

    public LogInfo() {
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }

	public int getLogid() {
		return logid;
	}

	public void setLogid(int logid) {
		this.logid = logid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	

}
