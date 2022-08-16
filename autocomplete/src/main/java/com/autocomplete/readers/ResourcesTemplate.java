package com.autocomplete.readers;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface ResourcesTemplate<T> {

	void excecute(T resourse) throws InterruptedException, ExecutionException;
}
