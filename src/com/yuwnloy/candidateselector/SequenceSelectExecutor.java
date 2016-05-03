package com.yuwnloy.candidateselector;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.yuwnloy.candidateselector.exceptions.ExecutorException;
import com.yuwnloy.candidateselector.IWorker.Status;
import com.yuwnloy.candidateselector.exceptions.CandidateExhaustException;


public class SequenceSelectExecutor<T> extends AbstractSelectExecutor<T>{
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private Lock readLock = lock.readLock();
	private Lock writeLock = lock.writeLock();
	
	/**
	 * 
	 * @return
	 * @throws ExecutorException
	 */
	public Status selectAndDo(IWorker<T> worker) throws ExecutorException{
		if(worker==null){
			throw new ExecutorException("Please provide not-null worker.");
		}
		if(this.canSelect()){
			Status result = null;
			readLock.lock();
			try{
				result = worker.doWork(this.getCandidate());
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
							result = worker.doWork(this.getCandidate());
							retryCount++;
						}
					}finally{
						writeLock.unlock();
					}
				}else{
					readLock.lock();
					try{
						result = worker.doWork(this.getCandidate());
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
}
