package com.zenchn.support.fragmentutil;

/**
 * @author:Hzj
 * @date :2019/9/23/023
 * desc  ： 处理fragment返回拦截
 * record：当然 Fragment 也要实现 FragmentBackHandler接口(按需),
 * //没有处理back键需求的Fragment不用实现
 * public abstract class BackHandledFragment extends Fragment implements FragmentBackHandler {
 * @Override public boolean onBackPressed() {
 * return BackHandlerHelper.handleBackPress(this);
 * }
 * }
 * Activity覆盖onBackPressed方法（必须）
 * public class MyActivity extends FragmentActivity {
 * //.....
 * @Override public void onBackPressed() {
 * if (!BackHandlerHelper.handleBackPress(this)) {
 * super.onBackPressed();
 * }
 * }
 * }
 */
public interface FragmentBackHandler {
    boolean onFragmentBackPressed();
}
