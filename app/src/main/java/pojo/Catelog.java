package pojo;

import java.io.Serializable;

public class Catelog implements Serializable{
	private static final long serialVersionUID = 2571950085333602799L;
	public int id;
	public String icon;
	public String name;

	public Catelog() {
	}

	public Catelog(String icon, int id, String name) {
		super();
		this.icon = icon;
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [icon=" + icon + ", id=" + id + ", name=" + name
				+ "]";
	}
}
