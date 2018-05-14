package com.jdsports.universityapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HP on 10/7/2017.
 */
public class CustomAdapter extends BaseAdapter
{
    private final LayoutInflater inflater;
    Context activity ;
    ArrayList<ClassDetails> m_lstClassDet ;
    private int lastPosition = -1;
    ClassDetails dataModel = null;
    int m_nStaff;
    // View lookup cache
    private class ViewHolder {
        TextView txtCourseName;
        TextView txtToTime;
        TextView txtFromTime;
        TextView txtDate;
        TextView txtRoom;
        TextView txtSubject;
        TextView txtFaculity;
    }


   public CustomAdapter(Context c, ArrayList<ClassDetails> lst_ClsDet , int nStaff)
    {
        activity = c ;
        m_lstClassDet = lst_ClsDet ;
        m_nStaff = nStaff;
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return m_lstClassDet.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.row_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txtCourseName = (TextView) vi.findViewById(R.id.txtCourseName);
            holder.txtSubject = (TextView) vi.findViewById(R.id.txtSubject);
            holder.txtDate = (TextView) vi.findViewById(R.id.txtDate);
            holder.txtFromTime = (TextView) vi.findViewById(R.id.txtFromTime);
            holder.txtToTime = (TextView) vi.findViewById(R.id.txtToTime);
            holder.txtRoom = (TextView) vi.findViewById(R.id.txtRoom);
            holder.txtFaculity = (TextView) vi.findViewById(R.id.txtFaculity);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (m_lstClassDet.size() <= 0) {
            holder.txtCourseName.setText("No Data");

        } else {
            /***** Get each Model object from Arraylist ********/
            dataModel = null;
            dataModel = (ClassDetails) m_lstClassDet.get(position);

            /************  Set Model values in Holder elements ***********/

            holder.txtCourseName.setText(dataModel.getCourse());
            holder.txtDate.setText(dataModel.getDate());
            holder.txtFromTime.setText(dataModel.getFromTime());
            holder.txtToTime.setText(dataModel.getToTime());
            holder.txtRoom.setText(dataModel.getRoom());
            holder.txtSubject.setText(dataModel.getSubject());
            if(m_nStaff == 0) {
                holder.txtFaculity.setText(dataModel.getFaculity());
                holder.txtFaculity.setVisibility(View.VISIBLE);
            }else
                holder.txtFaculity.setVisibility(View.GONE);




            /******** Set Item Click Listner for LayoutInflater for each row *******/

        }
        return vi;

    }

}
