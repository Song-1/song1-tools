package com.upload.aliyun;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.aliyun.openservices.oss.model.PartETag;

public class EtagComparator implements Comparator<PartETag> {

	public int compare(PartETag arg0, PartETag arg1) {
		PartETag part1 = arg0;
		PartETag part2 = arg1;

		return part1.getPartNumber() - part2.getPartNumber();
	}

	 
	public Comparator<PartETag> reversed() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public Comparator<PartETag> thenComparing(Comparator<? super PartETag> other) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public <U> Comparator<PartETag> thenComparing(Function<? super PartETag, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public <U extends Comparable<? super U>> Comparator<PartETag> thenComparing(Function<? super PartETag, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public Comparator<PartETag> thenComparingInt(ToIntFunction<? super PartETag> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public Comparator<PartETag> thenComparingLong(ToLongFunction<? super PartETag> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public Comparator<PartETag> thenComparingDouble(ToDoubleFunction<? super PartETag> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T> Comparator<T> nullsFirst(Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T> Comparator<T> nullsLast(Comparator<? super T> comparator) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T, U> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

	 
	public static <T> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor) {
		// TODO Auto-generated method stub
		return null;
	}

}
