package com.iamkatrechko.yandexschool2017;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iamkatrechko.yandexschool2017.adapter.HistoryCursorAdapter;
import com.iamkatrechko.yandexschool2017.database.HistoryDatabaseHelper.HistoryRecordCursor;

import static com.iamkatrechko.yandexschool2017.database.DatabaseDescription.*;

/**
 * Фрагмент со списком истории переводов
 * @author iamkatrechko
 *         Date: 22.04.2017
 */
public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    /** Константа для загрузки списка истории переводов */
    private static final int HISTORY_LOADER = 0;

    /** Виджет списка записей истории переводов */
    private RecyclerView mRecyclerViewHistory;
    /** Адаптер виджета списка истории */
    private HistoryCursorAdapter mHistoryCursorAdapter;

    private LinearLayout mLayoutInfo;
    private TextView mTextViewInfo;

    /**
     * Возвращает новый экземпляр фрагмента
     * @return новый экземпляр фрагмента
     */
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerViewHistory = (RecyclerView) v.findViewById(R.id.recycler_view_history);
        mLayoutInfo = (LinearLayout) v.findViewById(R.id.layout_info);
        mTextViewInfo = (TextView) v.findViewById(R.id.text_view_info);

        mHistoryCursorAdapter = new HistoryCursorAdapter(getActivity());
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewHistory.setHasFixedSize(false);
        mRecyclerViewHistory.setAdapter(mHistoryCursorAdapter);
        mHistoryCursorAdapter.setClickListener(new HistoryCursorAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(long id) {

            }

            @Override
            public void onBookmarkClickListener(long id, boolean isFavorite) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Record.COLUMN_IS_FAVORITE, !isFavorite);
                getActivity().getContentResolver().update(Record.buildClipUri(id), contentValues, null, null);
            }
        });

        getLoaderManager().restartLoader(HISTORY_LOADER, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case HISTORY_LOADER:
                return new CursorLoader(getActivity(),
                        Record.CONTENT_URI, // Uri таблицы
                        null, // все столбцы
                        null, // все записи
                        null, // без аргументов
                        null); // сортировка
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mHistoryCursorAdapter.setDataCursor(new HistoryRecordCursor(data));
        if (data.getCount() == 0) {
            showInfoMessage(true, getString(R.string.history_empty));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showInfoMessage(boolean isShow, String textInfo) {
        mRecyclerViewHistory.setVisibility(isShow ? View.GONE : View.VISIBLE);
        mLayoutInfo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mTextViewInfo.setText(textInfo);
    }
}
