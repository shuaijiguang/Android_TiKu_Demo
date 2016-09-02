package pojo;

import java.io.Serializable;
import java.util.List;

/**
 *分页模型类
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = -5259112447890337702L;
	
	public int size = 10;
	public int page = 1;
	public long totalElements;
	public int totalPages;
	public List<T> content;
	
	@Override
	public String toString() {
		return "Page [size=" + size + ", num=" + page + ", totalElements="
				+ totalElements + ", totalPages=" + totalPages + ", content="
				+ content + "]";
	}
}
