package cn.semtec.community2.adapter;


import android.util.Log;

import cn.semtec.community2.activity.AddPassActivity;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {
	
	/** The default min value */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value */
	private static final int DEFAULT_MIN_VALUE = 0;
	
	// Values
	private int minValue;
	private int maxValue;

	/**
	 * Default constructor
	 */
	public NumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericWheelAdapter(int minValue, int maxValue,int maxItem) {
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public Object getItem(int index) {
		int value ;
		if (index >= 0 && index < getItemsCount()) {
			value = minValue + index;
			return value;
		}
		return 0;
	}

	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1 ;
	}
	
	@Override
	public int indexOf(Object o){
//		Log.e("oooo","="+o+"==");
		if (!"".equals(o)){
			return (Integer)o - minValue;
		}
		return 0;
	}
}
