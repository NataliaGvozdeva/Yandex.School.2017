package com.iamkatrechko.yandexschool2017;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iamkatrechko.yandexschool2017.adapter.HistoryCursorAdapter;
import com.iamkatrechko.yandexschool2017.database.DatabaseDescription;
import com.iamkatrechko.yandexschool2017.database.HistoryDatabaseHelper.HistoryRecordCursor;

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

        mHistoryCursorAdapter = new HistoryCursorAdapter(getActivity());
        mRecyclerViewHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewHistory.setHasFixedSize(false);
        mRecyclerViewHistory.setAdapter(mHistoryCursorAdapter);

        getLoaderManager().restartLoader(HISTORY_LOADER, null, this);
        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case HISTORY_LOADER:
                return new CursorLoader(getActivity(),
                        DatabaseDescription.Record.CONTENT_URI, // Uri таблицы
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
