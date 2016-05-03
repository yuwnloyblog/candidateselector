package com.yuwnloy.candidateselector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.yuwnloy.candidateselector.exceptions.ExecutorException;
import com.yuwnloy.candidateselector.exceptions.CandidateExhaustException;


public class SelectExecutor<T> {
	public static enum Status{
		SUCCESS,FAIL;
	}
	
	public interface IWorker<T>{
		public Status doWork(T candidate);
	}
	
	private List<T> candidateList;
	private int currentIndex = 0;
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();
	
	/**
	 * 
	 * @return
	 * @throws ExecutorException
	 */
	public Status doSequenceSwitch(IWorker<T> worker) throws ExecutorException{
		if(worker==null){
			throw new ExecutorException("Please provide not-null worker.");
		}
		if(this.candidateList!=null&&this.candidateList.size()>0&&this.currentIndex<this.candidateList.size()){
			Status result = null;
			readLock.lock();
			try{
				result = worker.doWork(this.candidateList.get(this.currentIndex));
			}finally{
				readLock.unlock();
			}
			if(Status.FAIL == result){//retry
				if(writeLock.tryLock()){
					try{
						int retryCount = 1;
						while(Status.FAIL == result){
							if(retryCount == this.getCandidateCount()){//exhausted all candidate
								throw new CandidateExhaustException("Exhausted all candidator.");
							}
							this.increaseIndex();
							result = worker.doWork(this.candidateList.get(this.currentIndex));
							retryCount++;
						}
					}finally{
						writeLock.unlock();
					}
				}else{
					readLock.lock();
					try{
						result = worker.doWork(this.candidateList.get(this.currentIndex));
					}finally{
						readLock.unlock();
					}
				}
			}
			return result;
		}else{
			throw new ExecutorException("Have no avaliable candidator.");
		}
	}
	/**
	 * move to next
	 */
	private void increaseIndex(){
		this.currentIndex++;
		this.currentIndex = this.currentIndex%this.getCandidateCount();
	}
	
	/**
	 * add the candidate to list.
	 * @param candidate
	 */
	public void addCandidate(T candidate){
		if(this.candidateList==null)
			this.candidateList = new ArrayList<T>();
		this.candidateList.add(candidate);
	}
	/**
	 * get the candidate list
	 * @return
	 */
	public List<T> getCandidateList(){
		return this.candidateList;
	}
	/**
	 * get the count of candidate.
	 * @return
	 */
	public int getCandidateCount(){
		if(this.candidateList!=null)
			return this.candidateList.size();
		return 0;
	}
	public int getCurrentIndex(){
		return this.currentIndex;
	}
}
