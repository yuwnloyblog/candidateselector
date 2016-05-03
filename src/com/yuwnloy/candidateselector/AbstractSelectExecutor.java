package com.yuwnloy.candidateselector;

import java.util.ArrayList;
import java.util.List;
import com.yuwnloy.candidateselector.IWorker.Status;
import com.yuwnloy.candidateselector.exceptions.ExecutorException;

public abstract class AbstractSelectExecutor<T> {
	private List<T> candidateList;
	private int currentIndex = 0;
	
	/**
	 * 
	 * @return
	 * @throws ExecutorException
	 */
	public abstract Status selectAndDo(IWorker<T> worker) throws ExecutorException;
	/**
	 * move to next
	 */
	protected void increaseIndex(){
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
	protected void setCurrentIndex(int index){
		if(this.candidateList!=null&&index<this.candidateList.size())
			this.currentIndex = index;
	}
	/**
	 * 
	 * @return
	 */
	public boolean canSelect(){
		if(this.candidateList!=null&&this.candidateList.size()>0&&this.currentIndex<this.candidateList.size())
			return true;
		return false;
	}
	/**
	 * select current candidate
	 * @return
	 */
	public T getCandidate(){
		if(this.canSelect())
			return this.candidateList.get(this.currentIndex);
		return null;
	}
}
