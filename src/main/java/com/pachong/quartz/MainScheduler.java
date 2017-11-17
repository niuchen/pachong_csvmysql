package com.pachong.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pachong.ApiurlMain;
 

/**
 * 创建任务调度，并执行
 * @author long    
 *
 */
public class MainScheduler {
	private static final Logger logger = LoggerFactory.getLogger(MainScheduler.class) ;
    //创建调度器
    public static Scheduler getScheduler() throws SchedulerException{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }
    
    
    public static void schedulerJob() throws SchedulerException{
//        //创建任务
//        JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1", "group1").build();
//        //创建触发器 每3秒钟执行一次
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
//                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
//                            .build();
//        Scheduler scheduler = getScheduler();
//        //将任务及其触发器放入调度器
//        scheduler.scheduleJob(jobDetail, trigger);
//        //调度器开始调度任务
//        scheduler.start();
        
        ScheduleJob job = new ScheduleJob();
        job.setJobId("10001");// 此id用于在QuartzjobFactory中判断执行那个方法
        job.setJobName("定时器");
        job.setJobGroup("采集");
        //  job.setCronExpression("0/15 * * * * ?");//15秒执行一次
         job.setCronExpression(" 0 0 0 * * ? ");//晚上0点执行
        job.setDesc("采集-定时器");
        job.setObjclass(ApiurlMain.class);
        
        Scheduler scheduler = getScheduler();
    	//创建任务触发方案分组key
	    TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		logger.debug("任务信息:"+job.getJobName()+" - "+ job.getJobGroup());
		    //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"  目前肯定是空.并没有配置这个触发方案策略
	 
 		        JobDetail jobDetail = JobBuilder.newJob(job.getObjclass())
	            .withIdentity(job.getJobName(), job.getJobGroup()).build();
	      //  jobDetail.getJobDataMap().put("scheduleJob", job);
	      //  jobDetail.getJobDataMap().put("applicationcontext", act);//存储上下文参数
	        //表达式调度构建器
	        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
	            .getCronExpression());

	        //按新的cronExpression表达式构建一个新的trigger
	        CronTrigger  trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
	       // trigger.getJobDataMap().put("scheduleJob", job);//存储任务参数对象
	        scheduler.scheduleJob(jobDetail, trigger);
	        scheduler.start();
	       // trigger.getJobDataMap().put("applicationcontext", act);//存储上下文参数
	        //scheduler.scheduleJob(jobDetail, trigger);//添加一个定时设置容器中的任务和任务执行策略
	   
        
    }
    
    public static void main(String[] args) throws SchedulerException {
        MainScheduler mainScheduler = new MainScheduler();
        mainScheduler.schedulerJob();
    }

} 