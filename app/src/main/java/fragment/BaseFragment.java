package fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import tools.AppContext;

/**
 * Fragment的基类
 */
public class BaseFragment extends Fragment {
	
	public AppContext ctx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ctx = (AppContext) getActivity().getApplication();
	}
}
