package com.qd.welfare.fragment.shop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.qd.welfare.App;
import com.qd.welfare.MainActivity;
import com.qd.welfare.R;
import com.qd.welfare.base.BaseFragment;
import com.qd.welfare.config.PageConfig;
import com.qd.welfare.entity.GoodsDetailInfo;
import com.qd.welfare.http.api.ApiUtil;
import com.qd.welfare.http.base.LzyResponse;
import com.qd.welfare.http.callback.JsonCallback;
import com.qd.welfare.utils.NetWorkUtils;
import com.qd.welfare.utils.PriceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import wiki.scene.loadmore.PtrClassicFrameLayout;
import wiki.scene.loadmore.PtrDefaultHandler;
import wiki.scene.loadmore.PtrFrameLayout;
import wiki.scene.loadmore.StatusViewLayout;

/**
 * 商品详情
 * Created by scene on 2017/9/6.
 */

public class GoodsDetailFragment extends BaseFragment {
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.goods_name)
    TextView goodsName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.kuaidi)
    TextView kuaidi;
    @BindView(R.id.sales)
    TextView sales;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.image4)
    ImageView image4;
    @BindView(R.id.image5)
    ImageView image5;
    @BindView(R.id.image6)
    ImageView image6;
    @BindView(R.id.image7)
    ImageView image7;
    @BindView(R.id.image8)
    ImageView image8;
    @BindView(R.id.image9)
    ImageView image9;
    @BindView(R.id.image10)
    ImageView image10;
    @BindView(R.id.image11)
    ImageView image11;
    @BindView(R.id.image12)
    ImageView image12;
    @BindView(R.id.image13)
    ImageView image13;
    @BindView(R.id.image14)
    ImageView image14;
    @BindView(R.id.image15)
    ImageView image15;
    @BindView(R.id.image16)
    ImageView image16;
    @BindView(R.id.image17)
    ImageView image17;
    @BindView(R.id.image18)
    ImageView image18;
    @BindView(R.id.image19)
    ImageView image19;
    @BindView(R.id.image20)
    ImageView image20;
    @BindView(R.id.ptr_layout)
    PtrClassicFrameLayout ptrLayout;
    @BindView(R.id.status_layout)
    StatusViewLayout statusLayout;
    @BindView(R.id.buy_now)
    TextView buyNow;

    Unbinder unbinder;

    private int sex;
    private GoodsDetailInfo info;

    public static GoodsDetailFragment newInstance(int sex) {
        Bundle args = new Bundle();
        args.putInt("sex", sex);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sex = getArguments().getInt("sex");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
    }

    private void initView() {
        getData(true);
        ptrLayout.setLastUpdateTimeRelateObject(this);
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });
    }

    private void getData(final boolean isFirst) {
        if (NetWorkUtils.isNetworkConnected(getContext())) {
            if (isFirst) {
                statusLayout.showLoading();
            }
            HttpParams params = new HttpParams();
            params.put("sex", sex);
            OkGo.<LzyResponse<GoodsDetailInfo>>get(ApiUtil.API_PRE + ApiUtil.GOODS_DETAIL)
                    .tag(ApiUtil.GOODS_DETAIL_TAG)
                    .params(params)
                    .execute(new JsonCallback<LzyResponse<GoodsDetailInfo>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<GoodsDetailInfo>> response) {
                            try {
                                if (isFirst) {
                                    statusLayout.showContent();
                                    MainActivity.upLoadPageInfo(PageConfig.SHOP, response.body().data.getId());
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                                info = response.body().data;
                                goodsName.setText(info.getName());
                                price.setText("￥" + PriceUtil.getPrice(info.getPrice()));
                                kuaidi.setText("快递：" + PriceUtil.getPrice(info.getDelivery_money()));
                                sales.setText("销量：" + info.getSales());
                                address.setText(info.getAddress());
                                buyNow.setVisibility(View.VISIBLE);
                                Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getThumb()).into(banner);
                                switch (info.getImages().size()) {
                                    case 20:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(19)).into(image20);
                                    case 19:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(19)).into(image19);
                                    case 18:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(17)).into(image18);
                                    case 17:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(16)).into(image17);
                                    case 16:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(15)).into(image16);
                                    case 15:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(14)).into(image15);
                                    case 14:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(13)).into(image14);
                                    case 13:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(12)).into(image13);
                                    case 12:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(11)).into(image12);
                                    case 11:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(10)).into(image11);
                                    case 10:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(9)).into(image10);
                                    case 9:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(8)).into(image9);
                                    case 8:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(7)).into(image8);
                                    case 7:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(6)).into(image7);
                                    case 6:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(5)).into(image6);
                                    case 5:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(4)).into(image5);
                                    case 4:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(3)).into(image4);
                                    case 3:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(2)).into(image3);
                                    case 2:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(1)).into(image2);
                                    case 1:
                                        Glide.with(GoodsDetailFragment.this).load(App.commonInfo.getFile_domain() + info.getImages().get(0)).into(image1);
                                        break;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<LzyResponse<GoodsDetailInfo>> response) {
                            super.onError(response);
                            try {
                                buyNow.setVisibility(View.GONE);
                                if (isFirst) {
                                    statusLayout.showFailed(retryListener);
                                } else {
                                    ptrLayout.refreshComplete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });

        } else {
            buyNow.setVisibility(View.GONE);
            if (isFirst) {
                statusLayout.showNetError(retryListener);
            } else {
                ptrLayout.refreshComplete();
            }
        }
    }

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getData(true);
        }
    };

    @Override
    public void onDestroyView() {
        OkGo.getInstance().cancelTag(ApiUtil.GOODS_DETAIL_TAG);
        super.onDestroyView();
        unbinder.unbind();
    }
}
