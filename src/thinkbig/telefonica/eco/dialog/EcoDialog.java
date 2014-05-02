package thinkbig.telefonica.eco.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import thinkbig.telefonica.eco.R;

/**
 * Dialog que avisa de que estamos llamando al 112
 */
public class EcoDialog extends Dialog {

    public EcoDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aviso_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.FILL_PARENT;
        getWindow().setAttributes(params);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.btAceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
