package com.yuwnloy.candidateselector;


public interface IWorker<T> {
	public static enum Status{
		SUCCESS,FAIL;
	}
	public Status doWork(T candidate);
}
