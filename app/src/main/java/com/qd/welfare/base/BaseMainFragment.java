package com.qd.welfare.base;

import com.qd.welfare.App;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.utils.DialogUtil;

import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
public abstract class BaseMainFragment extends SupportFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    public Unbinder unbinder;

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
//        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
//            _mActivity.finish();
//        } else {
//            TOUCH_TIME = System.currentTimeMillis();
//            ToastUtils.getInstance(_mActivity).showToast(getString(R.string.press_again_exit));
//        }
        if (App.userInfo != null && App.userInfo.getRole() == 1) {
            DialogUtil.showYouhuiVipDialog(_mActivity, PageConfig.OPEN_VIP_YOUHUI, 0);
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
