# Fragment工具类：
## 1.HFragmentManager fragment切换工具类
## 2.FragmentBackHelper fragment返回按键拦截工具类

原理
1、不管是Activity也好，Fragment也好，其中内部包含的Fragment都是通过FragmentManager来管理的。
2、FragmentManager.getFragments()可以获取当前Fragment/Activity中处于活动状态的所有Fragment
3、事件由Activity交给当前Fragment处理，如果Fragment有子Fragment的情况同样可以处理。

当然 Fragment 也要实现 FragmentBackHandler接口(按需)
//没有处理back键需求的Fragment不用实现
public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {
    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }
}

Activity覆盖onBackPressed方法（必须）
public class MyActivity extends FragmentActivity {
    //.....
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}

fragment中，当你需要自己处理back事件时覆盖onBackPressed方法,如：
@Override
public boolean onBackPressed() {
// 当确认没有子Fragmnt时可以直接return false
    if (backHandled) {
        Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
        return true;
    } else { 
        return BackHandlerHelper.handleBackPress(this);
    }
}
