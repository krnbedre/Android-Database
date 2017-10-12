package com.dhdigital.lms.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.db.MasterDataTable;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.MasterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiran Bedre on 19/1/16.
 * DarkHorse BOA
 */
public class TaskActionDialogBuilder {
    private Context context;
    private OnPositiveOptListener onPositiveOptListener;
    private View rootView;
    private EditText mEditTextComment;
    private Spinner mSpinnerRejectReason, mSpinnerEmployee;
    private TextView errorComment, errorRejectReason, errorEmployee;
    private boolean isCommentRequired, rejectReason, delegate;
    private List<Employee> mListEmployee = new ArrayList<Employee>();

    public TaskActionDialogBuilder(Context context, boolean isCommentRequired, boolean rejectReason, boolean delegate, List<Employee> empList) {
        this.context = context;
        this.isCommentRequired = isCommentRequired;
        this.rejectReason = rejectReason;
        this.delegate = delegate;

        if (empList != null)
            this.mListEmployee.addAll(empList);

        rootView = LayoutInflater.from(this.context).inflate(R.layout.dialog_task_actions_confirmation, null);

        mEditTextComment = (EditText) rootView.findViewById(R.id.editText_comment);
        mSpinnerRejectReason = (Spinner) rootView.findViewById(R.id.spinner_reject_reason);
        mSpinnerEmployee = (Spinner) rootView.findViewById(R.id.spinner_reject_reason);
        errorComment = (TextView) rootView.findViewById(R.id.error_comment);
        errorRejectReason = (TextView) rootView.findViewById(R.id.error_reject_reason);
        errorEmployee = (TextView) rootView.findViewById(R.id.error_reject_reason);

        if (rejectReason) {
            mSpinnerRejectReason.setVisibility(View.VISIBLE);
            mSpinnerRejectReason.setPrompt("Select Reject Reason");
            initRejectReason();
        }

        if (delegate) {
            mSpinnerEmployee.setVisibility(View.VISIBLE);
            errorEmployee.setText(context.getString(R.string.error_task_action_delegate_to_required));
            initDelegateTo();
        }

        if (isCommentRequired) {
            mEditTextComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    errorComment.setVisibility(View.GONE);
                }
            });
        }
    }


    private void initDelegateTo() {
        Employee emp = new Employee();
        emp.setFirstName("Delegate To");
        mListEmployee.add(0, emp);
        ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(context, android.R.layout.simple_spinner_item, mListEmployee);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEmployee.setAdapter(adapter);
        mSpinnerEmployee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                errorEmployee.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                errorEmployee.setVisibility(View.GONE);
            }
        });
    }

    private void initRejectReason() {
        List<MasterData> listRejectReason = MasterDataTable.getInstance(context).getAllByType(MasterData.TYPE.rejectReason);
        MasterData _selectReason = new MasterData();
        _selectReason.setName("Select Reject Reason");
        listRejectReason.add(0, _selectReason);
        ArrayAdapter<MasterData> adapter = new ArrayAdapter<MasterData>(context, android.R.layout.simple_spinner_item, listRejectReason);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerRejectReason.setAdapter(adapter);
        mSpinnerRejectReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                errorRejectReason.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                errorRejectReason.setVisibility(View.GONE);
            }
        });
    }

    public AlertDialog build(String title, String buttontitle, OnPositiveOptListener onPositiveOptListener) {
        this.onPositiveOptListener = onPositiveOptListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(rootView).setCancelable(false);
        builder.setTitle(title).setPositiveButton(buttontitle, null).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                theButton.setTextColor(context.getColor(R.color.greenBulb));
                theButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validate()) return;
                        onPositiveBtnClicked();
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.card_style);
        return dialog;
    }

    private boolean validate() {
        String comment = mEditTextComment.getText().toString().trim();
        boolean isValid = true;
        if (isCommentRequired && comment.isEmpty()) {
            errorComment.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (rejectReason && mSpinnerRejectReason.getSelectedItemPosition() == 0) {
            errorRejectReason.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (delegate && mSpinnerEmployee.getSelectedItemPosition() == 0) {
            errorRejectReason.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }

    private void onPositiveBtnClicked() {
        String comment = mEditTextComment.getText().toString().trim();
        Object object = null;
        if (onPositiveOptListener != null) {
            if (this.rejectReason) {
                object = mSpinnerRejectReason.getSelectedItem();
            } else if (this.delegate) {
                object = mSpinnerEmployee.getSelectedItem();
            }
            onPositiveOptListener.onPositionOpt(object, comment);
        }
    }

    public interface OnPositiveOptListener {
        void onPositionOpt(Object object, String comment);
    }
}
