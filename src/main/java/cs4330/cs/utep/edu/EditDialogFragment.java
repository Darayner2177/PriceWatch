package cs4330.cs.utep.edu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

/*
* EditDialogFragment custom edit dialog
* allows user to change product name and url
*
*/
public class EditDialogFragment extends DialogFragment implements WebPriceFinder.AsyncResponse {

    private EditText editTextName;
    private EditText editTextUrl;
    private Listener listener;
    private int position;


    /*
     * Interface Listener to be implemented in main activity.
     * The dialog fields are set in the onCreateDialog
     * Main activity can now access the fields
     *
     */
    public interface Listener{
        void editProduct(String name, String url, int position, double price);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        listener = (Listener) context;
    }

    /**
    * Set Position of the product that was clicked
    *
    * @param position position of clicked product
    */
    public void setPosition(int position){
        this.position = position;
    }

    /**
     * Create a custom dialog. Inflate custom edit dialog xml menu
     * set dialog buttons add and cancel. override onclick listeners for buttons
     * Get the new name and new url. Add to listener method editProduct.
     *
     * @param savedInstanceState Bundle saved state
     * @return custom built dialog menu
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder mBuild = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);

        editTextName = view.findViewById(R.id.EditTextName);
        editTextUrl = view.findViewById(R.id.EditTextUrl);

        mBuild .setView(view)
                .setPositiveButton("CHANGE", (dialog, id) -> {

                    if(!editTextName.getText().toString().isEmpty() && !editTextUrl.getText().toString().isEmpty()){
                        boolean check =  URLUtil.isValidUrl(editTextUrl.getText().toString());
                        System.out.println(check);
                        if(check != false){
                            WebPriceFinder syncTask = new WebPriceFinder(getActivity());
                            syncTask.response = this;
                            String url = editTextUrl.getText().toString();
                            syncTask.execute(url);
                        }
                        else{
                            Toast.makeText(getContext(), "Invalid Url", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Fields Missing", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return mBuild.create();
    }

    @Override
    public void processFinish(double output) {
        if(output != -1) {
            listener.editProduct(editTextName.getText().toString(),editTextUrl.getText().toString(), position, output);
        }
        else{
            Toast.makeText(getActivity(), "Could not find price of Product", Toast.LENGTH_SHORT).show();
        }
    }

}
