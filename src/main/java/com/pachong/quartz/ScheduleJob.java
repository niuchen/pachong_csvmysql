package com.pachong.quartz;
/**
 * 计划任务信息
 * niuchen 2017
 */
public class ScheduleJob {
    /** 任务id */
    private String jobId;
    /** 任务名称 */
    private String jobName;
    /** 任务分组 */
    private String jobGroup;
    /** 任务状态 的返回值*/
    private String jobStatus;
    /** 任务状态 的返回值 含义*/
    private String jobStatusMeaning;
    /** 任务运行时间表达式 */
    private String cronExpression;
    /** 任务描述 */
    private String desc;
    /**需要执行任务方法的工厂类  类要继承Job  此参数传递要注意 表示任务具体要执行的类**/
    private Class objclass;
    
    
    public Class getObjclass() {
		return objclass;
	}

	public void setObjclass(Class objclass) {
		this.objclass = objclass;
	}

	//    None：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除
//    NORMAL:正常状态
//    PAUSED：暂停状态
//    COMPLETE：触发器完成，但是任务可能还正在执行中
//    BLOCKED：线程阻塞状态
//    ERROR：出现错误
	@Override
	public String toString() {
		return "ScheduleJob [jobId=" + jobId + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobStatus="
				+ jobStatus + ", cronExpression=" + cronExpression + ", desc=" + desc + "]";
	}
	/** 任务状态 的返回值 含义*/
	public String getJobStatusMeaning() {
		return jobStatusMeaning;
	}

	/** 任务状态 的返回值 含义*/
	public void setJobStatusMeaning(String jobStatusMeaning) {
		this.jobStatusMeaning = jobStatusMeaning;
	}
    /** 任务id */
	public String getJobId() {
		return jobId;
	}   
	/** 任务id */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	 /** 任务名称 */
	public String getJobName() {
		return jobName;
	}
	 /** 任务名称 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	/** 任务分组 */
	public String getJobGroup() {
		return jobGroup;
	}
	/** 任务分组 */
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	   /** 任务状态 的返回值*/
	public String getJobStatus() {
		return jobStatus;
	}
	   /** 任务状态 的返回值*/
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	 /** 任务运行时间表达式 */
	public String getCronExpression() {
		return cronExpression;
	}
	 /** 任务运行时间表达式 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	/** 任务描述 */
	public String getDesc() {
		return desc;
	}
	/** 任务描述 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
