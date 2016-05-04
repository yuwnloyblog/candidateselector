package com.yuwnloy.candidateselector;

import com.yuwnloy.candidateselector.IWorker.Status;
import com.yuwnloy.candidateselector.exceptions.CandidateExhaustException;
import com.yuwnloy.candidateselector.exceptions.ExecutorException;

public class SequenceSelectExecutor<T> extends AbstractSelectExecutor<T>{
	private int startIndex = 0;
	
	public void setStartIndex(int startIndex){
		this.startIndex = startIndex;
	}

	@Override
	public Status selectAndDo(IWorker<T> worker) throws ExecutorException {
		if(worker==null){
			throw new ExecutorException("Please provide not-null worker.");
		}
		if(this.getCandidateCount()>0){
			int index = startIndex;
			Status result = worker.doWork(this.getCandidate(index));
			if(Status.FAIL == result){
				int retryCount = 1;
				while(Status.FAIL == result){
					if(retryCount == this.getCandidateCount()){//exhausted all candidate
						throw new CandidateExhaustException("Exhausted all candidator.");
					}
					index++;
					result = worker.doWork(this.getCandidate(index));
					retryCount++;
				}
			}
			return result;
		}else{
			throw new ExecutorException("Have no avaliable candidator.");
		}
	}
	
}
