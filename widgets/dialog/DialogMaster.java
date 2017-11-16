package cc.pachira.support.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.Map;


/**
 * Dialog 弹窗工具类
 */
public class DialogMaster {
    public DialogMaster() {
    }

    private AlertDialog alertDialog;

    public int x = 0;
    public int y = 0;

    public int gravity;

    public static class Builder {
        public Builder() {
        }

        public interface WindowLayoutInit {
            void OnWindowLayoutInit(View rootView);
        }

        private WindowLayoutInit layoutInit;

        private Context context;
        private int layout;
        private boolean cancelabe = true;
        private boolean canceledOnTouchOutside = true;

        private int x = 0;
        private int y = 0;

        private int gravity;

        private DialogInterface.OnDismissListener dl;

        private Map<Integer, View.OnClickListener> listeners = new HashMap<>();

        private View.OnTouchListener l;

        public Builder setLayout(int id) {
            this.layout = id;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setLayoutInit(WindowLayoutInit layoutInit) {
            this.layoutInit = layoutInit;
            return this;
        }


        public Builder setPositionX(int x) {
            this.x = x;
            return this;
        }

        public Builder setPositionY(int y) {
            this.y = y;
            return this;
        }

        public Builder setCancelabe(boolean f) {
            this.cancelabe = f;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean f) {
            this.canceledOnTouchOutside = f;
            return this;
        }


        public Builder setDismissListener(DialogInterface.OnDismissListener dl) {
            this.dl = dl;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setItemClickListener(int id, View.OnClickListener listener) {
            this.listeners.put(id, listener);
            return this;
        }

        public DialogMaster create() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

            final View content = LayoutInflater.from(this.context).inflate(this.layout, null);

            builder.setView(content);

            if (this.layoutInit != null) {
                layoutInit.OnWindowLayoutInit(content);
            }

            for (Integer id : listeners.keySet()) {
                View item = content.findViewById(id);
                item.setOnClickListener(this.listeners.get(id));
            }

            final AlertDialog alertDialog = builder.create();

            alertDialog.setCancelable(this.cancelabe);
            alertDialog.setCanceledOnTouchOutside(this.canceledOnTouchOutside);
            //消除背景色
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            if (dl != null)
                alertDialog.setOnDismissListener(dl);

            DialogMaster mask = new DialogMaster();
            mask.alertDialog = alertDialog;
            mask.x = this.x;
            mask.y = this.y;
            mask.gravity = this.gravity;

            return mask;
        }
    }

    public void show() {
        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }
}
