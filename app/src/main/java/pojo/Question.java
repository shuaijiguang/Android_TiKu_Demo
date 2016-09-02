package pojo;

import java.io.Serializable;
import java.util.Date;


public class Question implements Serializable {
	private static final long serialVersionUID = -4107541081782079662L;
	
	public int cataid;
	public int id;
	public String content;
	public Date pubTime;
	/**题型：1单选题，2多选题，3判断题，4简答题*/
	public int typeid;
	
	public String options;
	public String answer;

	/**
	 * 选择题的选项实体类
	 * @author qiujy
	 */
	public static class Item implements Serializable{
		private static final long serialVersionUID = 6981430163511418465L;
		
		public String title;
		public boolean checked;
		
		
		public String toString() {
			return "Item [title=" + this.title + ", checked=" + this.checked
					+ "]";
		}
	}

	@Override
	public String toString() {
		return "Question [answer=" + answer + ", cataid=" + cataid
				+ ", content=" + content + ", id=" + id + ",  pubTime=" + pubTime + ", typeid=" + typeid + "]";
	}
}
