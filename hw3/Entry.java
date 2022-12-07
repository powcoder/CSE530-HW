https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package hw3;

import hw1.Field;

public class Entry {

	private Field f;
	private int page;

//	private boolean deleted;
	
	public Entry(Field f, int page) {
		this.f = f;
		this.page = page;
	}

//    public boolean isDeleted() {
//        return deleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        this.deleted = deleted;
//    }

    public Field getField() {
		return this.f;
	}
	
	public int getPage() {
		return this.page;
	}

    @Override
    public String toString() {
        return "Entry{" +
                "f=" + f +
                '}';
    }
}
