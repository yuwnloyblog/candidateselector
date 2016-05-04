package com.yuwnloy.candidateselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.yuwnloy.candidateselector.IWorker.Status;
import com.yuwnloy.candidateselector.exceptions.ExecutorException;

public abstract class AbstractSelectExecutor<T> {
	private List<CandidateHose<T>> candidateList;
	/**
	 * 
	 * @return
	 * @throws ExecutorException
	 */
	public abstract Status selectAndDo(IWorker<T> worker) throws ExecutorException;
	
	
	/**
	 * add the candidate to list.
	 * @param candidate
	 */
	public void addCandidate(T candidate){
		this.addCandidate(candidate, 0);
	}
	
	public void addCandidate(T candidate, int weight){
		if(this.candidateList==null)
			this.candidateList = new ArrayList<CandidateHose<T>>();
		//this.candidateList.add(candidate);
		CandidateHose<T> hose = new CandidateHose<T>(candidate,weight);
		this.candidateList.add(hose);
		//sort
		 Collections.sort(candidateList, new Comparator<CandidateHose<T>>() {
			@Override
			public int compare(CandidateHose<T> o1, CandidateHose<T> o2) {
				if(o1.getWeight()<o2.getWeight())
					return 1;
				else if(o1.getWeight()>o2.getWeight())
					return -1;
				return 0;
			}
		 });
	}
	
	
	/**
	 * get the candidate list
	 * @return
	 */
	public List<T> getCandidateList(){
		List<T> retList = null;
		if(this.candidateList!=null&&this.candidateList.size()>0){
			retList = new ArrayList<T>();
			for(CandidateHose<T> hose : this.candidateList)
				retList.add(hose.getCandidate());
		}
		return retList;
	}
	
	protected void sort(){
		
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
	
	public T getCandidate(int index){
		if(index<this.getCandidateCount())
		{
			return this.candidateList.get(index).getCandidate();
		}
		return null;
	}
	
	private static class CandidateHose<T>{
		private T candidate;
		private int weight;
		public CandidateHose(T candidate, int weight){
			this.candidate = candidate;
			this.weight = weight;
		}
		public T getCandidate(){
			return this.candidate;
		}
		public int getWeight(){
			return this.weight;
		}
	}
}
