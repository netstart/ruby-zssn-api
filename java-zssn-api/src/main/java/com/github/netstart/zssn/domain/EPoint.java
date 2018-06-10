package com.github.netstart.zssn.domain;

public enum EPoint {
	WATER(4L), FOOD(3L), MEDICATION(2L), AMMUNITION(1L);

	Long point;

	EPoint(Long v) {
		this.point = v;
	}

	public Long getPoint() {
		return this.point;
	}

}
