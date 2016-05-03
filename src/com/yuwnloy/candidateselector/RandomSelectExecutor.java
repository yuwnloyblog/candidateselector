package com.yuwnloy.candidateselector;

import java.util.Random;

import com.yuwnloy.candidateselector.IWorker.Status;
import com.yuwnloy.candidateselector.exceptions.ExecutorException;

public class RandomSelectExecutor<T> extends SequenceSelectExecutor<T>{

	@Override
	public Status selectAndDo(IWorker<T> worker) throws ExecutorException {
		int count = this.getCandidateCount();
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		this.setCurrentIndex(random.nextInt(count));
		return super.selectAndDo(worker);
	}
}
