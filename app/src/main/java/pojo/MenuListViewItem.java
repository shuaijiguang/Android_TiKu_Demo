package pojo;

import java.io.Serializable;

public class MenuListViewItem implements Serializable{


	private static final long serialVersionUID = 3205574743978490308L;
	public int pic;
	public String title;
	
	public MenuListViewItem() {}

	public MenuListViewItem(int pic, String title) {
		this.pic = pic;
		this.title = title;
	}

	@Override
	public String toString() {
		return "ListViewItem [pic=" + pic + ", title=" + title + "]";
	}
}
