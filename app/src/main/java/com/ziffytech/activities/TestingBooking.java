package com.ziffytech.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;

import com.ziffytech.R;

import java.util.ArrayList;

public class TestingBooking extends CommonActivity {
    EditText editSearchlab;
    ArrayList<String> al_lab_id,al_lab_category,al_lab_test_name;
    ArrayList<String> al_lab_id_copy,al_lab_category_copy,al_lab_test_name_copy;
    //Adaptergridlist gridadapter;
    GridView simpleGrid;
    ArrayList<String> tokens;
    int no_of_tokens, found = 0;
    boolean search_done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_booking);
        //setHeaderTitle("Book Lab");
        //allowBack();
        simpleGrid = (GridView) findViewById(R.id.homegrid);
        editSearchlab = (EditText)findViewById(R.id.editSearchlab);
        //editSearchlab.addTextChangedListener(this);
        al_lab_id = new ArrayList<String>();
        al_lab_category = new ArrayList<String>();
        al_lab_test_name = new ArrayList<String>();

       // getAlllabtest();
    }


   /* private void getAlllabtest()
    {
        al_lab_id.clear();
        al_lab_category.clear();
        al_lab_test_name.clear();

        String base = "https://www.ziffytech.com/admin/Lab_api/get_tests_all";

        AsyncHttpClient client =  new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(base, params, new AsyncHttpResponseHandler()
        {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(String items)
            {
                try
                {
                    JSONObject root = new JSONObject(items);
                    //String message = root.optString("message");
                    String Status = root.optString("responce");
                    JSONArray arr = root.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++)
                    {
                        JSONObject obj = (JSONObject) arr.get(i);

                        al_lab_id.add("" + obj.optString("id"));
                        al_lab_category.add("" + obj.optString("category"));
                        al_lab_test_name.add("" + obj.optString("test_name"));

                        Toast.makeText(TestingBooking.this, ""+al_lab_test_name, Toast.LENGTH_SHORT).show();

                       // SetAdapter();

                        Log.e("LoggedUserData", String.valueOf(al_lab_test_name));

                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                    //Toast.makeText(Productpage.this, " "+e, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error, String content)
            {
                Toast.makeText(TestingBooking.this, "Please wait , Server Currently Busy...", Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void SetAdapter()
    {
         gridadapter = new Adaptergridlist(TestingBooking.this,al_lab_category,al_lab_id,al_lab_test_name);
         simpleGrid.setAdapter(gridadapter);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class Adaptergridlist extends ArrayAdapter<String>
    {
        private final Activity context;
        ArrayList<String> al_lab_category,al_lab_id,al_lab_test_name;
        public Adaptergridlist(Activity context, ArrayList<String> al_lab_category,
                               ArrayList<String> al_lab_id, ArrayList<String> al_lab_test_name)
        {
            super(context,R.layout.item_product_info, R.id.testtitle, al_lab_id);
            this.context = context;
            this.al_lab_category=al_lab_category;
            this.al_lab_id = al_lab_id;
            this.al_lab_test_name = al_lab_test_name;
        }

        static class ViewHolder
        {
            TextView testtitle,testcategory;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_product_info, null, true);
            ViewHolder holder = new ViewHolder();
            holder.testtitle = (TextView)rowView.findViewById(R.id.testtitle);
            holder.testcategory = (TextView) rowView.findViewById(R.id.testcategory);
            holder.testtitle.setText(""+al_lab_test_name.get(position));
            holder.testcategory.setText(""+al_lab_category.get(position));


            return rowView;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        int textlength=0;



        al_lab_id_copy.clear();
        al_lab_category_copy.clear();
        al_lab_test_name_copy.clear();

        for (int i = 0; i < al_lab_id.size(); i++)
        {
            tokens.clear();
            int j=0;
            String term = editSearchlab.getText().toString();
            String copied= al_lab_test_name.get(i).toString().trim();

            //Toast.makeText(BrandsProduct.this, "Copied= "+copied, Toast.LENGTH_SHORT).show();

            StringTokenizer st = new StringTokenizer(copied," ");

            textlength = editSearchlab.getText().length();

            while(st.hasMoreTokens())
            {
                String tok = ""+st.nextToken();
                tokens.add(tok);
                //Toast.makeText(BrandsProduct.this, "Token= "+tok, Toast.LENGTH_SHORT).show();
                j++;
            }

            no_of_tokens = j;
            found = 0;

            for(int k=0; k<j; k++)
            {
                if(textlength <= tokens.get(k).length())
                {
                    if (term.equalsIgnoreCase( ""+tokens.get(k).subSequence(0,textlength)))
                    {
                        if(found==0)
                        {
                            found = 1;

                            al_lab_id_copy.add(""+al_lab_id.get(i));
                            al_lab_test_name_copy.add(""+al_lab_test_name.get(i));
                            al_lab_category_copy.add(""+al_lab_category.get(i));

                        }
                    }
                }
            }

            //Toast.makeText(BrandsProduct.this, ""+copied, Toast.LENGTH_SHORT).show();
        }
        search_done = true;
        gridadapter = new Adaptergridlist(TestingBooking.this,al_lab_category_copy,al_lab_id_copy,al_lab_test_name_copy);
        simpleGrid.setAdapter(gridadapter);

    }

    @Override
    public void afterTextChanged(Editable editable)
    {

    }*/

}
