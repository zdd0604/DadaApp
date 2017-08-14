package com.dadaxueche.student.dadaapp.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dada.mylibrary.DateBase.MyDataBase;
import com.dadaxueche.student.dadaapp.Activity.BTMSExamActivity;
import com.dadaxueche.student.dadaapp.Activity.H5Activity;
import com.dadaxueche.student.dadaapp.Activity.LGradesActivity;
import com.dadaxueche.student.dadaapp.Activity.LK1CollectActivity;
import com.dadaxueche.student.dadaapp.Activity.LK1ErrorActivity;
import com.dadaxueche.student.dadaapp.Activity.LK1ExcldeActivity;
import com.dadaxueche.student.dadaapp.Activity.LPracticeStatisticalActivity;
import com.dadaxueche.student.dadaapp.Activity.MNKSExamActivity;
import com.dadaxueche.student.dadaapp.Activity.SJLXExamActivity;
import com.dadaxueche.student.dadaapp.Activity.SXLXExamActivity;
import com.dadaxueche.student.dadaapp.R;
import com.dadaxueche.student.dadaapp.View.MyButton;
import com.dadaxueche.student.dadaapp.View.MyNoticeTextView;


public class KM1Fragment extends Fragment implements
        View.OnClickListener ,
        MyNoticeTextView.GetNotice {

    private String flag_Select_KM_Type = "KM_Type";
    private String flag_Select_CX_Type = "CX_Type";
    private String flag_Url = "url";
    private String flag_Content = "content";
    private TextView mTextView_error_Num;
    private MyNoticeTextView textView_gg;
    private LinearLayout gg;
    private MyButton mButton_sxlx,mButton_sjlx,mButton_btms,mButton_mnks,mButton_wdcj,myButton_phb;
    private MyButton mButton_wdct,mButton_wdsc,mButton_kttj,mButton_pcdt,mButton_kyjj;
    private String KM = "1";
    private String[] cxs = new String[]{"C1,C2,C3","B2,A2","A1,A3,B1"};
    private String CX = cxs[0];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_km1, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton_sxlx = (MyButton) view.findViewById(R.id.button_sxlx);
        mButton_sjlx = (MyButton) view.findViewById(R.id.button_sjlx);
        mButton_btms = (MyButton) view.findViewById(R.id.button_btms);
        mButton_wdct = (MyButton) view.findViewById(R.id.button_wdct);
        mButton_wdsc = (MyButton) view.findViewById(R.id.button_wdsc);
        mButton_kttj = (MyButton) view.findViewById(R.id.button_kttj);
        mButton_pcdt = (MyButton) view.findViewById(R.id.button_pcdt);
        mButton_kyjj = (MyButton) view.findViewById(R.id.button_kmjj);
        mButton_mnks = (MyButton) view.findViewById(R.id.button_mnks);
        mButton_wdcj = (MyButton) view.findViewById(R.id.button_wdcj);
        myButton_phb = (MyButton) view.findViewById(R.id.button_phb);

        mTextView_error_Num = (TextView) view.findViewById(R.id.textView_errorNum);
        gg = (LinearLayout) view.findViewById(R.id.gg);
        textView_gg = ((MyNoticeTextView)view.findViewById(R.id.textView_gg));
        textView_gg.setGetNotice(this);
        initButton();
    }

    private void initButton() {
        mButton_sxlx.setText("顺序练习");
        mButton_sxlx.setTextViewColor(getResources().getColor(R.color.color_exam_main_shunxu));
        mButton_sxlx.setImageView(R.mipmap.shunxulianxi);
        mButton_sxlx.setOrientation(LinearLayout.VERTICAL);
        mButton_sxlx.setIsSquare(true);
        mButton_sxlx.setOnClickListener(this);

        mButton_sjlx.setText("随机练习");
        mButton_sjlx.setTextViewColor(getResources().getColor(R.color.color_exam_main_suiji));
        mButton_sjlx.setImageView(R.mipmap.suijilianxi);
        mButton_sjlx.setOrientation(LinearLayout.VERTICAL);
        mButton_sjlx.setIsSquare(true);
        mButton_sjlx.setOnClickListener(this);

        mButton_btms.setText("背题模式");
        mButton_btms.setTextViewColor(getResources().getColor(R.color.color_exam_main_beiti));
        mButton_btms.setImageView(R.mipmap.beitimoshi);
        mButton_btms.setOrientation(LinearLayout.VERTICAL);
        mButton_btms.setIsSquare(true);
        mButton_btms.setOnClickListener(this);

        mButton_wdct.setText("我的错题");
        mButton_wdct.setTextSize(12);
        mButton_wdct.setTextViewColor(getResources().getColor(R.color.color_exam_main_cuoti));
        mButton_wdct.setImageView(R.mipmap.wodecuoti);
        mButton_wdct.setOrientation(LinearLayout.VERTICAL);
        mButton_wdct.setIsSquare(true);
        mButton_wdct.setOnClickListener(this);

        mButton_wdsc.setText("我的收藏");
        mButton_wdsc.setTextSize(12);
        mButton_wdsc.setTextViewColor(getResources().getColor(R.color.color_exam_main_shoucang));
        mButton_wdsc.setImageView(R.mipmap.wodeshouchang);
        mButton_wdsc.setOrientation(LinearLayout.VERTICAL);
        mButton_wdsc.setIsSquare(true);
        mButton_wdsc.setOnClickListener(this);

        mButton_kttj.setText("练习统计");
        mButton_kttj.setTextSize(12);
        mButton_kttj.setTextViewColor(getResources().getColor(R.color.color_exam_main_lianxi));
        mButton_kttj.setImageView(R.mipmap.lianxitongji);
        mButton_kttj.setOrientation(LinearLayout.VERTICAL);
        mButton_kttj.setIsSquare(true);
        mButton_kttj.setOnClickListener(this);

        mButton_pcdt.setText("排除的题");
        mButton_pcdt.setTextSize(12);
        mButton_pcdt.setTextViewColor(getResources().getColor(R.color.color_exam_main_paichu));
        mButton_pcdt.setImageView(R.mipmap.paichudeti);
        mButton_pcdt.setOrientation(LinearLayout.VERTICAL);
        mButton_pcdt.setIsSquare(true);
        mButton_pcdt.setOnClickListener(this);

        mButton_kyjj.setText("科一简介");
        mButton_kyjj.setTextSize(12);
        mButton_kyjj.setTextViewColor(getResources().getColor(R.color.color_exam_main_jianjie));
        mButton_kyjj.setImageView(R.mipmap.ke1jianjie);
        mButton_kyjj.setOrientation(LinearLayout.VERTICAL);
        mButton_kyjj.setIsSquare(true);
        mButton_kyjj.setOnClickListener(this);

        mButton_mnks.setText("模拟考试");
        mButton_mnks.setTextViewColor(getResources().getColor(R.color.color_exam_main_moni));
        mButton_mnks.setImageView(R.mipmap.monikaoshi);
        mButton_mnks.setOrientation(LinearLayout.VERTICAL);
        mButton_mnks.setIsSquare(true);
        mButton_mnks.setOnClickListener(this);

        mButton_wdcj.setText("我的成绩");
        mButton_wdcj.setTextViewColor(getResources().getColor(R.color.color_exam_main_chengji));
        mButton_wdcj.setImageView(R.mipmap.wodechengji);
        mButton_wdcj.setOrientation(LinearLayout.VERTICAL);
        mButton_wdcj.setIsSquare(true);
        mButton_wdcj.setOnClickListener(this);

        myButton_phb.setText("排行榜");
        myButton_phb.setTextViewColor(getResources().getColor(R.color.color_exam_main_paihangbang));
        myButton_phb.setImageView(R.mipmap.paihangbang);
        myButton_phb.setOrientation(LinearLayout.VERTICAL);
        myButton_phb.setIsSquare(true);
        myButton_phb.setOnClickListener(this);

        initError();
    }

    private void initError() {
        if(mTextView_error_Num != null) {
            int size = new MyDataBase(getActivity()).queryError(KM).getCount();
            if(size > 0) {
                mTextView_error_Num.setVisibility(View.VISIBLE);
                mTextView_error_Num.setText(String.valueOf(size));
            } else
                mTextView_error_Num.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sxlx:
                showSelectCXDialog("SXLX");
                break;
            case R.id.button_sjlx:
                showSelectCXDialog("SJLX");
                break;
            case R.id.button_btms:
                showSelectCXDialog("BTMS");
                break;
            case R.id.button_wdct:
                Intent intent_wdct = new Intent(getActivity(), LK1ErrorActivity.class);
                intent_wdct.putExtra(flag_Select_KM_Type,KM);
                startActivityForResult(intent_wdct, 100);
                break;
            case R.id.button_wdsc:
                Intent intent_wdsc = new Intent(getActivity(), LK1CollectActivity.class);
                intent_wdsc.putExtra(flag_Select_KM_Type,KM);
                startActivityForResult(intent_wdsc,100);
                break;
            case R.id.button_kttj:
                Intent intent_kttj = new Intent(getActivity(), LPracticeStatisticalActivity.class);
                intent_kttj.putExtra(flag_Select_KM_Type,KM);
                startActivityForResult(intent_kttj,100);
                break;
            case R.id.button_pcdt:
                Intent intent_pcdt = new Intent(getActivity(), LK1ExcldeActivity.class);
                intent_pcdt.putExtra(flag_Select_KM_Type,KM);
                startActivityForResult(intent_pcdt,100);
                break;
            case R.id.button_kmjj:
                Intent intent_kmjj = new Intent(getActivity(),H5Activity.class);
                intent_kmjj.putExtra(flag_Url,"http://www.dadaxueche.com/m/keyi.html");
                intent_kmjj.putExtra(flag_Content,"科一简介");
                startActivity(intent_kmjj);
                break;
            case R.id.button_mnks:
                showSelectCXDialog("MNKS");
                break;
            case R.id.button_wdcj:
                Intent intent_wdcj = new Intent(getActivity(), LGradesActivity.class);
                intent_wdcj.putExtra(flag_Select_KM_Type,KM);
                startActivityForResult(intent_wdcj,100);
                break;
            case R.id.button_phb:
                Intent intent_phb = new Intent(getActivity(), H5Activity.class);
                intent_phb.putExtra("url", "http://www.dadaxueche.com/m/phb.html");
                intent_phb.putExtra("content", "排行榜");
                intent_phb.putExtra("KM",KM);
                startActivity(intent_phb);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        initError();
    }

    private void showSelectCXDialog(final String Type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择车型");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_setcx,null,false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_CX);
        radioGroup.check(view.findViewById(R.id.radioButton).getId());
        CX = cxs[0];
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if(radioButton.getText().toString().contains("C1"))
                    CX = cxs[0];
                else if(radioButton.getText().toString().contains("B2"))
                    CX = cxs[1];
                else if(radioButton.getText().toString().contains("A1"))
                    CX = cxs[2];
            }
        });
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getSelectTypeIntent(Type);
                intent.putExtra(flag_Select_KM_Type, KM);
                intent.putExtra(flag_Select_CX_Type, CX);
                startActivityForResult(intent, 101);
            }
        });
        builder.show();
    }

    public Intent getSelectTypeIntent(String Type) {
        switch (Type) {
            case "SXLX":
                return new Intent(getActivity(), SXLXExamActivity.class);
            case "SJLX":
                return new Intent(getActivity(), SJLXExamActivity.class);
            case "BTMS":
                return new Intent(getActivity(), BTMSExamActivity.class);
            case "MNKS":
                return new Intent(getActivity(), MNKSExamActivity.class);
        }
        return null;
    }

    @Override
    public void getNotice(boolean bool) {
        if(!bool) {
            gg.setVisibility(View.GONE);
        } else {
            gg.setVisibility(View.VISIBLE);
        }
    }
}
