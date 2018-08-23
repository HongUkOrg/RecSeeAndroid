package com.example.user.database.Adapter;

import android.support.annotation.NonNull;
import android.view.View;

import com.example.user.database.base.AdapterDelegate;
import com.example.user.database.model.SettingItem;
import com.example.user.database.R;

import org.json.JSONException;

import java.util.List;

/**
 * @author Lshare
 * @date 2016/11/29
 */
public class HeaderAdapterDelegate extends AdapterDelegate<List<SettingItem>>
{
  private OnHeaderItemClickListener onHeaderItemClickListener;

  @Override public boolean isForViewType(@NonNull List<SettingItem> items, int position)
  {
    return items.get(position).isHeader();
  }

  @Override public int getLayoutResId() {
    return R.layout.list_item_setting_header;
  }

  @Override public void convert(List<SettingItem> items, ViewHolder holder, int position)
  {
    final SettingItem settingItem = items.get(position);
    holder.setText(R.id.title_txt, settingItem.getMainText());
    holder.itemView.setOnClickListener(new View.OnClickListener()
    {
      @Override public void onClick(View v)
      {
        if (onHeaderItemClickListener != null)
        {
          try {
            onHeaderItemClickListener.onHeaderItemClick(settingItem.getId(), settingItem);
          } catch (JSONException e)
          {
            e.printStackTrace();
          }
        }
      }
    });
  }

  public void setOnHeaderItemClickListener(OnHeaderItemClickListener listener)
  {
    this.onHeaderItemClickListener = listener;
  }

  public interface OnHeaderItemClickListener
  {
    void onHeaderItemClick(int id, SettingItem item) throws JSONException;
  }
}
