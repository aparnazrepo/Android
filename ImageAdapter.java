package aparna.outlook.androidtest.searchimageapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kgondkar on 5/6/2016.
 */
/*
public class SearchAndFilterList extends Activity {

    private ListView mSearchNFilterLv;

    private EditText mSearchEdt;

    private ArrayList<String> mStringList;

    private ValueAdapter valueAdapter;

    private TextWatcher mSearchTw;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_and_filter_list);

        initUI();

        initData();

        valueAdapter=new ValueAdapter(mStringList,this);

        mSearchNFilterLv.setAdapter(valueAdapter);

        mSearchEdt.addTextChangedListener(mSearchTw);


    }
    private void initData() {

        mStringList=new ArrayList<String>();

        mStringList.add("one");

        mStringList.add("two");

        mStringList.add("three");

        mStringList.add("four");

        mStringList.add("five");

        mStringList.add("six");

        mStringList.add("seven");

        mStringList.add("eight");

        mStringList.add("nine");

        mStringList.add("ten");

        mStringList.add("eleven");

        mStringList.add("twelve");

        mStringList.add("thirteen");

        mStringList.add("fourteen");

        mSearchTw=new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                valueAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

    }

    private void initUI() {

        mSearchNFilterLv=(ListView) findViewById(R.id.list_view);

        mSearchEdt=(EditText) findViewById(R.id.txt_search);
    }

}*/
public class ImageAdapter extends BaseAdapter implements Filterable {

    private ArrayList<String> mStringList;

    private ArrayList<String> mStringFilterList;

    private LayoutInflater mInflater;

    private ValueFilter valueFilter;

    public ImageAdapter(ArrayList<String> mStringList, Context context) {

        this.mStringList = mStringList;

        this.mStringFilterList = mStringList;

        mInflater = LayoutInflater.from(context);

        getFilter();
    }

    //How many items are in the data set represented by this Adapter.
    @Override
    public int getCount() {

        return mStringList.size();
    }

    //Get the data item associated with the specified position in the data set.
    @Override
    public Object getItem(int position) {

        return mStringList.get(position);
    }

    //Get the row id associated with the specified position in the list.
    @Override
    public long getItemId(int position) {

        return position;
    }

    //Get a View that displays the data at the specified position in the data set.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder viewHolder;

        if (convertView == null) {

            viewHolder = new Holder();

            convertView = mInflater.inflate(R.layout.image_list, null);

            viewHolder.imageNameText = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.icon);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.imageNameText.setText(mStringList.get(position).toString());

        return convertView;
    }

    private class Holder {

        TextView imageNameText;
        ImageView imageView;
    }

    //Returns a filter that can be used to constrain data with a filtering pattern.
    @Override
    public Filter getFilter() {

        if (valueFilter == null) {

            valueFilter = new ValueFilter();
        }

        return valueFilter;
    }


    private class ValueFilter extends Filter {


        //Invoked in a worker thread to filter the data according to the constraint.
        //Filter this data from the image link returned through JSON
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<String> filterList = new ArrayList<String>();

                for (int i = 0; i < mStringFilterList.size(); i++) {

                    if (mStringFilterList.get(i).contains(constraint)) {

                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;

            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }


        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      Filter.FilterResults results) {

            mStringList = (ArrayList<String>) results.values;

            notifyDataSetChanged();


        }

    }
}
